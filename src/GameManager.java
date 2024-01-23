import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class GameManager {
    private static GameManager instance;
    private SoundManager soundManager;
    public final double version = 0.2;
    private int screenWidth;
    private int screenHeight;
    private boolean isGameOver = false;
    private boolean isPause = false;
    private Properties settingProperties;
    private Properties gameProperties;
    private Scene mainMenuScene;

    private static final float MIN_DECIBEL = -30.0f;
    private static final float MAX_DECIBEL = 6.0f;
    private static final float MID_DECIBEL = (MAX_DECIBEL + MIN_DECIBEL) / 2;

    private float soundEffectVolume = MID_DECIBEL;
    private float soundEffectSlider = 50.0f;
    private float backgroundMusicVolume = MID_DECIBEL;
    private float backgroundMusicSlider = 50.0f;

    // Game Progression to be save
    private int highScore = 0;
    // Dino Girl's skins
    // Achievements

    public GameManager() {
        // default values
        screenWidth = 1280;
        screenHeight = 720;

        settingProperties = new Properties();
        gameProperties = new Properties();
        loadSettings();
        loadGame();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void initialize(SoundManager soundManager) {
        this.soundManager = soundManager;
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

    public int getHighScore() { return highScore; }
    public void setHighScore(int highScore) { this.highScore = highScore; }

    public float getBackgroundMusicVolume() { return backgroundMusicVolume; }
    public void setBackgroundMusicVolume(float backgroundMusicVolume) { this.backgroundMusicVolume = backgroundMusicVolume; }

    public float getSoundEffectVolume() { return soundEffectVolume; }
    public void setSoundEffectVolume(float soundEffectVolume) { this.soundEffectVolume = soundEffectVolume; }

    public float getBackgroundMusicSlider() { return backgroundMusicSlider; }
    public void setBackgroundMusicSlider(float backgroundMusicSlider) { this.backgroundMusicSlider = backgroundMusicSlider; }

    public float getSoundEffectSlider() { return soundEffectSlider; }
    public void setSoundEffectSlider(float soundEffectSlider) { this.soundEffectSlider = soundEffectSlider; }

    // Mapping function to convert slider values to decibel range
    private float mapToDecibelRange(float sliderValue) {
        // Assuming the slider value ranges from 0 to 100
        float normalizedValue = sliderValue / 100.0f;

        // Map the normalized value to the decibel range using a logarithmic scale
        return MIN_DECIBEL + normalizedValue * (MAX_DECIBEL - MIN_DECIBEL);
    }

    public Scene setting(Stage stage, Scene previousScene) {
        // Create UI elements for the settings scene
        Button backButton = new Button("Back");
        VBox settingsRoot = new VBox();
        settingsRoot.setAlignment(Pos.TOP_RIGHT);

        // Volume sliders for background music and SFX
        Slider bgMusicSlider = new Slider();
        Slider sfxSlider = new Slider();

        System.out.println("Set bg:" + getBackgroundMusicSlider());
        System.out.println("Set sfx:" + getSoundEffectSlider());
        bgMusicSlider.setValue(getBackgroundMusicSlider());
        sfxSlider.setValue(getSoundEffectSlider());

        // Event listener for background music volume slider
        bgMusicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Map the slider value to the decibel range
            float decibelValue = mapToDecibelRange(newValue.floatValue());

            // Adjust the background music volume in real-time
            setBackgroundMusicVolume(decibelValue);
            setBackgroundMusicSlider(newValue.floatValue());
            soundManager.adjustBackgroundMusicVolume(getBackgroundMusicVolume());
            System.out.println("new bg value(" + newValue + ", " + decibelValue + " dcb)");
        });

        // Event listener for SFX volume slider
        sfxSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Map the slider value to the decibel range
            float decibelValue = mapToDecibelRange(newValue.floatValue());

            // Adjust the sound effect volume in real-time
            setSoundEffectVolume(decibelValue);
            setSoundEffectSlider(newValue.floatValue());
            System.out.println("new sfx value(" + newValue + ", " + decibelValue + " dcb)");
        });

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
            saveSettings();
            soundManager.playSoundEffect(soundManager.clickingSound);
            stage.setScene(previousScene); // Return to the previous scene
        });

        return settingsScene;
    }

    private void loadSettings() {
        // Implement loading settings from file
        try (InputStream input = new FileInputStream("config.properties")) {
            settingProperties.load(input);

            // Load settings from properties
            setScreenWidth(Integer.parseInt(settingProperties.getProperty("screenWidth", "1280")));
            setScreenHeight(Integer.parseInt(settingProperties.getProperty("screenHeight", "720")));
            setBackgroundMusicSlider(Float.parseFloat(settingProperties.getProperty("backgroundMusicSlider", "50")));
            setBackgroundMusicVolume(Float.parseFloat(settingProperties.getProperty("backgroundMusicVolume", String.valueOf(MID_DECIBEL))));
            setSoundEffectSlider(Float.parseFloat(settingProperties.getProperty("soundEffectSlider", "50")));
            setSoundEffectVolume(Float.parseFloat(settingProperties.getProperty("soundEffectVolume", String.valueOf(MID_DECIBEL))));
            // Load other settings...

        } catch (IOException e) {
            System.out.println("Error when loading game setting:" + e.getMessage());
        }
    }
    private void saveSettings() {
        // Implement saving settings to file
        try (OutputStream output = new FileOutputStream("config.properties")) {
            // Save settings to properties
            settingProperties.setProperty("screenWidth", String.valueOf(getScreenWidth()));
            settingProperties.setProperty("screenHeight", String.valueOf(getScreenHeight()));
            settingProperties.setProperty("backgroundMusicVolume", String.valueOf(getBackgroundMusicVolume()));
            settingProperties.setProperty("backgroundMusicSlider", String.valueOf(getBackgroundMusicSlider()));
            settingProperties.setProperty("soundEffectVolume", String.valueOf(getSoundEffectVolume()));
            settingProperties.setProperty("soundEffectSlider", String.valueOf(getSoundEffectSlider()));
            // Save other settings...

            settingProperties.store(output, "Game Settings");

        } catch (IOException e) {
            System.out.println("Error when saving game settings:" + e.getMessage());
        }
    }

    public void saveGame() {
        // save the game progression
        try (OutputStream output = new FileOutputStream("config.progression")) {
            // Save settings to properties
            gameProperties.setProperty("HighScore", String.valueOf(highScore));

            gameProperties.store(output, "Game Progression");

        } catch (IOException e) {
            System.out.println("Error when saving game progress:" + e.getMessage());
        }
    }

    public void loadGame() {
        // load the game progression
        try (InputStream input = new FileInputStream("config.progression")) {
            gameProperties.load(input);

            // Load settings from properties
            setHighScore(Integer.parseInt(gameProperties.getProperty("HighScore", "0")));


        } catch (IOException e) {
            System.out.println("Error when loading game progression:" + e.getMessage());
        }
    }
}
