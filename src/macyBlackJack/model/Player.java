package macyBlackJack.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Player {

    private StringProperty nameProperty = new SimpleStringProperty("");
    private IntegerProperty bankProperty = new SimpleIntegerProperty(RuleSet.STARTING_BANK);
    private IntegerProperty currentBetProperty = new SimpleIntegerProperty(RuleSet.MIN_BET);
    private Hand currentHand;
    private boolean isDealer;

    public StringProperty getNameProperty() { return nameProperty; }
    public IntegerProperty getBankProperty() { return bankProperty; }
    public IntegerProperty getCurrentBetProperty() { return currentBetProperty; }

    public Player(String name, boolean isDealer){
        this.nameProperty.setValue(name);
        this.isDealer = isDealer;
        this.currentHand = new Hand();
    }

    public boolean isDealer() {
        return isDealer;
    }

    public Hand getCurrentHand() {
        return this.currentHand;
    }

    public boolean canAffordToPlay() {
        return getBankProperty().get() > 0;
    }
}
