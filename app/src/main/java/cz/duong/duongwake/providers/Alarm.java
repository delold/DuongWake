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

    private long absoluteTime;

    private boolean enabled;
    private boolean isRepeated;

    private ArrayList<Integer> days;

    public Alarm(String name, Integer hour, Integer minute, String days, Boolean enabled, Boolean isRepeated, long absoluteTime) {
        this.name = name;
        this.hour = hour;
        this.minute = minute;
        this.enabled = enabled;
        this.isRepeated = isRepeated;
        this.absoluteTime = absoluteTime;

        this.days = new ArrayList<>();

        for(String part : TextUtils.split(days, ",")) {
            this.days.add(Integer.parseInt(part));
        }

        Collections.sort(this.days);
    }

    public Alarm(String name, Integer hour, Integer minute, String days, Integer enabled, Integer isRepeated, long absoluteTime) {
        this(name, hour, minute, days, enabled == 1, isRepeated == 1, absoluteTime);
    }

    public Alarm(Parcel in) {
        id = in.readLong();
        name = in.readString();
        hour = in.readInt();
        minute = in.readInt();
        enabled = in.readInt() == 1;
        isRepeated = in.readInt() == 1;
        absoluteTime = in.readLong();

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
        parcel.writeLong(absoluteTime);

        int[] temp = new int[days.size()];
        for(int i = 0; i < days.size(); i++) {
            temp[i] = days.get(i);
        }

        parcel.writeIntArray(temp);
    }



    private Long getOnceTimestamp(Calendar calendar) {
        if(absoluteTime > 0L) {
            Calendar pastCal = (Calendar) calendar.clone();
            pastCal.setTimeInMillis(absoluteTime);

            Calendar alarmCal = (Calendar) pastCal.clone();
            alarmCal.set(Calendar.HOUR_OF_DAY, getHour());
            alarmCal.set(Calendar.MINUTE, getMinute());
            alarmCal.set(Calendar.SECOND, 0);
            alarmCal.set(Calendar.MILLISECOND, 0);


            if (alarmCal.getTimeInMillis() <= pastCal.getTimeInMillis()) {
                alarmCal.add(Calendar.DATE, 1);
            }

            if(alarmCal.getTimeInMillis() > calendar.getTimeInMillis()) {
                return alarmCal.getTimeInMillis();
            }
        }

        return null;
    }

    private Long getRepeatedTimestamp(Calendar calendar) {
        Integer current_day = calendar.get(Calendar.DAY_OF_WEEK);


        //fix pro neděli
        ArrayList<Integer> tempDays = new ArrayList<>();
        for(Integer i : days) {
            if(i == Calendar.SUNDAY) {
                i += 7;
            }
            tempDays.add(i);
        }
        current_day += (current_day == Calendar.SUNDAY) ? 7 : 0;

        Integer split = tempDays.size();

        for(int index = 0; index < tempDays.size(); index++) {
            if(tempDays.get(index) >= current_day) {
                split = index;
                break;
            }
        }

        //následující dny
        ArrayList<Integer> relativeDays = new ArrayList<>();
        
        for(Integer day : tempDays.subList(split, tempDays.size())) {
            relativeDays.add(day - current_day);
        }

        for(Integer nextDay : tempDays.subList(0, split)) {
            relativeDays.add(nextDay + 7 - current_day);
        }

        relativeDays.add(7);

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

        return null;
    }

    public Long getTimestamp(Calendar calendar) {
        return (days.size() > 0) ? getRepeatedTimestamp(calendar) : getOnceTimestamp(calendar);
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
    public boolean isRepeated() {
        return this.isRepeated;
    }
    public long getTimeChanged() {
        return this.absoluteTime;
    }

    public void setName(String name) { this.name = name; }
    public void setHour(int hour) { this.hour = hour; }
    public void setMinute(int minute) { this.minute = minute; }
    public void setDays(ArrayList<Integer> days) { this.days = days; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setRepeated(boolean repeated) { this.isRepeated = repeated; }
    public void setTimeChanged(Long time) { this.absoluteTime = time; }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) { this.id = id; }
    public boolean hasId() { return this.id != 0; }

    public String getDaysString() {
        return TextUtils.join(",", getDays());
    }
    public int getEnabled() {
        return isEnabled() ? 1 : 0;
    }
    public int getRepeated() {
        return isRepeated() ? 1 : 0;
    }

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
        public static final String COLUMN_ALARM_TIME = "alarmtime";
        public static final String COLUMN_ALARM_DAYS = "alarmdays";

        public static final String[] COLUMN_DB = {
            Entry._ID + " INTEGER PRIMARY KEY",
            Entry.COLUMN_ALARM_NAME + " TEXT",
            Entry.COLUMN_ALARM_HOUR + " INTEGER",
            Entry.COLUMN_ALARM_MINUTE + " INTEGER",
            Entry.COLUMN_ALARM_ENABLED + " INTEGER",
            Entry.COLUMN_ALARM_REPEATED + " INTEGER",
            Entry.COLUMN_ALARM_TIME + " INTEGER",
            Entry.COLUMN_ALARM_DAYS + " TEXT"
        };

        public static final String[] SELECTOR_SQL = {
            Entry._ID,
            Entry.COLUMN_ALARM_NAME,
            Entry.COLUMN_ALARM_HOUR,
            Entry.COLUMN_ALARM_MINUTE,
            Entry.COLUMN_ALARM_ENABLED,
            Entry.COLUMN_ALARM_REPEATED,
            Entry.COLUMN_ALARM_TIME,
            Entry.COLUMN_ALARM_DAYS
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
