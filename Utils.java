package com.adhishta.syam.brightfuture.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adhishta.syam.brightfuture.AsyncTask.Asyntask;
import com.adhishta.syam.brightfuture.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by admin on 12/03/2018.
 */

public class Utils {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private static final int PERMISSION_REQUEST_CODE = 200;

    public static boolean checkPermissionnew(Context ctx) {
        int result = ContextCompat.checkSelfPermission(ctx, ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(ctx, CAMERA);
        int result2 = ContextCompat.checkSelfPermission(ctx, WRITE_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(ctx, READ_CONTACTS);
        int result4 = ContextCompat.checkSelfPermission(ctx, RECEIVE_SMS);
        int result5 = ContextCompat.checkSelfPermission(ctx, READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
                && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED && result5 == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{ACCESS_FINE_LOCATION, CAMERA, WRITE_EXTERNAL_STORAGE, READ_CONTACTS, RECEIVE_SMS, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    public static void mCustomToast(Context ctx, String text, int color) {

        Toast toast = new Toast(ctx);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 120);
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.customtoast, null);
        TextView txt = (TextView) view.findViewById(R.id.tv_customtoast);
        txt.setGravity(Gravity.CENTER);
        if (color == 1) {
            txt.setTextColor(ctx.getResources().getColor(R.color.white_color));
            txt.setBackgroundColor(ctx.getResources().getColor(R.color.darkgreen));
        } else if (color == 2) {
            txt.setTextColor(ctx.getResources().getColor(R.color.darkgreen));
            txt.setBackgroundColor(ctx.getResources().getColor(R.color.white_color));
        }
        txt.setText(text);
        toast.setView(view);
        toast.show();
    }

    public static boolean isNetworkConnected(Context ctx) {
        if (ctx == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) ctx.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    public static void universalImageUpload(String imagepath, ImageView imageview, Context ctx) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .resetViewBeforeLoading(true).showImageOnFail(R.drawable.no_image)
                .showImageOnLoading(R.drawable.no_image)
                .build();

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(ctx));
        // END - UNIVERSAL IMAGE LOADER SETUP

        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imagepath.equals("")) {
            imageLoader.displayImage(imagepath, imageview, options);
        } else {
            imageview.setImageDrawable(ctx.getResources().getDrawable(R.drawable.no_image));
        }

    }


    public static void mSendRequest(Context ctx, String user_id_one, String user_id_two,
                                    String sendrqe) throws JSONException {
        String data = "";
        try {

            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id_one, "UTF-8") + "&";
            data += URLEncoder.encode("friend_id", "UTF-8") + "=" + URLEncoder.encode(user_id_two, "UTF-8");


            if (sendrqe.equals(Constants.FRIEND_REQUEST)) {
                Asyntask async = new Asyntask(ctx, new URL(Constants.base_url + Constants.sendfrndreq_url));
//                async.setShow_progress(false);
                async.execute(data);
            } else if (sendrqe.equals(Constants.USER_PROFILE)) {
                Asyntask async = new Asyntask(ctx, new URL(Constants.base_url + Constants.frienddetails_url));
                async.setShow_progress(false);
                async.execute(data);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String mDateTime(String date1) {

        int day, month, year;

        String str, date, time, finalmonth;

        str = date1;

        date = str.substring(0, str.indexOf(' '));
        time = str.substring(str.indexOf(' ') + 1);

        String tim[] = time.split(":");

        int hours = Integer.parseInt(tim[0]);
        int minutes = Integer.parseInt(tim[1]);
        // int hours = Integer.parseInt(tim[0]);
        String hours1, minutes1;
        if (hours < 10) {
            hours1 = "0" + hours;
        } else {
            hours1 = "" + hours;
        }
        if (minutes < 10) {
            minutes1 = "0" + minutes;
        } else {
            minutes1 = "" + minutes;
        }

        if (hours < 12) {
            time = hours1 + ":" + minutes1;
        } else {
            time = hours1 + ":" + minutes1;
        }

        String str1[] = date.split("-");
        day = Integer.parseInt(str1[2]);
        month = Integer.parseInt(str1[1]);
        year = Integer.parseInt(str1[0]);

        finalmonth = getMonthName(month);

        return day + " " + finalmonth + " at " + time;

    }

    public static String getMonthName(int month) {
        switch (month) {
            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "June";

            case 7:
                return "July";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";
        }

        return "";
    }

//    @SuppressLint("StringFormatMatches")
//    public static String timeAgo(Context context, String timegiven) {
//        Date date = null;
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            date = format.parse(timegiven);
//            System.out.println(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        long originaltime = date.getTime();
//        long diff = new Date().getTime() - originaltime;
//
//        Resources r = context.getResources();
//
//        String prefix = r.getString(R.string.time_ago_prefix);
//        String suffix = r.getString(R.string.time_ago_prefix);
//
//        double seconds = Math.abs(diff) / 1000;
//        double minutes = seconds / 60;
//        double hours = minutes / 60;
//        double days = hours / 24;
//        double years = days / 365;
//
//        String words;
//
//        if (seconds < 45) {
//            words = r.getString(R.string.tabbed_main_detail_from, Math.round(seconds));
//        } else if (seconds < 90) {
//            words = r.getString(R.string.tabbed_main_detail_from, 1);
//        } else if (minutes < 45) {
//            words = r.getString(R.string.tabbed_main_detail_from, Math.round(minutes));
//        } else if (minutes < 90) {
//            words = r.getString(R.string.tabbed_main_detail_from, 1);
//        } else if (hours < 24) {
//            words = r.getString(R.string.tabbed_main_detail_from, Math.round(hours));
//        } else if (hours < 42) {
//            words = r.getString(R.string.tabbed_main_detail_from, 1);
//        } else if (days < 30) {
//            words = r.getString(R.string.tabbed_main_detail_from, Math.round(days));
//        } else if (days < 45) {
//            words = r.getString(R.string.tabbed_main_detail_from, 1);
//        } else if (days < 365) {
//            words = r.getString(R.string.tabbed_main_detail_from, Math.round(days / 30));
//        } else if (years < 1.5) {
//            words = r.getString(R.string.tabbed_main_detail_from, 1);
//        } else {
//            words = r.getString(R.string.tabbed_main_detail_from, Math.round(years));
//        }
//
//        StringBuilder sb = new StringBuilder();
//
//        if (prefix != null && prefix.length() > 0) {
//            sb.append(prefix).append(" ");
//        }
//
//        sb.append(words);
//
//        if (suffix != null && suffix.length() > 0) {
//            sb.append(" ").append(suffix);
//        }
//
//        return sb.toString().trim();
//    }


    public static String timeAgo(String time_ago) {


        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = format.parse(time_ago);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long given_time = date.getTime() / 1000;

        long cur_time = (Calendar.getInstance().getTimeInMillis()) / 1000;
        long time_elapsed = cur_time - given_time;
        long seconds = time_elapsed;
        int minutes = Math.round(time_elapsed / 60);
        int hours = Math.round(time_elapsed / 3600);
        int days = Math.round(time_elapsed / 86400);
        int weeks = Math.round(time_elapsed / 604800);
        int months = Math.round(time_elapsed / 2600640);
        int years = Math.round(time_elapsed / 31207680);

        // Seconds
        if (seconds <= 60) {
            return "just now";
        }
        //Minutes
        else if (minutes <= 60) {
            if (minutes == 1) {
                return "one minute ago";
            } else {
                return minutes + " minutes ago";
            }
        }
        //Hours
        else if (hours <= 24) {
            if (hours == 1) {
                return "an hour ago";
            } else {
                return hours + " hrs ago";
            }
        }
        //Days
        else if (days <= 7) {
            if (days == 1) {
                return "yesterday";
            } else {
                return days + " days ago";
            }
        }
        //Weeks
        else if (weeks <= 4.3) {
            if (weeks == 1) {
                return "a week ago";
            } else {
                return weeks + " weeks ago";
            }
        }
        //Months
        else if (months <= 12) {
            if (months == 1) {
                return "a month ago";
            } else {
                return months + " months ago";
            }
        }
        //Years
        else {
            if (years == 1) {
                return "one year ago";
            } else {
                return years + " years ago";
            }
        }
    }
}
