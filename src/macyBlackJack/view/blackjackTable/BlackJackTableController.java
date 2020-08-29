package macyBlackJack.view.blackjackTable;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.scene.layout.AnchorPane;
import macyBlackJack.GamePresenter;
import macyBlackJack.Main;
import macyBlackJack.model.Player;
import macyBlackJack.model.PlayingCard;
import macyBlackJack.model.RuleSet;
import macyBlackJack.view.NoSelectionModel;
import macyBlackJack.view.playerList.PlayerListCellController;

import java.net.URL;
import java.util.ResourceBundle;

public class BlackJackTableController implements Initializable {

    private GamePresenter presenter;

    @FXML
    Button btnAddPlayer;

    @FXML
    Button btnDeal;

    @FXML
    ListView<Player> listViewPlayers;

    @FXML
    Label lblStatus;

    @FXML
    AnchorPane anchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void configure(GamePresenter presenter) {
        this.presenter = presenter;

        String cssPath = Main.class.getResource("/css/app.css").toExternalForm();
        anchorPane.getStylesheets().add(cssPath);

        btnAddPlayer.setOnAction(event -> presenter.addPlayerPressed());
        btnDeal.setOnAction(event -> presenter.dealHandPressed());

        listViewPlayers.setCellFactory(playerListView -> new PlayerListCellController(presenter));
        listViewPlayers.setItems(presenter.getGameViewModel().getPlayersProperty());
        listViewPlayers.getStyleClass().add("player-list");
        listViewPlayers.setSelectionModel(new NoSelectionModel<>());

        updateStatusLabel();

        presenter.getGameViewModel().getPlayersProperty().addListener((ListChangeListener<Player>) c -> {
            evaluateButtonsEnabled();
            listViewPlayers.refresh();
        });

        presenter.getGameViewModel().getGameInProgressProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                updateStatusLabel();
                evaluateButtonsEnabled();
            }
        });

        presenter.getGameViewModel().getCurrentTurnProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updateStatusLabel();
            }
        });
    }

    private void evaluateButtonsEnabled() {
        boolean gameInProgress = presenter.getGameViewModel().getGameInProgressProperty().get();
        btnAddPlayer.setDisable(gameInProgress || presenter.getGameViewModel().getPlayersProperty().size() == RuleSet.MAX_PLAYERS_INCLUDING_DEALER);
        btnDeal.setDisable(gameInProgress || presenter.getGameViewModel().getPlayersProperty().size() < RuleSet.MIN_PLAYERS_INCLUDING_DEALER);
    }

    private void updateStatusLabel() {
        if(presenter.getGameViewModel().getGameInProgressProperty().get()) {
            //show whose turn it is
            String playerName = presenter.getGameViewModel().getPlayersProperty().get(presenter.getGameViewModel().getCurrentTurnProperty().get()).getNameProperty().get();
            lblStatus.setText("Current Turn: " + playerName);
        } else {
            lblStatus.setText("");
        }
    }
}
