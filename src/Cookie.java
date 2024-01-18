public class Cookie extends Obstacle{
    public Cookie() {
        name = "Cookie";
        width = 50;
        height = 50;
        setType("Score");
        setAdditionalScore(10);
        setTextureFromPath("Sprites/Obstacle_Sprites/Cookie.png");
    }
}
