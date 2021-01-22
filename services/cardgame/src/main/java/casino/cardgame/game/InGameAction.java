package casino.cardgame.game;

import casino.api.v1.Card;
import casino.api.v1.Result;
import casino.cardgame.domain.Decks;

public interface InGameAction {

    boolean checkIfGameOver(Decks decks);

    Decks initCardGameDecks();

    void checkAndShuffle(Decks decks);

    void shuffle(boolean playerShuffle, Decks decks);

    Result compareCardsForSwitch(Card player, Card bot);

    void checkIfEnoughRestCards(Decks actionDecks);

    void changeCards(Card playerCard, Card botCard, Decks decks, boolean playerWin);
}
