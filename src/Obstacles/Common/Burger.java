package Obstacles.Common;

import Obstacles.Obstacle;

public class Burger extends Obstacle {
    public Burger() {
        name = "Obstacles.Common.Burger";
        width = 60;
        height = 60;
        setType("Score");
        setTextureFromPath("Sprites/Obstacle_Sprites/Burger.png");
        setAdditionalScore(100);
    }

    @Override
    public void isSpawned() {
        super.isSpawned();
    }

    @Override
    public Burger cloneObstacle() {
        return new Burger();
    }
}
