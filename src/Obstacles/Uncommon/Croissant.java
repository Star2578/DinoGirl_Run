package Obstacles.Uncommon;

import Obstacles.Obstacle;

public class Croissant extends Obstacle {
    public Croissant() {
        name = "Obstacles.Uncommon.Croissant";
        width = 60;
        height = 60;
        setType("Score");
        setTextureFromPath("Sprites/Obstacle_Sprites/Croissant.png");
        setAdditionalScore(250);
    }

    @Override
    public void isSpawned() {
        super.isSpawned();
    }

    @Override
    public Croissant cloneObstacle() {
        return new Croissant(); // Call the superclass clone method
    }
}
