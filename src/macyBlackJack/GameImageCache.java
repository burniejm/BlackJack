package macyBlackJack;

import javafx.scene.image.Image;
import macyBlackJack.model.PlayingCardType;

import java.util.HashMap;

public class GameImageCache {

    public static String BACK_IMAGE_KEY = "cardBack";
    private static String BACK_IMAGE_PATH = "/images/Red_back.jpg";

    private static GameImageCache single_instance = null;
    private HashMap<String, Image> images;

    private GameImageCache() {
        images = new HashMap<>();

        PlayingCardType.stream().forEach(t -> {
            String imagePath = "/images/" + t.getShortName() + ".jpg";
            Image image = new Image(imagePath);
            images.put(t.getShortName(), image);
        });

        Image backImage = new Image(BACK_IMAGE_PATH);
        images.put(BACK_IMAGE_KEY, backImage);
    }

    public static GameImageCache getInstance() {
        if (single_instance == null)
            single_instance = new GameImageCache();

        return single_instance;
    }

    public Image getCachedImage(String key) {
        return images.get(key);
    }
}