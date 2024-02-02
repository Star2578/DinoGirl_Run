package Obstacles.Uncommon;

import Obstacles.Obstacle;

public class Fake_Cake extends Obstacle {
    public Fake_Cake() {
        name = "Fake Obstacles.Uncommon.Cake";
        width = 100;
        height = 70;
        setType("Damage");
        setTextureFromPath("Sprites/Obstacle_Sprites/Fake_Cake.png");
        setDefaultGround(140);
    }

    @Override
    public Fake_Cake cloneObstacle() {
        return new Fake_Cake(); // Call the superclass clone method
    }
}
