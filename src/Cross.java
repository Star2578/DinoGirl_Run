public class Cross extends Obstacle {
    public Cross() {
        name = "Cross";
        width = 65;
        height = 80;
        setType("Damage");
        setTextureFromPath("Sprites/Obstacle_Sprites/Cross.png");
        defaultGround -= 15;
        defaultAir = 40;
    }
}
