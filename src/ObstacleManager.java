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
    private int currentLevel = 0; // Track the current level
    private int currentScore = 0;

    public ObstacleManager(StackPane root, Shape collider, int screenWidth, int screenHeight) {
        this.root = root;
        this.collider = collider;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void createObstacle() {
        obstacle = new ImageView("Sprites/TNT.png"); // Customize the obstacle dimensions
        int size = 50;
        obstacle.setFitWidth(size);
        obstacle.setFitHeight(size);
        obstacle.setPreserveRatio(true);

        root.getChildren().add(obstacle);

        // Set the initial position of the obstacle (e.g., off-screen)
        obstacle.setTranslateX((double) gameManager.getScreenWidth() / 2);
        obstacle.setTranslateY(150); // Adjust Y position as needed

        // Define the obstacle movement animation
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.01), e -> {
                    obstacle.setTranslateX(obstacle.getTranslateX() - obstacleSpeed); // Adjust speed as needed

                    // Check for collision with the Dino girl
                    if (obstacle.getBoundsInParent().intersects(collider.getBoundsInParent())) {
                        // Handle collision (e.g., game over logic)
                        System.out.println("Collision!");

                        // Handle Game Over
                        timeline.stop(); // Stop the Game
                        displayGameOver();
                    }

                    // Remove obstacle when off-screen
                    if (obstacle.getTranslateX()  < ((double) screenWidth / -2)) {
                        root.getChildren().remove(obstacle);
                        System.out.println("Vanished!");
                        timeline.stop();
                        createObstacle();
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

    public void displayGameOver() {
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
            root.getChildren().remove(this.obstacle); // Remove existing obstacles

            // Restart the game loop
            createObstacle();
        });
    }
}
