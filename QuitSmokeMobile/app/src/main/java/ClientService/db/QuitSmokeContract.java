package clientservice.db;

import android.provider.BaseColumns;

public class QuitSmokeContract {
    private QuitSmokeContract() {}

    // defines the table schema appliance usage
    public static class ApplianceUsage implements BaseColumns {
        public static final String TABLE_NAME = "ELECTRICITY_USAGE";
        public static final String COLUMN_NAME_RESID = "RESID";
        public static final String COLUMN_NAME_USAGEDATE = "USAGEDATE";
        public static final String COLUMN_NAME_USAGEHOUR = "USAGEHOUR";
        public static final String COLUMN_NAME_FRIDGEUSAGE = "FRIDGEUSAGE";
        public static final String COLUMN_NAME_ACUSAGE = "ACUSAGE";
        public static final String COLUMN_NAME_WMUSAGE = "WMUSAGE";
        public static final String COLUMN_NAME_TEMPERATURE = "TEMPERATURE";
    }
}
