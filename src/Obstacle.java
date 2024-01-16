import javafx.scene.image.ImageView;

public class Obstacle {
    private String name;
    private int size;
    private ImageView textTure;

    public Obstacle(String name, String textTurePath) {
        setName(name);
        this.textTure = new ImageView(textTurePath);
    }

    public void setName(String name) { this.name = name; }
    public void setTextTure(ImageView textTure) { this.textTure = textTure; }

    public void setSize(int size) { this.size = size; }

    public String getName() { return name; }
    public ImageView getTextTure() { return textTure; }

    public int getSize() { return size; }

    public void behavior() {
        // for override
    }
}
