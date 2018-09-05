package clientservice.webservice;

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;
import clientservice.QuitSmokeClientConstant;
import clientservice.QuitSmokeClientUtils;

public class BaseWebservice {

    // get a web service based on URL
    public static JSONObject requestWebService(String serviceUrl) throws IOException, JSONException {
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        JSONObject resultJSONObj = null;
        int statusCode;
        String exceptionJSON = "{%s:%s}";
        String responseFromWS = "";

        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();
            // set content type to JSON
            urlConnection.setRequestProperty("Content-Type", "application/json");
            // handle issues
            statusCode = urlConnection.getResponseCode();

            // handle different web service response status
            switch (statusCode) {
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    resultJSONObj = new JSONObject(String.format(exceptionJSON, QuitSmokeClientConstant.WS_KEY_EXCEPTION, QuitSmokeClientConstant.MSG_401));
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    resultJSONObj = new JSONObject(String.format(exceptionJSON, QuitSmokeClientConstant.WS_KEY_EXCEPTION, QuitSmokeClientConstant.MSG_404));
                    break;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    resultJSONObj = new JSONObject(String.format(exceptionJSON, QuitSmokeClientConstant.WS_KEY_EXCEPTION, QuitSmokeClientConstant.MSG_500));
                    break;
                case HttpURLConnection.HTTP_OK:
                    // get response stream from web service
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    // put the stream content into string
                    responseFromWS = getResponseText(in);
                    // create result Json from the content string
                    resultJSONObj = new JSONObject(responseFromWS);
                    break;
                default:
                    break;
            }
        } catch (MalformedURLException e) {
            // URL is invalid
            throw e;
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
            throw e;
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
            throw e;
        } catch (JSONException e) {
            // response body is no valid JSON string
            throw e;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return resultJSONObj;
    }

    // get a web service based on URL
    public static String requestWebServicePlainText(String serviceUrl) throws IOException, JSONException {
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        String result = null;
        int statusCode;
        String exceptionJSON = "{%s:%s}";

        try {
            // create connection
            Log.d("QuitSmokeDebug", "coming in url:" + serviceUrl);
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();
            // set content type to JSON
            urlConnection.setRequestProperty("Content-Type", "application/json");
            // handle issues
            statusCode = urlConnection.getResponseCode();

            // handle different web service response status
            switch (statusCode) {
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    result = String.format(exceptionJSON, QuitSmokeClientConstant.WS_KEY_EXCEPTION, QuitSmokeClientConstant.MSG_401);
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    result = String.format(exceptionJSON, QuitSmokeClientConstant.WS_KEY_EXCEPTION, QuitSmokeClientConstant.MSG_404);
                    break;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    result = String.format(String.format(exceptionJSON, QuitSmokeClientConstant.WS_KEY_EXCEPTION, QuitSmokeClientConstant.MSG_500));
                    break;
                case HttpURLConnection.HTTP_OK:
                    // get response stream from web service
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    // put the stream content into string
                    result = getResponseText(in);
                    Log.d("QuitSmokeDebug","check email exist result:" + result);
                    break;
                default:
                    break;
            }
        } catch (MalformedURLException e) {
            // URL is invalid
            throw e;
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
            throw e;
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
            throw e;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result;
    }

    // get a json array by ws URL
    public static JSONArray requestWebServiceArray(String serviceUrl) throws IOException, JSONException {
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        JSONArray resultJSONArray = null;
        int statusCode;
        String exceptionJSON = "{%s:%s}";
        String responseFromWS = "";

        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();
            // set content type to JSON
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            // handle issues
            statusCode = urlConnection.getResponseCode();

            // handle different web service response status
            switch (statusCode) {
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    resultJSONArray = new JSONArray(String.format(exceptionJSON, QuitSmokeClientConstant.WS_KEY_EXCEPTION, QuitSmokeClientConstant.MSG_401));
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    throw new NullPointerException("");
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    resultJSONArray = new JSONArray(String.format(exceptionJSON, QuitSmokeClientConstant.WS_KEY_EXCEPTION, QuitSmokeClientConstant.MSG_500));
                    break;
                case HttpURLConnection.HTTP_OK:
                    // get response stream from web service
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    // put the stream content into string
                    responseFromWS = getResponseText(in);
                    // create result Json from the content string
                    resultJSONArray = new JSONArray(responseFromWS);
                    break;
                default:
                    break;
            }
        } catch (MalformedURLException e) {
            // URL is invalid
            throw e;
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
            throw e;
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
            throw e;
        } catch (JSONException e) {
            // response body is no valid JSON string
            throw e;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return resultJSONArray;
    }

    // make a POST http request to a web service
    public static String postWebService(String serviceUrl, JSONObject jsonParam) throws IOException {
        // response result
        String result = "";

        // declare a url connection
        HttpURLConnection urlConnection=null;

        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();
            // set http request is POST
            urlConnection.setRequestMethod("POST");
            // disable caches
            urlConnection.setUseCaches(false);
            // set time out in case net is slow
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            // set post request header
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            // set post send true. allow to send to ws
            urlConnection.setDoOutput(true);

            // set stream sent to server
            OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
            outputPost.write(jsonParam.toString().getBytes());
            outputPost.flush();
            outputPost.close();

            // connect url
            urlConnection.connect();

            // get server response status
            int HttpResult = urlConnection.getResponseCode();
            String httpMsg = QuitSmokeClientUtils.readInputStreamToString(urlConnection);
            Log.d("QuitSmokeDebug", "http code:" + HttpResult + ",message:" + httpMsg);
            if(HttpResult == HttpURLConnection.HTTP_OK){
                if (QuitSmokeClientConstant.EMAIL_EXIST.equals(httpMsg))
                    result = QuitSmokeClientConstant.EMAIL_EXIST;
                else {
                    result = QuitSmokeClientConstant.SUCCESS_MSG;
                    // check request url so that to update different application level attribute
                    if (serviceUrl.endsWith(QuitSmokeClientConstant.REGISTER_WS)) {
                        // if this is a register request, set value of smoker node name
                        JSONObject smokerNodeNameJson = new JSONObject(httpMsg);
                        QuitSmokeClientUtils.setSmokerNodeName(smokerNodeNameJson.getString(QuitSmokeClientConstant.WS_JSON_USER_KEY_NAME));
                    }
                }
            }else{
                result = urlConnection.getResponseMessage();
            }
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        } finally {
            // close connection
            if(urlConnection!=null)
                urlConnection.disconnect();
            return result;
        }
    }

    public static JSONObject postWebServiceForGetRestrieveJSON(String serviceUrl, JSONObject jsonParam) throws IOException {
        // response result
        JSONObject result = null;

        // declare a url connection
        HttpURLConnection urlConnection=null;

        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();
            // set http request is POST
            urlConnection.setRequestMethod("POST");
            // disable caches
            urlConnection.setUseCaches(false);
            // set time out in case net is slow
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            // set post request header
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            // set post send true. allow to send to ws
            urlConnection.setDoOutput(true);

            // set stream sent to server
            OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
            outputPost.write(jsonParam.toString().getBytes());
            outputPost.flush();
            outputPost.close();

            // connect url
            urlConnection.connect();

            // get server response status
            int HttpResult = urlConnection.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_OK) {
                // get response stream from web service
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                // put the stream content into string
                String responseFromWS = getResponseText(in);
                result = new JSONObject(responseFromWS);
            }
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        } finally {
            // close connection
            if(urlConnection!=null)
                urlConnection.disconnect();
            return result;
        }
    }

    public static String postWSForGetRestrievePlainText(String serviceUrl, JSONObject jsonParam) throws IOException {
        // response result
        String result = null;

        // declare a url connection
        HttpURLConnection urlConnection=null;

        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();
            // set http request is POST
            urlConnection.setRequestMethod("POST");
            // disable caches
            urlConnection.setUseCaches(false);
            // set time out in case net is slow
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            // set post request header
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            // set post send true. allow to send to ws
            urlConnection.setDoOutput(true);

            // set stream sent to server
            OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
            outputPost.write(jsonParam.toString().getBytes());
            outputPost.flush();
            outputPost.close();

            // connect url
            urlConnection.connect();

            // get server response status
            int HttpResult = urlConnection.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_OK) {
                // get response stream from web service
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                // put the stream content into string
                String responseFromWS = getResponseText(in);
                result = responseFromWS;
            }
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        } finally {
            // close connection
            if(urlConnection!=null)
                urlConnection.disconnect();
            return result;
        }
    }

    public static String postWSForGetRestrievePlainText(String serviceUrl, String variable) throws IOException {
        // response result
        String result = null;

        // declare a url connection
        HttpURLConnection urlConnection = null;

        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();
            // set http request is POST
            urlConnection.setRequestMethod("POST");
            // disable caches
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            // set time out in case net is slow
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            // set post send true. allow to send to ws
            urlConnection.setDoOutput(true);

            // set stream sent to server
            OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
            OutputStreamWriter osw = new OutputStreamWriter(outputPost, "UTF-8");
            osw.write("\"" + variable + "\"");
            osw.flush();
            osw.close();
            outputPost.close();

            // connect url
            urlConnection.connect();

            // get server response status
            int HttpResult = urlConnection.getResponseCode();
            Log.d("QuitSmokeDebug", HttpResult + "\n" + urlConnection.getResponseMessage());

            if(HttpResult == HttpURLConnection.HTTP_OK) {
                // get response stream from web service
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                // put the stream content into string
                String responseFromWS = getResponseText(in);
                result = responseFromWS;
            }
        } catch (IOException ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
            throw ex;
        } catch (Exception ex) {
            Log.d("QuitSmokeDebug", QuitSmokeClientUtils.getExceptionInfo(ex));
            throw ex;
        } finally {
            // close connection
            if(urlConnection!=null)
                urlConnection.disconnect();
            return result;
        }
    }

    // make a POST http request to a web service
    public static String postWebServiceSyncAllData(String serviceUrl, JSONArray jsonParam) throws IOException {
        // response result
        String result = "";
        // declare a url connection
        HttpURLConnection urlConnection=null;

        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();
            // set http request is POST
            urlConnection.setRequestMethod("POST");
            // disable caches
            urlConnection.setUseCaches(false);
            // set time out in case net is slow
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            // set post request header
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            // set post send true. allow to send to ws
            urlConnection.setDoOutput(true);

            // set stream sent to server
            OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
            outputPost.write(jsonParam.toString().getBytes());
            outputPost.flush();
            outputPost.close();

            // connect url
            urlConnection.connect();

            // get server response status
            int HttpResult = urlConnection.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_NO_CONTENT){
                result = QuitSmokeClientConstant.SUCCESS_MSG;
            }else{
                result = urlConnection.getResponseMessage();
            }
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        } finally {
            // close connection
            if(urlConnection!=null)
                urlConnection.disconnect();
            return result;
        }
    }

    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        String str = new Scanner(inStream).useDelimiter("\\A").next();
        return str;
    }
}
