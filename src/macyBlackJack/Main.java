package macyBlackJack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import macyBlackJack.view.blackjackTable.BlackJackTableController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/blackjackTable/blackjack_table.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Macy BlackJack");
        primaryStage.setScene(new Scene(root, 850, 670));
        primaryStage.show();

        BlackJackTableController blackJackTableController = (BlackJackTableController) loader.getController();
        new GamePresenter(blackJackTableController);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
