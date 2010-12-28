package com.proxime.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.proxime.R;
import com.proxime.entities.Entity;

import java.util.List;

public class CustomListAdapter<T extends Entity> extends BaseAdapter {


    private List<T> data;
    private LayoutInflater inflater;

    public CustomListAdapter(Context context, List<T> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return data.size();
    }

    public T getItem(int position) {
        return data.get(position);
    }


    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);

            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.list_item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        T item = getItem(position);
        holder.id = item.getId();
        holder.text.setText(item.getName());

        return convertView;
    }

    public void add(T item) {
        data.add(item);
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        TextView text;
        public long id;
    }
}
