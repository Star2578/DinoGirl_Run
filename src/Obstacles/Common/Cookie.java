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
    public void isSpawned() {
        super.isSpawned();
    }
}
