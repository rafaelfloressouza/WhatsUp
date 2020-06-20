package com.rafaelfloressouza.whatsup;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ChatActivityItemDecorator extends RecyclerView.ItemDecoration {

    private final int decorationHeight;
    private Context context;

    public ChatActivityItemDecorator(Context context) {
        this.context = context;
        decorationHeight = 15;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent != null && view != null) {

            int itemPosition = parent.getChildAdapterPosition(view);
            int totalCount = parent.getAdapter().getItemCount();

            if (itemPosition >= 0 && itemPosition < totalCount - 1) {
                outRect.bottom = decorationHeight;
            }
        }
    }
}
