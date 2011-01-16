package com.proxime.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.proxime.R;
import com.proxime.entities.Location;
import com.proxime.repositories.LocationRepository;

public class LocationDialog {
    private Activity activity;
    private LayoutInflater inflater;
    private LocationRepository repository;
    private LocationChangeListener listener;

    public LocationDialog(Activity activity) {
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        repository = new LocationRepository(activity);
    }

    public Dialog show(final Location existing) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.new_location);

        final View view = inflater.inflate(R.layout.location_dialog, null);
        builder.setView(view);

        if (existing != null)
        {
            EditText locationText = getEditText(view);
            locationText.setText(existing.getName());
            locationText.setSelection(0, existing.getName().length());
        }

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.removeDialog(Locations.SET_LOCATION_DIALOG);
            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String locationName = getEditText(view).getText().toString();
                if (locationName == null || locationName.length() == 0) return;

                Location location;
                if (existing == null)
                {
                    location = new Location(0, locationName);
                    repository.save(location);
                }
                else {
                    repository.rename(existing.getId(), locationName);
                    location = repository.load(existing.getId());
                }

                activity.removeDialog(Locations.SET_LOCATION_DIALOG);
                if (listener != null) listener.change(location, (existing == null));
            }
        });

        return builder.create();
    }

    public void setLocationChangeListener(LocationChangeListener listener)
    {
        this.listener = listener;
    }

    private EditText getEditText(View view) {
        return (EditText) view.findViewById(R.id.location_name);
    }
}
