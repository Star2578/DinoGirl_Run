import java.util.Random;

public class Cake extends Obstacle{
    public Cake() {
        name = "Cake";
        width = 100;
        height = 100;
        setType("Score");
        setTextureFromPath("Sprites/Obstacle_Sprites/Cake.png");
        setAdditionalScore(500);
    }
}
