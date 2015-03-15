package cz.duong.duongwake.database.tasks;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

/**
 * Vytvořeno David on 14. 3. 2015.
 */

//nevím, jaké jsou konvence pro nazývání generics, rád mám A,B,C
public abstract class DatabaseTask<A, B, C> extends AsyncTask<A, Void, B> {
    private SQLiteOpenHelper mHelper;
    private C mListener;


    public DatabaseTask(SQLiteOpenHelper sqLiteOpenHelper, C listener) {
        this.mHelper = sqLiteOpenHelper;
        this.mListener = listener;
    }

    public C getListener() {
        return mListener;
    }

    public SQLiteDatabase getWritableDatabase() {
        return mHelper.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase() {
        return mHelper.getReadableDatabase();
    }

    @Override
    protected abstract B doInBackground(A... params);

    @Override
    protected void onPostExecute(B result) {
        super.onPostExecute(result);
    };
}
