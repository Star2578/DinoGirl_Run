package Obstacles;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.Random;

public class Obstacle {
    protected String name;
    protected int height;
    protected int width;
    protected String type;
    protected int additionalScore;
    protected ImageView texture;
    public double spawnPoint;
    protected double defaultGround = 150;
    protected double defaultAir = 50;

    public Obstacle() {

    }

    public Obstacle(String name, String textTurePath) {
        setName(name);
        this.texture = new ImageView(textTurePath);
    }

    public void setName(String name) { this.name = name; }
    public void setTexture(ImageView texture) { this.texture = texture; }
    public void setTextureFromPath(String texturePath) { this.texture = new ImageView(texturePath); }
    public void setType(String type) { this.type = type; }
    public void setHeight(int height) {
        if (height < 0) height = 0;
        this.height = height;
    }
    public void setWidth(int width) {
        if (width < 0) width = 0;
        this.width = width;
    }
    public void setAdditionalScore(int additionalScore) { this.additionalScore = additionalScore; }
    public void setDefaultAir(double defaultAir) { this.defaultAir = defaultAir; }
    public void setDefaultGround(double defaultGround) { this.defaultGround = defaultGround; }

    public String getName() { return name; }
    public ImageView getTexture() { return texture; }
    public String getType() { return type; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getAdditionalScore() { return additionalScore; }
    public double getDefaultAir() { return defaultAir; }
    public double getDefaultGround() { return defaultGround; }

    public void obstacleInfo() {
        // for override
        // use for setup some random spawn point
        Random random = new Random();
        int dif = random.nextInt(0, 50);
        if (dif <= 25) {
            spawnPoint = defaultGround;
        } else {
            spawnPoint = defaultAir;
        }
    }

    public void behavior(StackPane root, ImageView obstacle) {
        // for override
    }

    public Obstacle clone() {
        Obstacle newObstacle = new Obstacle();
        newObstacle.setName(this.getName());
        newObstacle.setTexture(this.getTexture());
        newObstacle.setType(this.getType());
        newObstacle.setHeight(this.getHeight());
        newObstacle.setWidth(this.getWidth());
        newObstacle.setAdditionalScore(this.getAdditionalScore());
        newObstacle.setDefaultGround(this.getDefaultGround());
        return newObstacle;
    }
}
