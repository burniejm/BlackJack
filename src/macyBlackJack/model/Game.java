package macyBlackJack.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.ArrayList;

public class Game {

    private ListProperty<Player> playersProperty = new SimpleListProperty<Player>(FXCollections.observableList(new ArrayList<>()));
    private BooleanProperty gameInProgressProperty = new SimpleBooleanProperty(false);
    private IntegerProperty currentTurnProperty = new SimpleIntegerProperty(1);
    private Shoe shoe;

    public Game() {
        resetShoe();
        addPlayer(new Player("Dealer", true));
        addPlayer(new Player("Player 1", false));
    }

    public ListProperty<Player> getPlayersProperty() {
        return playersProperty;
    }
    public IntegerProperty getCurrentTurnProperty() { return currentTurnProperty; }
    public BooleanProperty getGameInProgressProperty() {
        return gameInProgressProperty;
    }
    public Shoe getShoe() {
        return this.shoe;
    }

    public void resetShoe() {
        shoe = new Shoe(RuleSet.NUM_DECKS);
        shuffleShoe(RuleSet.NUM_SHUFFLES);
    }

    public void addPlayer(Player player) {
        playersProperty.add(player);
    }

    public void removePlayer(Player player) {
        playersProperty.get().remove(player);
    }

    public void shuffleShoe(int numShuffles) {
        this.shoe.shuffle(numShuffles);
    }
}
