public class Cactus extends Obstacle{
    public Cactus() {
        name = "Cactus";
        width = 50;
        height = 100;
        setType("Damage");
        setTextureFromPath("Sprites/Obstacle/Cactus.png");
        spawnOffsetY -= (double) height /3;
    }
}
