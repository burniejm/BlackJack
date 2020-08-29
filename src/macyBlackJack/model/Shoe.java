package macyBlackJack.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Random;

public class Shoe {
    private ListProperty<PlayingCard> cardsProperty = new SimpleListProperty<PlayingCard>(FXCollections.observableList(new ArrayList<>()));

    public Shoe(int numDecks) {
        for(int i = 0; i < numDecks; i++) {
            addDeckToShoe(new Deck());
        }
    }

    public ObservableList<PlayingCard> getCards() {
        return cardsProperty.get();
    }

    public void shuffle(int numShuffles){
        for(int i = 0; i < numShuffles; i++) {
            shuffleShoe();
        }
    }

    private void addDeckToShoe(Deck deck) {
        for(PlayingCard card : deck.getPlayingCards()) {
            this.cardsProperty.get().add(card);
        }
    }

    private void shuffleShoe() {
        ArrayList<PlayingCard> shuffled = new ArrayList<>();
        Random random = new Random();

        while (cardsProperty.get().size() > 0) {
            PlayingCard randomCard = cardsProperty.get(random.nextInt(cardsProperty.size()));
            shuffled.add(randomCard);
            cardsProperty.remove(randomCard);
        }

        cardsProperty.addAll(shuffled);
    }
}