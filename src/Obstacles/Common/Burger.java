package Obstacles.Common;

import Obstacles.Obstacle;

import java.util.Random;

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
    public void obstacleInfo() {
        super.obstacleInfo();
        soundPath = randomSFX(new String[]{"src/Sounds/Retro PickUp 10.wav", "src/Sounds/Retro PickUp 18.wav"});
    }
}
