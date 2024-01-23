import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class MainGame {
    GameManager gameManager = GameManager.getInstance();
    SoundManager soundManager = SoundManager.getInstance();

    private final double GRAVITY = 0.4;
    private final double CROUCH_GRAVITY = GRAVITY * 2;
    private double xVelocity = 0;
    private double yVelocity = 0;
    private boolean isJumping = false;
    private boolean isCrouching = false;
    private final double JUMP_STRENGTH = -15;
    private final double MOVEMENT_SPEED = 10;
    private AnimationTimer gameLoop;
    private double groundPos;
    private boolean isMoveLeft = false;
    private boolean isMoveRight = false;

    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        primaryStage.setResizable(false); // Set window non-resizable
        primaryStage.setMaximized(false); // Disable maximizing

        // Create the score text
        Text scoreText = new Text("00000000");
        scoreText.setFill(Color.BLACK);
        scoreText.setStyle("-fx-font-size: 20;");
        scoreText.setTranslateX((double) -gameManager.getScreenWidth() / 2 + 50); // Adjust X position as needed
        scoreText.setTranslateY((double) -gameManager.getScreenHeight() / 2 + 40); // Adjust Y position as needed
        root.getChildren().add(scoreText);

        // Create Ground Texture
        VBox groundContainer = new VBox();
        HBox grassContainer = new HBox();
        grassContainer.setSpacing(-1);
        int size = 50;

        for (int i = 0; i <= gameManager.getScreenWidth() / size; i++) {
            ImageView grass = new ImageView("Sprites/Map/Grass_Block.jpg");
            grass.setFitHeight(size);
            grass.setFitWidth(size);
            grass.setPreserveRatio(true);
            grassContainer.getChildren().add(grass);
        }
        groundContainer.getChildren().add(grassContainer);
        for (int j = 0; j < gameManager.getScreenHeight() / (size * 4); j++) {
            HBox dirtContainer = new HBox();
            dirtContainer.setSpacing(-1);

            for (int i = 0; i <= gameManager.getScreenWidth() / size; i++) {
                ImageView dirt = new ImageView("Sprites/Map/Dirt_Block.png");
                dirt.setFitWidth(size);
                dirt.setFitHeight(size);
                dirt.setPreserveRatio(true);
                dirtContainer.getChildren().add(dirt);
            }
            groundContainer.getChildren().add(dirtContainer);
        }

        root.getChildren().add(groundContainer);
        groundContainer.setAlignment(Pos.CENTER);
        groundContainer.setSpacing(-1);
        if (gameManager.getScreenHeight() > 720 || gameManager.getScreenWidth() > 1280) {
            groundContainer.setTranslateY(295);
        } else {
            groundContainer.setTranslateY(270);
        }

        // Create and add the T-Rex character (you'll need to provide the image path)
        ImageView dinoGirlView = new ImageView(new Image("Sprites/Player/Dino.png"));
        double maxWidth = 150; // Set the maximum width (adjust as needed)
        double maxHeight = 150; // Set the maximum height (adjust as needed)

        double crouchHeight = maxHeight / 2;

        dinoGirlView.setFitWidth(maxWidth);
        dinoGirlView.setFitHeight(maxHeight);
        dinoGirlView.setPreserveRatio(true);

        // Create the capsule-like collider using rectangles and circles
        double capsuleWidth = maxWidth * 0.6; // Adjust capsule width as needed
        double capsuleHeight = maxHeight * 0.8; // Adjust capsule height as needed

        Rectangle mainRectangle = new Rectangle(capsuleWidth, capsuleHeight);
        mainRectangle.setArcWidth(capsuleWidth);
        mainRectangle.setArcHeight(capsuleWidth);

        Circle upperCircle = new Circle(capsuleWidth / 2);
        upperCircle.setCenterX(capsuleWidth / 2);
        upperCircle.setCenterY(capsuleWidth / 2);

        Circle lowerCircle = new Circle(capsuleWidth / 2);
        lowerCircle.setCenterX(capsuleWidth / 2);
        lowerCircle.setCenterY(capsuleHeight - capsuleWidth / 2);

        Shape capsuleShape = Shape.union(mainRectangle, Shape.union(upperCircle, lowerCircle));
        capsuleShape.setFill(Color.BLUE); // Hide the capsule shape
        capsuleShape.setVisible(false);

        // Set initial position and binding for the capsule shape to the dino girl
        capsuleShape.translateXProperty().bind(dinoGirlView.translateXProperty().add((maxWidth - capsuleWidth) / 2 - 25));
        capsuleShape.translateYProperty().bind(dinoGirlView.translateYProperty().add((maxHeight - capsuleHeight) / 2));

        root.getChildren().add(dinoGirlView);
        root.getChildren().add(capsuleShape);

        // Set T-Rex initial position (you may need to adjust this)
        double initialYPosition = 100; // Adjust initial Y position as needed
        groundPos = initialYPosition;
        dinoGirlView.setTranslateX(-300);
        dinoGirlView.setTranslateY(initialYPosition);

        // Teletubbies Sun
        StackPane topRight = new StackPane();
        ImageView sun = new ImageView("Sprites/Map/Funny_Sun.png");
        sun.setPreserveRatio(true);
        int sunSize = (30 * gameManager.getScreenHeight()) / 100;
        sun.setFitHeight(sunSize);
        sun.setFitWidth(sunSize);
        topRight.getChildren().add(sun);
        topRight.setAlignment(Pos.TOP_RIGHT);
        root.getChildren().add(topRight);


        // Create the scene
        Scene gameScene = SceneManager.createScene(root);
        gameScene.getRoot().setStyle("-fx-background-color: #6EB1FF");

        // Handle obstacles spawning
        ObstacleManager obstacleManager = new ObstacleManager(root, capsuleShape, gameManager.getScreenWidth(), gameManager.getScreenHeight());
        obstacleManager.startObstacleAnimation(obstacleManager.createObstacle());

        // Pause Overlay
        Text pausedText = new Text("PAUSED");
        pausedText.setFont(new Font(40));
        pausedText.setFill(Color.WHITE);

        Button toMenu = new Button("To Main Menu");
        Button option = new Button("Option");
        Button quit = new Button("Quit");
        VBox pauseContainer = new VBox(pausedText, toMenu, option, quit);
        pauseContainer.setAlignment(Pos.CENTER);
        pauseContainer.setSpacing(10);

        toMenu.setOnAction(actionEvent -> {
            System.out.println("To Menu(Paused)");
            soundManager.playSoundEffect(soundManager.clickingSound);
            primaryStage.setScene(gameManager.getMainMenuScene());
        });

        option.setOnAction(actionEvent -> {
            System.out.println("Option(Paused)");
            soundManager.playSoundEffect(soundManager.clickingSound);
            primaryStage.setScene(gameManager.setting(primaryStage, gameScene));
        });

        quit.setOnAction(actionEvent -> {
            System.out.println("Quit(Paused)");
            soundManager.playSoundEffect(soundManager.clickingSound);
            Stage stage = (Stage) quit.getScene().getWindow();
            stage.close();
        });

        StackPane pauseOverlay = new StackPane();
        pauseOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // Adjust darkness by changing alpha value

        pauseOverlay.getChildren().addAll(pauseContainer);
        pauseOverlay.setAlignment(Pos.CENTER);
        pauseOverlay.setVisible(false);

        root.getChildren().add(pauseOverlay);

        // Handle input actions
        gameScene.setOnKeyPressed(event -> {
            handleMovementAndJumping(event.getCode());

            // Crouch
            if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.CONTROL) {
                isCrouching = true;
            }

            // Pause
            if (event.getCode() == KeyCode.ESCAPE) {
                if (!gameManager.getPauseStatus()) {
                    gameLoop.stop(); // Pause the game loop
                    gameManager.setPauseStatus(true);
                    pauseOverlay.setVisible(true); // Show the pause overlay
                } else {
                    gameLoop.start(); // Resume the game loop
                    gameManager.setPauseStatus(false);
                    pauseOverlay.setVisible(false); // Hide the pause overlay
                }

                obstacleManager.setPause(gameManager.getPauseStatus());
            }

            // Debug tool Press F3 to show player hitbox!
            if (event.getCode() == KeyCode.F3) {
                capsuleShape.setVisible(!capsuleShape.isVisible());
            }
            // F1 to show winner scene
            if (event.getCode() == KeyCode.F1) {
                youAreTheWinner(primaryStage);
            }
        });

        gameScene.setOnKeyReleased(event -> {
            handleKeyReleased(event.getCode());
        });

        // Game loop using AnimationTimer
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // if not Game Over
                int score = 0;

                if (!gameManager.getGameOverStatus()) {
                    // Apply gravity
                    if (isCrouching) {
                        yVelocity += CROUCH_GRAVITY; // Increase the fall speed when crouching
                    } else {
                        yVelocity += GRAVITY; // Default gravity
                    }
                    if (isMoveLeft) xVelocity = -MOVEMENT_SPEED;
                    if (isMoveRight) xVelocity = MOVEMENT_SPEED;
                    if (!isMoveLeft && !isMoveRight) xVelocity = 0;

                    // Update Dino girl's position
                    dinoGirlView.setTranslateY(dinoGirlView.getTranslateY() + yVelocity);
                    dinoGirlView.setTranslateX(dinoGirlView.getTranslateX() + xVelocity);

                    // Make her not go off the screen
                    if (dinoGirlView.getTranslateX() < -((double) gameManager.getScreenWidth() / 2) + maxWidth / 2) {
                        dinoGirlView.setTranslateX(-((double) gameManager.getScreenWidth() / 2) + maxWidth / 2);
                    } else if (dinoGirlView.getTranslateX() > ((double) gameManager.getScreenWidth() / 2) - maxWidth / 2) {
                        dinoGirlView.setTranslateX(((double) gameManager.getScreenWidth() / 2) - maxWidth / 2);
                    }

                    // Check if Dino girl is on the ground
                    if (dinoGirlView.getTranslateY() >= groundPos) {
                        dinoGirlView.setTranslateY(groundPos);
                        yVelocity = 0;
                        isJumping = false;
                    }

                    // Update Dino girl animation
                    if (isCrouching) {
                        dinoGirlView.setPreserveRatio(false);
                        dinoGirlView.setFitHeight(crouchHeight);

                        groundPos = initialYPosition + (maxHeight - crouchHeight)/2;
                    } else {
                        dinoGirlView.setPreserveRatio(true);
                        dinoGirlView.setFitHeight(maxHeight);
                        groundPos = initialYPosition;
                    }

                    // Handle Score
                    score = 1;
                    String formattedScore = String.format("%05d", obstacleManager.getCurrentScore()); // Leading zeros for 7 digits
                    scoreText.setText(formattedScore);
                    obstacleManager.updateSpeed(score); // Update the score for speed multiplier

                    if (obstacleManager.getCurrentScore() >= 99999) {
                        // Win!
                        gameManager.saveGame();
                        youAreTheWinner(primaryStage);
                    }
                }
            }
        };

        // Start the game loop
        gameLoop.start();

        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Dino Girl Game");
        primaryStage.show();
    }

    private void handleMovementAndJumping(KeyCode keyCode) {
        switch (keyCode) {
            case A:
            case LEFT:
                isMoveLeft = true;
                break;
            case D:
            case RIGHT:
                isMoveRight = true;
                break;
            case W:
            case SPACE:
            case UP:
                if (!isJumping) {
                    soundManager.playSoundEffect(soundManager.jumpingSound);
                    yVelocity = JUMP_STRENGTH;
                    isJumping = true;
                }
                break;
        }
    }

    private void handleKeyReleased(KeyCode keyCode) {
        switch (keyCode) {
            case A:
            case LEFT:
                isMoveLeft = false;
                break;
            case D:
            case RIGHT:
                isMoveRight = false;
                break;
            case S:
            case DOWN:
            case CONTROL:
                isCrouching = false;
                break;
        }
    }

    private void youAreTheWinner(Stage stage) {
        gameLoop.stop();
        StackPane root = new StackPane();
        Scene winScene = SceneManager.createScene(root);
        root.setStyle("-fx-background-color: black");

        Text congratsText = new Text("Congratulations!");
        congratsText.setFont(new Font(40));

        // Create a timeline to cycle through rainbow colors
        Timeline rainbowText = new Timeline(
                new KeyFrame(Duration.seconds(0.1), event -> {
                    congratsText.setFill(Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
                })
        );
        rainbowText.setCycleCount(Timeline.INDEFINITE);
        rainbowText.play();

        root.getChildren().add(congratsText);

        root.setOpacity(0);
        stage.setScene(winScene);

        FadeTransition fadeMainScene = new FadeTransition(Duration.seconds(3), root);
        fadeMainScene.setFromValue(0);
        fadeMainScene.setToValue(1);
        fadeMainScene.play();
    }
}
