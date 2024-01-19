public class David extends Obstacle{
    public David() {
        name = "David Martinez";
        width = 180;
        height = 200;
        setType("Score");
        setAdditionalScore(-2077);
        setTextureFromPath("Sprites/Secret/David.png");
    }

    @Override
    public void obstacleInfo() {
        defaultGround = 120;
        defaultAir = 40;
        super.obstacleInfo();
    }
}
