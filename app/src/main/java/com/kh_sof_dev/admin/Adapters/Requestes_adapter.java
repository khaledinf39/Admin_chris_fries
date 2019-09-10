package com.kh_sof_dev.admin.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kh_sof_dev.admin.Activities.Odrer_activity;
import com.kh_sof_dev.admin.Clasess.Product;
import com.kh_sof_dev.admin.Clasess.Request;
import com.kh_sof_dev.admin.R;

import java.util.ArrayList;
import java.util.List;

public class Requestes_adapter extends RecyclerView.Adapter<Requestes_adapter.ViewHolder> {

    public interface onEditeListenner{
       void Onselected(Product product);
    }
    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private List<Request> mItems = new ArrayList<>();

    private Context mContext;
private onEditeListenner mlistenner;
private FirebaseDatabase database;
private DatabaseReference reference;
private  String user_id;
    public Requestes_adapter(Context context, List<Request> names, onEditeListenner listenner) {
        mItems = names;
        mContext = context;
        mlistenner=listenner;
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Requests");
     user_id=Odrer_activity.userID;

    }
    private View mView;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //parent = theme type
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_item, parent, false);
        mView=view;

        return new ViewHolder(view); // Inflater means reading a layout XML
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
holder.product.setText(mItems.get(position).getProduct());
        holder.id.setText(mItems.get(position).getNb());
        holder.date.setText(mItems.get(position).getDate());

holder.price.setText(mItems.get(position).getPrice().toString()+"  EGP");
        holder.order_count.setText(mItems.get(position).getCount().toString());
        holder.order_talif.setText(mItems.get(position).getTalif().toString());


        switch (mItems.get(position).getType()){
            case 1:
                wait_request(holder,position);
                break;
            case 2:
               Current_request(holder,position);
                break;
            case 3:
               Complete_request(holder);
                break;
            case 4:
                Complete_request(holder);
                break;
        }

    }

    private void Complete_request(ViewHolder holder) {
        holder.delete.setVisibility(View.GONE);
        holder.validate.setVisibility(View.GONE);
    }

    private void Current_request(ViewHolder holder, final int position) {
        holder.validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.child("Complete").child(user_id).push().setValue(mItems.get(position));
                reference.child("Current").child(user_id).child(mItems.get(position).getId()).removeValue();
                mItems.remove(  mItems.get(position));
                notifyDataSetChanged();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_popup("Current",position);
            }
        });
    }

    private void wait_request(ViewHolder holder, final int position) {
        holder.validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("Current").child(user_id).push().setValue(mItems.get(position));
                reference.child("Waite").child(user_id).child(mItems.get(position).getId()).removeValue();
                mItems.remove(  mItems.get(position));
                notifyDataSetChanged();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_popup("Waite",position);
            }
        });
    }

    private void delete_popup(final String type, final int position) {
        final Dialog dialog=new Dialog(mContext);
        dialog.setContentView(R.layout.popup_delete);

        Button delet_pop=dialog.findViewById(R.id.delete);
        Button cancel_pop=dialog.findViewById(R.id.cancel);
        delet_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               reference.child("Cancel").child(user_id).push().setValue(mItems.get(position));
                reference.child(type).child(user_id).child(mItems.get(position).getId()).removeValue();
                Toast.makeText(mContext,"تم الحذف بنجاح ",Toast.LENGTH_LONG).show();
                mItems.remove( mItems.get(position));
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        cancel_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView id,date,product,price,order_count,order_talif;
ImageView delete,validate;
        public ViewHolder(View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.order_nb);
            date=itemView.findViewById(R.id.order_date);
            price=itemView.findViewById(R.id.order_price);
            order_count=itemView.findViewById(R.id.order_count);
            order_talif=itemView.findViewById(R.id.order_talif);
            product=itemView.findViewById(R.id.prod_name);

            delete=itemView.findViewById(R.id.delete);
            validate=itemView.findViewById(R.id.validate);


        }
    }
}