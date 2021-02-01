package casino.cardgame.dao;

import casino.api.v1.Result;
import casino.cardgame.domain.MongoCardGame;
import casino.cardgame.domain.MongoDecks;
import casino.cardgame.domain.MongoHand;
import io.lettuce.core.ExceptionFactory;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.reactivex.Single;

import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class InMemoRedis implements InMemo {


    private RedisAsyncCommands<String, String> client;
//    private RedisReactiveCommands<String, String> client;

    public InMemoRedis(RedisClient client) {
//        this.client = client.connect().reactive();
        this.client = client.connect().async();
    }//TODO: reactice instead of async


    @Override
    public Single<MongoCardGame> getGame(UUID gameId) {
        String roundsKey = String.format("game:%s:rounds", gameId.toString());
        String gameKey = getGameKey(gameId);
        Map<String, String> gameMap = client.hgetall(gameKey).toCompletableFuture().join();
        if (gameMap.isEmpty())
            throw ExceptionFactory.createExecutionException("Game:  " + gameId + "    error fetching from Redis ->  gameMap is empty");
        Set<String> roundSet = client.smembers(roundsKey).toCompletableFuture().join();
        List<MongoHand> handList = roundSet.stream().map(handString -> {
            String handHashKey = String.format("%s:%s", roundsKey, handString);
            return convertMapToHand(client.hgetall(handHashKey).toCompletableFuture().join());
        }).collect(Collectors.toList());
        return Single.just(new MongoCardGame(UUID.fromString(gameMap.get("id")), UUID.fromString(gameMap.get("user")), Long.parseLong(gameMap.get("wager")),
                handList, Boolean.parseBoolean(gameMap.get("playerWin")), Boolean.parseBoolean(gameMap.get("finished"))));

    }

    @Override
    public Single<Boolean> saveEndOfGame(UUID gameId, boolean playerWin) {
        String gameKey = getGameKey(gameId);
        Map<String, String> gameMap = new HashMap<>();
        gameMap.put("playerWin", String.valueOf(playerWin));
        gameMap.put("finished", String.valueOf(true));
        long gameResult = client.hset(gameKey, gameMap).toCompletableFuture().join();
        return Single.just(gameResult == 2L);
    }

    @Override
    public List<MongoCardGame> getAllForUser(UUID userId) {
        return null;//TODO:getAllForUser
    }

    @Override
    public Single<MongoCardGame> save(MongoCardGame game) {
        String userKey = String.format("user:%s", game.getUser().toString());
        String gameKey = getGameKey(game.getGameId()) ;

        long gameInUserSet = client.sadd(userKey, gameKey).toCompletableFuture().join();
        long gameAsHashString = client.hset(gameKey, convertGameToMap(game)).toCompletableFuture().join();
        if (gameInUserSet != 1) {
            throw ExceptionFactory.createExecutionException("Unable to save game:   " + gameKey + " in userSet:   " + userKey);
        } else if (gameAsHashString != 3L) {
            throw ExceptionFactory.createExecutionException("Unable to save game as hash:   " + gameKey);
        }
        return Single.just(game);
    }


    @Override
    public void save(UUID gameId, MongoHand hand, MongoDecks decks) {
        Map<String, String> handMap = convertHandToMap(hand);
        String gameRoundsSetKey = String.format("game:%s:rounds", gameId.toString());
        String handValueInGameSet = String.format("hand:%s", hand.getHandId().toString());
        String handHashKey = String.format("%s:%s", gameRoundsSetKey, handValueInGameSet);

        long handInGameKey = client.sadd(gameRoundsSetKey, handValueInGameSet).toCompletableFuture().join();
        long handAsHash = client.hset(handHashKey, handMap).toCompletableFuture().join();
        if (handInGameKey != 1L) {
            throw ExceptionFactory.createExecutionException("Unable to save hand in gameSet:   " + handHashKey);
        } else if (handAsHash != 6L) {
            throw ExceptionFactory.createExecutionException("Unable to save hand as hash:   " + handHashKey);
        }
        save(hand.getHandId(), decks);
    }


    private void save(UUID handId, MongoDecks decks) {
        String key = String.format("hand:%s", handId.toString());
        String decksPlayerDrawPileKey = String.format("%s:playerDrawPile", key);
        String decksBotDrawPileKey = String.format("%s:botDrawPile", key);
        String decksPlayerDiscardPileKey = String.format("%s:playerDiscardPile", key);
        String decksBotDiscardPileKey = String.format("%s:botDiscardPile", key);
        String decksDrawPileKey = String.format("%s:drawPile", key);
        long playerDraw = convertDeckList(decksPlayerDrawPileKey, decks.getPlayerDrawPile());
        long botDraw = convertDeckList(decksBotDrawPileKey, decks.getBotDrawPile());
        long playerDiscard = convertDeckList(decksPlayerDiscardPileKey, decks.getPlayerDiscardPile());
        long botDiscard = convertDeckList(decksBotDiscardPileKey, decks.getBotDiscardPile());
        long drawPile = convertDeckList(decksDrawPileKey, decks.getDrawPile());
        if ((int) playerDraw != decks.getPlayerDrawPile().size())
            throw ExceptionFactory.createExecutionException("Unable to save playerDrawPile:  " + decksPlayerDrawPileKey);
        else if ((int) botDraw != decks.getBotDrawPile().size())
            throw ExceptionFactory.createExecutionException("Unable to save botDrawPile:  " + decksBotDrawPileKey);
        else if ((int) playerDiscard != decks.getPlayerDiscardPile().size())
            throw ExceptionFactory.createExecutionException("Unable to save playerDiscardPile:  " + decksPlayerDiscardPileKey);
        else if ((int) botDiscard != decks.getBotDiscardPile().size())
            throw ExceptionFactory.createExecutionException("Unable to save botDiscardPile:  " + decksBotDiscardPileKey);
        else if ((int) drawPile != decks.getDrawPile().size())
            throw ExceptionFactory.createExecutionException("Unable to save drawPile:  " + decksDrawPileKey);
    }



    private long convertDeckList(String key, List<String> decksList) {
        if (decksList.isEmpty()) return 0L;
        else return client.rpush(key, decksList.toArray(String[]::new)).toCompletableFuture().join();
    }

    private Map<String, String> convertGameToMap(MongoCardGame game) {
        Map<String, String> map = new HashMap<>();
        map.put("id", game.getGameId().toString());//TODO: warum id speichern
        map.put("user", game.getUser().toString());
        map.put("wager", String.valueOf(game.getWager()));
        return map;
    }

    private Map<String, String> convertHandToMap(MongoHand hand) {
        Map<String, String> map = new HashMap<>();
        map.put("id", hand.getHandId().toString());
        map.put("roundNumber", String.valueOf(hand.getRoundNumber()));
        map.put("handPlayedInRound", String.valueOf(hand.getHandPlayedInRound()));
        map.put("result", hand.getResult().toString());
        map.put("playerCardPlayed", hand.getPlayerCardPlayed());
        map.put("botCardPlayed", hand.getBotCardPlayed());
        return map;
    }

    private MongoHand convertMapToHand(Map<String, String> mapHand) {
        if (mapHand.isEmpty()) {
            throw ExceptionFactory.createExecutionException("Map of Hand is empty");
        } else return new MongoHand(
                Long.parseLong(mapHand.get("roundNumber")), Integer.parseInt(mapHand.get("handPlayedInRound")),
                Result.valueOf(mapHand.get("result")), mapHand.get("playerCardPlayed"),
                mapHand.get("botCardPlayed"), UUID.fromString(mapHand.get("id")));
    }
    private String getGameKey(UUID gameId){
        return String.format("game:%s", gameId.toString());
    }
}
