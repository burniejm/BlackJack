package macyBlackJack.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Hand {

    private ListProperty<PlayingCard> cardsProperty = new SimpleListProperty<PlayingCard>(FXCollections.observableList(new ArrayList<>()));
    private IntegerProperty currentScoreProperty = new SimpleIntegerProperty(0);
    private HandResult handResult;

    public Hand() {
        this.cardsProperty.addListener((ListChangeListener<PlayingCard>) c -> calculateScore());
    }

    public ObservableList<PlayingCard> getCards() {
        return cardsProperty.get();
    }

    public IntegerProperty getCurrentScoreProperty() { return currentScoreProperty; }

    public HandResult getHandResult() { return handResult; }

    public void setHandResult(HandResult result) { this.handResult = result; }

    public void addCard(PlayingCard card) {
        this.cardsProperty.get().add(card);
    }

    public boolean isBlackJack() {
        return this.cardsProperty.get().size() == GameConstants.BLACKJACK_NUM_CARDS
                && currentScoreProperty.get() == GameConstants.BLACKJACK_SCORE;
    }

    public boolean isBust() {
        return this.currentScoreProperty.get() > GameConstants.BLACKJACK_SCORE;
    }

    public void calculateScore() {
        int score = 0;
        int aceCount = 0;

        for(PlayingCard card : cardsProperty.get()) {
            score += card.getValue();

            if(card.getValue() == GameConstants.ACE_HIGH_VALUE) {
                aceCount++;
            }
        }

        if(score > GameConstants.BLACKJACK_SCORE) {
            while(aceCount > 0 && score > GameConstants.BLACKJACK_SCORE) {
                score -= (GameConstants.ACE_HIGH_VALUE - GameConstants.ACE_LOW_VALUE);
                aceCount--;
            }
        }

        this.currentScoreProperty.setValue(score);
    }

    public void reset() {
        cardsProperty.clear();
    }

    public String playerResultDescription() {
        switch (handResult) {
            case WIN:
                if(isBlackJack()) {
                    return "BlackJack !!!!!!";
                } else {
                    return "Winner !!";
                }
            case LOSS:
                if(isBust()) {
                    return "Busted !!";
                } else {
                    return "Lost :(";
                }
            case TIE:
                return "Tie :/";
        }

        return "";
    }

    public String dealerResultDescription() {
        if(isBust()) {
            return "Dealer Busts !!!!!";
        }

        return "";
    }
}
