package com.kh_sof_dev.admin.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kh_sof_dev.admin.Activities.Production;
import com.kh_sof_dev.admin.Clasess.Product;
import com.kh_sof_dev.admin.Clasess.production;
import com.kh_sof_dev.admin.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import static android.content.Context.MODE_PRIVATE;

public class Products_adapter extends RecyclerView.Adapter<Products_adapter.ViewHolder> {

    public interface onEditeListenner{
       void Onselected(Product product);
    }
    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private List<Product> mItems = new ArrayList<>();
private String mPermissions;
    private Context mContext;
private onEditeListenner mlistenner;
public Boolean bay=false;
    public Products_adapter(Context context, List<Product> names,onEditeListenner listenner) {
        mItems = names;
        mContext = context;
        mlistenner=listenner;
        SharedPreferences sp=mContext.getSharedPreferences("user_info", MODE_PRIVATE);
        mPermissions=sp.getString("permissions"," ");
    }
    private View mView;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //parent = theme type
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_produt_items, parent, false);
        mView=view;

        return new ViewHolder(view); // Inflater means reading a layout XML
    }

    private void addProductionFun(final String prod_id) {
        final Dialog dialog=new Dialog(mContext);
        dialog.setContentView(R.layout.popup_add_production);
        Button ok=dialog.findViewById(R.id.checkout_btn);
        Button cancel=dialog.findViewById(R.id.cancel);
        final EditText price=dialog.findViewById(R.id.count);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (price.getText().toString().isEmpty()){
                    price.setError("الكمية المنتجة");
                    dialog.dismiss();
                    return;
                }
                Double price_=Double.parseDouble(price.getText().toString());
                if (price_!=0  ){
                    add_todata_base(price_,prod_id);
                    save_archiv(price_,prod_id);
                    dialog.dismiss();
                }else {
                    price.setError("الكمية المنتجة");
                    dialog.dismiss();
                    return;
                }
            }
        });
    }

    private void add_todata_base(final Double new_weight, String prod_id) {
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        final DatabaseReference reference=database.getReference("Products").child(prod_id).child("weight");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Double weight=dataSnapshot.getValue(Double.class)+new_weight;
                    reference.setValue(weight);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void save_archiv(final Double new_weight, String prod_id) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        final DatabaseReference reference=database.getReference("Products").child(prod_id)
                .child("Productions").child(dateFormat.format(date)).child("production");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Double weight=dataSnapshot.getValue(Double.class)+new_weight;
                    reference.setValue(weight);
                }else {
                    reference.setValue(new_weight);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        
        if (!mPermissions.contains("Production")){
            holder.add.setVisibility(View.GONE);
        }
        if (!mPermissions.contains("MPL")){
            holder.delete.setVisibility(View.GONE);
            holder.Edite.setVisibility(View.GONE);
        }

if (bay){
    holder.bay.setVisibility(View.VISIBLE);
    holder.bay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mlistenner.Onselected(mItems.get(position));
        }
    });
}

try{
        holder.name.setText(mItems.get(position).getName());
}catch (Exception e){

}
try {
    holder.weight.setText(mItems.get(position).getWeight().toString() +" kg");
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

holder.add.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        addProductionFun(mItems.get(position).getId());
    }
});

mView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(mContext, Production.class);
        intent.putExtra("prod",mItems.get(position).getId());
        mContext.startActivity(intent);
    }
});
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,weight,price;
   ImageView delete,Edite,add;
   Button bay;
        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.prod_name);
            weight=itemView.findViewById(R.id.wight);
            price=itemView.findViewById(R.id.price);

            delete=itemView.findViewById(R.id.delete);
            bay=itemView.findViewById(R.id.bay);
            Edite=itemView.findViewById(R.id.edit);
            add=itemView.findViewById(R.id.add);


        }
    }
}