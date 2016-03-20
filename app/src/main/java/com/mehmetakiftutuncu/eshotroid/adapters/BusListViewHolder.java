package com.mehmetakiftutuncu.eshotroid.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.models.Bus;

public class BusListViewHolder extends RecyclerView.ViewHolder {
    private View mLayout;
    private ImageView mImageViewId;
    private TextView mTextViewDeparture;
    private TextView mTextViewArrival;
    private CheckBox mCheckBoxIsStarred;

    private BusListAdapter.OnBusSelectedListener mOnBusSelectedListener;

    public BusListViewHolder(View layout, BusListAdapter.OnBusSelectedListener onBusSelectedListener) {
        super(layout);

        mLayout            = layout;
        mImageViewId       = (ImageView) layout.findViewById(R.id.item_bus_list_id);
        mTextViewDeparture = (TextView) layout.findViewById(R.id.item_bus_list_departure);
        mTextViewArrival   = (TextView) layout.findViewById(R.id.item_bus_list_arrival);
        mCheckBoxIsStarred = (CheckBox) layout.findViewById(R.id.item_bus_list_isStarred);

        mOnBusSelectedListener = onBusSelectedListener;
    }

    public void setForBus(final Bus bus) {
        String busId = String.valueOf(bus.id);
        TextDrawable drawable = TextDrawable
                .builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .bold()
                .endConfig()
                .buildRect(busId, ColorGenerator.MATERIAL.getColor(busId));

        mImageViewId.setImageDrawable(drawable);
        mTextViewDeparture.setText(bus.departure);
        mTextViewArrival.setText(bus.arrival);
        mCheckBoxIsStarred.setChecked(bus.isStarred);

        mCheckBoxIsStarred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBusSelectedListener.onBusStarSelected(mCheckBoxIsStarred, bus);
            }
        });

        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mOnBusSelectedListener.onBusSelected(bus);
            }
        });
    }
}
