import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.shape.Shape;

import java.util.Objects;
import java.util.Random;

public class ObstacleManager {
    GameManager gameManager = GameManager.getInstance();
    private final StackPane ROOT;
    private final Shape COLLIDER;
    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;
    private ImageView obstacle;
    private Timeline timeline;
    private double obstacleSpeed = 3.0; // Initial obstacle speed
    private final int[] THRESHOLDS = {0, 100, 500, 1000, 5000, 10000}; // Thresholds for each level
    private final double[] SPEED_MULTIPLIER = {0, 1.5, 2.0, 2.5, 3.0, 4.0}; // Corresponding speed multipliers
    private final Obstacle[] OBSTACLES = {
            new TNT(), new Cactus(), new Steak(), new Bone()
    };
    private int currentLevel = 0; // Track the current level
    private int currentScore = 0;
    private double widthSize;
    private double heightSize;
    private double defaultSpawnY;

    // TODO: Add obstacle rarity
    // Prepare for rare obstacles in future
    private final int legendaryProbability = 1; // Giga Chad, Big Mac, Elon Musk, Walter White, Arcane Rune(+5000, +1 block)
    private final int superRareProbability = 5; // David, Spartan Shield(+1 block until hit), Weird Potion(Low Gravity)
    private final int rareProbability = 10; // Sans (Will not actually hit you "Miss!"), Emiya, Legosi, Syringe(+1000)
    private final int uncommonProbability = 34; // Cake(+500), Emiya's blades, Ezreal, Croissant(+250), Fake_Cake(Kill)
    private final int commonProbability = 50; // TNT, Steak(+50), Cactus, Bone, UFO, Hamburger(+100), Cross, Cookie(+10), Rotten_Flesh(-100)

    public ObstacleManager(StackPane root, Shape collider, int screenWidth, int screenHeight) {
        this.ROOT = root;
        this.COLLIDER = collider;
        this.SCREEN_WIDTH = screenWidth;
        this.SCREEN_HEIGHT = screenHeight;
    }

    public Obstacle createObstacle() {
        Random random = new Random();
        int randomNumber = random.nextInt(100); // 100% probability

        int currentRandomNumber = random.nextInt(0, OBSTACLES.length);
        Obstacle newObstacle = OBSTACLES[currentRandomNumber];
        newObstacle.getTexture().setFitHeight(newObstacle.getHeight());
        newObstacle.getTexture().setFitWidth(newObstacle.getWidth());
        newObstacle.getTexture().setPreserveRatio(true);

        defaultSpawnY = newObstacle.spawnOffsetY;

        return newObstacle;
    }

    public void startObstacleAnimation(Obstacle obstacle) {
        ImageView obstacleTexture = obstacle.getTexture();
        obstacle.obstacleInfo();

        ROOT.getChildren().add(obstacleTexture);

        // Set the initial position of the obstacle (e.g., off-screen)
        obstacleTexture.setTranslateX((double) gameManager.getScreenWidth() / 2);
        obstacleTexture.setTranslateY(defaultSpawnY); // Adjust Y position as needed

        // Define the obstacle movement animation
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.01), e -> {
                    obstacleTexture.setTranslateX(obstacleTexture.getTranslateX() - obstacleSpeed); // Adjust speed as needed

                    // Check for collision with the Dino girl
                    if (obstacleTexture.getBoundsInParent().intersects(COLLIDER.getBoundsInParent())) {
                        String type = obstacle.getType();

                        if (Objects.equals(type, "Score")) {
                            currentScore += obstacle.getAdditionalScore();

                            displayScoreMessage(obstacle.getAdditionalScore(), obstacleTexture.getTranslateX(), obstacleTexture.getTranslateY());
                            timeline.stop();

                            // Destroy obj and start new obj timeline
                            ROOT.getChildren().remove(obstacleTexture);
                            startObstacleAnimation(createObstacle());
                        }

                        if (Objects.equals(type, "Damage")) {
                            // Handle Game Over
                            timeline.stop(); // Stop the Game
                            displayGameOver(obstacleTexture);
                        }
                    }

                    // Remove obstacle when off-screen
                    if (obstacleTexture.getTranslateX()  < ((double) SCREEN_WIDTH / -2)) {
                        ROOT.getChildren().remove(obstacleTexture);
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
        currentScore += score;

        int nextLevel = currentLevel + 1;
        if (nextLevel < THRESHOLDS.length && currentScore >= THRESHOLDS[nextLevel]) {
            currentLevel = nextLevel;
            obstacleSpeed = 3.0 * SPEED_MULTIPLIER[currentLevel];
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
        ROOT.getChildren().add(gameOverLayout);

        gameManager.setGameOverStatus(true);

        restart.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            // Check if the event was triggered by a primary mouse click
            if (event.getButton() == MouseButton.PRIMARY) {
                gameManager.setGameOverStatus(false);
                ROOT.getChildren().remove(gameOverLayout);
                currentScore = 0;

                // reset speed
                obstacleSpeed = 3.0;
                currentLevel = 0;

                // Stop the current obstacle animation
                timeline.stop();
                ROOT.getChildren().remove(deleteMe); // Remove existing obstacles

                // Restart the game loop
                startObstacleAnimation(createObstacle());
            }
        });
    }

    // Display a score message for a brief moment
    private void displayScoreMessage(int additionalScore, double posX, double posY) {
        Text scoreMessage = new Text("+" + additionalScore);
        scoreMessage.setStyle("-fx-font-size: 30; -fx-fill: #26ff00; -fx-font-weight: bold;");

        // Add the score message to the root
        ROOT.getChildren().add(scoreMessage);

        scoreMessage.setTranslateX(posX);
        scoreMessage.setTranslateY(posY - 50);

        // Create a timeline to fade out the score message after 1 second
        Timeline scoreMessageTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    ROOT.getChildren().remove(scoreMessage);
                })
        );
        scoreMessageTimeline.play();
    }

    public void setCurrentScore(int currentScore) { this.currentScore = currentScore; }
    public int getCurrentScore() { return currentScore; }
}
