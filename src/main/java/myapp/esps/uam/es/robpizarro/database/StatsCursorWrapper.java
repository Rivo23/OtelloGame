package myapp.esps.uam.es.robpizarro.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import static myapp.esps.uam.es.robpizarro.database.RoundDataBaseSchema.StatsTable;
/**
 * Created by localuser01 on 12/04/17.
 */

public class StatsCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public StatsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public String[] getStats() {
        String win = getString(getColumnIndex(StatsTable.Cols.WINS));
        String draw = getString(getColumnIndex(StatsTable.Cols.DRAWS));
        String lost = getString(getColumnIndex(StatsTable.Cols.LOST));
        String[] foo = {win, draw, lost};
        return foo;
    }
}
