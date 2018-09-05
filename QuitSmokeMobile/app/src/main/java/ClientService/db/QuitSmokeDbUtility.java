package clientservice.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import clientservice.QuitSmokeClientConstant;
import clientservice.QuitSmokeClientUtils;

public class QuitSmokeDbUtility {
    // get db helper
    private QuitSmokeDbHelper dbHelper;
    //appliance usage contract
    QuitSmokeContract.ApplianceUsage applianceUsage;

    // constructor
    public QuitSmokeDbUtility(){}

    // constructor with param Context
    public QuitSmokeDbUtility(Context context) {
        dbHelper = new QuitSmokeDbHelper(context);
        applianceUsage = dbHelper.getContract();
    }

    // delete all data in electricity table
    public void truncateElectricityTable() {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = db.delete(applianceUsage.TABLE_NAME, null, null);
        db.close();
    }

    // insert appliance usage
    public long insertAppUsage(String usageDate, int usageHour, double fridgeUsage, double wsUsage, double acUsage, int temperature) {
        // row id of new usage data in SQLite Table
        long newRowId = -1;
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(applianceUsage.COLUMN_NAME_RESID, 0); // TODO: hardcode variable here, change it
            values.put(applianceUsage.COLUMN_NAME_USAGEDATE, usageDate);
            values.put(applianceUsage.COLUMN_NAME_USAGEHOUR, usageHour);
            values.put(applianceUsage.COLUMN_NAME_FRIDGEUSAGE, fridgeUsage);
            values.put(applianceUsage.COLUMN_NAME_ACUSAGE, acUsage);
            values.put(applianceUsage.COLUMN_NAME_WMUSAGE, wsUsage);
            values.put(applianceUsage.COLUMN_NAME_TEMPERATURE, temperature);

            // Insert the new row, returning the primary key value of the new row
            newRowId = db.insertOrThrow(applianceUsage.TABLE_NAME, null, values);
        } catch (SQLException ex) {
            Log.e("SmartERDebug", "SQLException when inserting hourly usage.\n" + QuitSmokeClientUtils.getExceptionInfo(ex));

        } catch (Exception ex) {
            Log.e("SmartERDebug", "Error occurred when inserting hourly usage.\n" + QuitSmokeClientUtils.getExceptionInfo(ex));
        } finally {
            db.close();
            return newRowId;
        }
    }

    // query current hour appliance usage by date, hour and resId
    public AppUsageEntity getCurrentHourAppUsage(int currentHour, int resId) {
        // get SQLite db
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // create table contract
        QuitSmokeContract.ApplianceUsage applianceUsage = new QuitSmokeContract.ApplianceUsage();
        // date formatter to covert current date to string so that can be used in SQLite query
        DateFormat df = new SimpleDateFormat(QuitSmokeClientConstant.DATE_FORMAT);
        // SQL string
        String queryString = "SELECT " + applianceUsage.COLUMN_NAME_FRIDGEUSAGE  + "," +
                applianceUsage.COLUMN_NAME_ACUSAGE + "," +
                applianceUsage.COLUMN_NAME_WMUSAGE +
                " FROM " + applianceUsage.TABLE_NAME +
                " WHERE " +
                applianceUsage.COLUMN_NAME_RESID + " = " +
                0 + " AND " +
                applianceUsage.COLUMN_NAME_USAGEDATE + " = '" +
                df.format(new Date()) + "' AND " +
                applianceUsage.COLUMN_NAME_USAGEHOUR + " = " +
                currentHour;// TODO: hardcode variable here, change it
        Log.d("SmartERDebug", "sql:" + queryString);

        // execute query
        Cursor c = db.rawQuery(queryString, null);
        // get the first met query record
        c.moveToFirst();
        // get usage data
        double fridgeData = 0.0;
        double acData = 0.0;
        double wmData = 0.0;
        if (c.getCount() > 0) {
            fridgeData = c.getDouble(0);
            acData = c.getDouble(1);
            wmData = c.getDouble(2);
        }

        db.close();
        // if no match reocord found, return null
        return c.getCount() > 0 ?  new AppUsageEntity(fridgeData, wmData, acData) : null;
    }

    // query all data exist in SQLite
    public List<AppUsageEntity> getAllExistData() {
        List<AppUsageEntity> result = new ArrayList<>();
        // get SQLite db
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // create table contract
        QuitSmokeContract.ApplianceUsage applianceUsage = new QuitSmokeContract.ApplianceUsage();
        // SQL string
        String queryString = "SELECT " + applianceUsage.COLUMN_NAME_FRIDGEUSAGE  + "," +
                applianceUsage.COLUMN_NAME_ACUSAGE + "," +
                applianceUsage.COLUMN_NAME_WMUSAGE +
                " FROM " + applianceUsage.TABLE_NAME;
        Log.d("SmartERDebug", "sql:" + queryString);

        // execute query
        Cursor c = db.rawQuery(queryString, null);
        // loop query result from SQLite and construct return reuslt
        while (c.moveToNext()) {
            // get usage data
            double fridgeData = c.getDouble(0);
            double acData = c.getDouble(1);
            double wmData = c.getDouble(2);

            // create one data entity and add it to result list
            AppUsageEntity appUsageEntity = new AppUsageEntity(fridgeData, wmData, acData);
            result.add(appUsageEntity);
        }

        // cloase db connection
        db.close();
        // returne result
        return result;
    }

    // entity class to transfer appliances usage for current hour
    public class AppUsageEntity {
        // fridge usage
        private double firdgeUsage;
        // washing machine usage
        private double wmUsage;
        // air conditioner usage
        private double acUsage;

        public AppUsageEntity(){}

        public  AppUsageEntity(double fridgeData, double wmData, double acData) {
            this.firdgeUsage = fridgeData;
            this.wmUsage = wmData;
            this.acUsage = acData;
        }

        // getters and setters
        public double getFirdgeUsage() {
            return firdgeUsage;
        }

        public double getWmUsage() {
            return wmUsage;
        }

        public double getAcUsage() {
            return acUsage;
        }

        public void setFirdgeUsage(double firdgeUsage) {
            this.firdgeUsage = firdgeUsage;
        }

        public void setWmUsage(double wmUsage) {
            this.wmUsage = wmUsage;
        }

        public void setAcUsage(double acUsage) {
            this.acUsage = acUsage;
        }
    }
}
