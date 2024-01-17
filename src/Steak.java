public class Steak extends Obstacle{
    public Steak() {
        name = "Steak";
        width = 50;
        height = 50;
        setType("Score");
        setTextureFromPath("Sprites/Obstacle/Steak.png");
        setAdditionalScore(50);
    }
}
