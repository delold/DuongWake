package cz.duong.duongwake.database.tasks;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

/**
 * Vytvořeno David on 14. 3. 2015.
 */

public abstract class DatabaseTask<Params, Result, Listener> extends AsyncTask<Params, Void, Result> {
    private SQLiteOpenHelper mHelper;
    private Listener mListener;


    public DatabaseTask(SQLiteOpenHelper sqLiteOpenHelper, Listener listener) {
        this.mHelper = sqLiteOpenHelper;
        this.mListener = listener;
    }

    public Listener getListener() {
        return mListener;
    }

    public SQLiteDatabase getWritableDatabase() {
        return mHelper.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase() {
        return mHelper.getReadableDatabase();
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    protected abstract Result doInBackground(Params... params);

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
    }
}
