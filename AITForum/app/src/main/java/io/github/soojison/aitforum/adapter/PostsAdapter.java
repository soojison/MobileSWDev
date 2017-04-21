package io.github.soojison.aitforum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.github.soojison.aitforum.PostsActivity;
import io.github.soojison.aitforum.R;
import io.github.soojison.aitforum.data.Post;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

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
        Post tempPost = postList.get(position);
        holder.tvAuthor.setText(tempPost.getAuthor());
        holder.tvTitle.setText(tempPost.getTitle());
        holder.tvBody.setText(tempPost.getBody());

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

        public ViewHolder(View itemView) {
            super(itemView);
            // gotta import R package because we're not in the root package
            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
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
