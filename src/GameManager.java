import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GameManager {
    private static final float MIN_DECIBEL = -30.0f;
    private static final float MAX_DECIBEL = 6.0f;
    private static final float MID_DECIBEL = (MAX_DECIBEL + MIN_DECIBEL) / 2;

    private static final String CONFIG_FILE_PATH = "config.properties";
    private static final String PROGRESSION_FILE_PATH = "config.progression";

    private static GameManager instance;
    private SoundManager soundManager;
    private AchievementManager achievementManager;
    private final Properties settingProperties;
    private final Properties gameProperties;

    private int screenWidth;
    private int screenHeight;
    private boolean isGameOver = false;
    private boolean isPause = false;
    private Scene mainMenuScene;

    private float soundEffectVolume = MID_DECIBEL;
    private float soundEffectSlider = 50.0f;
    private float backgroundMusicVolume = MID_DECIBEL;
    private float backgroundMusicSlider = 50.0f;

    public String version = "1.1";

    // Dino Girl's skins
    private String currentSkin;
    public boolean dino_cool = false;
    public boolean dino_deer_eat_meat = false;
    public boolean dino_emiya = false;
    public boolean dino_gaming = false;
    public boolean dino_joji = false;
    public boolean dino_martinez = false;
    public boolean dino_sans = false;
    public boolean dino_space = false;
    public boolean dino_spartan = false;
    private boolean[] unlockedSkins;

    // Progression
    public int highScore = 0; // Better than last time, Beyond Limit
    public int deathCount = 0; // Ouch!, Raging Gamer, Immortal
    public int playCount = 0; // Having fun!, Wasting Time, Please do something more productive
    public int eatCount = 0; // Delicious, Fat *** Dino
    public int meatEatenCount = 0; // Too much meat will hurt your stomach
    public int chadCount = 0; // Giga Dino, Brud is so lucky
    public int davidCount = 0; // -2077, I really want to stay at your house...
    public int sansCount = 0; // Where's tomato sauce?, Always missed
    public int shieldCount = 0; // The shield always broken, This is SPARTAN!
    public int sunClicked = 0; // Clicking Simulator?, Teletubbie fans, Autism
    public int jumpCount = 0; // Wannabe Ninja, Broken Space bar
    public int winCount = 0; // Yay!, But at what cost?, True Gamer

    public GameManager() {
        // default values
        currentSkin = "Sprites/Player/Dino_Default.png";
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
    public void initialize(AchievementManager achievementManager) {
        this.achievementManager = achievementManager;
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
        if (sliderValue == 0) return -80;

        // Assuming the slider value ranges from 0 to 100
        float normalizedValue = sliderValue / 100.0f;

        // Map the normalized value to the decibel range using a logarithmic scale
        return MIN_DECIBEL + normalizedValue * (MAX_DECIBEL - MIN_DECIBEL);
    }

    public Scene setting(Stage stage, Scene previousScene) {
        // Create UI elements for the settings scene
        Button backButton = new Button("Back");
        backButton.getStyleClass().add("menu-button-2");
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
        settingsRoot.getChildren().addAll(backButton, settingsContainer);

        // Create a new scene for settings
        Scene settingsScene = SceneManager.createScene(settingsRoot);
        settingsScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

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
        try (InputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
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
        try (OutputStream output = new FileOutputStream(CONFIG_FILE_PATH)) {
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
        try (OutputStream output = new FileOutputStream(PROGRESSION_FILE_PATH)) {
            // Progression
            gameProperties.setProperty("HighScore", String.valueOf(highScore));
            gameProperties.setProperty("DeathCount", String.valueOf(deathCount));
            gameProperties.setProperty("PlayCount", String.valueOf(playCount));
            gameProperties.setProperty("EatCount", String.valueOf(eatCount));
            gameProperties.setProperty("MeatEatenCount", String.valueOf(meatEatenCount));
            gameProperties.setProperty("ChadCount", String.valueOf(chadCount));
            gameProperties.setProperty("DavidCount", String.valueOf(davidCount));
            gameProperties.setProperty("SansCount", String.valueOf(sansCount));
            gameProperties.setProperty("ShieldCount", String.valueOf(shieldCount));
            gameProperties.setProperty("SunClicked", String.valueOf(sunClicked));
            gameProperties.setProperty("JumpCount", String.valueOf(jumpCount));
            gameProperties.setProperty("WinCount", String.valueOf(winCount));

            // Skin
            gameProperties.setProperty("Skin_Dino_Cool", String.valueOf(dino_cool));
            gameProperties.setProperty("Skin_Dino_Deer_Eat_Meat", String.valueOf(dino_deer_eat_meat));
            gameProperties.setProperty("Skin_Dino_Emiya", String.valueOf(dino_emiya));
            gameProperties.setProperty("Skin_Dino_Gaming", String.valueOf(dino_gaming));
            gameProperties.setProperty("Skin_Dino_Joji", String.valueOf(dino_joji));
            gameProperties.setProperty("Skin_Dino_Martinez", String.valueOf(dino_martinez));
            gameProperties.setProperty("Skin_Dino_Sans", String.valueOf(dino_sans));
            gameProperties.setProperty("Skin_Dino_Space", String.valueOf(dino_space));
            gameProperties.setProperty("Skin_Dino_Spartan", String.valueOf(dino_spartan));

            gameProperties.store(output, "Game Progression");

        } catch (IOException e) {
            System.out.println("Error when saving game progress:" + e.getMessage());
        }
    }

    public void loadGame() {
        // load the game progression
        try (InputStream input = new FileInputStream(PROGRESSION_FILE_PATH)) {
            gameProperties.load(input);

            // Load progression from properties
            highScore = Integer.parseInt(gameProperties.getProperty("HighScore", "0"));
            deathCount = Integer.parseInt(gameProperties.getProperty("DeathCount", "0"));
            playCount = Integer.parseInt(gameProperties.getProperty("PlayCount", "0"));
            eatCount = Integer.parseInt(gameProperties.getProperty("EatCount", "0"));
            meatEatenCount = Integer.parseInt(gameProperties.getProperty("MeatEatenCount", "0"));
            chadCount = Integer.parseInt(gameProperties.getProperty("ChadCount", "0"));
            davidCount = Integer.parseInt(gameProperties.getProperty("DavidCount", "0"));
            sansCount = Integer.parseInt(gameProperties.getProperty("SansCount", "0"));
            shieldCount = Integer.parseInt(gameProperties.getProperty("ShieldCount", "0"));
            sunClicked = Integer.parseInt(gameProperties.getProperty("SunClicked", "0"));
            jumpCount = Integer.parseInt(gameProperties.getProperty("JumpCount", "0"));
            winCount = Integer.parseInt(gameProperties.getProperty("WinCount", "0"));

            dino_cool = Boolean.parseBoolean(gameProperties.getProperty("Skin_Dino_Cool", "false"));
            dino_deer_eat_meat = Boolean.parseBoolean(gameProperties.getProperty("Skin_Dino_Deer_Eat_Meat", "false"));
            dino_emiya = Boolean.parseBoolean(gameProperties.getProperty("Skin_Dino_Emiya", "false"));
            dino_gaming = Boolean.parseBoolean(gameProperties.getProperty("Skin_Dino_Gaming", "false"));
            dino_joji = Boolean.parseBoolean(gameProperties.getProperty("Skin_Dino_Joji", "false"));
            dino_martinez = Boolean.parseBoolean(gameProperties.getProperty("Skin_Dino_Martinez", "false"));
            dino_sans = Boolean.parseBoolean(gameProperties.getProperty("Skin_Dino_Sans", "false"));
            dino_space = Boolean.parseBoolean(gameProperties.getProperty("Skin_Dino_Space", "false"));
            dino_spartan = Boolean.parseBoolean(gameProperties.getProperty("Skin_Dino_Spartan", "false"));


        } catch (IOException e) {
            System.out.println("Error when loading game progression:" + e.getMessage());
        }
    }

    public Image changeSkin() {
        return new Image(currentSkin);
    }

//    public Scene AchievementMenu(Stage stage, Scene previousScene) {
//        Scene achievementScene;
//
//        return achievementScene;
//    }

    private void loadUnlockedSkins() {
        // Load unlocked skins logic here
        // For simplicity, assuming the length of unlockedSkins array is same as number of skins
        unlockedSkins = new boolean[]{
                true, dino_cool, dino_deer_eat_meat, dino_emiya, dino_gaming, dino_joji, dino_martinez, dino_sans,
                dino_space, dino_spartan
        };
    }

    public Scene SkinMenu(Stage stage, Scene previousScene) {
        ScrollPane skinMenuScrollPane = new ScrollPane();
        skinMenuScrollPane.setFitToHeight(true);
        skinMenuScrollPane.setFitToWidth(true);

        TilePane skinMenuRoot = new TilePane();
        skinMenuRoot.setPrefColumns(4);
        skinMenuRoot.setHgap(20);
        skinMenuRoot.setVgap(20);
        skinMenuRoot.setAlignment(Pos.CENTER);

        loadUnlockedSkins();  // Load unlocked skins here

        List<HBox> skinCards = createSkinCards();

        skinMenuRoot.getChildren().addAll(skinCards);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> stage.setScene(previousScene));

        VBox skinMenuVBox = new VBox(skinMenuRoot, backButton);
        skinMenuVBox.setAlignment(Pos.CENTER);

        skinMenuVBox.setStyle("-fx-background-color: #32a852;"); // Adjust Background Color
        skinMenuScrollPane.setContent(skinMenuVBox);

        Scene skinMenuScene = new Scene(skinMenuScrollPane, getScreenWidth(), getScreenHeight());
        skinMenuScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());


        return skinMenuScene;
    }

    private List<HBox> createSkinCards() {
        String[] skinNames = {"Default", "Cool", "Deer", "Emiya", "Gaming", "Joji", "Martinez", "Sans", "Space", "Spartan"};
        Image[] skinImages = loadSkinImages(skinNames);
        String[] skinDescriptions = {"Default Description", "Cool Description", "Deer Description", "Emiya Description", "Gaming Description",
                "Joji Description", "Martinez Description", "Sans Description", "Space Description", "Spartan Description"};

        List<HBox> skinCards = new ArrayList<>();

        for (int i = 0; i < skinImages.length; i++) {
            if (unlockedSkins[i]) {
                HBox skinCard = createSkinCard(skinNames[i], skinDescriptions[i], skinImages[i], i);
                skinCards.add(skinCard);
            }
        }

        return skinCards;
    }

    private Image[] loadSkinImages(String[] names) {
        // Assuming you have images for each skin, you can replace these with your own paths
        Image[] skinImages = new Image[names.length];
        for (int i = 0; i < 10; i++) {
            String imagePath = "Sprites/Player/Dino_" + names[i] + ".png";
            skinImages[i] = new Image(imagePath);
        }
        return skinImages;
    }

    private HBox createSkinCard(String skinName, String description, Image skinImage, int skinIndex) {
        HBox skinCard = new HBox();
        skinCard.setAlignment(Pos.CENTER);
        skinCard.setSpacing(10);

        ImageView imageView = new ImageView(skinImage);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);

        VBox skinInfo = new VBox();
        skinInfo.setAlignment(Pos.CENTER_LEFT);

        Label skinNameLabel = new Label(skinName);
        Label descriptionLabel = new Label(description);

        Button changeSkinButton = new Button("Change Skin");
        changeSkinButton.setOnAction(e -> {
            // Implement skin changing logic here
            if (unlockedSkins[skinIndex]) {
                setCurrentSkin(skinName);
                saveGame();  // Save the current skin
                System.out.println("Changing to " + skinName);
            } else {
                System.out.println("Skin locked: " + skinName);
            }
        });

        skinInfo.getChildren().addAll(skinNameLabel, descriptionLabel);
        skinCard.getChildren().addAll(imageView, skinInfo, changeSkinButton);

        return skinCard;
    }

    private void setCurrentSkin(String skinPath) {
        // Assuming skin paths follow a specific pattern
        currentSkin = "Sprites/Player/Dino_" + skinPath + ".png";
    }
}
