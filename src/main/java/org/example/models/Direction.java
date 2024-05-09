package org.example.models;

public class Direction {

    public final int x;
    public final int y;

    public Direction(int dir) {
        if (dir == 0) {
            x = 1;
            y = 0;
        } else if (dir == 45) {
            x = 1;
            y = 1;
        } else if (dir == 90) {
            x = 0;
            y = 1;
        } else if (dir == 135) {
            x = -1;
            y = 1;
        } else if (dir == 180) {
            x = -1;
            y = 0;
        } else if (dir == -135) {
            x = -1;
            y = -1;
        } else if (dir == -90) {
            x = 0;
            y = -1;
        } else if (dir == -45) {
            x = 1;
            y = -1;
        } else {
            x = 0;
            y = 0;
        }
    }


}
