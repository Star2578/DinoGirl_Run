public class Rotten_Flesh extends Obstacle{
    public Rotten_Flesh() {
        name = "Rotten Flesh";
        width = 60;
        height = 60;
        setType("Score");
        setTextureFromPath("Sprites/Obstacle_Sprites/Rotten_Flesh.png");
        setAdditionalScore(-100);
    }
}
