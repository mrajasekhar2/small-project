package com.adhishta.syam.brightfuture.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adhishta.syam.brightfuture.Activity.PostActivity;
import com.adhishta.syam.brightfuture.AsyncTask.Asyntask;
import com.adhishta.syam.brightfuture.AsyncTask.ResponseListner;
import com.adhishta.syam.brightfuture.Dto.PostDetailsdto;
import com.adhishta.syam.brightfuture.R;
import com.adhishta.syam.brightfuture.Utils.Constants;
import com.adhishta.syam.brightfuture.Utils.Utils;
import com.adhishta.syam.brightfuture.adapter.AllPostsDetailsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 03/03/2018.
 */

public class WallFragment extends Fragment implements ResponseListner {

    Context ctx;
    RecyclerView rec_puzzile;
    AllPostsDetailsAdapter sampleAdapter;
    TextView tv_postcontent;
    WallFragment wallfragment;
    List<PostDetailsdto> list_allposts;
    String user_id;
    boolean is_firstTime = true;
    LinearLayout ll_post;

    public WallFragment() {
        wallfragment = this;
    }


//    public static WallFragment newInstance(String s, String s1) {
//        WallFragment fragment = new WallFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.homefragment, container, false);
        ctx = this.getActivity();
//        MainActivity.toolBarTitle("Home", ctx);

        SharedPreferences sp = ctx.getApplicationContext().getSharedPreferences("BrightFuture", Context.MODE_PRIVATE);
        user_id = sp.getString("user_id", "");


        tv_postcontent = rootView.findViewById(R.id.tv_postcontent);
        ll_post = rootView.findViewById(R.id.ll_post);


        rec_puzzile = rootView.findViewById(R.id.rec_puzzile);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        rec_puzzile.setLayoutManager(mLayoutManager);

        tv_postcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ctx, PostActivity.class);
                ctx.startActivity(in);
            }
        });

        ll_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ctx, PostActivity.class);
                ctx.startActivity(in);
            }
        });

        if (is_firstTime) {
            mAllPostsService();
        }

        return rootView;

    }

    private void mAllPostsService() {
        String data = "";
        try {
            data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            Asyntask asyn = new Asyntask(ctx, new URL(Constants.base_url + Constants.allposts_url), wallfragment);
            asyn.execute(data);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serverResponse(String response, String path) throws JSONException, Exception {
        if (path.contains(Constants.allposts_url)) {
            JSONObject job = new JSONObject(response);
            if (job.has("status")) {
                if (job.getBoolean("status")) {
                    JSONArray jarr = job.getJSONArray("post_details");
                    list_allposts = new ArrayList<>();
                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject jo = jarr.getJSONObject(i);
                        PostDetailsdto dto = new PostDetailsdto();
                        dto.setPostid(jo.getString("postid"));
                        dto.setUserid(jo.getString("userid"));
                        dto.setPost_title(jo.getString("post_title"));
                        dto.setLikecount(jo.getString("likecount"));
                        dto.setPostdate(jo.getString("postdate"));
                        dto.setPost_image(jo.getString("post_image"));
                        dto.setFirst_name(jo.getString("first_name"));
                        dto.setLast_name(jo.getString("last_name"));
                        dto.setEmail(jo.getString("email"));
                        dto.setCommentcount(jo.getString("commentcount"));
                        dto.setProfile_img(jo.getString("profile_img"));
                        list_allposts.add(dto);
                    }
                    if (is_firstTime) {
                        is_firstTime = false;
                        sampleAdapter = new AllPostsDetailsAdapter(ctx, list_allposts, wallfragment);
                        rec_puzzile.setAdapter(sampleAdapter);
                    } else {
                        sampleAdapter.setData(list_allposts);
                        sampleAdapter.notifyDataSetChanged();
                    }
                } else {
                    Utils.mCustomToast(ctx, job.getString("msg"), 1);
                }
            } else {
                Utils.mCustomToast(ctx, "Please try again later..", 1);
            }
        } else if (path.contains(Constants.like_url)) {
            JSONObject job = new JSONObject(response);
            if (job.has("status")) {
                if (job.getBoolean("status")) {
                    mAllPostsService();
                } else {
                    Utils.mCustomToast(ctx, job.getString("msg"), 1);
                }
            } else {
                Utils.mCustomToast(ctx, "Please try again later..", 1);
            }
        } else if (path.equals(Constants.base_url + Constants.sharepost_url)) {
            JSONObject job = new JSONObject(response);
            if (job.has("status")) {
                if (job.getBoolean("status")) {
                    Utils.mCustomToast(ctx, job.getString("msg"), 1);
                    mAllPostsService();
                } else {
                    Utils.mCustomToast(ctx, job.getString("msg"), 1);
                }
            } else {
                Utils.mCustomToast(ctx, "Please try again later..", 1);
            }
        } else if (path.equals(Constants.base_url + Constants.deletepost_url)) {
            JSONObject job = new JSONObject(response);
            if (job.has("status")) {
                if (job.getBoolean("status")) {
                    Utils.mCustomToast(ctx, job.getString("msg"), 1);
                    mAllPostsService();
                } else {
                    Utils.mCustomToast(ctx, job.getString("msg"), 1);
                }
            } else {
                Utils.mCustomToast(ctx, "Please try again later..", 1);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!is_firstTime) {
            mAllPostsService();
        }
    }
}
