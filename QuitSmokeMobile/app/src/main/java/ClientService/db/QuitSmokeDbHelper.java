package clientservice.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuitSmokeDbHelper extends SQLiteOpenHelper {
    // create table contract
    private final QuitSmokeContract.NoSmokePlace noSmokePlace = new QuitSmokeContract.NoSmokePlace();

    // create Table SQL
    private final String SQL_CREATE_NO_SMOKE_PLACE_TABLE =
            "CREATE TABLE " + noSmokePlace.TABLE_NAME + " (" +
                    noSmokePlace.COLUMN_NAME_ADDRESS + " TEXT," +
                    noSmokePlace.COLUMN_NAME_LATITUDE + " REAL," +
                    noSmokePlace.COLUMN_NAME_LONGITUDE + " REAL," +
                    noSmokePlace.COLUMN_NAME_NAME + " TEXT," +
                    noSmokePlace.COLUMN_NAME_TYPE + " TEXT)";

    // drop table SQL
    private final String SQL_DELETE_NO_SMOKE_PLACE_TABLE = "DROP TABLE IF EXISTS " + noSmokePlace.TABLE_NAME;

    // define db version
    public static final int DATABASE_VERSION = 1;
    // set db name
    public static final String DATABASE_NAME = "QuitSmoke.db";

    // constructor
    public QuitSmokeDbHelper(Context context) {
        // create db
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // create db NoSmokePlace table
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NO_SMOKE_PLACE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is to simply to discard the data and start over
        db.execSQL(SQL_DELETE_NO_SMOKE_PLACE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // get contract
    public QuitSmokeContract.NoSmokePlace getContract() {
        return noSmokePlace;
    }
}
