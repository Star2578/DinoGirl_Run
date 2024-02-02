import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.util.Duration;

import java.util.Random;

public class MainMenu extends Application {
    GameManager gameManager = GameManager.getInstance();
    SoundManager soundManager = SoundManager.getInstance();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        int screenWidth = gameManager.getScreenWidth();
        int screenHeight = gameManager.getScreenHeight();

        primaryStage.setResizable(false); // Set window non-resizable
        primaryStage.setMaximized(false); // Disable maximizing

        // Play background music
        soundManager.playBackgroundMusic(soundManager.backgroundMusicPath);

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #32a852;");

        // Components that would be in the scene
        Text version = new Text("v" + gameManager.version);
        VBox versionContainer = new VBox(version);
        versionContainer.setAlignment(Pos.BOTTOM_RIGHT);
        versionContainer.setPadding(new Insets(5));

        Text title = new Text("Dino Girl: RUN!");
        Text subTitle = new Text("ディノガール: 走れ!");

        title.setStyle("-fx-font-size:50;");
        VBox titleContainer = new VBox(title, subTitle);
        titleContainer.setAlignment(Pos.TOP_LEFT);
        titleContainer.setSpacing(5);
        titleContainer.setPadding(new Insets(20));

        VBox highScoreContainer = new VBox();
        Text highScore = new Text("High Score: " + gameManager.highScore);
        highScore.getStyleClass().add("high-score-text");
        highScoreContainer.getChildren().add(highScore);
        highScoreContainer.setAlignment(Pos.TOP_RIGHT);
        highScoreContainer.setPadding(new Insets(20));

        Button start = new Button("Start");
        Button option = new Button("Option");
        Button skin = new Button("Skins");
        Button achievement = new Button("Achievements");
        Button quit = new Button("Quit");
        start.getStyleClass().add("menu-button-1");
        option.getStyleClass().add("menu-button-1");
        skin.getStyleClass().add("menu-button-1");
        achievement.getStyleClass().add("menu-button-1");
        quit.getStyleClass().add("menu-button-1");
        VBox buttonContainer = new VBox(start, option, skin, achievement, quit);
        buttonContainer.setAlignment(Pos.BOTTOM_LEFT);
        buttonContainer.setSpacing(20);
        buttonContainer.setPadding(new Insets(20));

        root.getChildren().addAll(titleContainer, versionContainer, highScoreContainer);
        root.getChildren().add(buttonContainer);

        addChangingDropShadow(title);

        // create new Scene from SceneManager
        SceneManager.setScreenSize(screenWidth, screenHeight);
        Scene menuScene = SceneManager.createScene(root);
        gameManager.setMainMenuScene(menuScene);

        primaryStage.setScene(menuScene);
        primaryStage.setTitle("Dino Girl Game");

        // Start the Game
        start.setOnAction(actionEvent -> {
            System.out.println("Starting!");
            soundManager.playSoundEffect(soundManager.clickingSound);
            MainGame game = new MainGame();
            game.start(primaryStage);
        });

        // Open Option
        option.setOnAction(actionEvent -> {
            System.out.println("Option");
            Scene setting = gameManager.setting(primaryStage, gameManager.getMainMenuScene());
            soundManager.playSoundEffect(soundManager.clickingSound);

            primaryStage.setScene(setting);
        });

        skin.setOnAction(actionEvent -> {
            System.out.println("Skins");
            Scene skinMenu = gameManager.SkinMenu(primaryStage, gameManager.getMainMenuScene());
            soundManager.playSoundEffect(soundManager.clickingSound);

            primaryStage.setScene(skinMenu);
        });

        // Quit
        quit.setOnAction(actionEvent -> {
            System.out.println("Quit");
            soundManager.playSoundEffect(soundManager.clickingSound);
            gameManager.saveGame();
            Stage stage = (Stage) quit.getScene().getWindow();
            stage.close();
        });

        menuScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.show();
    }

    private void addChangingDropShadow(Text text) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(0);
        dropShadow.setOffsetX(2);
        dropShadow.setOffsetY(2);
        text.setEffect(dropShadow);

        // Create a Timeline to change the drop shadow color randomly
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.25), event -> {
                    dropShadow.setColor(generateRandomColor());
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private Color generateRandomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}
