public class Bone extends Obstacle{
    public Bone() {
        name = "Bone";
        width = 100;
        height = 50;
        setType("Damage");
        setTextureFromPath("Sprites/Obstacle_Sprites/Bone.png");
        spawnOffsetY = 50;
    }
}