import javafx.scene.image.ImageView;

public class Obstacle {
    private String name;
    private int size;
    private String type;
    private int additionalScore;
    private ImageView textTure;

    public Obstacle(String name, String textTurePath) {
        setName(name);
        this.textTure = new ImageView(textTurePath);
    }

    public void setName(String name) { this.name = name; }
    public void setTextTure(ImageView textTure) { this.textTure = textTure; }
    public void setType(String type) { this.type = type; }
    public void setSize(int size) { this.size = size; }
    public void setAdditionalScore(int additionalScore) { this.additionalScore = additionalScore; }

    public String getName() { return name; }
    public ImageView getTextTure() { return textTure; }
    public String getType() { return type; }
    public int getSize() { return size; }
    public int getAdditionalScore() { return additionalScore; }

    public void behavior() {
        // for override
    }
}
