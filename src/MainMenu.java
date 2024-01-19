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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        int screenWidth = gameManager.getScreenWidth();
        int screenHeight = gameManager.getScreenHeight();

        primaryStage.setResizable(false); // Set window non-resizable
        primaryStage.setMaximized(false); // Disable maximizing

        StackPane root = new StackPane();

        // Components that would be in the scene
        Text version = new Text("v" + gameManager.version);
        VBox versionContainer = new VBox(version);
        versionContainer.setAlignment(Pos.BOTTOM_RIGHT);
        versionContainer.setPadding(new Insets(5));

        Text title = new Text("Dino Girl: RUN!");
        Text subTitle = new Text("ディノガール: 走れ!");

        title.setStyle("-fx-font-size:50;");
        VBox titleContainer = new VBox(title, subTitle);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setSpacing(5);

        Button start = new Button("Start");
        Button option = new Button("Option");
        Button quit = new Button("Quit");
        VBox buttonContainer = new VBox(start, option, quit);
        buttonContainer.setAlignment(Pos.BOTTOM_LEFT);
        buttonContainer.setSpacing(20);
        buttonContainer.setPadding(new Insets(20));

        root.getChildren().addAll(titleContainer, versionContainer, buttonContainer);

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
            MainGame game = new MainGame();
            game.start(primaryStage);
        });

        // Open Option
        option.setOnAction(actionEvent -> {
            System.out.println("Option");
            Scene setting = gameManager.setting(primaryStage, gameManager.getMainMenuScene());

            primaryStage.setScene(setting);
        });

        // Quit
        quit.setOnAction(actionEvent -> {
            System.out.println("Quit");
            gameManager.saveGame();
            Stage stage = (Stage) quit.getScene().getWindow();
            stage.close();
        });

        menuScene.getRoot().setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size:16;");
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
