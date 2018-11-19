package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class QuakeAdapter extends ArrayAdapter {

    private final DecimalFormat decimalFormatter = new DecimalFormat("0.0");

    public QuakeAdapter(Activity context, List<QuakeInfo> quakesInfo) {
        super(context, 0, quakesInfo);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View quakeView = convertView;
        if (quakeView == null) {
            quakeView = LayoutInflater.from(getContext()).inflate(R.layout.quakes_list_item, parent, false);
        }
        QuakeInfo currentQuakeInfo = (QuakeInfo) getItem(position);

        double magnitude = currentQuakeInfo.getMagnitude();
        TextView quakeMag = (TextView) quakeView.findViewById(R.id.quake_mag);
        quakeMag.setText(decimalFormatter.format(magnitude));

        GradientDrawable magnitudeCircle = (GradientDrawable) quakeMag.getBackground();
        int magnitudeColorRID = getMagnitudeColorRID(magnitude);
        int magnitudeColor = ContextCompat.getColor(getContext(), magnitudeColorRID);
        magnitudeCircle.setColor(magnitudeColor);

        String primaryPlace = currentQuakeInfo.getPlace();
        int indexOf = primaryPlace.indexOf(" of ");
        TextView quakePlaceOffset = (TextView) quakeView.findViewById(R.id.quake_place_offset);
        if (indexOf != -1) {
            String placeOffset = primaryPlace.substring(0, indexOf + 3);
            primaryPlace = primaryPlace.substring(indexOf+4, primaryPlace.length());
            quakePlaceOffset.setText(placeOffset);
            quakePlaceOffset.setVisibility(View.VISIBLE);
        } else {
            quakePlaceOffset.setVisibility(View.GONE);
        }

        TextView quakePrimaryPlace = (TextView) quakeView.findViewById(R.id.quake_primary_place);
        quakePrimaryPlace.setText(primaryPlace);

        TextView quakeDate = (TextView) quakeView.findViewById(R.id.quake_date);
        quakeDate.setText(currentQuakeInfo.getFormattedDate());

        TextView quakeTime = (TextView) quakeView.findViewById(R.id.quake_time);
        quakeTime.setText(currentQuakeInfo.getFormattedTime());

        return quakeView;
    }

    public static int getMagnitudeColorRID(double magnitude) {
        int greaterThan = (int)Math.floor(magnitude);
        switch (greaterThan) {
            case 9: return R.color.magnitude9;
            case 8: return R.color.magnitude8;
            case 7: return R.color.magnitude7;
            case 6: return R.color.magnitude6;
            case 5: return R.color.magnitude5;
            case 4: return R.color.magnitude4;
            case 3: return R.color.magnitude3;
            case 2: return R.color.magnitude2;
            case 1:
            case 0: return R.color.magnitude1;
            default: return R.color.magnitude10plus;
        }
    }
}
