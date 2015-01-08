package com.example.lolo.andro2048;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class GameActivity extends Activity {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private GestureDetector mGestureDetector;

    private RecyclerView mRecyclerView;
    private GameGridAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGestureDetector = new GestureDetector(this, new OnSwipeGestureListener());

        mRecyclerView = (RecyclerView) findViewById(R.id.game_gridview);
        mRecyclerView.setHasFixedSize(true);

        // mLayoutManager = new GridLayoutManager(this, mGame.getColumnCount());
        mLayoutManager = new GridLayoutManager(this, 4);
        mLayoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GameGridAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_reset:
                mAdapter.getGame().startGame();
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.action_save:
                mAdapter.getGame().save(this);
                break;
            case R.id.action_load:
                mAdapter.getGame().load(this);
                mAdapter.notifyDataSetChanged();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**
     * Class handling swipe gesture
     */
    private class OnSwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            System.out.println("fling");
            float deltaX = e2.getX() - e1.getX();
            if ((Math.abs(deltaX) < SWIPE_MIN_DISTANCE)
                    || (Math.abs(velocityX) < SWIPE_THRESHOLD_VELOCITY)) {
                return false; // insignificant swipe
            } else {
                if (deltaX < 0) { // left to right
                    handleSwipeLeftToRight();
                } else { // right to left
                    handleSwipeRightToLeft();
                }
            }
            return true;
        }
    }

    private void handleSwipeLeftToRight() {
        System.out.println("lefttoright");
        // TODO: implement the business logic here
    }

    private void handleSwipeRightToLeft() {
        System.out.println("righttoleft");
        // TODO: implement the business logic here
    }
}
