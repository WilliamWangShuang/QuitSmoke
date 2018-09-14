package clientservice.db;

import android.provider.BaseColumns;

public class QuitSmokeContract {
    private QuitSmokeContract() {}

    // defines the table schema no smoke place
    public static class NoSmokePlace implements BaseColumns {
        public static final String TABLE_NAME = "NO_SMOKE_PLACE";
        public static final String COLUMN_NAME_ADDRESS = "ADDRESS";
        public static final String COLUMN_NAME_LATITUDE = "LATITUDE";
        public static final String COLUMN_NAME_LONGITUDE = "LONGITUDE";
        public static final String COLUMN_NAME_NAME = "NAME";
        public static final String COLUMN_NAME_TYPE = "TYPE";
    }
}
