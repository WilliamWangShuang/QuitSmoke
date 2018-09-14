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
import clientservice.entities.NoSmokeItem;
import clientservice.entities.NoSmokePlace;

public class QuitSmokeDbUtility {
    // get db helper
    private QuitSmokeDbHelper dbHelper;
    //appliance usage contract
    QuitSmokeContract.NoSmokePlace noSmokePlace;

    // constructor
    public QuitSmokeDbUtility(){}

    // constructor with param Context
    public QuitSmokeDbUtility(Context context) {
        dbHelper = new QuitSmokeDbHelper(context);
        noSmokePlace = dbHelper.getContract();
    }

    // delete all data in no smoke place table
    public void truncateNoSmokePlaceTable() {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = db.delete(noSmokePlace.TABLE_NAME, null, null);
        db.close();
    }

    // insert no smoke place
    public long insertNoSmokePlaceUsage(String address, double lat, double lon, String name, String type) {
        // row id of new usage data in SQLite Table
        long newRowId = -1;
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(noSmokePlace.COLUMN_NAME_ADDRESS, address);
            values.put(noSmokePlace.COLUMN_NAME_LATITUDE, lat);
            values.put(noSmokePlace.COLUMN_NAME_LONGITUDE, lon);
            values.put(noSmokePlace.COLUMN_NAME_NAME, name);
            values.put(noSmokePlace.COLUMN_NAME_TYPE, type);

            // Insert the new row, returning the primary key value of the new row
            newRowId = db.insertOrThrow(noSmokePlace.TABLE_NAME, null, values);
        } catch (SQLException ex) {
            Log.e("QuitSmokeDebug", "SQLException when inserting no smoke place.\n" + QuitSmokeClientUtils.getExceptionInfo(ex));

        } catch (Exception ex) {
            Log.e("QuitSmokeDebug", "Error occurred when inserting no smoke place.\n" + QuitSmokeClientUtils.getExceptionInfo(ex));
        } finally {
            db.close();
            return newRowId;
        }
    }

    public List<String> getPlaceTypeList() {
        List<String> placeTypeList = new ArrayList<>();
        // get SQLite db
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // create table contract
        QuitSmokeContract.NoSmokePlace noSmokePlace = new QuitSmokeContract.NoSmokePlace();
        // SQL string
        String queryString = "SELECT DISTINCT " + noSmokePlace.COLUMN_NAME_TYPE + " FROM " + noSmokePlace.TABLE_NAME;
        // execute query
        Cursor c = db.rawQuery(queryString, null);
        while (c.moveToNext()) {
            // get usage data
            String placeType = c.getString(0);
            placeTypeList.add(placeType);
        }

        Log.d("QuitSmokeDebug", "sql:" + queryString + "\nType No.:" + placeTypeList.size());
        return placeTypeList;
    }

    // query no smoke place by type
    public List<NoSmokePlace> getNoSmokePlaceByType(String type) {
        List<NoSmokePlace> result = new ArrayList<>();
        // get SQLite db
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // create table contract
        QuitSmokeContract.NoSmokePlace noSmokePlace = new QuitSmokeContract.NoSmokePlace();
        // SQL string
        String queryString = "SELECT " + noSmokePlace.COLUMN_NAME_ADDRESS  + "," +
                noSmokePlace.COLUMN_NAME_LATITUDE + "," +
                noSmokePlace.COLUMN_NAME_LONGITUDE + "," +
                noSmokePlace.COLUMN_NAME_NAME + "," +
                noSmokePlace.COLUMN_NAME_TYPE +
                " FROM " + noSmokePlace.TABLE_NAME +
                " WHERE " +
                noSmokePlace.COLUMN_NAME_TYPE + " = " + type;
        Log.d("QuitSmokeDebug", "sql:" + queryString);

        // execute query
        Cursor c = db.rawQuery(queryString, null);
        // get the first met query record
        while (c.moveToNext()) {
            // get usage data
            String address = c.getString(0);
            double latitude = c.getDouble(1);
            double longitude = c.getDouble(2);
            String name = c.getString(3);
            String entityType = c.getString(4);

            // create one data entity and add it to result list
            NoSmokePlace entity = new NoSmokePlace();
            entity.setType(entityType);
            NoSmokeItem noSmokeItem = new NoSmokeItem(address, latitude, longitude, name, entityType);
            // if the current no smoke place item has the type we want, then proceed
            boolean isSameTypeExist = false;
            for (NoSmokePlace place : result) {
                if (place.getType().equals(entityType)) {
                    isSameTypeExist = true;
                    place.getList().add(noSmokeItem);
                    break;
                }
            }
            // if no same type place exist, add it into result
            if (!isSameTypeExist) {
                List<NoSmokeItem> noSmokeItemList = new ArrayList<>();
                noSmokeItemList.add(noSmokeItem);
                entity.setList(noSmokeItemList);
                result.add(entity);
            }

            result.add(entity);
        }

        db.close();
        // if no match reocord found, return null
        return result;
    }

    // query all data exist in SQLite
    public List<NoSmokePlace> getAllExistingPlaces() {
        List<NoSmokePlace> result = new ArrayList<>();
        // get SQLite db
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // create table contract
        QuitSmokeContract.NoSmokePlace noSmokePlaceContract = new QuitSmokeContract.NoSmokePlace();
        // SQL string
        String queryString = "SELECT " + noSmokePlace.COLUMN_NAME_ADDRESS  + "," +
                noSmokePlace.COLUMN_NAME_LATITUDE + "," +
                noSmokePlace.COLUMN_NAME_LONGITUDE + "," +
                noSmokePlace.COLUMN_NAME_NAME + "," +
                noSmokePlace.COLUMN_NAME_TYPE +
                " FROM " + noSmokePlace.TABLE_NAME;
        Log.d("QuitSmokeDebug", "sql:" + queryString);

        // execute query
        Cursor c = db.rawQuery(queryString, null);
        // loop query result from SQLite and construct return reuslt
        while (c.moveToNext()) {
            // get usage data
            String address = c.getString(0);
            double latitude = c.getDouble(1);
            double longitude = c.getDouble(2);
            String name = c.getString(3);
            String entityType = c.getString(4);

            // create one data entity and add it to result list
            NoSmokePlace entity = new NoSmokePlace();
            entity.setType(entityType);
            NoSmokeItem noSmokeItem = new NoSmokeItem(address, latitude, longitude, name, entityType);
            // if the current no smoke place item has the type we want, then proceed
            boolean isSameTypeExist = false;
            for (NoSmokePlace place : result) {
                if (place.getType().equals(entityType)) {
                    isSameTypeExist = true;
                    place.getList().add(noSmokeItem);
                    break;
                }
            }
            // if no same type place exist, add it into result
            if (!isSameTypeExist) {
                List<NoSmokeItem> noSmokeItemList = new ArrayList<>();
                noSmokeItemList.add(noSmokeItem);
                entity.setList(noSmokeItemList);
//                result.add(entity);
            }

            result.add(entity);
        }

        // cloase db connection
        db.close();
        // returne result
        return result;
    }
}
