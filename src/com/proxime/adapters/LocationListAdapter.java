package com.proxime.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.proxime.R;
import com.proxime.entities.Location;

import java.util.List;

public class LocationListAdapter extends BaseListAdapter<Location>  {

    public LocationListAdapter(Context context, List<Location> data)
    {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
       LocationViewHolder locationHolder = new LocationViewHolder();
       if (convertView ==null)  {
            convertView = inflater.inflate(R.layout.location_list_item,null);


           locationHolder.name = (TextView) convertView.findViewById(R.id.list_item_location_name);
           locationHolder.formattedAddress= (TextView) convertView.findViewById(R.id.list_item_location_address);

           convertView.setTag(locationHolder);
       } else{
           locationHolder = (LocationViewHolder) convertView.getTag();
       }

       Location location = (Location) getItem(position);
       locationHolder.id = location.getId();
       locationHolder.name.setText(location.getName());
       locationHolder.formattedAddress.setText(location.getAddress());
       return convertView;
    }

    public static class LocationViewHolder  {
        public long id;
        TextView name;
        TextView formattedAddress;
        }
}
