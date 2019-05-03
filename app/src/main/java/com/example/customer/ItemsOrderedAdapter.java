package com.example.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemsOrderedAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    ArrayList<ItemsOrdered> items;

    public ItemsOrderedAdapter(Context c, ArrayList<ItemsOrdered> items){
        this.items = items;
        mInflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemsOrdered currentItem = items.get(position);
        View v = mInflater.inflate(R.layout.itemsordered_list, null);
        TextView nameTextView = (TextView) v.findViewById(R.id.nameTextView);
        TextView priceTextView = (TextView) v.findViewById(R.id.priceTextView);
        TextView quantityTextView = (TextView) v.findViewById(R.id.quantityTextView);

        String name = ""+currentItem.getName();
        String quantity = ""+currentItem.getQuantity();
        String price = ""+(currentItem.getPrice()*currentItem.getQuantity())+"€";

        nameTextView.setText(name);
        priceTextView.setText(price);
        quantityTextView.setText(quantity);

        return v;
    }
}
