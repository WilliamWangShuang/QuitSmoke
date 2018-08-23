package ClientService;

import android.app.Application;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuitSmokeClientUtils extends Application {
    // global attributes
    private static String email;
    private static String password;
    private static String uid;

    // setters and getters

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        QuitSmokeClientUtils.uid = uid;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        QuitSmokeClientUtils.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        QuitSmokeClientUtils.password = password;
    }

    // validate one field - empty
    public static boolean validateEmpty(String str, String message, TextView msgView) {
        boolean result = false;
        if (str == null || str.isEmpty()){
            result = false;
            msgView.setText(message);
        } else {
            result = true;
            msgView.setText("");
        }

        // return validate result
        return result;
    }

    // validate email format
    public static boolean validateEmailFormat(String str, String message, TextView msgView) {
        boolean result = false;
        if(!str.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*+@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            result = false;
            msgView.setText(message);
        } else {
            result = true;
            msgView.setText("");
        }
        return result;
    }

    // valdate password format
    public static boolean validatePwdFormat(String str, String message, TextView msgView) {
        boolean result = false;
        // check if the string is a strong password. At least 8 length. Contains at least 1 special character, 1 lower&upper letter, and 1 number
        if(!str.trim().matches("^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8}$")){
            result = false;
            msgView.setText(message);
        } else {
            result = true;
            msgView.setText("");
        }
        return result;
    }

    public static boolean validateAge(String age, String message, TextView msgView) {
        boolean result = false;
        try {
            int inputAge = Integer.parseInt(age);
            if (inputAge >=20 && inputAge<= 80) {
                result = true;
                msgView.setText("");
            } else {
                result = false;
                msgView.setText(message);
            }
        } catch (Exception ex) {
            Log.e("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
            result = false;
            msgView.setText(message);
        }
        return result;
    }

    //  encrypt password
    public static String encryptPwd(String pwd) {
        String result = "";

        try{
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(pwd.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            result = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    // format exception message
    public static String getExceptionInfo(Exception ex) {
        StringBuilder sb = new StringBuilder((ex.getMessage() + "\n"));
        for (StackTraceElement el : ex.getStackTrace()) {
            sb.append(el.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    // format date to string
    public static String convertDateToString(Date date) {
        SimpleDateFormat df =  new SimpleDateFormat(QuitSmokeClientConstant.DATE_FORMAT);
        return df.format(date);
    }

    // format string to date yyyy-MM-dd
    public static Date convertStringToDate(String str) throws ParseException {
        try {
            return new SimpleDateFormat(QuitSmokeClientConstant.DATE_FORMAT).parse(str);
        } catch (Exception ex) {
            throw ex;
        }
    }

    // check string null or empty
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    // read http response into a stream
    public static String readInputStreamToString(HttpURLConnection connection) {
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;

        try {
            is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        }
        catch (Exception e) {
            Log.i("QuitSmokeDebug", "Error reading InputStream");
            result = null;
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                    Log.i("QuitSmokeDebug", "Error closing InputStream");
                }
            }
        }

        return result;
    }
}
