public class TNT extends Obstacle {
    public TNT() {
        name = "TNT";
        width = 50;
        height = 50;
        setType("Damage");
        setTextureFromPath("Sprites/Obstacle_Sprites/TNT.png");
    }

    @Override
    public void obstacleInfo() {
        spawnPoint = defaultGround;
    }
}
