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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kh_sof_dev.admin.Activities.Odrer_activity;
import com.kh_sof_dev.admin.Clasess.Notifi;
import com.kh_sof_dev.admin.Clasess.Product;
import com.kh_sof_dev.admin.Clasess.Request;
import com.kh_sof_dev.admin.Clasess.production;
import com.kh_sof_dev.admin.Clasess.users;
import com.kh_sof_dev.admin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

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

private users mUser;
    public Requestes_adapter(Context context, List<Request> names,onEditeListenner listenner) {
        mItems = names;
        mContext = context;
        mlistenner=listenner;
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Requests");
     mUser=Odrer_activity.mUser;


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
        if (!users.getpermissions(mContext,"MO")){
            holder.delete.setVisibility(View.GONE);
            holder.validate.setVisibility(View.GONE);
        }
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
    public void Post_notificition(String prodname,String body,String token) throws JSONException {
        Notifi notifi_=new Notifi();
        notifi_.setTitle(prodname);
        notifi_.setBody(body);
        notifi_.setToken(token);


        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url ="https://fcm.googleapis.com/fcm/send";

        // POST parameters

        JSONObject cart=notifi_.Notifi();

        Log.d("results",cart.toString());
// Request a json response from the provided URL
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.POST, url, cart,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("results",jsonObject.toString());
                    }
                }, new Response.ErrorListener (){

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        ){
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String>  Headers = new HashMap<String, String>();
                Headers.put("Authorization",
                        "key=" +
                                mContext.getString(R.string.notify_key)
                );
//

                return Headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
        // prepare the Request

    }
    private void Current_request(ViewHolder holder, final int position) {
        holder.validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Post_notificition(mItems.get(position).getProduct(),mContext.getString(R.string.ur_order_dane)
                            +"\n"+"الطلب رقم :"+mItems.get(position).getNb()
                            +"\n"+"الكمية المطلوبة :"+mItems.get(position).getCount(),mUser.getToken());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                reference.child("Complete").child(mUser.getId()).push().setValue(mItems.get(position));
                reference.child("Current").child(mUser.getId()).child(mItems.get(position).getId()).removeValue();
                add_wallet(mItems.get(position).getPrice());
                save_archiv(
                        mItems.get(position).getCount(),
                        mItems.get(position).getTalif(),
                        mItems.get(position).getProd_id()
                );


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
                try {
                    Post_notificition(mItems.get(position).getProduct()
                            ,mContext.getString(R.string.ur_order_accept)
                                    +"\n"+"الطلب رقم :"+mItems.get(position).getNb()
                                    +"\n"+"الكمية المطلوبة :"+mItems.get(position).getCount(),mUser.getToken());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                reference.child("Current").child(mUser.getId()).push().setValue(mItems.get(position));
                reference.child("Waite").child(mUser.getId()).child(mItems.get(position).getId()).removeValue();
                save_requestNB();
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

    private void save_requestNB() {
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference reference=database.getReference("Users")
                .child(mUser.getId()).child("request_wail_nb");
reference.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()){
            int nb=dataSnapshot.getValue(int.class)-1;
            if(nb<=0) {
                reference.setValue(nb);
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
    }

    private void save_archiv(final Double new_weight, final Double talif, String prod_id) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        final DatabaseReference reference=database.getReference("Products").child(prod_id)
                .child("Productions").child(dateFormat.format(date));
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    production production_ =dataSnapshot.getValue(production.class);
                    production_.setOrder(production_.getOrder()+new_weight);
                    production_.setTalif(production_.getTalif()+talif);
                    reference.setValue(production_);
                }else {

                    production production_=new production();
                    production_.setTalif(talif);
                    production_.setOrder(new_weight);
                    reference.setValue(production_);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    private void add_wallet(final Double newPrice){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference reference=database.getReference("Users")
                .child(mUser.getId());
        reference.child("wallet").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Double wallet=dataSnapshot.getValue(Double.class)+newPrice;
                    reference.child("wallet").setValue(wallet);
                }else {
                    reference.child("wallet").setValue(newPrice);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                if (type.equals("Waite")){
                    save_requestNB();

                }
               reference.child("Cancel").child(mUser.getId()).push().setValue(mItems.get(position));
                reference.child(type).child(mUser.getId()).child(mItems.get(position).getId()).removeValue();
                Toast.makeText(mContext,"تم الحذف بنجاح ",Toast.LENGTH_LONG).show();


                try {
                    Post_notificition(mItems.get(position).getProduct()
                            ,mContext.getString(R.string.ur_order_delete)+"\n"+"الطلب رقم :"+mItems.get(position).getNb()
                                    +"\n"+"الكمية المطلوبة :"+mItems.get(position).getCount(),mUser.getToken());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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