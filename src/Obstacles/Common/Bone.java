package Obstacles.Common;

import Obstacles.Obstacle;

public class Bone extends Obstacle {
    public Bone() {
        name = "Obstacles.Common.Bone";
        width = 100;
        height = 50;
        setType("Damage");
        setTextureFromPath("Sprites/Obstacle_Sprites/Bone.png");
        defaultGround = 50;
    }
}
