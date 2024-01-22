package Obstacles.Common;

import Obstacles.Obstacle;

public class Cactus extends Obstacle {
    public Cactus() {
        name = "Obstacles.Common.Cactus";
        width = 50;
        height = 100;
        setType("Damage");
        setTextureFromPath("Sprites/Obstacle_Sprites/Cactus.png");
        defaultGround -= (double) height /3 - 5;
    }

    @Override
    public void obstacleInfo() {
        spawnPoint = defaultGround;
    }
}
