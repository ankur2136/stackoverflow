package com.ankur.stackoverflow.presentation.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerDecoration extends RecyclerView.ItemDecoration {
    private int mInsets;

    public DividerDecoration(int size) {
        mInsets = size;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // We can supply forced insets for each item view here in the Rect

        int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            outRect.set(0, 0, mInsets, 0);
        } else if (position == state.getItemCount() - 1) {
            outRect.set(mInsets, 0, 0, 0);
        } else {
            outRect.set(mInsets, 0, mInsets, 0);
        }
    }

}
