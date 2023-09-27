package com.sanatmondal.gasdistribution.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.sanatmondal.gasdistribution.R;
import com.sanatmondal.gasdistribution.model.ContactsModal;

import java.util.ArrayList;

public class ContactRVAdapter extends RecyclerView.Adapter<ContactRVAdapter.ViewHolder> {
    //creating variables for context and array list.
    private Context context;
    private ArrayList<ContactsModal> contactsModalArrayList;

    //creating a constructior.
    public ContactRVAdapter(Context context, ArrayList<ContactsModal> contactsModalArrayList) {
        this.context = context;
        this.contactsModalArrayList = contactsModalArrayList;
    }

    @NonNull
    @Override
    public ContactRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passing our layout file for displaying our card item
        return new ContactRVAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.contacts_rv_item, parent, false));

    }

    //below mwthod is use for filtering data in our array list.
    public void filterList(ArrayList<ContactsModal> filterllist) {
        //on below line we are passing filtered array list in our original array list
        contactsModalArrayList = filterllist;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRVAdapter.ViewHolder holder, int position) {
        //getting data from array list in our modal.
        ContactsModal modal = contactsModalArrayList.get(position);
        //on below line we are setting data to our text view.
        holder.contactTV.setText(modal.getUserName());
        holder.idTVContactNumber.setText(modal.getContactNumber());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on below line we are opening a new activity and passing data to it.

            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //on below line creating a variable for our image view and text view.
        private ImageView contactIV;
        private TextView contactTV, idTVContactNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initializing our image view and text view.
            contactIV = itemView.findViewById(R.id.idIVContact);
            contactTV = itemView.findViewById(R.id.idTVContactName);
            idTVContactNumber = itemView.findViewById(R.id.idTVContactNumber);
        }
    }
}