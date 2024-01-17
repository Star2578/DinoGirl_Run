public class Cross extends Obstacle {
    public Cross() {
        name = "Cross";
        width = 50;
        height = 100;
        setType("Damage");
        setTextureFromPath("Sprites/Obstacle_Sprites/Cross.png");
        spawnOffsetY -= (double) height;
    }

    @Override
    public void obstacleInfo() {

    }
}
