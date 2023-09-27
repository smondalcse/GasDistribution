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

import com.sanatmondal.gasdistribution.R;
import com.sanatmondal.gasdistribution.model.CustomerModel;
import com.sanatmondal.gasdistribution.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AutoCompleteCustomerAdapter extends ArrayAdapter<CustomerModel> {
    private static final String TAG = "CustomerAdapter";

    private List<CustomerModel> CustomerModelListFull;

    public AutoCompleteCustomerAdapter(@NonNull Context context, @NonNull List<CustomerModel> BikeList) {
        super(context, R.layout.layout_itemmodel_sugg_row, BikeList);
        CustomerModelListFull = new ArrayList<>(BikeList);
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

        CustomerModel item = getItem(position);

        if (item != null) {
            txtEmpName.setText(item.getCustomerName());
        }

        return convertView;
    }

    private Filter searchModelFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d(TAG, "performFiltering: " + constraint);
            FilterResults results = new FilterResults();
            List<CustomerModel> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(CustomerModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                String last = filterPattern.substring(filterPattern.lastIndexOf(',') + 1);
                Log.i(TAG, "performFiltering:last:  " + last.trim());
                for (CustomerModel item : CustomerModelListFull) {
                    // String title = item.getName()+"-"+item.getDeptName()+"-"+item.getDesignationName()+"<" + item.getEmpid()+">";
                    if (item.getCustomerName().toLowerCase().contains(last)) {
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
            return ((CustomerModel) resultValue).getCustomerName();
        }
    };

}