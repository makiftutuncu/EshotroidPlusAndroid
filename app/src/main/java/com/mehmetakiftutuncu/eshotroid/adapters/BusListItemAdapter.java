package com.mehmetakiftutuncu.eshotroid.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mehmetakiftutuncu.eshotroid.R;
import com.mehmetakiftutuncu.eshotroid.models.BusListItem;
import com.mehmetakiftutuncu.eshotroid.utilities.Data;
import com.mehmetakiftutuncu.eshotroid.utilities.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BusListItemAdapter extends RecyclerView.Adapter<BusListItemAdapter.BusListItemViewHolder> {
    public static class BusListItemViewHolder extends RecyclerView.ViewHolder {
        public BusListItem item;
        public ImageView id;
        public TextView start;
        public TextView end;
        public CheckBox star;
        public OnBusListItemStarChangedListener onBusListItemStarChangedListener;

        public BusListItemViewHolder(View layout) {
            super(layout);

            id = (ImageView) layout.findViewById(R.id.imageView_busListItem_id);
            start = (TextView) layout.findViewById(R.id.textView_busListItem_start);
            end = (TextView) layout.findViewById(R.id.textView_busListItem_end);
            star = (CheckBox) layout.findViewById(R.id.checkBox_busListItem_star);

            star.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (onBusListItemStarChangedListener != null) {
                        onBusListItemStarChangedListener.onBusListItemStarChanged(BusListItemViewHolder.this, item, isChecked);
                    }
                }
            });
        }

        public void setOnBusListItemStarChangedListener(OnBusListItemStarChangedListener onBusListItemStarChangedListener) {
            this.onBusListItemStarChangedListener = onBusListItemStarChangedListener;
        }

        public void setItem(BusListItem item) {
            this.item = item;

            if (item != null) {
                TextDrawable idDrawable = TextDrawable.builder().buildRound(item.id, ColorGenerator.DEFAULT.getColor(item.id));
                id.setImageDrawable(idDrawable);
                start.setText(item.start);
                end.setText(item.end);
                star.setChecked(item.starred);
            }
        }
    }

    public interface OnBusListItemStarChangedListener {
        public void onBusListItemStarChanged(BusListItemViewHolder holder, BusListItem item, boolean isChecked);
    }

    private List<BusListItem> busList;
    private List<BusListItem> busListToShow;

    private boolean isSearching;

    private Locale locale = new Locale("tr");

    private OnBusListItemStarChangedListener onBusListItemStarChangedListener = new OnBusListItemStarChangedListener() {
        @Override
        public void onBusListItemStarChanged(BusListItemViewHolder holder, BusListItem item, boolean isChecked) {
            if (item.starred ^ isChecked) {
                item.starred = isChecked;
                holder.star.setEnabled(false); // TODO Check this half star thing
                Data.saveBusList(busList);
                holder.star.setEnabled(true);

                // TODO Load all times of bus here!
            }
        }
    };

    public BusListItemAdapter(List<BusListItem> busList) {
        setBusList(busList);
    }

    @Override
    public BusListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_buslist_item, parent, false);

        return new BusListItemViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(BusListItemViewHolder holder, int position) {
        BusListItem item = isSearching
                ? (busListToShow != null ? busListToShow.get(position) : null)
                : (busList != null ? busList.get(position) : null);

        holder.setOnBusListItemStarChangedListener(onBusListItemStarChangedListener);

        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        List<BusListItem> list = getBusList();
        return list != null ? list.size() : 0;
    }

    public List<BusListItem> getBusList() {
        return isSearching ? busListToShow : busList;
    }

    public void setBusList(List<BusListItem> busList) {
        this.busList = busList;
        this.busListToShow = new ArrayList<>();

        search("");
    }

    public void search(String query) {
        if (busListToShow != null) {
            busListToShow.clear();
        }

        String trimmedQuery = query != null ? query.trim() : "";

        isSearching = !StringUtils.isEmpty(query);

        if (isSearching) {
            for (BusListItem busListItem : busList) {
                String finalQuery    = trimmedQuery.toLowerCase(locale);
                boolean idMatches    = busListItem.id.contains(finalQuery);
                boolean startMatches = busListItem.start.toLowerCase(locale).contains(finalQuery);
                boolean endMatches   = busListItem.end.toLowerCase(locale).contains(finalQuery);

                if (idMatches || startMatches || endMatches) {
                    busListToShow.add(busListItem);
                }
            }
        }

        notifyDataSetChanged();
    }
}
