package cz.duong.duongwake.ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;

import cz.duong.duongwake.providers.Alarm;
import cz.duong.duongwake.R;

/**
 * Vytvořeno David on 14. 3. 2015.
 */
public class AlarmList extends BaseAdapter {
    private Context mContext;
    private ArrayList<Alarm> mData;

    public static class ViewHolder {
        private TextView name;
        private TextView date;
        private TextView remaining;
    }

    public AlarmList(Context context, ArrayList<Alarm> data) {
        mContext = context;
        mData = data;
    }

    public void update(ArrayList<Alarm> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Alarm getItem(int pos) {
        return mData.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if(view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.alarm_list_item, viewGroup, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.alarm_list_name);
            holder.date = (TextView) view.findViewById(R.id.alarm_list_date);
            holder.remaining = (TextView) view.findViewById(R.id.alarm_list_remaining);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Calendar cal = Calendar.getInstance();

        Alarm item = getItem(pos);
        holder.name.setText(item.getName());

        Long timestamp = item.getTimestamp(cal);
        Formatter formatter = new Formatter();

        holder.date.setText(item.getHour() + ":" + item.getMinute());
        holder.date.setText(formatter.format(mContext.getString(R.string.alarm_list_time), item.getHour(), item.getMinute()).toString());

        if(timestamp != null) {

            String relative = (String) DateUtils.getRelativeTimeSpanString(timestamp, System.currentTimeMillis(), 0L);
            Log.d("DUONG-WAKE", relative);
            holder.remaining.setText(relative);

        } else {
            holder.remaining.setText("Not defined");
        }


        return view;
    }
}
