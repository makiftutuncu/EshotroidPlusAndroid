package com.mehmetakiftutuncu.eshotroid.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.akashandroid90.imageletter.MaterialLetterIcon;
import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.models.Bus;

public class BusListViewHolder extends RecyclerView.ViewHolder {
    private View mLayout;
    private MaterialLetterIcon mMaterialLetterIconId;
    private TextView mTextViewDeparture;
    private TextView mTextViewArrival;

    private BusListAdapter.OnBusSelectedListener mOnBusSelectedListener;

    public BusListViewHolder(View layout, BusListAdapter.OnBusSelectedListener onBusSelectedListener) {
        super(layout);

        mLayout               = layout;
        mMaterialLetterIconId = (MaterialLetterIcon) layout.findViewById(R.id.item_bus_list_id);
        mTextViewDeparture    = (TextView) layout.findViewById(R.id.item_bus_list_departure);
        mTextViewArrival      = (TextView) layout.findViewById(R.id.item_bus_list_arrival);

        mOnBusSelectedListener = onBusSelectedListener;
    }

    public void setForBus(final Bus bus) {
        mMaterialLetterIconId.setShapeColor(bus.getColor());
        mMaterialLetterIconId.setLetter(String.valueOf(bus.id));
        mTextViewDeparture.setText(bus.departure);
        mTextViewArrival.setText(bus.arrival);

        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mOnBusSelectedListener.onBusSelected(bus);
            }
        });
    }
}
