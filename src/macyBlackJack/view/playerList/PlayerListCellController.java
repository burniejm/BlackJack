package macyBlackJack.view.playerList;

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
    private Pane paneBankControls;

    @FXML
    private Label lblBank;

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

            txtPlayerName.setEditable(false);
            txtPlayerName.setFocusTraversable(false);

            if(player.isDealer()) {
                paneBankControls.setVisible(false);
            }

            lblHandResult.setVisible(false);

            listViewCards.setCellFactory(param -> new CardListCellController(player.isDealer(), presenter.getGameModel().getGameInProgressProperty()));
            listViewCards.setItems(player.getCurrentHand().getCards());
            listViewCards.setSelectionModel(new NoSelectionModel<>());
            listViewCards.setFocusTraversable(false);

            btnHit.setOnAction(event -> presenter.hitPressed(player));
            btnStand.setOnAction(event -> presenter.standPressed());
            btnDecrementBet.setOnAction(event -> presenter.decrementBetPressed(player));
            btnIncrementBet.setOnAction(event -> presenter.incrementBetPressed(player));
            btnLeaveTable.setOnAction(event -> btnLeaveTablePressed(player));

            txtPlayerName.setText(player.getPlayerName());
            lblPlayerBank.textProperty().set(String.valueOf(player.getPlayerBank()));
            lblScore.textProperty().set(player.getCurrentHand().getCurrentScoreProperty().asString().get());
            lblCurrentBet.textProperty().set(String.valueOf(player.getPlayerBet()));

            evaluateControlsState(player);
            updateHandResult(player);
            updateCurrentPlayer(player);

            setText(null);
            setGraphic(anchorPane);
        }
    }

    private void btnLeaveTablePressed(Player player) {
        presenter.leaveTablePressed(player);
    }

    private void resetControls() {
        btnHit.setVisible(true);
        btnStand.setVisible(true);
        btnLeaveTable.setVisible(true);
        btnDecrementBet.setVisible(true);
        btnIncrementBet.setVisible(true);
        paneBetControls.setVisible(true);
        paneBankControls.setVisible(true);

        btnHit.setDisable(false);
        btnStand.setDisable(false);
        btnLeaveTable.setDisable(false);
        btnDecrementBet.setDisable(false);
        btnIncrementBet.setDisable(false);
        btnLeaveTable.setDisable(false);
    }

    private void evaluateControlsState(Player player) {
        resetControls();

        lblScoreTitle.setVisible(player.getCurrentHand().getCurrentScoreProperty().get() != 0);
        lblScore.setVisible(player.getCurrentHand().getCurrentScoreProperty().get() != 0);

        if(player.isDealer()) {
            btnHit.setVisible(false);
            btnStand.setVisible(false);
            btnLeaveTable.setVisible(false);
            paneBetControls.setVisible(false);
            paneBankControls.setVisible(false);

            if(presenter.getGameModel().getGameInProgressProperty().get()) {
                lblScoreTitle.setVisible(false);
                lblScore.setVisible(false);
            }
        }

        if(presenter.getGameModel().getGameInProgressProperty().get()) {
            btnIncrementBet.setDisable(true);
            btnDecrementBet.setDisable(true);
            btnLeaveTable.setDisable(true);
        }

        if(!presenter.isPlayersTurn(player) || !presenter.getGameModel().getGameInProgressProperty().get()) {
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
        if(presenter.getGameModel().getGameInProgressProperty().get()) {
            lblHandResult.setVisible(false);
            return;
        }

        if(player.getCurrentHand().getCards().size() == 0) {
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
        if(presenter.isPlayersTurn(player) || !presenter.getGameModel().getGameInProgressProperty().get()) {
            anchorPane.disableProperty().set(false);
        } else {
            anchorPane.disableProperty().set(true);
        }
    }
}