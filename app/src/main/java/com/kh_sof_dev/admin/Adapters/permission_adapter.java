package com.kh_sof_dev.admin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kh_sof_dev.admin.Activities.Odrer_activity;
import com.kh_sof_dev.admin.Clasess.Location_;
import com.kh_sof_dev.admin.Clasess.Product;
import com.kh_sof_dev.admin.Clasess.permissin;
import com.kh_sof_dev.admin.Clasess.users;
import com.kh_sof_dev.admin.R;

import java.util.ArrayList;
import java.util.List;

public class permission_adapter extends RecyclerView.Adapter<permission_adapter.ViewHolder> {

    public interface onEditeListenner{
       void Onselected(Product product);
    }
    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private List<permissin> mItems = new ArrayList<>();

    private Context mContext;
private onEditeListenner mlistenner;
    public permission_adapter(Context context, List<permissin> names, onEditeListenner listenner) {
        mItems = names;
        mContext = context;
        mlistenner=listenner;

    }
    private View mView;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //parent = theme type
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_permissin, parent, false);
        mView=view;

        return new ViewHolder(view); // Inflater means reading a layout XML
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
holder.name.setText(mItems.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);


        }
    }
}