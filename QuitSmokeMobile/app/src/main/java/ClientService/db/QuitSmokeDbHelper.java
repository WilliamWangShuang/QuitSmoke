package clientservice.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuitSmokeDbHelper extends SQLiteOpenHelper {
    // create table contract
    private final QuitSmokeContract.ApplianceUsage applianceUsage = new QuitSmokeContract.ApplianceUsage();

    // create Table SQL
    private final String SQL_CREATE_ELECTRICITY_TABLE =
            "CREATE TABLE " + applianceUsage.TABLE_NAME + " (" +
                    applianceUsage.COLUMN_NAME_RESID + " INTEGER," +
                    applianceUsage.COLUMN_NAME_USAGEDATE + " TEXT," +
                    applianceUsage.COLUMN_NAME_USAGEHOUR + " INTEGER," +
                    applianceUsage.COLUMN_NAME_FRIDGEUSAGE + " REAL," +
                    applianceUsage.COLUMN_NAME_ACUSAGE + " REAL," +
                    applianceUsage.COLUMN_NAME_WMUSAGE + " REAL," +
                    applianceUsage.COLUMN_NAME_TEMPERATURE + " INTEGER)";

    // drop table SQL
    private final String SQL_DELETE_ELECTRICITY = "DROP TABLE IF EXISTS " + applianceUsage.TABLE_NAME;

    // define db version
    public static final int DATABASE_VERSION = 1;
    // set db name
    public static final String DATABASE_NAME = "SmartER.db";

    // constructer
    public QuitSmokeDbHelper(Context context) {
        // create db
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // create db Electricity table
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ELECTRICITY_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ELECTRICITY);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // get contract
    public QuitSmokeContract.ApplianceUsage getContract() {
        return applianceUsage;
    }
}
