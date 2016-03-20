package com.mehmetakiftutuncu.eshotroid.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.models.Bus;

import java.util.ArrayList;
import java.util.Locale;

public class BusListAdapter extends RecyclerView.Adapter<BusListViewHolder> {
    private ArrayList<Bus> busList;
    private ArrayList<Bus> matchingBuses;

    private OnBusSelectedListener onBusSelectedListener;

    public BusListAdapter(ArrayList<Bus> busList, OnBusSelectedListener onBusSelectedListener) {
        this.busList       = busList;
        this.matchingBuses = busList;

        this.onBusSelectedListener = onBusSelectedListener;
    }

    @Override public BusListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View busView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus_list, parent, false);

        return new BusListViewHolder(busView, onBusSelectedListener);
    }

    @Override public void onBindViewHolder(BusListViewHolder holder, int position) {
        Bus bus = matchingBuses.get(position);

        holder.setForBus(bus);
    }

    @Override public int getItemCount() {
        return matchingBuses != null ? matchingBuses.size() : 0;
    }

    public void search(String query) {
        String q = query.trim();

        if (q.equals("")) {
            matchingBuses = busList;
        } else {
            Locale tr = new Locale("tr");
            matchingBuses = new ArrayList<>();

            for (int i = 0, size = busList.size(); i < size; i++) {
                Bus bus = busList.get(i);

                if (String.valueOf(bus.id).startsWith(q) ||
                        bus.departure.toLowerCase(tr).contains(q) ||
                        bus.arrival.toLowerCase(tr).contains(q)) {
                    matchingBuses.add(bus);
                }
            }
        }

        notifyDataSetChanged();
    }

    public interface OnBusSelectedListener {
        void onBusSelected(Bus bus);
        void onBusStarSelected(CheckBox starView, Bus bus);
    }
}
