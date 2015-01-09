package com.example.lolo.andro2048;

/**
 * Created by ben on 1/8/2015.
 */
interface SwipeHandler {
    static public int UP = 0;
    static public int RIGHT = 1;
    static public int DOWN = 2;
    static public int LEFT = 3;

    abstract void onSwipe(int direction);
}
