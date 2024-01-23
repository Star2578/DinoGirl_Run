package Obstacles.Legendary;

import Obstacles.Obstacle;

public class GigaChad extends Obstacle {
    public GigaChad() {
        name = "Giga Chad";
        width = 200;
        height = 200;
        setType("Score");
        setAdditionalScore(10000);
        setTextureFromPath("Sprites/Secret/GigaChad.jpg");
    }

    @Override
    public void isSpawned() {
        spawnPoint = 72;
    }
}
