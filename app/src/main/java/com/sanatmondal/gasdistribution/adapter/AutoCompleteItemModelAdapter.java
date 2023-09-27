package com.sanatmondal.gasdistribution.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sanatmondal.gasdistribution.R;
import com.sanatmondal.gasdistribution.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteItemModelAdapter extends ArrayAdapter<ItemModel> {
    private static final String TAG = "AutoComBikeAdapter";

    private List<ItemModel> ItemModelListFull;

    public AutoCompleteItemModelAdapter(@NonNull Context context, @NonNull List<ItemModel> BikeList) {
        super(context, R.layout.layout_itemmodel_sugg_row, BikeList);
        ItemModelListFull = new ArrayList<>(BikeList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return searchModelFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_itemmodel_sugg_row, parent, false
            );
        }

        TextView txtEmpName = convertView.findViewById(R.id.txtEmpName);
        ImageView profile_image = convertView.findViewById(R.id.profile_image);

        ItemModel item = getItem(position);

        if (item != null) {
            txtEmpName.setText(item.getItemName());
        }

        return convertView;
    }

    private Filter searchModelFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d(TAG, "performFiltering: " + constraint);
            FilterResults results = new FilterResults();
            List<ItemModel> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(ItemModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                String last = filterPattern.substring(filterPattern.lastIndexOf(',') + 1);
                Log.i(TAG, "performFiltering:last:  " + last.trim());
                for (ItemModel item : ItemModelListFull) {
                    // String title = item.getName()+"-"+item.getDeptName()+"-"+item.getDesignationName()+"<" + item.getEmpid()+">";
                    if (item.getItemName().toLowerCase().contains(last)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((ItemModel) resultValue).getItemName();
        }
    };

}