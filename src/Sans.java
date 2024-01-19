import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Sans extends Obstacle{

    boolean Missed = false;
    public Sans() {
        name = "Sans";
        width = 150;
        height = 150;
        setType("Trap");
        setTextureFromPath("Sprites/Secret/Sans.png");
    }

    @Override
    public void obstacleInfo() {
        Missed = false;
        defaultGround = 100;
        defaultAir = 0;
        super.obstacleInfo();
        setTextureFromPath("Sprites/Secret/Sans.png");
    }

    @Override
    public void behavior(StackPane root, ImageView obstacle) {
        if (Missed) return;

        Text missText = new Text("Miss");
        missText.setStyle("-fx-font-size: 30; -fx-fill: Black; -fx-font-weight: bold; -fx-font-family: 'Press Start 2P'");
        root.getChildren().add(missText);
        missText.translateXProperty().bind(obstacle.translateXProperty());
        missText.setTranslateY(obstacle.getTranslateY() - 50);

        Timeline missTextTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), actionEvent -> {
                    root.getChildren().remove(missText);
                })
        );
        missTextTimeline.play();
        Missed = true;
    }
}
