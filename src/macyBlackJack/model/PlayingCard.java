package macyBlackJack.model;

public class PlayingCard implements Comparable{
    private PlayingCardType type;

    public PlayingCard(PlayingCardType type) {
        this.type = type;
    }

    public PlayingCardType getType() { return this.type; }
    public int getValue() {
        return this.type.getValue();
    }

    public String getShortName() {
        return this.type.getShortName();
    }

    @Override
    public int compareTo(Object o) {
        PlayingCard card = (PlayingCard) o;
        return Integer.compare(this.getValue(), card.getValue() );
    }
}
