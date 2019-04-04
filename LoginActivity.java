package com.adhishta.syam.brightfuture.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.adhishta.syam.brightfuture.AsyncTask.Asyntask;
import com.adhishta.syam.brightfuture.AsyncTask.ResponseListner;
import com.adhishta.syam.brightfuture.Dto.RegisterDto;
import com.adhishta.syam.brightfuture.MainActivity;
import com.adhishta.syam.brightfuture.R;
import com.adhishta.syam.brightfuture.Utils.Constants;
import com.adhishta.syam.brightfuture.Utils.GCMregistration;
import com.adhishta.syam.brightfuture.Utils.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Created by admin on 05/03/2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ResponseListner, GoogleApiClient.OnConnectionFailedListener {

    TextView tv_login, tv_newuser, tv_forpassword;
    ImageView iv_facebook, iv_google;
    EditText et_username, et_password;
    Context mContext;
    RegisterDto dto;
    String User_id, versionName = "", login_source = "", user_fstname;
    CallbackManager callbackManager;
    String fname, lname, id, gender, profile_picture, emailPattern, Email, Dob;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;

    int is_fromlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mContext = this;

        FacebookSdk.sdkInitialize(mContext);
        getVersionInfo();
        callbackManager = CallbackManager.Factory.create();

        SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences("BrightFuture", Context.MODE_PRIVATE);
        User_id = sp.getString("user_id", "");
        user_fstname = sp.getString("user_firstname", "");
        if (user_fstname.equals("null")) {
            Intent in = new Intent(mContext, ProfileActivity.class);
            startActivity(in);
            finish();
        } else if (!User_id.isEmpty()) {
            Intent in = new Intent(mContext, MainActivity.class);
            startActivity(in);
            finish();
        }


        tv_login = findViewById(R.id.tv_login);
        tv_newuser = findViewById(R.id.tv_newuser);
        tv_forpassword = findViewById(R.id.tv_forpass);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        iv_facebook = findViewById(R.id.iv_facebook);
        iv_google = findViewById(R.id.iv_google);

        tv_login.setOnClickListener(this);
        tv_newuser.setOnClickListener(this);
        tv_forpassword.setOnClickListener(this);
        iv_facebook.setOnClickListener(this);
        iv_google.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_login) {

            boolean digitsOnly = TextUtils.isDigitsOnly(et_username.getText().toString().trim());


            if (digitsOnly && et_username.getText().toString().length() != 10) {
                Utils.mCustomToast(mContext, "please enter valid mobile number..", 1);
            } else if (et_username.getText().toString().isEmpty()) {
                Utils.mCustomToast(mContext, "email id should not be empty..", 1);
            } else if (!digitsOnly && !isValidEmail(et_username.getText().toString())) {
                Utils.mCustomToast(mContext, "please enter valid email id..", 1);
            } else if (et_password.getText().toString().trim().isEmpty()) {
                Utils.mCustomToast(mContext, "please enter password..", 1);
            } else {

                dto = new RegisterDto();

                if (Utils.isNetworkConnected(mContext)) {

                    dto.setUsername(et_username.getText().toString().trim());
                    dto.setPassword(et_password.getText().toString().trim());

                    is_fromlogin = 0;

                    GCMregistration gcm = new GCMregistration();
                    gcm.setFrom_login(true);
                    gcm.initial(this, dto, this, is_fromlogin);

                } else {
                    Utils.mCustomToast(mContext, "Please check your Internet connection", 1);
                }


            }
//            Intent in = new Intent(this, MainActivity.class);
//            startActivity(in);
//            finish();
        } else if (v.getId() == R.id.tv_newuser) {
            Intent in = new Intent(this, RegisterActivity.class);
            startActivity(in);
//            finish();
        } else if (v.getId() == R.id.tv_forpass) {

            if (et_username.getText().toString().isEmpty()) {
                Utils.mCustomToast(mContext, "please enter email id..", 1);
            } else if (!isValidEmail(et_username.getText().toString())) {
                Utils.mCustomToast(mContext, "please enter valid email id..", 1);
            } else {
                mForgotPassword(et_username.getText().toString().trim());
            }

        } else if (v.getId() == R.id.iv_facebook) {
//            Intent in = new Intent(this, MainActivity.class);
//            startActivity(in);

            is_fromlogin = 3;


            login_source = "facebook";
            SharedPreferences.Editor editor11 = mContext.getSharedPreferences("BrightFuture", Context.MODE_PRIVATE).edit();
            editor11.putString("type", login_source);

            editor11.commit();
            LoginManager.getInstance().logInWithReadPermissions(
                    this,
                    Arrays.asList("user_friends", "email", "public_profile"));

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    getFacebookData(object);
//                                        LoginManager.getInstance().logOut();
                                }
                            });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                            request.setParameters(parameters);
                            request.executeAsync();
                        }

                        @Override
                        public void onCancel() {
                            LoginManager.getInstance().logOut();
//                                Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            if (exception instanceof FacebookAuthorizationException) {
                                if (AccessToken.getCurrentAccessToken() != null) {
                                    LoginManager.getInstance().logOut();
                                }
                            }
                        }
                    });


        } else if (v.getId() == R.id.iv_google) {

            is_fromlogin = 2;

            login_source = "google";
            SharedPreferences.Editor editor = mContext.getSharedPreferences("BrightFuture", Context.MODE_PRIVATE).edit();
            editor.putString("type", login_source);
            editor.commit();

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);


        }
    }

    private void getVersionInfo() {

        int versionCode = -1;
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    private Bundle getFacebookData(JSONObject object) {

        Bundle bundle = new Bundle();
        try {
            id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                fname = object.getString("first_name");
            if (object.has("last_name"))
                lname = object.getString("last_name");

            if (object.has("email")) {
                String email = object.getString("email");
                if (email != null && !email.isEmpty())
                    Email = email;
            }


            if (object.has("gender")) {
                String genderss = object.getString("gender");
                if (genderss != null && !genderss.isEmpty())
                    if (object.getString("gender").equals("male")) {
                        gender = "M";
                    } else if (object.getString("gender").equals("female")) {
                        gender = "F";
                    } else {
                        gender = "M";
                    }
            }
            if (object.has("birthday")) {
                String dob = object.getString("birthday");
                if (dob != null && !dob.isEmpty())
                    Dob = dob;
            }


            eMailValidation(Email);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        signOut();
    }

    private void eMailValidation(String email) {

        String data = "";
        try {
            data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&";


            if (Utils.isNetworkConnected(mContext)) {
                Asyntask asyntask = new Asyntask(mContext, new URL(Constants.base_url + Constants.emailvalidation_url));
                asyntask.execute(data);
            } else {
                Utils.mCustomToast(mContext, "Ckeck Your Internet Connectivity..", 1);
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void mForgotPassword(String email) {

        String data = "";
        try {
            data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");


            if (Utils.isNetworkConnected(mContext)) {
                Asyntask asyntask = new Asyntask(mContext, new URL(Constants.base_url + Constants.forgotpassword_url));
                asyntask.execute(data);
            } else {
                Utils.mCustomToast(mContext, "Ckeck Your Internet Connectivity..", 1);
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serverResponse(String response, String path) throws JSONException, Exception {
        if (path.contains(Constants.emailvalidation_url)) {
            JSONObject jo = new JSONObject(response);
            if (jo.has("status")) {
                if (jo.getBoolean("status")) {

                    SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences("BrightFuture",
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

                    Intent intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();

                } else if (!jo.getBoolean("status")) {
                    dto = new RegisterDto();

                    dto.setFirstname(fname);
                    dto.setLsatname(lname);
                    dto.setUsername(fname);
                    dto.setGender("");
                    dto.setEmail(Email);
                    dto.setDOB("");
                    dto.setMobileNumber("");
                    dto.setPassword("");


                    GCMregistration gcm = new GCMregistration();
                    gcm.setFrom_login(false);
                    gcm.initial(this, dto, this, is_fromlogin);
                }
            }
        } else if (path.equals(Constants.base_url + Constants.forgotpassword_url)) {
            JSONObject job = new JSONObject(response);
            if (job.getBoolean("status")) {
                Utils.mCustomToast(mContext, "New Password has been sent to " + et_username.getText().toString().trim(), 1);
            } else {
                Utils.mCustomToast(mContext, "Failed to send OTP.. Please try later..", 1);
            }
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            id = acct.getId();
            fname = acct.getGivenName();
            lname = acct.getFamilyName();
            Email = acct.getEmail();
            eMailValidation(Email);
        } else {
            signOut();
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
//                        updateUI(false);
                    }
                });
    }
}
