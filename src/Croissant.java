public class Croissant extends Obstacle{
    public Croissant() {
        name = "Croissant";
        width = 60;
        height = 60;
        setType("Score");
        setTextureFromPath("Sprites/Obstacle_Sprites/Croissant.png");
        setAdditionalScore(250);
    }
}
