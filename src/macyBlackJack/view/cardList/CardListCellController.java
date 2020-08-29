package macyBlackJack.view.cardList;

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import macyBlackJack.GameImageCache;
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

            if(hideFirstCard && getIndex() == 0 && gameInProgressProperty.get()) {
                loadBackCardImage();
            } else {
                loadCardImage(playingCard.getShortName());
            }

            setText(null);
            setGraphic(anchorPane);
        }
    }

    private void loadCardImage(String cardName) {
        imgCard.setImage(GameImageCache.getInstance().getCachedImage(cardName));
    }

    private void loadBackCardImage() {
        imgCard.setImage(GameImageCache.getInstance().getCachedImage(GameImageCache.BACK_IMAGE_KEY));
    }
}