package com.kh_sof_dev.admin.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kh_sof_dev.admin.Activities.Odrer_activity;
import com.kh_sof_dev.admin.Clasess.Admin;
import com.kh_sof_dev.admin.Clasess.Location_;
import com.kh_sof_dev.admin.Clasess.Product;
import com.kh_sof_dev.admin.Clasess.permissin;
import com.kh_sof_dev.admin.Clasess.users;
import com.kh_sof_dev.admin.R;

import java.util.ArrayList;
import java.util.List;

public class admins_adapter extends RecyclerView.Adapter<admins_adapter.ViewHolder> {

    public interface onEditeListenner{
       void Onselected(Product product);
    }
    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private List<Admin> mItems = new ArrayList<>();

    private Context mContext;
private onEditeListenner mlistenner;
    public admins_adapter(Context context, List<Admin> names, onEditeListenner listenner) {
        mItems = names;
        mContext = context;
        mlistenner=listenner;

    }
    private View mView;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //parent = theme type
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_admins, parent, false);
        mView=view;

        return new ViewHolder(view); // Inflater means reading a layout XML
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
holder.name.setText(mItems.get(position).getName());
        holder.email.setText(mItems.get(position).getEmail());
        holder.passwod.setText(mItems.get(position).getPassword()+"");


 permission_adapter adapter=new permission_adapter(mContext,mItems.get(position).getPermissin(),null);
 holder.RV.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,true));
 holder.RV.setAdapter(adapter);

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
                 reference.child("Admins").child(mItems.get(position).getUid()).removeValue();
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
   holder.edite.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
Edite_popup(position);
       }
   });
mView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

    }
});
    }

    private void Edite_popup(final int position) {
        final BottomSheetDialog dialog=new BottomSheetDialog(mContext);
        dialog.setContentView(R.layout.popup_edite_compt);
        final EditText email=dialog.findViewById(R.id.email);
        final EditText name=dialog.findViewById(R.id.user_name);
        final EditText pw=dialog.findViewById(R.id.password);
        final EditText repw=dialog.findViewById(R.id.repassword);
        email.setText(mItems.get(position).getEmail());
        name.setText(mItems.get(position).getName());
        pw.setText(mItems.get(position).getPassword());
        LinearLayout permissions_lay=dialog.findViewById(R.id.permissin_lay);
        permissions_lay.setVisibility(View.VISIBLE);

        final CheckBox MAL=dialog.findViewById(R.id.MAL);
        final CheckBox checkout=dialog.findViewById(R.id.checkout);
        final CheckBox MO=dialog.findViewById(R.id.MO);
        final CheckBox block=dialog.findViewById(R.id.block);
        final CheckBox production=dialog.findViewById(R.id.production);
        final CheckBox MPL=dialog.findViewById(R.id.MPL);
        final CheckBox Point=dialog.findViewById(R.id.Point);
getPermissions(mItems.get(position).getPermissin(),MAL,checkout,MO,block,production,MPL,Point);


        Button save=dialog.findViewById(R.id.save);
        Button cancel=dialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email_=email.getText().toString();
                final String pw_=pw.getText().toString();
                final String repw_=repw.getText().toString();
                final String name_=name.getText().toString();

                if (email_.isEmpty()){
                    email.setError(email.getHint());
                    return;
                }
                if (name_.isEmpty()){
                    name.setError(name.getHint());
                    return;
                }
                if (pw_.isEmpty() || !pw_.equals(repw_)){
                    pw.setError(pw.getHint());
                    return;
                }
                Admin admin=new Admin();
                admin.setEmail(email_);
                admin.setName(name_);
                admin.setPassword(pw_);

                checkBox(admin.getPermissin(),MAL);
                checkBox(admin.getPermissin(),checkout);
                checkBox(admin.getPermissin(),MO);
                checkBox(admin.getPermissin(),block);
                checkBox(admin.getPermissin(),production);
                checkBox(admin.getPermissin(),MPL);
                checkBox(admin.getPermissin(),Point);

                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Admins");
                reference.child(mItems.get(position).getUid()).setValue(admin);
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void getPermissions(List<permissin> permissin, CheckBox mal, CheckBox checkout, CheckBox mo, CheckBox block, CheckBox production, CheckBox mpl, CheckBox point) {

        for (permissin prm:permissin
             ) {
            if (prm.getName().equals(mContext.getString(R.string.MAL))){
                mal.setChecked(true);
            }
            if (prm.getName().equals(mContext.getString(R.string.checkout))){
                checkout.setChecked(true);
            }
            if (prm.getName().equals(mContext.getString(R.string.MO))){
                mo.setChecked(true);
            }
            if (prm.getName().equals(mContext.getString(R.string.block))){
                block.setChecked(true);
            }
            if (prm.getName().equals(mContext.getString(R.string.Production))){
                production.setChecked(true);
            }
            if (prm.getName().equals(mContext.getString(R.string.MPL))){
                mpl.setChecked(true);
            }
            if (prm.getName().equals(mContext.getString(R.string.Point))){
                point.setChecked(true);
            }

        }

    }

    private void checkBox(List<permissin> permissin, CheckBox mal) {
        if (mal.isChecked()){
            permissin.add(new permissin(mal.getText().toString()));
        }
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,email,passwod;
RecyclerView RV;
ImageView delete,edite;
        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.user_name);
                       email=itemView.findViewById(R.id.email);
            passwod=itemView.findViewById(R.id.password);
            delete=itemView.findViewById(R.id.delete);
            edite=itemView.findViewById(R.id.edit);
RV=itemView.findViewById(R.id.RV);

        }
    }
}