package com.mehmetakiftutuncu.eshotroid.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.models.BusListItem;

import java.util.List;

public class BusListItemAdapter extends RecyclerView.Adapter<BusListItemAdapter.ViewHolder>{
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public BusListItem item;
        public ImageView id;
        public TextView start;
        public TextView end;

        public ViewHolder(View layout) {
            super(layout);

            id = (ImageView) layout.findViewById(R.id.imageView_busListItem_id);
            start = (TextView) layout.findViewById(R.id.textView_busListItem_start);
            end = (TextView) layout.findViewById(R.id.textView_busListItem_end);
        }

        public void setItem(BusListItem item) {
            this.item = item;

            if (item != null) {
                TextDrawable idDrawable = TextDrawable.builder().buildRound(item.id, ColorGenerator.DEFAULT.getColor(item.id));
                id.setImageDrawable(idDrawable);
                start.setText(item.start);
                end.setText(item.end);
            }
        }
    }

    private List<BusListItem> busList;

    public BusListItemAdapter(List<BusListItem> busList) {
        setBusList(busList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_buslist_item, parent, false);

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItem(busList != null ? busList.get(position) : null);
    }

    @Override
    public int getItemCount() {
        return busList != null ? busList.size() : 0;
    }

    public List<BusListItem> getBusList() {
        return busList;
    }

    public void setBusList(List<BusListItem> busList) {
        this.busList = busList;
        notifyDataSetChanged();
    }
}
