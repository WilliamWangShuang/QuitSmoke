package clientservice;

import android.animation.ValueAnimator;
import android.app.Application;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPoint;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import clientservice.entities.PlanEntity;

public class QuitSmokeClientUtils extends Application {
    // global attributes
    private static String name;
    private static String email;
    private static String password;
    private static String uid;
    private static int age;
    private static String smokerNodeName;
    private static String gender;
    private static String planNodeName;
    private static boolean isSmoker;
    private static boolean isPartner;
    private static CircleProgressBar mCustomProgressBar;

    // setters and getters

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        QuitSmokeClientUtils.name = name;
    }

    public static boolean isIsSmoker() {
        return isSmoker;
    }

    public static void setIsSmoker(boolean isSmoker) {
        QuitSmokeClientUtils.isSmoker = isSmoker;
    }

    public static boolean isIsPartner() {
        return isPartner;
    }

    public static void setIsPartner(boolean isPartner) {
        QuitSmokeClientUtils.isPartner = isPartner;
    }

    public static String getPlanNodeName() {
        return planNodeName;
    }

    public static void setPlanNodeName(String planNodeName) {
        QuitSmokeClientUtils.planNodeName = planNodeName;
    }

    public static String getGender() {
        return gender;
    }

    public static void setGender(String gender) {
        QuitSmokeClientUtils.gender = gender;
    }

    public static int getAge() {
        return age;
    }

    public static void setAge(int age) {
        QuitSmokeClientUtils.age = age;
    }

    public static String getSmokerNodeName() {
        return smokerNodeName;
    }

    public static void setSmokerNodeName(String smokerNodeName) {
        QuitSmokeClientUtils.smokerNodeName = smokerNodeName;
    }

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

    public static boolean validateGender(String gender, String message, TextView msgView) {
        boolean result = false;
        try {
            if ("M".equals(gender) || "F".equals(gender)) {
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

    @NonNull
    public static String getHttpUrlConnectionResponseErrorContent(HttpURLConnection urlConnection) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((urlConnection.getErrorStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        return sb.toString();
    }

    public static void simulateProgress(CircleProgressBar mCustomProgressBar, int progress) {
        // get progress bar
        QuitSmokeClientUtils.mCustomProgressBar = mCustomProgressBar;
        ValueAnimator animator = ValueAnimator.ofInt(0, progress);
        Log.d("QuitSmokeDebug", "progress from frontend is " + progress);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                QuitSmokeClientUtils.mCustomProgressBar.setProgress((int)animation.getAnimatedValue());
            }
        });
        animator.setRepeatCount(-2);
        animator.setDuration(4000);
        animator.start();
    }

    public static int getPlanPositionInPlanList(PlanEntity entity, List<PlanEntity> collection) {
        int pos = 0;
        for (PlanEntity item : collection) {
            // if both uid and status are equal, means we found the item. Because in a plan list, for each user (uid) can only have two plan entity (pending & agree)
            if (entity.getStatus().equals(item.getStatus()) && entity.getUid().equals(item.getUid())) {
                break;
            }
            // otherwise, iterate pos
            pos++;
        }
        return pos;
    }

    /**
     * Compares two bitmaps and gives the percentage of similarity
     *
     * @param bitmap1 input bitmap 1
     * @param bitmap2 input bitmap 2
     * @return a value between 0.0 to 1.0 . Note the method will return 0.0 if either of bitmaps are null nor of same size.
     *
     */
    public static float compareEquivalance(Bitmap bitmap1, Bitmap bitmap2) {

        if (bitmap1 == null || bitmap2 == null || bitmap1.getWidth() != bitmap2.getWidth() || bitmap1.getHeight() != bitmap2.getHeight()) {
            return 0f;
        }


        ByteBuffer buffer1 = ByteBuffer.allocate(bitmap1.getHeight() * bitmap1.getRowBytes());
        bitmap1.copyPixelsToBuffer(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(bitmap2.getHeight() * bitmap2.getRowBytes());
        bitmap2.copyPixelsToBuffer(buffer2);

        byte[] array1 = buffer1.array();
        byte[] array2 = buffer2.array();

        int len = array1.length; // array1 and array2 will be of some length.
        int count = 0;

        for(int i=0;i<len;i++) {
            if(array1[i] == array2[i]) {
                count++;
            }
        }

        return ((float)(count))/len;
    }

    /**
     * Finds the percentage of pixels that do are empty.
     *
     * @param bitmap input bitmap
     * @return a value between 0.0 to 1.0 . Note the method will return 0.0 if either of bitmaps are null nor of same size.
     *
     */
    public static float getTransparentPixelPercent(Bitmap bitmap) {

        if (bitmap == null) {
            return 0f;
        }

        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getHeight() * bitmap.getRowBytes());
        bitmap.copyPixelsToBuffer(buffer);

        byte[] array = buffer.array();

        int len = array.length;
        int count = 0;

        for(int i=0;i<len;i++) {
            if(array[i] == 0) {
                count++;
            }
        }

        return ((float)(count))/len;
    }
}
