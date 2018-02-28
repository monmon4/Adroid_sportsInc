package com.quantumsit.sportsinc.Aaa_looks;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    public void onBindViewHolder(RecyclerView_Adapter_certificate.ViewHolder holder, final int position) {

        //holder.certificate_img.setImageResource(List_Item.get(position));
        Picasso.with(context.getApplicationContext())
                .load(List_Item.get(position))
                .into(holder.certificate_img);

        holder.certificate_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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


        public ViewHolder(View view) {
            super(view);
            certificate_card = view.findViewById(R.id.certificate_cardView);
            certificate_img =  view.findViewById(R.id.certificate_imageView);
        }
    }

}
