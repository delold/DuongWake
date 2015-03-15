package cz.duong.duongwake;

import cz.duong.duongwake.database.Database;

/**
 * Vytvořeno David on 15. 3. 2015.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        Database.deleteDatabase(this);

        super.onCreate();
    }

}
