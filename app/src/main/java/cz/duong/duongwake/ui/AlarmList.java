package cz.duong.duongwake.ui;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.SwitchCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;

import cz.duong.duongwake.listeners.AlarmChangeListener;
import cz.duong.duongwake.providers.Alarm;
import cz.duong.duongwake.R;

/**
 * Vytvořeno David on 14. 3. 2015.
 */
public class AlarmList extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
    private Context mContext;
    private AlarmChangeListener mListener;
    private ArrayList<Alarm> mData;

    private boolean switchUpdated = true;

    private final Handler mHandler;

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switchUpdated = false;

        Alarm item = (Alarm) compoundButton.getTag();

        if(item.isEnabled() != b) {
            item.setEnabled(b);
            mListener.onAlarmChange(item);
        }
    }

    public static class ViewHolder {
        private TextView name;
        private TextView date;
        private TextView remaining;
        private SwitchCompat switchEnable;
    }

    public AlarmList(Context context, AlarmChangeListener listener, ArrayList<Alarm> data) {
        mContext = context;
        mListener = listener;
        mData = data;

        mHandler = new Handler();

//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                notifyDataSetChanged();
//                mHandler.postDelayed(this, 1000);
//            }
//        }, 1000);
    }

    public void update(ArrayList<Alarm> data) {
        mData = data;
        switchUpdated = true;
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
            holder.switchEnable = (SwitchCompat) view.findViewById(R.id.alarm_list_switch);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Calendar cal = Calendar.getInstance();

        Alarm item = getItem(pos);
        holder.name.setText(item.getName());

        Long timestamp = item.getTimestamp(cal);
        Formatter formatter = new Formatter();

        holder.date.setText(formatter.format(mContext.getString(R.string.alarm_list_time), item.getHour(), item.getMinute()).toString());

        if(!item.isEnabled()) {
            holder.remaining.setText("");

        } else if(timestamp != null) {
            String relative = (String) DateUtils.getRelativeTimeSpanString(timestamp, System.currentTimeMillis(), 0L);
            holder.remaining.setText(relative);


        } else {
            holder.remaining.setText(mContext.getText(R.string.alarm_list_expired));
        }





        holder.switchEnable.setTag(item);
        holder.switchEnable.setOnCheckedChangeListener(this);

        if(timestamp == null) {
            holder.switchEnable.setEnabled(false);
        } else {
            holder.switchEnable.setEnabled(true);
        }

        if(switchUpdated) {
            holder.switchEnable.setChecked(item.isEnabled());
        }



        return view;
    }
}
