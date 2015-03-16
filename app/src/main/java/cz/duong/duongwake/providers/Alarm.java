package cz.duong.duongwake.providers;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Vytvořeno David on 14. 3. 2015.
 */
public class Alarm implements Parcelable {
    private long id;
    private String name;

    private int hour;
    private int minute;
    private boolean enabled;

    private boolean isRepeated;

    private ArrayList<Integer> days;



    public Alarm(String name, Integer hour, Integer minute, String days, Boolean enabled, Boolean isRepeated) {
        this.name = name;
        this.hour = hour;
        this.minute = minute;
        this.enabled = enabled;
        this.isRepeated = isRepeated;

        this.days = new ArrayList<>();

        for(String part : TextUtils.split(days, ",")) {
            this.days.add(Integer.parseInt(part));
        }

        Collections.sort(this.days);
    }


    public Alarm(String name, Integer hour, Integer minute, String days, Integer enabled, Integer isRepeated) {
        this(name, hour, minute, days, enabled == 1, isRepeated == 1);
    }

    public Alarm(Parcel in) {
        id = in.readLong();
        name = in.readString();
        hour = in.readInt();
        minute = in.readInt();
        enabled = in.readInt() == 1;
        isRepeated = in.readInt() == 1;

        int[] temp = in.createIntArray();
        days = new ArrayList<>();

        for(int i : temp) {
            days.add(i);
        }

        Collections.sort(this.days);
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeInt(hour);
        parcel.writeInt(minute);
        parcel.writeInt(enabled ? 1 : 0);
        parcel.writeInt(isRepeated ? 1 : 0);

        int[] temp = new int[days.size()];
        for(int i = 0; i < days.size(); i++) {
            temp[i] = days.get(i);
        }

        parcel.writeIntArray(temp);
    }

    public Long getTimestamp(Calendar calendar) {

        Integer current_day = calendar.get(Calendar.DAY_OF_WEEK);
        Integer split = current_day;

        for(int index = 0; index < days.size(); index++) {
            if(days.get(index) >= current_day) {
                split = index;
                break;
            }
        }

        //následující dny
        ArrayList<Integer> relativeDays = new ArrayList<>();
        if(days.size() > 0) {
            for(Integer day : days.subList(split, days.size())) {
                relativeDays.add(day - current_day);
            }
            for(Integer nextDay : days.subList(0, split)) {
                relativeDays.add(nextDay + 7 - current_day);
            }
            if(isRepeated()) {
                relativeDays.add(7);
            }
        } else {
            relativeDays.add(0);
        }

        for(Integer day : relativeDays) {
            Calendar newCal = (Calendar) calendar.clone();
            newCal.add(Calendar.DATE, day);
            newCal.set(Calendar.HOUR_OF_DAY, getHour());
            newCal.set(Calendar.MINUTE, getMinute());
            newCal.set(Calendar.SECOND, 0);
            newCal.set(Calendar.MILLISECOND, 0);

            if (newCal.getTimeInMillis() > calendar.getTimeInMillis()) { //je v budoucnu
                return newCal.getTimeInMillis();
            }
        }

        return null; //není k dispozici žádný další alarm
    }

    public String getName() {
        return this.name;
    }
    public int getHour() {
        return this.hour;
    }
    public int getMinute() {
        return this.minute;
    }
    public ArrayList<Integer> getDays() {
        return this.days;
    }
    public boolean isEnabled() {
        return this.enabled;
    }
    public boolean isRepeated() { return this.isRepeated; }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) { this.id = id; }
    public boolean hasId() { return this.id != 0; }

    public void setName(String name) { this.name = name; }
    public void setHour(int hour) { this.hour = hour; }
    public void setMinute(int minute) { this.minute = minute; }
    public void setDays(ArrayList<Integer> days) { this.days = days; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public void setRepeated(boolean repeated) { this.isRepeated = repeated; }

    public String getDaysString() {
        return TextUtils.join(",", getDays());
    }
    public int getEnabled() {
        return isEnabled() ? 1 : 0;
    }
    public int getRepeated() { return isRepeated() ? 1 : 0; }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Alarm createFromParcel(Parcel parcel) {
            return new Alarm(parcel);
        }

        @Override
        public Alarm[] newArray(int i) {
            return new Alarm[i];
        }
    };

    public static class Entry implements BaseColumns {
        public static final String TABLE_NAME = "alarm";
        public static final String COLUMN_ALARM_NAME = "alarmname";
        public static final String COLUMN_ALARM_HOUR = "alarmhour";
        public static final String COLUMN_ALARM_MINUTE = "alarmminute";
        public static final String COLUMN_ALARM_ENABLED = "alarmenabled";
        public static final String COLUMN_ALARM_REPEATED = "alarmrepeated";
        public static final String COLUMN_ALARM_DAYS = "alarmdays";

        public static final String[] COLUMN_DB = {
            Alarm.Entry._ID + " INTEGER PRIMARY KEY",
            Alarm.Entry.COLUMN_ALARM_NAME + " TEXT",
            Alarm.Entry.COLUMN_ALARM_HOUR + " INTEGER",
            Alarm.Entry.COLUMN_ALARM_MINUTE + " INTEGER",
            Alarm.Entry.COLUMN_ALARM_ENABLED + " INTEGER",
            Alarm.Entry.COLUMN_ALARM_REPEATED + " INTEGER",
            Alarm.Entry.COLUMN_ALARM_DAYS + " TEXT"
        };

        public static final String[] SELECTOR_SQL = {
            Alarm.Entry._ID,
            Alarm.Entry.COLUMN_ALARM_NAME,
            Alarm.Entry.COLUMN_ALARM_HOUR,
            Alarm.Entry.COLUMN_ALARM_MINUTE,
            Alarm.Entry.COLUMN_ALARM_ENABLED,
            Alarm.Entry.COLUMN_ALARM_REPEATED,
            Alarm.Entry.COLUMN_ALARM_DAYS
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
