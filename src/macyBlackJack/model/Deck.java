package macyBlackJack.model;

import java.util.ArrayList;

public class Deck {
    private ArrayList<PlayingCard> playingCards;

    public Deck(){
        playingCards = new ArrayList<>();
        PlayingCardType.stream().forEach(t -> playingCards.add(new PlayingCard(t)));
    }

    public ArrayList<PlayingCard> getPlayingCards() {
        return playingCards;
    }
}
