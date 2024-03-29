import Obstacles.*;
import Obstacles.Common.*;
import Obstacles.Legendary.*;
import Obstacles.Rare.*;
import Obstacles.Superrare.*;
import Obstacles.Uncommon.*;
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
    SoundManager soundManager = SoundManager.getInstance();

    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;

    private Timeline timeline;
    private double obstacleSpeed = 3.0; // Initial obstacle speed

    private final StackPane ROOT;
    private final Shape COLLIDER;
    private final int[] THRESHOLDS = {0, 5000, 10000, 25000, 50000, 75000}; // Thresholds for each level
    private final double[] SPEED_MULTIPLIER = {0, 1.5, 2.0, 2.5, 3.0, 4.0}; // Corresponding speed multipliers

    private final Obstacle[] legendaryObstacles = {
            new GigaChad()
    };
    private final Obstacle[] superRareObstacles = {
            new David()
    };
    private final Obstacle[] rareObstacles = {
            new Sans()
    };
    private final Obstacle[] uncommonObstacles = {
            new Cake(), new Fake_Cake(), new Croissant()
    };
    private final Obstacle[] commonObstacles = {
            new TNT(), new Cactus(), new Steak(), new Bone(), new UFO(), new Burger(), new Cross(), new Cookie(), new Rotten_Flesh()
    };

    private int currentLevel = 0; // Track the current level
    private int currentScore = 0;
    private int legendaryProbability = 1; // Giga Chad(+10000), Big Mac, Elon Musk, Walter White, Arcane Rune(+5000, +1 block)
    private int superRareProbability = 5; // David (-2077), Spartan Shield(+1 block until hit), Weird Potion(Low Gravity)
    private int rareProbability = 10; // Sans (Will not actually hit you "Miss!"), Emiya, Legosi, Syringe(+1000)
    private int uncommonProbability = 34; // Cake(+500), Emiya's blades, Ezreal, Croissant(+250), Fake_Cake(Kill)
    private int commonProbability = 50; // TNT, Steak(+50), Cactus, Bone, UFO, Hamburger(+100), Cross, Cookie(+10), Rotten_Flesh(-100)

    private Timeline spawnObstacleTimeline;
    private Duration spawnInterval = Duration.seconds(2); // Set the interval between obstacles

    public void setSpawnInterval(Duration interval) {
        this.spawnInterval = interval;
        spawnObstacleTimeline.stop(); // Stop the current timeline
        // Create a new timeline with the updated interval
        spawnObstacleTimeline = new Timeline(
                new KeyFrame(spawnInterval, event -> {
                    if (!gameManager.getGameOverStatus() && !gameManager.getPauseStatus()) {
                        startObstacleAnimation(createObstacle());
                    }
                })
        );
        spawnObstacleTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat the spawn timeline indefinitely
        spawnObstacleTimeline.play(); // Start the new spawn timeline
    }
    public ObstacleManager(StackPane root, Shape collider, int screenWidth, int screenHeight) {
        this.ROOT = root;
        this.COLLIDER = collider;
        this.SCREEN_WIDTH = screenWidth;
        this.SCREEN_HEIGHT = screenHeight;

        // Initialize the timeline for spawning obstacles
        spawnObstacleTimeline = new Timeline(
                new KeyFrame(spawnInterval, event -> {
                    if (!gameManager.getGameOverStatus() && !gameManager.getPauseStatus()) {
                        // Check if the game is not over or paused
                        startObstacleAnimation(createObstacle());
                    }
                })
        );
        spawnObstacleTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat the spawn timeline indefinitely
        spawnObstacleTimeline.play(); // Start the spawn timeline
    }

    public void adjustProbabilities(int newCommon, int newUncommon, int newRare, int newSuperRare, int newLegendary) {
        if (newCommon + newUncommon + newRare + newSuperRare + newLegendary > 100) {
            System.out.println("ERROR: Probability exceeded 100!!!");
            return;
        }
        commonProbability = newCommon;
        uncommonProbability = newUncommon;
        rareProbability = newRare;
        superRareProbability = newSuperRare;
        legendaryProbability = newLegendary;
    }

    public Obstacle createObstacle() {
        Random random = new Random();
        int randomNumber = random.nextInt(100); // 100% probability

        Obstacle newObstacle;

        if (randomNumber < commonProbability) {

            // random one of the common obstacle
            System.out.println(randomNumber + " Common");
            int randomCommon = random.nextInt(0, commonObstacles.length);
            newObstacle = commonObstacles[randomCommon].cloneObstacle();

        } else if (randomNumber < commonProbability + uncommonProbability) {

            // random one of the uncommon obstacle
            System.out.println(randomNumber + " Uncommon");
            int randomUncommon = random.nextInt(0, uncommonObstacles.length);
            newObstacle = uncommonObstacles[randomUncommon].cloneObstacle();

        } else if (randomNumber < commonProbability + uncommonProbability + rareProbability) {

            // random one of the rare obstacle
            System.out.println(randomNumber + " Rare");
            int randomRare = random.nextInt(0, rareObstacles.length);
            newObstacle =rareObstacles[randomRare].cloneObstacle();

        } else if (randomNumber < commonProbability + uncommonProbability + rareProbability + superRareProbability) {

            // random one of the super rare obstacle
            System.out.println(randomNumber + " Super Rare");
            int randomSuperRare = random.nextInt(0, superRareObstacles.length);
            newObstacle = superRareObstacles[randomSuperRare].cloneObstacle();

        } else {

            // random one of the legendary obstacle
            System.out.println(randomNumber + " Legendary");
            int randomLegendary = random.nextInt(0, legendaryObstacles.length);
            newObstacle = legendaryObstacles[randomLegendary].cloneObstacle();

        }

        newObstacle.getTexture().setFitHeight(newObstacle.getHeight());
        newObstacle.getTexture().setFitWidth(newObstacle.getWidth());
        newObstacle.getTexture().setPreserveRatio(true);

        return newObstacle;
    }

    public void startObstacleAnimation(Obstacle obstacle) {
        ImageView obstacleTexture = obstacle.getTexture();
        obstacle.isSpawned();
        double spawnPointY = obstacle.spawnPoint;

        ROOT.getChildren().add(obstacleTexture);

        // Set the initial position of the obstacle (e.g., off-screen)
        obstacleTexture.setTranslateX((double) gameManager.getScreenWidth() / 2);
        obstacleTexture.setTranslateY(spawnPointY); // Adjust Y position as needed

        // Define the obstacle movement animation
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.0167), e -> {
                    obstacleTexture.setTranslateX(obstacleTexture.getTranslateX() - obstacleSpeed); // Adjust speed as needed

                    // Check for collision with the Dino girl
                    if (obstacleTexture.getBoundsInParent().intersects(COLLIDER.getBoundsInParent()) && !obstacle.isDisable()) {
                        handleObstacleCollision(obstacle, obstacleTexture);
                    }

                    // Remove obstacle when off-screen
                    if (obstacleTexture.getTranslateX() < ((double) SCREEN_WIDTH / -2)) {
                        ROOT.getChildren().remove(obstacleTexture);
                    }
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE); // Repeat the animation indefinitely
        timeline.play();
    }

    private void handleObstacleCollision(Obstacle obstacle, ImageView currentObj) {
        String type = obstacle.getType();

        if (Objects.equals(type, "Score")) {
            currentScore += obstacle.getAdditionalScore();
            displayScoreMessage(obstacle.getAdditionalScore(), obstacle.getTexture().getTranslateX(), obstacle.getTexture().getTranslateY());
            ROOT.getChildren().remove(currentObj);
            obstacle.setDisable(true);
        } else if (Objects.equals(type, "Damage")) {
            // Handle Game Over
            timeline.stop(); // Stop the Game
            displayGameOver(obstacle.getTexture());
        } else if (Objects.equals(type, "Trap")) {
            soundManager.playSoundEffect(obstacle.soundPath);
            obstacle.trapActivated(ROOT, obstacle.getTexture());
        }
    }

    // Method to update the speed based on the score
    public void updateSpeed(int score) {
        currentScore += score;

        int nextLevel = currentLevel + 1;
        if (nextLevel < THRESHOLDS.length && currentScore >= THRESHOLDS[nextLevel]) {
            currentLevel = nextLevel;
            obstacleSpeed = 5.0 * SPEED_MULTIPLIER[currentLevel];
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
        if (currentScore > gameManager.highScore)
            gameManager.highScore = currentScore;
        gameManager.deathCount += 1;
        gameManager.saveGame();

        Random random = new Random();
        int randomGameOverSound = random.nextInt(0, soundManager.gameOverSounds.length);
        soundManager.playSoundEffect(soundManager.gameOverSounds[randomGameOverSound]);

        Text gameOverText = new Text("Game Over!");
        gameOverText.setStyle("-fx-font-size: 80;");

        Text deathCountText = new Text("\uD83D\uDC80 " + gameManager.deathCount);
        deathCountText.setStyle("-fx-font-size: 40;");

        Text scoreText = new Text("Your Score is " + currentScore);
        scoreText.setStyle("-fx-font-size: 40;");

        Button restart = new Button("Restart");

        VBox gameOverLayout = new VBox(20); // Set the spacing between elements
        gameOverLayout.setAlignment(Pos.CENTER); // Center align the VBox content

        // Add the "Game Over" text and the restart button to the VBox
        gameOverLayout.getChildren().addAll(gameOverText, deathCountText, scoreText, restart);

        // Set the alignment of the VBox within the StackPane
        StackPane.setAlignment(gameOverLayout, Pos.CENTER);

        // Add the VBox containing both elements to the root StackPane
        ROOT.getChildren().add(gameOverLayout);

        gameManager.setGameOverStatus(true);

        restart.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            // Check if the event was triggered by a primary mouse click
            if (event.getButton() == MouseButton.PRIMARY) {
                soundManager.playSoundEffect(soundManager.clickingSound);
                gameManager.setGameOverStatus(false);
                ROOT.getChildren().remove(gameOverLayout);
                currentScore = 0;

                // reset speed
                obstacleSpeed = 5.0;
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
        if (additionalScore >= 0) {
            soundManager.playSoundEffect(soundManager.randomSFX(new String[]{
                    "src/Sounds/Retro PickUp 10.wav", "src/Sounds/Retro PickUp 18.wav"
            }));
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
        } else {
            soundManager.playSoundEffect(soundManager.randomSFX(new String[] {
                    "src/Sounds/Alarm/Alarm_03.wav", "src/Sounds/Alarm/Alarm_44.wav", "src/Sounds/Alarm/Alarm_47.wav"
            }));
            Text scoreMessage = new Text("" + additionalScore);
            scoreMessage.setStyle("-fx-font-size: 30; -fx-fill: #ff0000; -fx-font-weight: bold;");

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
    }

    public void setCurrentScore(int currentScore) { this.currentScore = currentScore; }
    public int getCurrentScore() { return currentScore; }
}
