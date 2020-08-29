package macyBlackJack.view.playerList;

import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import macyBlackJack.GamePresenter;
import macyBlackJack.model.Player;
import macyBlackJack.model.RuleSet;
import macyBlackJack.view.NoSelectionModel;
import macyBlackJack.view.cardList.CardListCellController;

import java.io.IOException;

public class PlayerListCellController extends ListCell<Player> {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField txtPlayerName;

    @FXML
    private Label lblBank;

    @FXML
    private Label lblDollarSign;

    @FXML
    private Label lblPlayerBank;

    @FXML
    private ListView listViewCards;

    @FXML
    private Pane paneHandControls;

    @FXML
    private Button btnHit;

    @FXML
    private Button btnStand;

    @FXML
    private Label lblScoreTitle;

    @FXML
    private Label lblScore;

    @FXML
    private Label lblHandResult;

    @FXML
    private Pane paneBetControls;

    @FXML
    private Label lblCurrentBet;

    @FXML
    private Button btnDecrementBet;

    @FXML
    private Button btnIncrementBet;

    @FXML Button btnLeaveTable;

    private GamePresenter presenter;
    private FXMLLoader loader;

    private ListChangeListener cardListChangeListener;
    private ChangeListener gameInProgressChangeListener;
    private ChangeListener turnChangeListener;
    private boolean listenersAdded;

    public PlayerListCellController(GamePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void updateItem(Player player, boolean empty) {
        super.updateItem(player, empty);

        if(empty || player == null) {
            setText(null);
            setGraphic(null);
        } else {
            if(loader == null) {
                loader = new FXMLLoader(getClass().getResource("playerListCell.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(player.isDealer()) {
                txtPlayerName.setEditable(false);
                txtPlayerName.setFocusTraversable(false);
                lblDollarSign.setVisible(false);
                lblBank.setVisible(false);
                lblPlayerBank.setVisible(false);
            }

            lblHandResult.setVisible(false);

            listViewCards.setCellFactory(param -> new CardListCellController(player.isDealer(), presenter.getGameViewModel().getGameInProgressProperty()));
            listViewCards.setItems(player.getCurrentHand().getCards());
            listViewCards.setSelectionModel(new NoSelectionModel<>());
            listViewCards.setFocusTraversable(false);

            btnHit.setOnAction(event -> presenter.hitPressed(player));
            btnStand.setOnAction(event -> presenter.standPressed());
            btnDecrementBet.setOnAction(event -> presenter.decrementBetPressed(player));
            btnIncrementBet.setOnAction(event -> presenter.incrementBetPressed(player));
            btnLeaveTable.setOnAction(event -> btnLeaveTablePressed(player));

            //TODO: try moving all listeners to somewhere else

            if(!listenersAdded) {
                cardListChangeListener = c -> evaluateControlsState(player);
                player.getCurrentHand().getCards().addListener(cardListChangeListener);

                txtPlayerName.textProperty().bindBidirectional(player.getNameProperty());
                lblPlayerBank.textProperty().bind(player.getBankProperty().asString());
                lblScore.textProperty().bind(player.getCurrentHand().getCurrentScoreProperty().asString());
                lblCurrentBet.textProperty().bind(player.getCurrentBetProperty().asString());

                gameInProgressChangeListener = (observable, oldValue, newValue) -> {
                    updateHandResult(player);
                    updateCurrentPlayer(player);
                    listViewCards.refresh();
                };
                presenter.getGameViewModel().getGameInProgressProperty().addListener(gameInProgressChangeListener);

                turnChangeListener = (observable, oldValue, newValue) -> {
                    evaluateControlsState(player);
                    updateCurrentPlayer(player);
                };
                presenter.getGameViewModel().getCurrentTurnProperty().addListener(turnChangeListener);

                listenersAdded = true;
            }

            evaluateControlsState(player);

            setText(null);
            setGraphic(anchorPane);
        }
    }

    private void btnLeaveTablePressed(Player player) {
        removeListeners(player);
        presenter.leaveTablePressed(player);
    }

    private void removeListeners(Player player) {
        player.getCurrentHand().getCards().removeListener(cardListChangeListener);
        txtPlayerName.textProperty().unbindBidirectional(player.getNameProperty());
        lblPlayerBank.textProperty().unbind();
        lblScore.textProperty().unbind();
        lblCurrentBet.textProperty().unbind();
        presenter.getGameViewModel().getGameInProgressProperty().removeListener(gameInProgressChangeListener);
        presenter.getGameViewModel().getCurrentTurnProperty().removeListener(turnChangeListener);

        listenersAdded = false;
    }

    private void evaluateControlsState(Player player) {
        btnHit.setDisable(false);
        btnStand.setDisable(false);
        lblScore.setVisible(player.getCurrentHand().getCurrentScoreProperty().get() != 0);
        lblScore.setVisible(player.getCurrentHand().getCurrentScoreProperty().get() != 0);
        btnIncrementBet.setDisable(false);
        btnDecrementBet.setDisable(false);
        btnLeaveTable.setDisable(false);

        if(player.isDealer()) {
            btnHit.setVisible(false);
            btnStand.setVisible(false);
            btnLeaveTable.setVisible(false);
            paneBetControls.setVisible(false);

            if(presenter.getGameViewModel().getGameInProgressProperty().get()) {
                lblScore.setVisible(false);
            }
        }

        if(presenter.getGameViewModel().getGameInProgressProperty().get()) {
            btnIncrementBet.setDisable(true);
            btnDecrementBet.setDisable(true);
            btnLeaveTable.setDisable(true);
        }

        if(!presenter.isPlayersTurn(player) || !presenter.getGameViewModel().getGameInProgressProperty().get()) {
            btnHit.setDisable(true);
            btnStand.setDisable(true);
            return;
        }

        if(player.getCurrentHand().getCards().size() < RuleSet.NUM_STARTING_CARDS) {
            btnHit.setDisable(true);
            btnStand.setDisable(true);
            return;
        }

        if(player.getCurrentHand().getCurrentScoreProperty().get() == RuleSet.BLACKJACK_SCORE) {
            btnHit.setDisable(true);
            btnStand.setDisable(false);
        }

        if(player.getCurrentHand().isBust()) {
            btnHit.setDisable(true);
            btnStand.setDisable(true);
        }
    }

    private void updateHandResult(Player player) {
        if(presenter.getGameViewModel().getGameInProgressProperty().get()) {
            lblHandResult.setVisible(false);
            return;
        }

        lblHandResult.setVisible(true);

        if(player.isDealer()) {
            lblHandResult.setText(player.getCurrentHand().dealerResultDescription());
        } else {
            lblHandResult.setText(player.getCurrentHand().playerResultDescription());
        }
    }

    private void updateCurrentPlayer(Player player) {
        if(presenter.isPlayersTurn(player) || !presenter.getGameViewModel().getGameInProgressProperty().get()) {
            anchorPane.disableProperty().set(false);
        } else {
            anchorPane.disableProperty().set(true);
        }
    }
}
