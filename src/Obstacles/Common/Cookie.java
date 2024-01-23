package Obstacles.Common;

import Obstacles.Obstacle;

public class Cookie extends Obstacle {
    public Cookie() {
        name = "Obstacles.Common.Cookie";
        width = 50;
        height = 50;
        setType("Score");
        setAdditionalScore(10);
        setTextureFromPath("Sprites/Obstacle_Sprites/Cookie.png");
    }

    @Override
    public void obstacleInfo() {
        super.obstacleInfo();
        soundPath = randomSFX(new String[]{"src/Sounds/Retro PickUp 10.wav", "src/Sounds/Retro PickUp 18.wav"});
    }
}
