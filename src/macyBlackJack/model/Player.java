package macyBlackJack.model;

public class Player {

    private String playerName = "";
    private int playerBank = GameConstants.STARTING_BANK;
    private int playerBet = GameConstants.MIN_BET;
    private Hand currentHand;
    private boolean isDealer;

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String name) { playerName = name; }
    public int getPlayerBank() { return playerBank; }
    public void setPlayerBank(int bank) { playerBank = bank; }
    public int getPlayerBet() { return playerBet; }
    public void setPlayerBet(int bet) { playerBet = bet; }

    public Player(String name, boolean isDealer){
        this.playerName = name;
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
        return playerBank > 0 || isDealer;
    }
}
