package sample;

import java.awt.*;

public class Angle {
    //https://stackoverflow.com/questions/9970281/java-calculating-the-angle-between-two-points-in-degrees
    //slight edit to fit needs
    public float getAngle(Point target, Point target2) {
        float angle = (float) (Math.atan2(target2.y - target.y, target2.x - target.x));

        if(angle < 0){
            angle += 2*Math.PI;
        }

        return angle;
    }
}
