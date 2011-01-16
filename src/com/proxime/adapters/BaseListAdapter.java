package com.proxime.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.proxime.R;
import com.proxime.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class BaseListAdapter<T extends Entity> extends BaseAdapter {
    private List<T> data;
    private List<T> filtered = new ArrayList<T>();
    protected LayoutInflater inflater;

    public BaseListAdapter(Context context, List<T> data) {
        this.data = data;
        copy();

        inflater = LayoutInflater.from(context);
    }

    private void copy() {
        filtered.clear();
        for (T item : data) {
            filtered.add(item);
        }
        notifyDataSetChanged();
    }

    public int getCount() {
        return filtered.size();
    }

    public T getItem(int position) {
        return filtered.get(position);
    }

    public long getItemId(int position) {
        return getItem(position).getId();
    }

    public void add(T item) {
        data.add(item);
        copy();
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

    public void remove(T item) {
        data.remove(item);
        copy();
    }

    public void filter(String s) {
        if (s == null || s.length() == 0) {
            copy();
            return;
        }
        
        filtered.clear();
        for (T item : data) {
            if (item.getName().contains(s))
                filtered.add(item);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        TextView text;
        public long id;
    }
}
