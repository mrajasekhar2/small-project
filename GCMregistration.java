package com.adhishta.syam.brightfuture.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.adhishta.syam.brightfuture.Activity.LoginActivity;
import com.adhishta.syam.brightfuture.Activity.RegisterActivity;
import com.adhishta.syam.brightfuture.AsyncTask.HttpHelper;
import com.adhishta.syam.brightfuture.Dto.RegisterDto;
import com.adhishta.syam.brightfuture.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class GCMregistration {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    String SENDER_ID = "352694963361";//"352694963361";// 129176368736 //
    String response;
    String mobile;
    String pwd;
    GoogleCloudMessaging gcm;
    Context context;
    String regid, data, customer_status;
    // String data;
    RegisterDto register;
    long totalSize = 0;
    RegisterActivity signup;
    private boolean fromlogin = true;
    LoginActivity login;
    int is_fromlogin;

    public String initial(Context ctx, RegisterDto register, RegisterActivity signup, int is_fromlogin) {
        this.context = ctx;
        this.register = register;
        this.signup = signup;
        this.is_fromlogin = is_fromlogin;
        if (checkPlayServices()) {

            gcm = GoogleCloudMessaging.getInstance(context);
            registerInBackground();
        }
        return regid;
    }

    public String initial(Context ctx, RegisterDto register, LoginActivity login, int is_fromlogin) {
        this.context = ctx;
        this.register = register;
        this.login = login;
        this.is_fromlogin = is_fromlogin;
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(context);
            registerInBackground();
        }
        return regid;
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            // check if playservices avaliable are not.
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("Play services", "This device is not supported.");
                ((Activity) context).finish();
            }
            return false;
        }
        return true;
    }

    private void registerInBackground() {
        new AsyncTask<String, Object, String>() {
            ProgressDialog progressBar;

            protected void onPreExecute() {
                ProgressDialog progressDialog = new ProgressDialog((Activity) context);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressBar = progressDialog;
                progressBar.show();
            }

            @Override
            protected String doInBackground(String... params) {
                String msg = "";

                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context.getApplicationContext());
                    }

                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    SharedPreferences sp = context.getApplicationContext().getSharedPreferences("BrightFuture",
                            context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("devicekey", "" + gcm.register(SENDER_ID));
                    editor.commit();

                    if (fromlogin) {
                        fromlogin();
                    } else {
                        fromsignup();
                    }

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                    Utils.mCustomToast(context, "Device Registration Failed", 1);

                }
                return msg;
            }

            protected void onPostExecute(String result) {
                progressBar.dismiss();
                JSONObject jo;
                if (fromlogin) {
                    try {
                        jo = new JSONObject(response);
                        if (jo.getBoolean("status")) {
//                            JSONObject jsonObject = jo.getJSONObject("user");

                            SharedPreferences sp = context.getApplicationContext().getSharedPreferences("BrightFuture",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("user_id", jo.getJSONArray("user_details").getJSONObject(0).getString("user_id"));
                            editor.putString("user_firstname", jo.getJSONArray("user_details").getJSONObject(0).getString("user_firstname"));
                            editor.putString("user_lastname", jo.getJSONArray("user_details").getJSONObject(0).getString("user_lastname"));
                            editor.putString("user_email", jo.getJSONArray("user_details").getJSONObject(0).getString("user_email"));
                            editor.putString("user_mobile", jo.getJSONArray("user_details").getJSONObject(0).getString("user_mobile"));
                            editor.putString("user_dob", jo.getJSONArray("user_details").getJSONObject(0).getString("user_dob"));
                            editor.putString("ProfileImage", jo.getJSONArray("user_details").getJSONObject(0).getString("ProfileImage"));
                            editor.putString("user_gender", jo.getJSONArray("user_details").getJSONObject(0).getString("user_gender"));
                            editor.putString("school", jo.getJSONArray("user_details").getJSONObject(0).getString("school"));
                            editor.putString("college", jo.getJSONArray("user_details").getJSONObject(0).getString("college"));
                            editor.putString("work", jo.getJSONArray("user_details").getJSONObject(0).getString("work"));
                            editor.commit();

//                            DbHelper dh = new DbHelper(context);
//                            int c = DH.GETUSERCOUNT();
//                            if (c == 0) {
//                                dh.insertUserId(new User("1",
//                                        jsonObject.getString("emp_id"),
//                                        jsonObject.getString("emp_mobile"),
//                                        jsonObject.getString("emp_name"),
//                                        jsonObject.getString("emp_franchise"),
//                                        jsonObject.getString("emp_code")));
//                            } else {
//                                dh.insertUserId(new User("1",
//                                        jsonObject.getString("emp_id"),
//                                        jsonObject.getString("emp_mobile"),
//                                        jsonObject.getString("emp_name"),
//                                        jsonObject.getString("emp_franchise"),
//                                        jsonObject.getString("emp_code")));
//                            }

                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).finish();

                        } else {
                            Utils.mCustomToast(context, jo.getString("msg"), 1);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("@@@exception", "responce " + e.toString());
                    }

                } else if (!fromlogin) {
                    try {
                        jo = new JSONObject(response);
//                        {"status":true,"msg":"User added successfully"}

                        if (jo.getBoolean("status")) {

                            SharedPreferences sp = context.getApplicationContext().getSharedPreferences("BrightFuture",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("user_id", jo.getJSONArray("details").getJSONObject(0).getString("user_id"));
                            editor.putString("user_firstname", jo.getJSONArray("details").getJSONObject(0).getString("user_firstname"));
                            editor.putString("user_lastname", jo.getJSONArray("details").getJSONObject(0).getString("user_lastname"));
                            editor.putString("user_email", jo.getJSONArray("details").getJSONObject(0).getString("user_email"));
                            editor.putString("user_mobile", jo.getJSONArray("details").getJSONObject(0).getString("user_mobile"));
                            editor.putString("user_dob", jo.getJSONArray("details").getJSONObject(0).getString("user_dob"));
                            editor.putString("ProfileImage", jo.getJSONArray("details").getJSONObject(0).getString("ProfileImage"));
                            editor.putString("user_gender", jo.getJSONArray("details").getJSONObject(0).getString("user_gender"));
                            editor.putString("school", jo.getJSONArray("details").getJSONObject(0).getString("school"));
                            editor.putString("college", jo.getJSONArray("details").getJSONObject(0).getString("college"));
                            editor.putString("work", jo.getJSONArray("details").getJSONObject(0).getString("work"));
                            editor.commit();


                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).finish();

                        } else {
                            Utils.mCustomToast(context, jo.getString("msg"), 1);
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }

            ;
        }.execute(null, null, null);
    }

    public void fromsignup() {

        String data = "";
        try {

            data = URLEncoder.encode("user_firstname", "UTF-8") + "=" + URLEncoder.encode(register.getFirstname(), "UTF-8") + "&";
            data += URLEncoder.encode("user_lastname", "UTF-8") + "=" + URLEncoder.encode(register.getLsatname(), "UTF-8") + "&";
            data += URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(register.getUsername(), "UTF-8") + "&";
            data += URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(register.getEmail(), "UTF-8") + "&";
            data += URLEncoder.encode("user_mobile", "UTF-8") + "=" + URLEncoder.encode(register.getMobileNumber(), "UTF-8") + "&";
            data += URLEncoder.encode("user_dob", "UTF-8") + "=" + URLEncoder.encode(register.getDOB(), "UTF-8") + "&";
            data += URLEncoder.encode("user_gender", "UTF-8") + "=" + URLEncoder.encode(register.getGender(), "UTF-8") + "&";
            data += URLEncoder.encode("user_password", "UTF-8") + "=" + URLEncoder.encode(register.getPassword(), "UTF-8") + "&";
            data += URLEncoder.encode("Device_Unique_ID", "UTF-8") + "=" + URLEncoder.encode(Build.SERIAL, "UTF-8") + "&";
            data += URLEncoder.encode("Device_Make", "UTF-8") + "=" + URLEncoder.encode(Build.MANUFACTURER, "UTF-8") + "&";
            data += URLEncoder.encode("Device_Type", "UTF-8") + "=" + URLEncoder.encode("android", "UTF-8") + "&";
            data += URLEncoder.encode("Device_OS_Version", "UTF-8") + "=" + URLEncoder.encode(Build.VERSION.RELEASE, "UTF-8") + "&";
            data += URLEncoder.encode("Device_Model", "UTF-8") + "=" + URLEncoder.encode(Build.MODEL, "UTF-8") + "&";
            data += URLEncoder.encode("App_Version", "UTF-8") + "=" + URLEncoder.encode("1.0.0", "UTF-8") + "&";
            data += URLEncoder.encode("source", "UTF-8") + "=" + URLEncoder.encode("" + is_fromlogin, "UTF-8") + "&";
            data += URLEncoder.encode("gcm_key", "UTF-8") + "=" + URLEncoder.encode(regid, "UTF-8");


            if (Utils.isNetworkConnected(context)) {
                response = HttpHelper.postRequestToServer(data, new URL(Constants.base_url + Constants.signup_url));
            } else {
                Utils.mCustomToast(context, "Ckeck Your Internet Connectivity..", 1);
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private void fromlogin() {

        String data = "";
        try {

            data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(register.getUsername(), "UTF-8") + "&";
            data += URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(register.getPassword(), "UTF-8") + "&";
            data += URLEncoder.encode("gcm_key", "UTF-8") + "=" + URLEncoder.encode(regid, "UTF-8");


            if (Utils.isNetworkConnected(context)) {
                response = HttpHelper.postRequestToServer(data, new URL(Constants.base_url + Constants.login_url));
            } else {
                Utils.mCustomToast(context, "Ckeck Your Internet Connectivity..", 1);
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void setFrom_login(boolean b) {
        this.fromlogin = b;

    }

}
