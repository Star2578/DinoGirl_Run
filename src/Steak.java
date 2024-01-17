import java.util.Random;

public class Steak extends Obstacle{
    public Steak() {
        name = "Steak";
        width = 50;
        height = 50;
        setType("Score");
        setTextureFromPath("Sprites/Obstacle_Sprites/Steak.png");
        setAdditionalScore(50);
    }
}
