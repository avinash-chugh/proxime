package com.proxime.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.proxime.R;
import com.proxime.entities.Event;

import java.util.List;

public class EventListAdapter extends BaseListAdapter<Event> {
    public EventListAdapter(Context context, List<Event> data) {
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
            holder.contactLabel = (TextView) convertView.findViewById(R.id.event_list_item_contact_label);
            holder.message = (TextView) convertView.findViewById(R.id.event_list_item_message);

            convertView.setTag(holder);
        } else {
            holder = (EventViewHolder) convertView.getTag();
        }

        Event event = (Event) getItem(position);
        holder.id = event.getId();
        holder.name.setText(event.getName());
        holder.message.setText(event.getMessage());
        if (event.getContact() != null) holder.contact.setText(event.getContact().getName());
        if (event.getLocation() != null) holder.location.setText(event.getLocation().getName());
        if (event.getContact() != null) holder.contact.setText(event.getContact().getName());

        if (event.isNotifySelf()) setContactVisibility(holder, View.GONE);
        else setContactVisibility(holder, View.VISIBLE);


        return convertView;

    }

    private void setContactVisibility(EventViewHolder holder, int visibility) {
        holder.contact.setVisibility(visibility);
        holder.contactLabel.setVisibility(visibility);
    }

    public static class EventViewHolder {
        TextView name;
        TextView location;
        TextView message;
        TextView contact;
        public long id;
        TextView contactLabel;

    }

}
