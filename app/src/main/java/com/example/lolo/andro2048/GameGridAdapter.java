package com.example.lolo.andro2048;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GameGridAdapter extends RecyclerView.Adapter<GameGridAdapter.ViewHolder> {
    private Game mGame;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTileNumber;

        public ViewHolder(View v) {
            super(v);

            mTileNumber = (TextView) v.findViewById(R.id.tile_number);
        }
    }

    public GameGridAdapter() {
        mGame = new Game();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTileNumber.setText(mGame.getTileNumber(position).toString());
    }

    @Override
    public int getItemCount() {
        // return mGame.getSize();
        return 16;
    }

    public Game getGame() {
        return mGame;
    }
}
