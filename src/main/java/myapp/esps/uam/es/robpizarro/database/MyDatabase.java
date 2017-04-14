package myapp.esps.uam.es.robpizarro.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import myapp.esps.uam.es.robpizarro.R;
import myapp.esps.uam.es.robpizarro.activities.PreferenceActivity;
import myapp.esps.uam.es.robpizarro.models.Round;
import myapp.esps.uam.es.robpizarro.models.RoundRepository;

import static myapp.esps.uam.es.robpizarro.database.RoundDataBaseSchema.RoundTable;
import static myapp.esps.uam.es.robpizarro.database.RoundDataBaseSchema.UserTable;
import static myapp.esps.uam.es.robpizarro.database.RoundDataBaseSchema.StatsTable;

/**
 * Created by localuser01 on 31/03/17.
 */

public class MyDatabase implements RoundRepository {
    private final String DEBUG_TAG = "DEBUG";
    private static final String DATABASE_NAME = "er.db";
    private static final int DATABASE_VERSION = 8;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public MyDatabase(Context context) {
        helper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            createTable(db);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
            db.execSQL("DROP TABLE IF EXISTS " + UserTable.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + RoundTable.NAME);
            createTable(db);
        }

        private void createTable(SQLiteDatabase db) {
            String str1 = "CREATE TABLE " + UserTable.NAME + " ("
                    + "_id integer primary key autoincrement, "
                    + UserTable.Cols.PLAYERUUID + " TEXT UNIQUE, "
                    + UserTable.Cols.PLAYERNAME + " TEXT UNIQUE, "
                    + UserTable.Cols.PLAYERPASSWORD + " TEXT);";
            String str2 = "CREATE TABLE " + RoundTable.NAME + " ("
                    + "_id integer primary key autoincrement, "
                    + RoundTable.Cols.ROUNDUUID + " TEXT UNIQUE, "
                    + RoundTable.Cols.PLAYERUUID + " TEXT REFERENCES "+UserTable.Cols.PLAYERUUID + ", "
                    + RoundTable.Cols.DATE + " TEXT, "
                    + RoundTable.Cols.TITLE + " TEXT, "
                    + RoundTable.Cols.SIZE + " TEXT, "
                    + RoundTable.Cols.BOARD + " TEXT);";
            String str3 = "CREATE TABLE "+ StatsTable.NAME+" ("
                    +StatsTable.Cols.PLAYERUUID+" TEXT UNIQUE, "
                    +StatsTable.Cols.WINS+" TEXT, "
                    +StatsTable.Cols.DRAWS+" TEXT, "
                    +StatsTable.Cols.LOST+" TEXT);";
            try {
                db.execSQL(str1);
                db.execSQL(str2);
                db.execSQL(str3);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    @Override
    public void login(String playername, String playerpassword, LoginRegisterCallback callback) {
        Log.d(DEBUG_TAG, "Login " + playername);
        Cursor cursor = db.query(UserTable.NAME,
                new String[]{UserTable.Cols.PLAYERUUID},
                UserTable.Cols.PLAYERNAME + " = ? AND " + UserTable.Cols.PLAYERPASSWORD + " = ?",
                new String[]{playername, playerpassword}, null, null, null);
        int count = cursor.getCount();
        String uuid = count == 1 && cursor.moveToFirst() ? cursor.getString(0) : "";
        cursor.close();
        if (count == 1)
            callback.onLogin(uuid);
        else
            callback.onError("Username or password incorrect.");
    }

    @Override
    public void register(String playername, String password, LoginRegisterCallback callback) {
        ContentValues values = new ContentValues();
        String uuid = UUID.randomUUID().toString();
        values.put(UserTable.Cols.PLAYERUUID, uuid);
        values.put(UserTable.Cols.PLAYERNAME, playername);
        values.put(UserTable.Cols.PLAYERPASSWORD, password);
        long id = db.insert(UserTable.NAME, null, values);
        if (id < 0)
            callback.onError("Error inserting new player named " + playername);
        else
            callback.onLogin(uuid);
        ContentValues values1 = new ContentValues();
        values1.put(StatsTable.Cols.PLAYERUUID, uuid);
        values1.put(StatsTable.Cols.WINS, "0");
        values1.put(StatsTable.Cols.DRAWS, "0");
        values1.put(StatsTable.Cols.LOST, "0");
        db.insert(StatsTable.NAME, null, values1);
    }
    private ContentValues getContentValues(Round round) {
        ContentValues values = new ContentValues();
        values.put(RoundTable.Cols.PLAYERUUID, round.getPlayerUUID());
        values.put(RoundTable.Cols.ROUNDUUID, round.getId());
        values.put(RoundTable.Cols.DATE, round.getDate());
        values.put(RoundTable.Cols.TITLE, round.getTitle());
        values.put(RoundTable.Cols.SIZE, round.getSize());
        values.put(RoundTable.Cols.BOARD, round.getBoard().tableroToString());
        return values;
    }

    @Override
    public void addRound(Round round, BooleanCallback callback) {
        ContentValues values = getContentValues(round);
        long id = db.insert(RoundTable.NAME, null, values);
        if (callback != null)
            callback.onResponse(id >= 0);
    }

    @Override
    public void updateRound(Round round, BooleanCallback callback) {
        ContentValues values = getContentValues(round);
        long id = db.update(RoundTable.NAME, values, RoundTable.Cols.ROUNDUUID+"=?", new String[]{round.getId()});
        if (callback != null)
            callback.onResponse(id >= 1);
    }
    @Override
    public String[] getStats(String playeruuid){
        String sql = "SELECT " +StatsTable.Cols.WINS+", "+
                StatsTable.Cols.DRAWS+", "+
                StatsTable.Cols.LOST+" "+
                "FROM "+StatsTable.NAME+" AS p "+
                "WHERE "+"p."+StatsTable.Cols.PLAYERUUID+"='"+playeruuid+"';";
        Cursor cursor = db.rawQuery(sql, null);
        StatsCursorWrapper cursorWrapper = new StatsCursorWrapper(cursor);
        cursor.moveToFirst();
        String[] foo = cursorWrapper.getStats();
        cursor.close();
        return foo;
    }

    @Override
    public void updateStats(String result[], String playeruuid, BooleanCallback callback) {
        ContentValues values = new ContentValues();
        values.put(StatsTable.Cols.PLAYERUUID, playeruuid);
        values.put(StatsTable.Cols.WINS, result[0]);
        values.put(StatsTable.Cols.DRAWS, result[1]);
        values.put(StatsTable.Cols.LOST, result[2]);
        long id = db.update(StatsTable.NAME, values, StatsTable.Cols.PLAYERUUID+"=?", new String[]{playeruuid});
        if (callback != null)
            callback.onResponse(id>=1);
    }

    private RoundCursorWrapper queryRounds() {
        String sql = "SELECT " + UserTable.Cols.PLAYERNAME + ", " +
                UserTable.Cols.PLAYERUUID + ", " +
                RoundTable.Cols.ROUNDUUID + ", " +
                RoundTable.Cols.DATE + ", " +
                RoundTable.Cols.TITLE + ", " +
                RoundTable.Cols.SIZE + ", " +
                RoundTable.Cols.BOARD + " " +
                "FROM " + UserTable.NAME + " AS p, " +
                RoundTable.NAME + " AS r " +
                "WHERE " + "p." + UserTable.Cols.PLAYERUUID + "=" +
                "r." + RoundTable.Cols.PLAYERUUID + ";";
        Cursor cursor = db.rawQuery(sql, null);
        return new RoundCursorWrapper(cursor);
    }

    @Override
    public void getRounds(String playeruuid, String orderByField, String group, RoundsCallback callback) {
            List<Round> rounds = new ArrayList<>();
            RoundCursorWrapper cursor = queryRounds();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Round round = cursor.getRound();
                if (round.getPlayerUUID().equals(playeruuid))
                    rounds.add(round);
                cursor.moveToNext();
            }
            cursor.close();
            if (cursor.getCount() > 0)
                callback.onResponse(rounds);
            else {
                if(cursor!=null){
                    callback.onError("No rounds found in database");
                }else {
                    callback.onError("Fallo en abrir repositorio");
                }
            }
    }


}
