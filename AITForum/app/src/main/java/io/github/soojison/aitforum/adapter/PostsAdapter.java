package io.github.soojison.aitforum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.github.soojison.aitforum.PostsActivity;
import io.github.soojison.aitforum.R;
import io.github.soojison.aitforum.data.Post;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    // adding push notification
    // tools > Firebase > notifications => that's it!
    // not a real time service -> not guaranteed that it will arrive immediately

    private Context context;
    private List<Post> postList;
    private List<String> postKeys;
    private String uId;
    private int lastPosition = -1;
    // we need the firebase posts "branch", so we need to import the firebase db into project
    // Tools > Firebase > Realtime Database > add the db to app
    private DatabaseReference postsRef;

    public PostsAdapter(Context context, String uId) {
        this.context = context;
        this.uId = uId; // need to delete posts etc.
        this.postList = new ArrayList<Post>();
        this.postKeys = new ArrayList<String>();

        // hooking to the posts branch of the db tree
        postsRef = FirebaseDatabase.getInstance().getReference(PostsActivity.KEY_POST);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // just inflating the view and putting into view holder
        // standard recyclerView stuff...
        View postView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_post,
                parent,
                false
        );
        return new ViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // assign values to the views
        final Post tempPost = postList.get(position);
        holder.tvAuthor.setText(tempPost.getAuthor());
        holder.tvTitle.setText(tempPost.getTitle());
        holder.tvBody.setText(tempPost.getBody());

        if(!TextUtils.isEmpty(tempPost.getImgURL())) {
            holder.ivPost.setVisibility(View.VISIBLE);
            Glide.with(context).load(tempPost.getImgURL()).into(holder.ivPost);
        } else {
            // since recyclerview is reusing the viewholders, you gotta reset it
            holder.ivPost.setVisibility(View.GONE);
        }

        // if current user id = post uid display delete button
        if(tempPost.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "HELLOO", Toast.LENGTH_SHORT).show();
                    //removePostByKey(tempPost.getUid());
                }
            });
        }


        // play animation when new item is added! this is the spot to call it
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    // add and delete methods
    public void addPost(Post place, String key) {
        postList.add(place);
        postKeys.add(key);
        notifyDataSetChanged();
    }

    public void removePost(int index) {
        // have to find the child that should be removed by the key
        postsRef.child(postKeys.get(index)).removeValue();
        postList.remove(index);
        postKeys.remove(index);
        notifyItemRemoved(index);
    }

    public void removePostByKey(String key) {
        int index = postKeys.indexOf(key);
        if (index != -1) {
            postList.remove(index);
            postKeys.remove(index);
            notifyItemRemoved(index);
        }
    }

    // student: why did you make it public?
    // pekler: (nervous laughter) why not....?
    // public because onBindViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvAuthor;
        public TextView tvTitle;
        public TextView tvBody;
        public ImageView ivPost;
        public Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            // gotta import R package because we're not in the root package
            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            ivPost = (ImageView) itemView.findViewById(R.id.ivPost);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }

    // built in animation, not creating new animation file
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
