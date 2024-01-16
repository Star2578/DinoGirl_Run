import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.shape.Shape;

import java.util.Objects;
import java.util.Random;

public class ObstacleManager {
    GameManager gameManager = GameManager.getInstance();
    private final StackPane root;
    private final Shape collider;
    private final int screenWidth;
    private final int screenHeight;
    private ImageView obstacle;
    private Timeline timeline;
    private double obstacleSpeed = 3.0; // Initial obstacle speed
    private final int[] thresholds = {0, 100, 500, 1000, 5000, 10000}; // Thresholds for each level
    private final double[] speedMultipliers = {0, 1.5, 2.0, 2.5, 3.0, 4.0}; // Corresponding speed multipliers
    private final String[] obstacleNames = {"TNT", "Cactus"};
    private final String[] obstacleTextures = {"Sprites/TNT.png", "Sprites/Cactus.png"};
    private Obstacle newObstacle;
    private int currentRandomNumber;
    private int currentLevel = 0; // Track the current level
    private int currentScore = 0;
    private double widthSize;
    private double heightSize;
    private double defaultSpawnY;

    public ObstacleManager(StackPane root, Shape collider, int screenWidth, int screenHeight) {
        this.root = root;
        this.collider = collider;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public Obstacle createObstacle() {
        Random random = new Random();
        currentRandomNumber = random.nextInt(0, obstacleNames.length);
        newObstacle = new Obstacle(obstacleNames[currentRandomNumber], obstacleTextures[currentRandomNumber]) {
            @Override
            public void behavior() {
                String name = obstacleNames[currentRandomNumber];
                defaultSpawnY = 150;

                if (Objects.equals(name, "TNT")) {
                    // TODO: TNT Behavior
                    widthSize = 50;
                    heightSize = 50;
                }

                if (Objects.equals(name, "Cactus")) {
                    // TODO: Cactus Behavior
                    widthSize = 50;
                    heightSize = 100;
                    defaultSpawnY -= (heightSize/3);
                }
                newObstacle.getTextTure().setFitWidth(widthSize);
                newObstacle.getTextTure().setFitHeight(heightSize);
                newObstacle.getTextTure().setPreserveRatio(true);

                System.out.println("Spawned " + name);
            }
        };


        return  newObstacle;
    }

    public void startObstacleAnimation(Obstacle obstacle) {
        ImageView obstacleTexture = obstacle.getTextTure();
        obstacle.behavior();

        root.getChildren().add(obstacleTexture);

        // Set the initial position of the obstacle (e.g., off-screen)
        obstacleTexture.setTranslateX((double) gameManager.getScreenWidth() / 2);
        obstacleTexture.setTranslateY(defaultSpawnY); // Adjust Y position as needed

        // Define the obstacle movement animation
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.01), e -> {
                    obstacleTexture.setTranslateX(obstacleTexture.getTranslateX() - obstacleSpeed); // Adjust speed as needed

                    // Check for collision with the Dino girl
                    if (obstacleTexture.getBoundsInParent().intersects(collider.getBoundsInParent())) {
                        // Handle collision (e.g., game over logic)
                        // System.out.println("Collision!");

                        // Handle Game Over
                        timeline.stop(); // Stop the Game
                        displayGameOver(obstacleTexture);
                    }

                    // Remove obstacle when off-screen
                    if (obstacleTexture.getTranslateX()  < ((double) screenWidth / -2)) {
                        root.getChildren().remove(obstacleTexture);
                        // System.out.println("Vanished!");
                        timeline.stop();
                        startObstacleAnimation(createObstacle());
                    }
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE); // Repeat the animation indefinitely
        timeline.play();
    }

    // Method to update the speed based on the score
    public void updateSpeed(int score) {
        currentScore = score;

        int nextLevel = currentLevel + 1;
        if (nextLevel < thresholds.length && score >= thresholds[nextLevel]) {
            currentLevel = nextLevel;
            obstacleSpeed = 3.0 * speedMultipliers[currentLevel];
        }
    }

    public void setPause(boolean status) {
        if (status) {
            timeline.stop();
        } else {
            timeline.play();
        }
    }

    public void displayGameOver(ImageView deleteMe) {
        Text gameOverText = new Text("Game Over!");
        gameOverText.setStyle("-fx-font-size: 80;");

        Text scoreText = new Text("Your Score is " + currentScore);
        scoreText.setStyle("-fx-font-size: 40;");

        Button restart = new Button("Restart");

        VBox gameOverLayout = new VBox(20); // Set the spacing between elements
        gameOverLayout.setAlignment(Pos.CENTER); // Center align the VBox content

        // Add the "Game Over" text and the restart button to the VBox
        gameOverLayout.getChildren().addAll(gameOverText, scoreText, restart);

        // Set the alignment of the VBox within the StackPane
        StackPane.setAlignment(gameOverLayout, Pos.CENTER);

        // Add the VBox containing both elements to the root StackPane
        root.getChildren().add(gameOverLayout);

        gameManager.setGameOverStatus(true);

        restart.setOnAction(actionEvent -> {
            gameManager.setGameOverStatus(false);
            root.getChildren().remove(gameOverLayout);
            currentScore = 0;

            // Stop the current obstacle animation
            timeline.stop();
            root.getChildren().remove(deleteMe); // Remove existing obstacles

            // Restart the game loop
            startObstacleAnimation(createObstacle());
        });
    }
}
