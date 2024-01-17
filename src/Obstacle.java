import javafx.scene.image.ImageView;

public class Obstacle {
    protected String name;
    protected int height;
    protected int width;
    protected String type;
    protected int additionalScore;
    protected ImageView texture;
    protected double spawnOffsetY = 150;

    public Obstacle() {
        this.name = "null Obstacle";
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

    public void setSpawnOffsetY(double spawnOffsetY) { this.spawnOffsetY = spawnOffsetY; }

    public String getName() { return name; }
    public ImageView getTexture() { return texture; }
    public String getType() { return type; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getAdditionalScore() { return additionalScore; }
    public double getSpawnOffsetY() { return spawnOffsetY; }

    public void obstacleInfo() {
        // for override
    }

    public void behavior() {
        // for override
    }
}
