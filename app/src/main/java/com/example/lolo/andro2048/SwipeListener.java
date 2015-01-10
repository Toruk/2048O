package com.example.lolo.andro2048;

interface SwipeListener {
    // This specific order is important (clockwise rotation)
    static public int UP = 0;
    static public int RIGHT = 1;
    static public int DOWN = 2;
    static public int LEFT = 3;

    abstract void onSwipe(int direction);
}
