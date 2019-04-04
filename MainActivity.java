package com.adhishta.syam.brightfuture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adhishta.syam.brightfuture.Activity.ComplaintActivity;
import com.adhishta.syam.brightfuture.Activity.CreateAlbum;
import com.adhishta.syam.brightfuture.Activity.EventsActivity;
import com.adhishta.syam.brightfuture.Activity.FriendsActivity;
import com.adhishta.syam.brightfuture.Activity.GroupsActivity;
import com.adhishta.syam.brightfuture.Activity.MessagesActivity;
import com.adhishta.syam.brightfuture.Activity.NotificationActivity;
import com.adhishta.syam.brightfuture.Activity.PostActivity;
import com.adhishta.syam.brightfuture.Activity.ProfileActivity;
import com.adhishta.syam.brightfuture.Activity.SendMessages;
import com.adhishta.syam.brightfuture.Activity.SplashActivity;
import com.adhishta.syam.brightfuture.Activity.ViewAlbums;
import com.adhishta.syam.brightfuture.Fragment.CreateEventFragment;
import com.adhishta.syam.brightfuture.Fragment.FriendsListFragment;
import com.adhishta.syam.brightfuture.Fragment.HomeFragment;
import com.adhishta.syam.brightfuture.Fragment.ViewEventFragment;
import com.adhishta.syam.brightfuture.Fragment.WallFragment;
import com.adhishta.syam.brightfuture.Utils.Utils;

public class MainActivity extends AppCompatActivity
        implements  View.OnClickListener {

    private Fragment fragment;
    Context ctx;
    TabLayout paymentsTab;
    ViewPager paymentsViewpager;
    TextView tv_postcontent;
    public static TextView tv_profilename, tv_profileemail;
    String user_id, user_name, user_email, profileImage;
    public static de.hdodenhof.circleimageview.CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fragment = new WallFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack("1").commit();

//        if (!Utils.checkPermissionnew(ctx)) {
//            Utils.requestPermission(MainActivity.this);
//        }


        SharedPreferences sp = ctx.getApplicationContext().getSharedPreferences("BrightFuture", Context.MODE_PRIVATE);
        user_id = sp.getString("user_id", "");
        user_name = sp.getString("user_firstname", "");
        user_email = sp.getString("user_email", "");
        profileImage = sp.getString("ProfileImage", "");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        tv_profilename = navigationView.findViewById(R.id.tv_profilename);
        tv_profileemail = navigationView.findViewById(R.id.tv_profileemail);

        imageView = navigationView.findViewById(R.id.imageView);

        tv_profilename.setText(user_name);
        tv_profileemail.setText(user_email);
        if (profileImage != null) {
            Utils.universalImageUpload("http://demo.adhishta.com/projects/brightfuture/services/uploads/" + profileImage, imageView, ctx);
        }


        LinearLayout ll_profilelayout = navigationView.findViewById(R.id.ll_profilelayout);
        LinearLayout ll_friends = navigationView.findViewById(R.id.ll_friends);
        LinearLayout ll_events = navigationView.findViewById(R.id.ll_events);
        LinearLayout ll_groups = navigationView.findViewById(R.id.ll_groups);
        LinearLayout ll_albums = navigationView.findViewById(R.id.ll_albums);
        LinearLayout ll_messages = navigationView.findViewById(R.id.ll_messages);
        LinearLayout ll_notifications = navigationView.findViewById(R.id.ll_notifications);
        LinearLayout ll_complains = navigationView.findViewById(R.id.ll_complains);
        LinearLayout ll_logout = navigationView.findViewById(R.id.ll_logout);


        ll_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ctx, FriendsActivity.class);
                startActivity(in);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        ll_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ctx, EventsActivity.class);
                startActivity(in);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        ll_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ctx, GroupsActivity.class);
                startActivity(in);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        ll_albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ctx, ViewAlbums.class);
                startActivity(in);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        ll_messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ctx, MessagesActivity.class);
                startActivity(in);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        ll_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ctx, NotificationActivity.class);
                startActivity(in);
                finish();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        ll_complains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ctx, ComplaintActivity.class);
                startActivity(in);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent in = new Intent(ctx, ComplaintActivity.class);
//                startActivity(in);

                SharedPreferences sp = ctx.getApplicationContext().getSharedPreferences("BrightFuture",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("user_id", "");
//                editor.putString("user_firstname", job.getJSONObject("user_details").getString("user_firstname"));
//                editor.putString("user_lastname", job.getJSONObject("user_details").getString("user_lastname"));
//                editor.putString("user_email", job.getJSONObject("user_details").getString("user_email"));
//                editor.putString("user_mobile", job.getJSONObject("user_details").getString("user_mobile"));
                editor.commit();

                Intent in = new Intent(ctx, SplashActivity.class);
                startActivity(in);
                finish();

            }
        });

        ll_profilelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ctx, ProfileActivity.class);
                startActivity(in);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

//        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int cnt = getSupportFragmentManager().getBackStackEntryCount();
            if (cnt <= 1) {
                getSupportFragmentManager().popBackStack();
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            fragment = new HomeFragment();
//        } else if (id == R.id.nav_gallery) {
//            fragment = new ViewEventFragment();
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        }
//
//        if (fragment != null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, fragment);
//            ft.addToBackStack("1").commit();
//        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }


    public static void toolBarTitle(String home, Context ctx) {
        ((MainActivity) ctx).getSupportActionBar().setTitle(Html.fromHtml("<small>" + home + "</small>"));
    }

    @Override
    public void onClick(View view) {

    }
}
