package com.mehmetakiftutuncu.eshotroid.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.models.Bus;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

public class BusListAdapter extends RecyclerView.Adapter<BusListViewHolder> implements FastScrollRecyclerView.SectionedAdapter {
    private ArrayList<Bus> busList;
    private OnBusSelectedListener onBusSelectedListener;

    public BusListAdapter(ArrayList<Bus> busList, OnBusSelectedListener onBusSelectedListener) {
        this.busList               = busList;
        this.onBusSelectedListener = onBusSelectedListener;
    }

    @Override public BusListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View busView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus_list, parent, false);

        return new BusListViewHolder(busView, onBusSelectedListener);
    }

    @Override public void onBindViewHolder(BusListViewHolder holder, int position) {
        Bus bus = busList.get(position);

        holder.setForBus(bus);
    }

    @Override public int getItemCount() {
        return busList != null ? busList.size() : 0;
    }

    @Override @NonNull public String getSectionName(int position) {
        return busList != null ? String.valueOf(busList.get(position).id) : "";
    }

    public interface OnBusSelectedListener {
        void onBusSelected(Bus bus);
    }
}
