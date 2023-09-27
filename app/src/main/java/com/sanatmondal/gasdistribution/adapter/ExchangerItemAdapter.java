package com.sanatmondal.gasdistribution.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.sanatmondal.gasdistribution.R;
import com.sanatmondal.gasdistribution.model.ExchangerItemListModel;
import com.sanatmondal.gasdistribution.model.TempOrderSale;

import java.util.ArrayList;
import java.util.List;

public class ExchangerItemAdapter extends RecyclerView.Adapter<ExchangerItemAdapter.ViewHolder> {
    private static final String TAG = "ExchangerItemAdapter";
    private List<ExchangerItemListModel> mData;
    private LayoutInflater mInflater;
    Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onRemoveItemClick(int position, List<ExchangerItemListModel> mData);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // data is passed into the constructor
    public ExchangerItemAdapter(Context context, List<ExchangerItemListModel> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_exchange_item_row, parent, false);
        ViewHolder evh = new ViewHolder(view, mListener);
        return evh;
    }

    // binds the data to the TextView in each row
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExchangerItemListModel model = mData.get(position);
        holder.txt_item_name.setText(model.getItemInfo());
        holder.txtGiveQty.setText(String.valueOf(model.getGQty()));
        holder.txtRecQty.setText(String.valueOf(model.getRQty()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_item_name, txtGiveQty, txtRecQty;
        public ImageView btn_delete;
        LinearLayout row;
        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            txt_item_name = itemView.findViewById(R.id.txt_item_name);
            txtGiveQty = itemView.findViewById(R.id.txtGiveQty);
            txtRecQty = itemView.findViewById(R.id.txtRecQty);
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
    ExchangerItemListModel getItem(int id) {
        return mData.get(id);
    }

    public void filterList(ArrayList<ExchangerItemListModel> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }
}