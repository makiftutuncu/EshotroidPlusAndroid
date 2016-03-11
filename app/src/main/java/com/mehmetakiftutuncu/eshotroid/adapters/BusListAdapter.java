package com.mehmetakiftutuncu.eshotroid.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.models.Bus;

import java.util.ArrayList;

public class BusListAdapter extends RecyclerView.Adapter<BusListViewHolder> {
    private ArrayList<Bus> busList;

    public BusListAdapter(ArrayList<Bus> busList) {
        this.busList = busList;
    }

    @Override public BusListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View busView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus_list, parent, false);

        return new BusListViewHolder(busView);
    }

    @Override public void onBindViewHolder(BusListViewHolder holder, int position) {
        Bus bus = busList.get(position);

        holder.updateViews(bus);
    }

    @Override public int getItemCount() {
        return busList.size();
    }
}
