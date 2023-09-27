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
import com.sanatmondal.gasdistribution.model.ReportModel;
import com.sanatmondal.gasdistribution.model.TempOrderSale;

import java.util.ArrayList;
import java.util.List;

public class ReportItemAdapter extends RecyclerView.Adapter<ReportItemAdapter.ViewHolder> {
    private static final String TAG = "ReportItemAdapter";
    private List<ReportModel> mData;
    private LayoutInflater mInflater;
    Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onRemoveItemClick(int position, List<ReportModel> mData);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // data is passed into the constructor
    public ReportItemAdapter(Context context, List<ReportModel> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_report_item_row, parent, false);
        ViewHolder evh = new ViewHolder(view, mListener);
        return evh;
    }

    // binds the data to the TextView in each row
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReportModel model = mData.get(position);
        holder.txtItemName.setText(model.getItemName());

        /*
        int a =  Integer.parseInt((model.getAssignQty().toString()));
        int b =  Integer.parseInt((model.getCylinder().toString()));
        int c =  Integer.parseInt((model.getGasOnly().toString()));
        int d =  Integer.parseInt((model.getGasCylinderBoth().toString()));
        */
        holder.txtAssignQty.setText(String.valueOf(model.getAssignQty().intValue()));
        holder.txtCollectionQty.setText(String.valueOf(model.getCylinder().intValue()));
        holder.txtRefill.setText(String.valueOf(model.getGasOnly().intValue()));
        holder.txtPackage.setText(String.valueOf(model.getGasCylinderBoth().intValue()));

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtItemName, txtAssignQty, txtCollectionQty, txtRefill, txtPackage;
        public ImageView btn_delete;
        LinearLayout row;
        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            txtItemName = itemView.findViewById(R.id.txtItemName);
            txtAssignQty = itemView.findViewById(R.id.txtAssignQty);
            txtCollectionQty = itemView.findViewById(R.id.txtCollectionQty);
            txtRefill = itemView.findViewById(R.id.txtRefill);
            txtPackage = itemView.findViewById(R.id.txtPackage);
            row = itemView.findViewById(R.id.row);

        }

    }
    ReportModel getItem(int id) {
        return mData.get(id);
    }

    public void filterList(ArrayList<ReportModel> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }
}