package casino.cardgame.game;


import casino.api.v1.Card;
import casino.api.v1.CardColor;
import casino.api.v1.CardType;
import casino.api.v1.Result;
import casino.cardgame.domain.Decks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Singleton
public class InGameActionImpl implements InGameAction {
    Logger logger = LoggerFactory.getLogger(InGameActionImpl.class);

    @Override
    public boolean checkIfGameOver(Decks decks) {
        logger.info("InGameActionImpl:  checkIfGameOver  hands played:   {}     in round:    {} ", decks.getHandPlayedInRoundNumber(), decks.getRoundNumber());
        boolean bot = decks.getBotDiscardPile().iterator().hasNext() || decks.getBotDrawPile().iterator().hasNext();
        boolean player = decks.getPlayerDiscardPile().iterator().hasNext() || decks.getPlayerDrawPile().iterator().hasNext();
        return player && bot;
    }

    @Override
    public Decks initCardGameDecks() {
        logger.info("InGameActionImpl:  initCardGameDecks");
        List<Card> newGameDeck = newGameDeck();
        List<Card> botDeck = newGameDeck.stream().limit(16).collect(Collectors.toList());
        List<Card> playerDeck = newGameDeck.stream().skip(16).collect(Collectors.toList());
        return new Decks(playerDeck, botDeck, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 1, 0);
    }

    private List<Card> newGameDeck() {
        logger.info("InGameActionImpl:  newGameDeck");
        List<Card> cardDeck = new ArrayList<>();
        for (CardType ct : CardType.values()) {
            for (CardColor cc : CardColor.values()) {
                cardDeck.add(new Card(ct, cc));
            }
        }
        Collections.shuffle(cardDeck);
        Collections.shuffle(cardDeck, new Random());
        return new ArrayList<>(cardDeck);
    }

    @Override
    public void checkAndShuffle(Decks decks) {
        logger.info("InGameActionImpl:  checkAndShuffle hands played   {}    in round:   {}", decks.getHandPlayedInRoundNumber(), decks.getRoundNumber());
        if (decks.getPlayerDrawPile().isEmpty()) {
            long newRound = decks.getRoundNumber() + 1;
            decks.setRoundNumber(newRound);
            decks.setHandPlayedInRoundNumber(0);
            shuffle(true, decks);
        }
        if (decks.getBotDrawPile().isEmpty()) {
            shuffle(false, decks);
        }

    }

    @Override
    public void shuffle(boolean playerShuffle, Decks decks) {
        logger.info("InGameActionImpl:  shuffle()   hands played:  {}   in round:   {}   playerShuffle:   {}",decks.getHandPlayedInRoundNumber() ,decks.getRoundNumber() ,  playerShuffle);
        if (playerShuffle) {
            List<Card> newDrawPile = new ArrayList<>(decks.getPlayerDiscardPile());
            Collections.shuffle(newDrawPile);
            decks.getPlayerDiscardPile().clear();
            decks.getPlayerDrawPile().addAll(newDrawPile);
        } else {
            List<Card> newDrawPile = new ArrayList<>(decks.getBotDiscardPile());
            Collections.shuffle(newDrawPile);
            decks.getBotDiscardPile().clear();
            decks.getBotDrawPile().addAll(newDrawPile);
        }
    }

    @Override
    public Result compareCardsForSwitch(Card player, Card bot) {
        logger.info("InGameActionImpl:  compareCardsForSwitch   playerCard:  {}    botCard:   {}",player ,  bot);
        if (player.compareTo(bot) < 0) {
            return Result.PLAYER_WIN;
        }
        if (player.compareTo(bot) > 0) {
            return Result.BOT_WIN;
        }
        return Result.DRAW;
    }

    @Override
    public void checkIfEnoughRestCards(Decks decks) {
        logger.info("InGameActionImpl:  checkIfEnoughRestCards hands played   {}     in round:   {}", decks.getHandPlayedInRoundNumber() , decks.getRoundNumber());

        if (decks.getPlayerDrawPile().isEmpty() && decks.getPlayerDiscardPile().isEmpty()) {

            if (decks.getBotDrawPile().isEmpty()) {
                decks.getPlayerDrawPile().add(decks.getBotDrawPile().get(0));
                decks.getBotDrawPile().remove(0);

            } else {
                decks.getPlayerDrawPile().add(decks.getBotDiscardPile().get(0));
                decks.getBotDiscardPile().remove(0);
            }
        }
        if (decks.getBotDrawPile().isEmpty() && decks.getBotDiscardPile().isEmpty()) {

            if (decks.getPlayerDrawPile().isEmpty()) {
                decks.getBotDrawPile().add(decks.getPlayerDrawPile().get(0));
                decks.getPlayerDrawPile().remove(0);
            } else {
                decks.getBotDrawPile().add(decks.getPlayerDiscardPile().get(0));
                decks.getPlayerDiscardPile().remove(0);
            }
        }
    }

    //    public void checkIfAndShuffle(){}
    @Override
    public void changeCards(Card playerCard, Card botCard, Decks decks, boolean playerWin) {
        logger.info("InGameActionImpl:  changeCards hands played:  {}   in round:     {}    playerCard:   {}    botCard:  {}    playerWin:   {}", decks.getHandPlayedInRoundNumber(),decks.getRoundNumber(),playerCard,botCard , playerWin);
        if (playerWin) {
            decks.getPlayerDiscardPile().add(playerCard);
            decks.getPlayerDiscardPile().add(botCard);
            decks.getPlayerDiscardPile().addAll(decks.getDrawPile());
        } else {
            decks.getBotDiscardPile().add(playerCard);
            decks.getBotDiscardPile().add(botCard);
            decks.getBotDiscardPile().addAll(decks.getDrawPile());
        }
        decks.getDrawPile().clear();

    }
}
