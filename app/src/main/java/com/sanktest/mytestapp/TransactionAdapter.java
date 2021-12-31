package com.sanktest.mytestapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sank on 03-02-2018.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private Context context;
    private List<AccountUtils> personUtils;
    SharedPreferences sharedpreferences;

    public TransactionAdapter(Context context, List personUtils) {
        this.context = context;
        this.personUtils = personUtils;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_transaction_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(personUtils.get(position));

        sharedpreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String userID = sharedpreferences.getString("UserId", "");;

        //String userID = "1";

        AccountUtils pu = personUtils.get(position);

        String senderID = pu.getSenderid();
        String receiverID = pu.getReceiverid();



        if(receiverID.equals(userID)){

            holder.devicename.setText(pu.getReceiverdevicename());
            holder.paymentSR.setImageDrawable(context.getResources().getDrawable(R.drawable.received));
        } else {
            holder.devicename.setText(pu.getSenderdevicename());
            holder.paymentSR.setImageDrawable(context.getResources().getDrawable(R.drawable.sent));
        }

        holder.senderAccNo.setText("From: "+pu.getSenderaccno());
        holder.receiverAccNo.setText("To: "+pu.getReceiveraccno());

        holder.datetime.setText("On: "+pu.getThtime());
        holder.amt.setText("\u20B9"+pu.getAmt());



    }

    @Override
    public int getItemCount() {
        return personUtils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView senderAccNo;
        public TextView receiverAccNo;
        public TextView devicename;
        public TextView datetime;
        public TextView amt;
        public ImageView paymentSR;

        public ViewHolder(View itemView) {
            super(itemView);

            senderAccNo = (TextView) itemView.findViewById(R.id.toacno);
            receiverAccNo = (TextView) itemView.findViewById(R.id.fromacno);
            devicename = (TextView) itemView.findViewById(R.id.tdname);
            datetime = (TextView) itemView.findViewById(R.id.tdate);
            amt = (TextView) itemView.findViewById(R.id.tamt);
            paymentSR = (ImageView) itemView.findViewById(R.id.paymentIco);


        }
    }

}
