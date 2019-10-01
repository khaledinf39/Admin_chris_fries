package com.kh_sof_dev.admin.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.kh_sof_dev.admin.Clasess.Location_;
import com.kh_sof_dev.admin.Clasess.Product;
import com.kh_sof_dev.admin.Clasess.users;
import com.kh_sof_dev.admin.R;

import java.util.ArrayList;
import java.util.List;

public class Users_adapter extends RecyclerView.Adapter<Users_adapter.ViewHolder> {

    public interface onEditeListenner{
       void Onselected(Product product);
    }
    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private List<users> mItems = new ArrayList<>();

    private Context mContext;
private onEditeListenner mlistenner;
    public Users_adapter(Context context, List<users> names, onEditeListenner listenner) {
        mItems = names;
        mContext = context;
        mlistenner=listenner;

    }
    private View mView;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //parent = theme type
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users, parent, false);
        mView=view;

        return new ViewHolder(view); // Inflater means reading a layout XML
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
holder.name.setText(mItems.get(position).getName());
        holder.phone.setText(mItems.get(position).getPhone());
        holder.nb.setText(mItems.get(position).getRequest_wail_nb());
mView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(mContext, Odrer_activity.class);
        intent.putExtra("name",mItems.get(position).getName());
        intent.putExtra("address",mItems.get(position).getAddress());
        intent.putExtra("phone",mItems.get(position).getPhone());
        intent.putExtra("id",mItems.get(position).getId());
        intent.putExtra("nb",mItems.get(position).getNb());

       try {
           Location_ location=mItems.get(position).getLocation();
           intent.putExtra("lat",location.getLat());
           intent.putExtra("lng",location.getLng());
       }catch (Exception e){

       }


        mContext.startActivity(intent);
    }
});
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,phone,nb;

        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.user_name);
                       phone=itemView.findViewById(R.id.phoneNB);
            nb=itemView.findViewById(R.id.nb);


        }
    }
}