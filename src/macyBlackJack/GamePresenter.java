package macyBlackJack;

import macyBlackJack.model.Game;
import macyBlackJack.model.Player;
import macyBlackJack.view.blackjackTable.BlackJackTableController;

public class GamePresenter {

    private Game gameViewModel;
    private GameInteractor gameInteractor;
    private BlackJackTableController tableController;

    public GamePresenter(BlackJackTableController tableController) {
        this.tableController = tableController;
        this.gameViewModel = new Game();
        this.gameInteractor = new GameInteractor(this);

        tableController.configure(this);
    }

    public Game getGameViewModel() {
        return gameViewModel;
    }

    public void dealHandPressed() {
        gameInteractor.dealNewHand();
    }

    public void addPlayerPressed() { gameInteractor.addPlayer(); }

    public void leaveTablePressed(Player player) { gameInteractor.removePlayer(player); }

    public void hitPressed(Player player) {
        gameInteractor.dealCard(player);
    }

    public void standPressed() {
        gameInteractor.stand();
    }

    public void decrementBetPressed(Player player) { gameInteractor.decrementBet(player); }

    public void incrementBetPressed(Player player) { gameInteractor.incrementBet(player); }

    public boolean isPlayersTurn(Player player) {
        return gameViewModel.getCurrentTurnProperty().get() == gameViewModel.getPlayersProperty().indexOf(player);
    }
}
