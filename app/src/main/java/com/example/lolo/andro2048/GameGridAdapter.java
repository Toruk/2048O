package com.example.lolo.andro2048;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GameGridAdapter extends RecyclerView.Adapter<GameGridAdapter.ViewHolder> implements SwipeHandler {
    private Game mGame;
    private GameListener mGameListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTileNumber;

        public ViewHolder(View v) {
            super(v);

            mTileNumber = (TextView) v.findViewById(R.id.tile_number);
        }
    }

    public GameGridAdapter(GameListener gameListener) {
        mGameListener = gameListener;
        mGame = new Game();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Integer tileNumber = mGame.getTileNumber(position);
        if (tileNumber == 0) {
            holder.mTileNumber.setBackgroundResource(Palette.TILES.get(tileNumber));
            holder.mTileNumber.setText("");
        }
        else {
            holder.mTileNumber.setBackgroundResource(Palette.TILES.get((int)(Math.log(tileNumber) / Math.log(2))));
            holder.mTileNumber.setText(tileNumber.toString());
        }
        mGameListener.onScoreUpdate();
        if (mGame.getWon()) {
            mGameListener.onWin();
        }
        else if (mGame.getOver()) {
            mGameListener.onLose();
        }
    }

    public void onSwipe(int dir) {
        mGame.play(dir);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mGame.getSize();
    }

    public Game getGame() {
        return mGame;
    }
}
