package macyBlackJack;

import macyBlackJack.model.Game;
import macyBlackJack.model.Player;
import macyBlackJack.view.blackjackTable.BlackJackTableController;

public class GamePresenter {

    private Game gameModel;
    private GameInteractor gameInteractor;
    private BlackJackTableController tableController;

    public GamePresenter(BlackJackTableController tableController) {
        this.tableController = tableController;
        this.gameModel = new Game();
        this.gameInteractor = new GameInteractor(gameModel);
        GameImageCache.getInstance();

        tableController.configure(this);
    }

    public Game getGameModel() {
        return gameModel;
    }

    public void dealHandPressed() {
        gameInteractor.dealNewHand();
        tableController.refreshUI();
    }

    public void addPlayerPressed() {
        gameInteractor.addPlayer();
        tableController.refreshUI();
    }

    public void leaveTablePressed(Player player) {
        gameInteractor.removePlayer(player);
        tableController.refreshUI();
    }

    public void hitPressed(Player player) {
        gameInteractor.dealCard(player);
        tableController.refreshUI();
    }

    public void standPressed() {
        gameInteractor.stand();
        tableController.refreshUI();
    }

    public void decrementBetPressed(Player player) {
        gameInteractor.decrementBet(player);
        tableController.refreshUI();
    }

    public void incrementBetPressed(Player player) {
        gameInteractor.incrementBet(player);
        tableController.refreshUI();
    }

    public boolean isPlayersTurn(Player player) {
        return gameModel.getCurrentTurnProperty().get() == gameModel.getPlayersProperty().indexOf(player);
    }

    public boolean gameHasPlayablePlayers() {
        for(Player player : gameModel.getPlayersProperty().get()) {
            if(player.isDealer()) {
                continue;
            }

            if(player.canAffordToPlay()) {
                return true;
            }
        }

        return false;
    }
}