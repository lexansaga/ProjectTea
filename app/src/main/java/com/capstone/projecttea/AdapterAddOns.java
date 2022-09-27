package com.capstone.projecttea;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdapterAddOns extends ArrayAdapter<ModelAddOns> {

    private ArrayList<ModelAddOns> modelAddOns;
    private  Context context;
    public AdapterAddOns(@NonNull Context context,ArrayList<ModelAddOns> modelAddOns) {
        super(context, R.layout.layout_list_addons,modelAddOns);

        this.modelAddOns = modelAddOns;
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ModelAddOns modelAddOns = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if(convertView == null){
    viewHolder  = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.layout_list_addons, parent, false);

            viewHolder.name = (TextView)convertView.findViewById(R.id.listAddOnsName);
            viewHolder.price = (TextView) convertView.findViewById(R.id.listAddOnsPrice);
            result = convertView;

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.name.setText(modelAddOns.getName());
        viewHolder.price.setText(modelAddOns.getPrice());

        return convertView;

    }

    private static class ViewHolder {
        TextView name;
        TextView price;
    }
}
