package Obstacles.Superrare;

import Obstacles.Obstacle;

public class David extends Obstacle {
    public David() {
        name = "Obstacles.Superrare.David Martinez";
        width = 180;
        height = 200;
        setType("Score");
        setAdditionalScore(-2077);
        setTextureFromPath("Sprites/Secret/David.png");
    }

    @Override
    public void isSpawned() {
        defaultGround = 120;
        defaultAir = 40;
        super.isSpawned();
    }
}
