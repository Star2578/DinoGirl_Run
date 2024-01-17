public class Rotten_Flesh extends Obstacle{
    public Rotten_Flesh() {
        name = "Rotten Flesh";
        width = 80;
        height = 80;
        setType("Score");
        setTextureFromPath("Sprites/Obstacle_Sprites/Rotten_Flesh.png");
        setAdditionalScore(-100);
    }
}
