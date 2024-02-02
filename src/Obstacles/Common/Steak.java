package Obstacles.Common;

import Obstacles.Obstacle;

public class Steak extends Obstacle {
    public Steak() {
        name = "Obstacles.Common.Steak";
        width = 50;
        height = 50;
        setType("Score");
        setTextureFromPath("Sprites/Obstacle_Sprites/Steak.png");
        setAdditionalScore(50);
    }

    @Override
    public void isSpawned() {
        super.isSpawned();
    }

    @Override
    public Steak cloneObstacle() {
        return new Steak();
    }
}
