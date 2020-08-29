package macyBlackJack.view.cardList;

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import macyBlackJack.model.PlayingCard;


import java.io.IOException;

public class CardListCellController extends ListCell<PlayingCard> {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView imgCard;

    private FXMLLoader loader;

    private boolean hideFirstCard;
    private BooleanProperty gameInProgressProperty;
    private String imagePath;
    private final String FLIPPED_IMAGE_PATH = "/images/Red_back.jpg";

    public CardListCellController(boolean hideFirstCard, BooleanProperty gameInProgressProperty) {
        this.hideFirstCard = hideFirstCard;
        this.gameInProgressProperty = gameInProgressProperty;
    }

    @Override
    protected void updateItem(PlayingCard playingCard, boolean empty) {
        super.updateItem(playingCard, empty);

        if(empty || playingCard == null) {
            setText(null);
            setGraphic(null);
        } else {
            if(loader == null) {
                loader = new FXMLLoader(getClass().getResource("cardListCell.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            imagePath = "/images/" + playingCard.getShortName() + ".jpg";

            if(hideFirstCard && getIndex() == 0 && gameInProgressProperty.get()) {
                loadBackCardImage();
            } else {
                loadCardImage();
            }

            setText(null);
            setGraphic(anchorPane);
        }
    }

    private void loadCardImage() {
        Image image = new Image(imagePath);
        imgCard.setImage(image);
    }

    private void loadBackCardImage() {
        Image image = new Image(FLIPPED_IMAGE_PATH);
        imgCard.setImage(image);
    }
}
