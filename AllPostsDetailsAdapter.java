package com.adhishta.syam.brightfuture.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adhishta.syam.brightfuture.Activity.CommentActivity;
import com.adhishta.syam.brightfuture.Activity.ImageViewActivity;
import com.adhishta.syam.brightfuture.Activity.OthersProfileActivity;
import com.adhishta.syam.brightfuture.AsyncTask.Asyntask;
import com.adhishta.syam.brightfuture.AsyncTask.ResponseListner;
import com.adhishta.syam.brightfuture.Dto.PostDetailsdto;
import com.adhishta.syam.brightfuture.Fragment.WallFragment;
import com.adhishta.syam.brightfuture.R;
import com.adhishta.syam.brightfuture.Utils.Constants;
import com.adhishta.syam.brightfuture.Utils.Utils;

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
 * Created by Home on 11/6/2017.
 */

public class AllPostsDetailsAdapter extends RecyclerView.Adapter<AllPostsDetailsAdapter.ViewHolder> implements ResponseListner {

    Context context;
    List<PostDetailsdto> list_allposts;
    WallFragment wallfragment;
    Activity wallactivity;
    String user_id;

    public void setData(List<PostDetailsdto> list_allposts) {
        this.list_allposts = list_allposts;
    }


    public AllPostsDetailsAdapter(Context context, List<PostDetailsdto> list_allposts, WallFragment wallfragment) {
        super();
        this.context = context;
        this.list_allposts = list_allposts;
        this.wallfragment = wallfragment;
    }

    public AllPostsDetailsAdapter(Context context, List<PostDetailsdto> list_allposts, Activity wallactivity) {
        super();
        this.context = context;
        this.list_allposts = list_allposts;
        this.wallactivity = wallactivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.sample_item, viewGroup, false);

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("BrightFuture", Context.MODE_PRIVATE);
        user_id = sp.getString("user_id", "");


        ViewHolder viewHolder = new ViewHolder(v, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {


        viewHolder.tv_username.setText(list_allposts.get(i).getFirst_name());
        viewHolder.tv_postdate.setText(Utils.mDateTime(list_allposts.get(i).getPostdate()));
        viewHolder.tv_postdesc.setText(list_allposts.get(i).getPost_title());
        Utils.universalImageUpload("http://demo.adhishta.com/projects/brightfuture/services/uploads/" + list_allposts.get(i).getPost_image(), viewHolder.iv_postimage, context);

        viewHolder.tv_like.setText("Likes " + list_allposts.get(i).getLikecount());
        viewHolder.tv_comment.setText("Comment " + list_allposts.get(i).getCommentcount());

        Utils.universalImageUpload("http://demo.adhishta.com/projects/brightfuture/services/uploads/" + list_allposts.get(i).getProfile_img(), viewHolder.profile_image, context);


        if (user_id.equals(list_allposts.get(i).getUserid())) {
            viewHolder.iv_deletepost.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_deletepost.setVisibility(View.GONE);
        }


        final int pos = i;


        viewHolder.tv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLikeServicecall(pos);
            }
        });

        viewHolder.tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, CommentActivity.class);
                in.putExtra("post_id", list_allposts.get(pos).getPostid());
                context.startActivity(in);

            }
        });

        viewHolder.tv_postshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShareServicecall(pos);
            }
        });

        viewHolder.iv_deletepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeletePostService(pos);
            }
        });

        viewHolder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user_id.equals(list_allposts.get(pos).getUserid())) {
                    Intent in = new Intent(context, OthersProfileActivity.class);
                    in.putExtra("userid", list_allposts.get(pos).getUserid());
                    in.putExtra("username", list_allposts.get(pos).getFirst_name());
                    context.startActivity(in);
                }

            }
        });

        viewHolder.tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user_id.equals(list_allposts.get(pos).getUserid())) {
                    Intent in = new Intent(context, OthersProfileActivity.class);
                    in.putExtra("userid", list_allposts.get(pos).getUserid());
                    in.putExtra("username", list_allposts.get(pos).getFirst_name());
                    context.startActivity(in);
                }
            }
        });

        viewHolder.iv_postimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, ImageViewActivity.class);
                in.putExtra("post_id", list_allposts.get(pos).getPostid());
                in.putExtra("post_image", list_allposts.get(pos).getPost_image());
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_allposts.size();
    }

    @Override
    public void serverResponse(String response, String path) throws JSONException, Exception {
        if (path.contains(Constants.like_url)) {
            JSONObject job = new JSONObject(response);
            if (job.has("status")) {
                if (job.getBoolean("status")) {
                    String data = "";
                    try {
                        data = URLEncoder.encode("userid", "UTF-8") + "" + URLEncoder.encode(user_id, "UTF-8");
                        Asyntask asyn = new Asyntask(context, new URL(Constants.base_url + Constants.allposts_url), AllPostsDetailsAdapter.this);
                        asyn.execute(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.mCustomToast(context, "Already liked the post..", 1);
                }
            } else {
                Utils.mCustomToast(context, "Please try again later..", 1);
            }
        } else if (path.contains(Constants.allposts_url)) {
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
                    notifyDataSetChanged();
                } else {
                    Utils.mCustomToast(context, job.getString("msg"), 1);
                }
            } else {
                Utils.mCustomToast(context, job.getString("msg"), 1);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        de.hdodenhof.circleimageview.CircleImageView profile_image;
        TextView tv_username, tv_postdate, tv_postdesc, tv_like, tv_comment, tv_postshare;
        ImageView iv_postimage, iv_deletepost;
        Context ctx;

        public ViewHolder(View itemView, Context ctx) {
            super(itemView);
            this.ctx = ctx;

            profile_image = itemView.findViewById(R.id.profile_image);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_postdate = itemView.findViewById(R.id.tv_postdate);
            tv_postdesc = itemView.findViewById(R.id.tv_postdesc);
            iv_postimage = itemView.findViewById(R.id.iv_postimage);
            tv_like = itemView.findViewById(R.id.tv_like);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            tv_postshare = itemView.findViewById(R.id.tv_postshare);
            iv_deletepost = itemView.findViewById(R.id.iv_deletepost);


        }
    }

    private void mLikeServicecall(int position) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("BrightFuture", Context.MODE_PRIVATE);
        String User_id = sp.getString("user_id", "");
        String data = "";
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(User_id, "UTF-8") + "&";
            data += URLEncoder.encode("PostID", "UTF-8") + "=" + URLEncoder.encode(list_allposts.get(position).getPostid(), "UTF-8");
            if (Utils.isNetworkConnected(context)) {
                Asyntask asyntask = new Asyntask(context, new URL(Constants.base_url + Constants.like_url), AllPostsDetailsAdapter.this);
                asyntask.execute(data);
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


    private void mShareServicecall(int position) {

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("BrightFuture", Context.MODE_PRIVATE);
        String User_id = sp.getString("user_id", "");

        String data = "";
        try {
            data = URLEncoder.encode("shared_userid", "UTF-8") + "=" + URLEncoder.encode(User_id, "UTF-8") + "&";
            data += URLEncoder.encode("post_id", "UTF-8") + "=" + URLEncoder.encode(list_allposts.get(position).getPostid(), "UTF-8") + "&";
            data += URLEncoder.encode("post_userid", "UTF-8") + "=" + URLEncoder.encode(list_allposts.get(position).getUserid(), "UTF-8");


            if (Utils.isNetworkConnected(context)) {
                Asyntask asyntask = new Asyntask(context, new URL(Constants.base_url + Constants.sharepost_url), wallfragment);
                asyntask.execute(data);
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

    private void mDeletePostService(int position) {

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("BrightFuture", Context.MODE_PRIVATE);
        String User_id = sp.getString("user_id", "");

        String data = "";
        try {
            data = URLEncoder.encode("post_id", "UTF-8") + "=" + URLEncoder.encode(list_allposts.get(position).getPostid(), "UTF-8");


            if (Utils.isNetworkConnected(context)) {
                Asyntask asyntask = new Asyntask(context, new URL(Constants.base_url + Constants.deletepost_url), wallfragment);
                asyntask.execute(data);
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

}
