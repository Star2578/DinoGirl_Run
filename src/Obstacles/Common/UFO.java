package Obstacles.Common;

import Obstacles.Obstacle;
import com.sun.source.tree.YieldTree;

public class UFO extends Obstacle {
    public UFO() {
        name = "Obstacles.Common.UFO";
        width = 70;
        height = 50;
        setType("Damage");
        setTextureFromPath("Sprites/Obstacle_Sprites/UFO.png");
    }

    @Override
    public UFO cloneObstacle() {
        return new UFO();
    }
}
