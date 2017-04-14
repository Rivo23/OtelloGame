package myapp.esps.uam.es.robpizarro.models;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import myapp.esps.uam.es.robpizarro.database.MyDatabase;

/**
 * Created by localuser01 on 31/03/17.
 */

public class RoundRepositoryFactory {
    private static final boolean LOCAL = true;
    public static RoundRepository createRepository(Context context) {
        RoundRepository repository;
        repository = LOCAL ? new MyDatabase(context) : new MyDatabase(context);
        try {
            repository.open();
        }
        catch (Exception e) {
            Log.w("myApp", e.getMessage());
            return null;
        }
        return repository;
    }
}
