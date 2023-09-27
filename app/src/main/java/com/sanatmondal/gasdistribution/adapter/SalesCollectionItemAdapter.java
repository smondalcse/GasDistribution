package com.sanatmondal.gasdistribution.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanatmondal.gasdistribution.R;
import com.sanatmondal.gasdistribution.model.SalesCollectionItemModel;
import com.sanatmondal.gasdistribution.model.TempOrderSale;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class SalesCollectionItemAdapter extends RecyclerView.Adapter<SalesCollectionItemAdapter.ViewHolder> {
    private static final String TAG = "OrderItemAdapter";
    private List<TempOrderSale> mData;
    private LayoutInflater mInflater;
    Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onRemoveItemClick(int position, List<TempOrderSale> mData);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // data is passed into the constructor
    public SalesCollectionItemAdapter(Context context, List<TempOrderSale> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_order_item_row, parent, false);
        ViewHolder evh = new ViewHolder(view, mListener);
        return evh;
    }

    // binds the data to the TextView in each row
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TempOrderSale model = mData.get(position);
        holder.txt_item_name.setText(model.getItemInfo());
        holder.txt_qty.setText(String.valueOf(model.getSalesQty()));
        holder.txtSalesType.setText(model.getSalesType());
        holder.txt_amount.setText(String.valueOf(model.getNetAmount()));

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_qty, txt_item_name, txtSalesType, txt_amount;
        public ImageView btn_delete;
        LinearLayout row;
        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            txt_qty = itemView.findViewById(R.id.txt_qty);
            txt_item_name = itemView.findViewById(R.id.txt_item_name);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            txtSalesType = itemView.findViewById(R.id.txtSalesType);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            row = itemView.findViewById(R.id.row);

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRemoveItemClick(position, mData);
                        }
                    }
                }
            });

        }

    }
    TempOrderSale getItem(int id) {
        return mData.get(id);
    }

    public void filterList(ArrayList<TempOrderSale> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }
}