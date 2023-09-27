package com.sanatmondal.gasdistribution.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sanatmondal.gasdistribution.R;
import com.sanatmondal.gasdistribution.model.CustomerModel;
import com.sanatmondal.gasdistribution.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerModelAdapter extends RecyclerView.Adapter<CustomerModelAdapter.ViewHolder> {
    private static final String TAG = "CustomerModelAdapter";
    private List<CustomerModel> mData;
    private LayoutInflater mInflater;
    Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, List<CustomerModel> mData);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // data is passed into the constructor
    public CustomerModelAdapter(Context context, List<CustomerModel> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_customer_row, parent, false);
        ViewHolder evh = new ViewHolder(view, mListener);
        return evh;
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CustomerModel model = mData.get(position);
        holder.tvCustName.setText("Name: " + model.getCustomerName());
        holder.tvCustMobile.setText("Mobile: " + model.getPhoneNumber());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustName, tvCustMobile;
        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvCustName = itemView.findViewById(R.id.tvCustName);
            tvCustMobile = itemView.findViewById(R.id.tvCustMobile);

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
    CustomerModel getItem(int id) {
        return mData.get(id);
    }

    public void filterListCustomer(ArrayList<CustomerModel> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }
}