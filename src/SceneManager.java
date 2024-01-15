import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class SceneManager {
    private static int screenWidth;
    private static int screenHeight;

    public static void setScreenSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public static Scene createScene(Pane root) {
        return new Scene(root, screenWidth, screenHeight);
    }
}
