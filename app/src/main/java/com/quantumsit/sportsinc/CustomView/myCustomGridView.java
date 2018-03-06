package com.quantumsit.sportsinc.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quantumsit.sportsinc.R;

/**
 * Created by Bassam on 6/3/2018.
 */

public class myCustomGridView  extends RelativeLayout {
    private Context context;
    private GridView gridView;
    View loadMoreFooter;
    private View mEmptyView;
    private View mRetryView;
    private ProgressBar mProgressBar;
    private myCustomListView.OnRetryClick mOnRetryClick;
    private TextView mRetryView_Button;
    private TextView mEmptyView_Text;
    private ImageView mEmptyView_Image;

    public myCustomGridView(Context context) {
        this(context, null);
    }

    public myCustomGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
        initializeUILayout();
        loading();
    }

    public myCustomGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initializeUILayout(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        loadMoreFooter = inflater.inflate(R.layout.progressbar_view,null,false);
        View mView = inflater.inflate(R.layout.custom_grid_view, this);
        gridView =  mView.findViewById(R.id.gridView);
        mEmptyView = mView.findViewById(R.id.layout_empty);
        mEmptyView_Image = mView.findViewById(R.id.empty_layout_image);
        mEmptyView_Text = mView.findViewById(R.id.empty_layout_text);
        mRetryView = mView.findViewById(R.id.layout_retry);
        mRetryView_Button = mView.findViewById(R.id.layout_retry_button);
        mRetryView_Button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mOnRetryClick.onRetry();
                    }
                }, 1500);
            }
        });
        mProgressBar = mView.findViewById(R.id.progress_bar);

    }

    public void setmEmptyView(int imageResource ,int emptyText){
        mEmptyView_Image.setImageResource(imageResource);
        mEmptyView_Text.setText(emptyText);
    }

    public GridView getGridView() {
        return gridView;
    }

    public void loading() {
        mRetryView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /*public void loadMore(){
        listView.addFooterView(loadMoreFooter);
    }

    private void finishedLoading(){
        listView.removeFooterView(loadMoreFooter);
    }*/

    public void empty() {
        mEmptyView.setVisibility(View.VISIBLE);
        mRetryView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void retry() {
        mRetryView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void success() {
        mRetryView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void notifyChange(int size){
        //finishedLoading();
        if (size>0)
            success();
        else
            empty();
    }

    public void setOnRetryClick(myCustomListView.OnRetryClick onRetryClick) {
        mOnRetryClick = onRetryClick;
    }

    public interface OnRetryClick {
        void onRetry();
    }
}