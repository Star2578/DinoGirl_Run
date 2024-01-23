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
        soundPath = randomSFX(new String[]{"src/Sounds/Retro PickUp 10.wav", "src/Sounds/Retro PickUp 18.wav"});
    }
}
