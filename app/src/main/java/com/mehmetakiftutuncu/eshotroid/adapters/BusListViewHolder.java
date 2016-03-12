package com.mehmetakiftutuncu.eshotroid.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.akashandroid90.imageletter.MaterialLetterIcon;
import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.models.Bus;

public class BusListViewHolder extends RecyclerView.ViewHolder {
    private MaterialLetterIcon mMaterialLetterIconId;
    private TextView mTextViewDeparture;
    private TextView mTextViewArrival;

    public BusListViewHolder(View busView) {
        super(busView);

        mMaterialLetterIconId = (MaterialLetterIcon) busView.findViewById(R.id.item_bus_list_id);
        mTextViewDeparture    = (TextView) busView.findViewById(R.id.item_bus_list_departure);
        mTextViewArrival      = (TextView) busView.findViewById(R.id.item_bus_list_arrival);
    }

    public void updateViews(Bus bus) {
        mMaterialLetterIconId.setShapeColor(bus.getColor());
        mMaterialLetterIconId.setLetter(String.valueOf(bus.id));
        mTextViewDeparture.setText(bus.departure);
        mTextViewArrival.setText(bus.arrival);
    }
}
