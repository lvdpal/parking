package nl.jduches.parking.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nl.jduches.parking.domain.ParkingSpot;

import nl.jduches.parking.R;

/**
 * Created by klm37699 on 12/2/2015.
 */
public class ListAdapter extends BaseAdapter {

    private Context context;
    private List<ParkingSpot> parkingSpots;

    public ListAdapter(Context context, List<ParkingSpot> parkingSpots) {
        this.context = context;

        this.parkingSpots = parkingSpots;
    }


    @Override
    public int getCount() {
        return parkingSpots.size();
    }

    @Override
    public Object getItem(int position) {
        return parkingSpots.get(position);
    }

    @Override
    public long getItemId(int position) {
        return parkingSpots.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        convertView = LayoutInflater.from(context).inflate(R.layout.parking_spot_item, parent, false);
        viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);
        return convertView;
    }

    private class ViewHolder {
        @InjectView(R.id.text_item)
        TextView textView;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}