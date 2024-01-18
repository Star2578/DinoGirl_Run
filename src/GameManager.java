import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GameManager {
    private static GameManager instance;
    public final double version = 0.2;
    private int screenWidth;
    private int screenHeight;
    private boolean isGameOver = false;
    private boolean isPause = false;

    private Scene mainMenuScene;

    public GameManager() {
        // default values
        screenWidth = 1280;
        screenHeight = 720;

        loadSettings();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenWidth(int width) {
        screenWidth = width;
    }

    public void setScreenHeight(int height) {
        screenHeight = height;
    }

    public boolean getGameOverStatus() { return isGameOver; }
    public void setGameOverStatus(boolean bool) { isGameOver = bool; }

    public boolean getPauseStatus() { return isPause; }
    public void setPauseStatus(boolean bool) { isPause = bool; }

    public Scene getMainMenuScene() { return mainMenuScene; }
    public void setMainMenuScene(Scene mainMenuScene) { this.mainMenuScene = mainMenuScene; }

    public Scene setting(Stage stage, Scene previousScene) {
        // Create UI elements for the settings scene
        Button backButton = new Button("Back");
        VBox settingsRoot = new VBox();
        settingsRoot.setAlignment(Pos.TOP_RIGHT);

        // Volume sliders for background music and SFX
        Slider bgMusicSlider = new Slider();
        Slider sfxSlider = new Slider();

        // TODO: Handle Music Slider

        // Screen resolution adjustment (ComboBox)
        ComboBox<String> resolutionComboBox = new ComboBox<>();
        resolutionComboBox.getItems().addAll("1920x1080", "1280x720", "800x600"); // Populate with resolutions
        String screen = screenWidth + "x" + screenHeight;
        resolutionComboBox.setValue(screen); // default value

        resolutionComboBox.setOnAction(event -> {
            String selectedResolution = resolutionComboBox.getSelectionModel().getSelectedItem();

            if (selectedResolution != null) {
                String[] dimensions = selectedResolution.split("x");

                if (dimensions.length == 2) {
                    int width = Integer.parseInt(dimensions[0]);
                    int height = Integer.parseInt(dimensions[1]);

                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

                    // Ensure the window fits within the screen bounds
                    width = Math.min(width, (int) screenBounds.getWidth());
                    height = Math.min(height, (int) screenBounds.getHeight());

                    stage.setWidth(width);
                    stage.setHeight(height);

                    // Calculate new X and Y positions for centering
                    double newX = (screenBounds.getWidth() - width) / 2 + screenBounds.getMinX();
                    double newY = (screenBounds.getHeight() - height) / 2 + screenBounds.getMinY();

                    stage.setX(newX);
                    stage.setY(newY);

                    setScreenHeight(height);
                    setScreenWidth(width);
                }
            }
        });

        // Text labels for sliders and combo box
        Label bgMusicLabel = new Label("Background Music Volume:");
        Label sfxLabel = new Label("SFX Volume:");
        Label resolutionLabel = new Label("Screen Resolution:");

        VBox settingsContainer = new VBox(bgMusicLabel, bgMusicSlider, sfxLabel, sfxSlider, resolutionLabel, resolutionComboBox);
        settingsContainer.setPadding(new Insets(40));

        // Add labels and components to the settingsRoot VBox
        settingsRoot.getChildren().addAll(
                backButton,
                settingsContainer
        );

        // Create a new scene for settings
        Scene settingsScene = SceneManager.createScene(settingsRoot);

        // Handle action for the "Back" button to return to the main menu
        backButton.setOnAction(e -> {
            stage.setScene(previousScene); // Return to the previous scene
        });

        return settingsScene;
    }

    private void loadSettings() {
        // Implement loading settings from file
    }
    private void saveSettings() {
        // Implement saving settings to file
    }
}
