package Obstacles.Uncommon;

import Obstacles.Obstacle;

public class Cake extends Obstacle {
    public Cake() {
        name = "Obstacles.Uncommon.Cake";
        width = 100;
        height = 70;
        setType("Score");
        setTextureFromPath("Sprites/Obstacle_Sprites/Cake.png");
        setAdditionalScore(500);
        setDefaultGround(140);
    }

    @Override
    public void isSpawned() {
        super.isSpawned();
    }

    @Override
    public Cake cloneObstacle() {
        return new Cake(); // Call the superclass clone method
    }
}
