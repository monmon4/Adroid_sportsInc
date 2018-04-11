package com.quantumsit.sportsinc.Aaa_looks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mona on 26-Dec-17.
 */

public class RecyclerView_Adapter_certificate extends RecyclerView.Adapter<RecyclerView_Adapter_certificate.ViewHolder>{


    private List<String> List_Item;
    private Context context;


    public RecyclerView_Adapter_certificate(List<String> list_Item, Context context) {
        List_Item = list_Item;
        this.context = context;

    }

    @Override
    public RecyclerView_Adapter_certificate.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_certficate, parent, false);
        RecyclerView_Adapter_certificate.ViewHolder viewHolder = new RecyclerView_Adapter_certificate.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView_Adapter_certificate.ViewHolder holder, final int position) {

        //holder.certificate_img.setImageResource(List_Item.get(position));
        Picasso.with(context.getApplicationContext())
                .load(Constants.certification_host + List_Item.get(position))
                .into(holder.certificate_img, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        Log.d("Certification_Loading","Error In Loading: "+(Constants.others_host + List_Item.get(position)));
                    }
                });

        holder.certificate_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog builder = new Dialog(context);
                builder.setCanceledOnTouchOutside(true);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //nothing;
                    }
                });
                builder.setCancelable(true);
                ImageView imageView = new ImageView(context);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.dismiss();
                    }
                });
                imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return false;
                    }
                });
                Picasso.with(context.getApplicationContext())
                        .load(Constants.certification_host + List_Item.get(position))
                        .into(imageView);
                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != List_Item ? List_Item.size() : 0);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        private CardView certificate_card;
        private ImageView certificate_img;
        private ProgressBar progressBar;
        public ViewHolder(View view) {
            super(view);
            certificate_card = view.findViewById(R.id.certificate_cardView);
            certificate_img =  view.findViewById(R.id.certificate_imageView);
            progressBar = view.findViewById(R.id.certificate_progressBar);
        }
    }

}
