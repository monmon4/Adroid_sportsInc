package com.quantumsit.sportsinc.Aaa_looks;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.quantumsit.sportsinc.R;

import java.util.List;

/**
 * Created by Mona on 26-Dec-17.
 */

public class RecyclerView_Adapter_reportpayment extends RecyclerView.Adapter<RecyclerView_Adapter_reportpayment.ViewHolder>{


    private List<item_reports_payment> List_Item;
    private Context context;

    public int person_id;
    public boolean parent;

    public RecyclerView_Adapter_reportpayment(List<item_reports_payment> list_Item, Context context) {
        List_Item = list_Item;
        this.context = context;
    }

    @Override
    public RecyclerView_Adapter_reportpayment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reports_payment, parent, false);
        RecyclerView_Adapter_reportpayment.ViewHolder viewHolder = new RecyclerView_Adapter_reportpayment.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView_Adapter_reportpayment.ViewHolder holder, final int position) {


        holder.course_name.setText(List_Item.get(position).course_name);
        holder.creation_date.setText(List_Item.get(position).creation_date);
        String payment = "$" + List_Item.get(position).payment;
        holder.payment.setText(payment);
        String due_date_string = context.getResources().getString(R.string.due) +": " + List_Item.get(position).due_date;
        holder.due_date.setText(due_date_string);
        holder.childName.setText(List_Item.get(position).trainee_name);
        if (person_id == List_Item.get(position).trainee_id)
            holder.childName.setText("me");
        if (parent)
            holder.childName.setVisibility(View.VISIBLE);
        else
            holder.childName.setVisibility(View.GONE);

        if (List_Item.get(position).status != 0){
            holder.payment_image_button.setBackgroundResource(R.drawable.ic_done_all);
        }
        else
            holder.payment_image_button.setBackgroundResource(R.drawable.ic_warning);
    }

    @Override
    public int getItemCount() {
        return (null != List_Item ? List_Item.size() : 0);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        private CardView payment_card;
        private TextView course_name, creation_date, payment, due_date , childName;
        private ImageButton payment_image_button;


        public ViewHolder(View view) {
            super(view);
            payment_card = view.findViewById(R.id.paymentCardView);
            course_name =  view.findViewById(R.id.courseNameTextView_paymentitem);
            creation_date = view.findViewById(R.id.dateTextView_paymentitem);
            payment =  view.findViewById(R.id.totalpayTextView_paymentitem);
            due_date = view.findViewById(R.id.dueTextView_paymentitem);
            payment_image_button = view.findViewById(R.id.imageButton_paymentitem);
            childName = view.findViewById(R.id.child_name);

        }

    }



}
