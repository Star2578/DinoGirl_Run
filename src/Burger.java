import java.util.Random;

public class Burger extends Obstacle {
    public Burger() {
        name = "Burger";
        width = 60;
        height = 60;
        setType("Score");
        setTextureFromPath("Sprites/Obstacle_Sprites/Burger.png");
        setAdditionalScore(100);
    }
}
