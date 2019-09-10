package com.kh_sof_dev.admin.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
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
import com.kh_sof_dev.admin.Clasess.Product;
import com.kh_sof_dev.admin.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class Products_adapter extends RecyclerView.Adapter<Products_adapter.ViewHolder> {

    public interface onEditeListenner{
       void Onselected(Product product);
    }
    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private List<Product> mItems = new ArrayList<>();

    private Context mContext;
private onEditeListenner mlistenner;
    public Products_adapter(Context context, List<Product> names,onEditeListenner listenner) {
        mItems = names;
        mContext = context;
        mlistenner=listenner;

    }
    private View mView;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //parent = theme type
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_produt_items, parent, false);
        mView=view;

        return new ViewHolder(view); // Inflater means reading a layout XML
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
try{
        holder.name.setText(mItems.get(position).getName());
}catch (Exception e){

}
try {
    holder.weight.setText(mItems.get(position).getWeight().toString() +" KG");
}catch (Exception e){

}
try{
        holder.price.setText(mItems.get(position).getPrice().toString() +" EGP");
}catch (Exception e){

}

        holder.Edite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistenner.Onselected(mItems.get(position));
            }
        });
holder.delete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        final Dialog dialog=new Dialog(mContext);
        dialog.setContentView(R.layout.popup_delete);

        Button delet_pop=dialog.findViewById(R.id.delete);
        Button cancel_pop=dialog.findViewById(R.id.cancel);
        delet_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference reference=database.getReference();
                reference.child("Products").child(mItems.get(position).getId()).removeValue();
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
});

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,weight,price;
   ImageView delete,Edite;
        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.prod_name);
            weight=itemView.findViewById(R.id.wight);
            price=itemView.findViewById(R.id.price);

            delete=itemView.findViewById(R.id.delete);
            Edite=itemView.findViewById(R.id.edit);


        }
    }
}