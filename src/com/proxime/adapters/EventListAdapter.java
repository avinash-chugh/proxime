package com.proxime.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.proxime.R;
import com.proxime.entities.Entity;
import com.proxime.entities.Event;

import java.util.List;

public class EventListAdapter<T extends Entity> extends BaseListAdapter<T> {
    public EventListAdapter(Context context, List<T> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        EventViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.event_list_item, null);
            holder = new EventViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.event_list_item_name);
            holder.location = (TextView) convertView.findViewById(R.id.event_list_item_location);
            holder.contact = (TextView) convertView.findViewById(R.id.event_list_item_contact);
            holder.message = (TextView) convertView.findViewById(R.id.event_list_item_message);
            convertView.setTag(holder);
        } else {
            holder = (EventViewHolder) convertView.getTag();
        }

        Event event = (Event) getItem(position);
        holder.id = event.getId();
        holder.name.setText(event.getName());
        holder.message.setText(event.getMessage());
        if(event.getContact() != null)  holder.contact.setText(event.getContact().getName());
        if(event.getLocation() != null)  holder.location.setText(event.getLocation().getName());

        return convertView;

    }

    public static class EventViewHolder {
        TextView name;
        TextView location;
        TextView message;
        TextView contact;
        public long id;
    }

}
