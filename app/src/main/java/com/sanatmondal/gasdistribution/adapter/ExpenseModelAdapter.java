package com.sanatmondal.gasdistribution.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanatmondal.gasdistribution.R;
import com.sanatmondal.gasdistribution.model.CustomerModel;
import com.sanatmondal.gasdistribution.model.ExpenceModel;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ExpenseModelAdapter extends RecyclerView.Adapter<ExpenseModelAdapter.ViewHolder> {
    private static final String TAG = "ExpenseModelAdapter";
    private List<ExpenceModel>  mData;
    private LayoutInflater mInflater;
    Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, List<ExpenceModel> mData);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // data is passed into the constructor
    public ExpenseModelAdapter(Context context, List<ExpenceModel> data) {
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
        ExpenceModel model = mData.get(position);
        holder.tvCustName.setText("Name: " + model.getDailyExpenceTypeName());
        holder.tvCustMobile.setText("ID: " + model.getId());
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
    ExpenceModel getItem(int id) {
        return mData.get(id);
    }

    public void filterListCustomer(ArrayList<ExpenceModel> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }
}