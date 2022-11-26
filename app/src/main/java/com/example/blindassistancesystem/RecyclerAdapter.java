package com.example.blindassistancesystem;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<Contact> contacts;
    private RecyclerViewClicklistener listener;
    private Button btn_cancel;
    private Button btn_update;
    RecyclerAdapter adapter;




    public RecyclerAdapter(ArrayList<Contact> contacts){
        this.contacts=contacts;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView number;
        private Button delete;
        private Button update;


        public  MyViewHolder(final View view){
            super(view);
                name= itemView.findViewById(R.id.Name);
                number=itemView.findViewById(R.id.Number);
                delete=itemView.findViewById(R.id.delete);
                update=itemView.findViewById(R.id.update);

        }

    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.emergency_contact_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String Name= contacts.get(position).getName();
        String Number= contacts.get(position).getNumber();

        holder.name.setText(Name);
        holder.number.setText(Number);

        holder.delete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Contact contact = contacts.get(position);
                String name=contacts.get(position).getName();
                String number=contacts.get(position).getNumber();
                DatabaseHelper helper=new DatabaseHelper(v.getContext());
                helper.DeleteData(Name,Number);
                contacts.remove(position);  // remove the item from list
                notifyItemRemoved(position); // notify the adapter about the removed item
                notifyItemRangeChanged(position,getItemCount());

            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog alert= new Dialog(view.getRootView().getContext());
                View mview = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.custom_dialog, null);
                final EditText name = (EditText) mview.findViewById(R.id.name);
                final EditText number = (EditText) mview.findViewById(R.id.number);
                btn_cancel=(Button) mview.findViewById(R.id.cancel);
                btn_update=(Button) mview.findViewById(R.id.updatefinal);
                alert.setContentView(mview);
                alert.setCancelable(true);

                Contact contact = contacts.get(position);
                final String cname = contacts.get(position).getName();
                final String cnumber = contacts.get(position).getNumber();
                Toast.makeText(view.getContext(), cname +" "+ cnumber,Toast.LENGTH_LONG).show();
                DatabaseHelper helper=new DatabaseHelper(view.getContext());
                number.setText(cnumber);
                name.setText(cname);


                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.cancel();
                    }
                });
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String contactName =name.getText().toString();
                        String contactNumber =number.getText().toString();
                        helper.updateContact(contactName,contactNumber,cname,cnumber);
                        notifyItemChanged(position);
                        alert.dismiss();
                        DatabaseHelper databaseHelper = new DatabaseHelper((view.getContext()));
                        contacts = databaseHelper.fetchAlldata();



                    }
                });

                helper.close();
                alert.show();


            }
        });

    }

    @Override
    public int getItemCount() {

        return  contacts.size();
    }
    public interface RecyclerViewClicklistener
    {
        void onClick(View v, int position);
    }


}
