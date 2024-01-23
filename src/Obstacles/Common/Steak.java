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
        soundPath = randomSFX(new String[]{"src/Sounds/Retro PickUp 10.wav", "src/Sounds/Retro PickUp 18.wav"});
    }
}
