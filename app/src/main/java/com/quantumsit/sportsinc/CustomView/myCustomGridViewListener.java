package com.quantumsit.sportsinc.CustomView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Created by Bassam on 6/3/2018.
 */

public class myCustomGridViewListener  implements AbsListView.OnScrollListener {

    private GridView gridView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public myCustomGridViewListener(GridView gridView , SwipeRefreshLayout mSwipeRefreshLayout){
        this.gridView = gridView;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
    }

    public myCustomGridViewListener(ListView listView ){
        this.gridView = gridView;
        this.mSwipeRefreshLayout = null;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int topRowVerticalPosition =
                (gridView == null || gridView.getChildCount() == 0) ?
                        0 : gridView.getChildAt(0).getTop();
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

    }


}