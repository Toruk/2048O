package com.example.lolo.andro2048;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Layout;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameActivity extends Activity implements GameListener {

    private static final String PREFS_NAME = "game_data";
    private RecyclerView mRecyclerView;
    private GameGridAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mScoreView;
    private Integer mHighScore;
    private TextView mHighScoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mScoreView = (TextView) findViewById(R.id.score);
        mHighScoreView = (TextView) findViewById(R.id.high_score);

        SharedPreferences gameData = getSharedPreferences(PREFS_NAME, 0);
        mHighScore = gameData.getInt("high_score", 0);
        mHighScoreView.setText(mHighScore.toString());

        mRecyclerView = (RecyclerView) findViewById(R.id.game_gridview);
        mRecyclerView.setHasFixedSize(true);

        // mLayoutManager = new GridLayoutManager(this, mGame.getColumnCount());
        mLayoutManager = new GridLayoutManager(this, 4);
        mLayoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GameGridAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        SwipeDetector touchListener = new SwipeDetector(mRecyclerView, mAdapter);
        mRecyclerView.setOnTouchListener(touchListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences gameData = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = gameData.edit();
        editor.putInt("high_score", mHighScore);
        editor.commit();
    }

    @Override
    public void onScoreUpdate() {
        final Integer score = mAdapter.getGame().getScore();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mScoreView.setText(score.toString());
            }
        });
        if (score > mHighScore) {
            mHighScore = score;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mHighScoreView.setText(mHighScore.toString());
                }
            });
        }
    }

    @Override
    public void onWin() {

    }

    @Override
    public void onLose() {

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
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

    public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;
        GestureDetector mGestureDetector;
        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildPosition(childView));
            }
            return false;
        }
        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }
    }
}
