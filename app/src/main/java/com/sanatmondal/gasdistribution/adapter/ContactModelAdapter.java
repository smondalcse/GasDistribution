package com.sanatmondal.gasdistribution.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sanatmondal.gasdistribution.R;
import com.sanatmondal.gasdistribution.model.ContactsModal;
import com.sanatmondal.gasdistribution.model.CustomerModel;

import java.util.ArrayList;
import java.util.List;

public class ContactModelAdapter extends RecyclerView.Adapter<ContactModelAdapter.ViewHolder> {
    private static final String TAG = "ContactModelAdapter";
    private List<ContactsModal> mData;
    private LayoutInflater mInflater;
    Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, List<ContactsModal> mData);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // data is passed into the constructor
    public ContactModelAdapter(Context context, List<ContactsModal> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.contacts_rv_item, parent, false);
        ViewHolder evh = new ViewHolder(view, mListener);
        return evh;
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactsModal model = mData.get(position);
        if(model.getUserName() != null)
            holder.idTVContactName.setText(model.getUserName());
        if (model.getContactNumber() != null)
            holder.idTVContactNumber.setText(model.getContactNumber());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView idTVContactName, idTVContactNumber;
        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            idTVContactName = itemView.findViewById(R.id.idTVContactName);
            idTVContactNumber = itemView.findViewById(R.id.idTVContactNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, mData);
                        }
                    }
                }
            });

        }
    }
    ContactsModal getItem(int id) {
        return mData.get(id);
    }

    public void filterListCustomer(ArrayList<ContactsModal> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }
}