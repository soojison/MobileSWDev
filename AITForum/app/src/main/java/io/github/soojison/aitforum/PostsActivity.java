package io.github.soojison.aitforum;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.github.soojison.aitforum.adapter.PostsAdapter;
import io.github.soojison.aitforum.data.Post;

public class PostsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String KEY_POST = "posts";

    // whole activity is not a linear layout, but a navigation drawer layout
    // the activity has two parts
    // * navigation view
    //     > header
    //     > menu
    // * content behind the main screen
    //     > toolbar with action button

    private PostsAdapter postsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostsActivity.this, CreatePostActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // set email address of user on the header (same way you can change the name and profile pic)
        TextView tvEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView);
        // Firebase is a singleton thingie
        tvEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        // connect recyclerview with the adapter
        postsAdapter = new PostsAdapter(getApplicationContext(),
                FirebaseAuth.getInstance().getCurrentUser().getUid());
        RecyclerView recyclerViewPosts = (RecyclerView) findViewById(R.id.recyclerViewPosts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewPosts.setLayoutManager(layoutManager);
        recyclerViewPosts.setAdapter(postsAdapter);

        // let's get some data
        initPostsListener();
    }

    // how to query data from databse?
    // you can't get all posts like relational databse, need to create onchild listener on the leaf
    private void initPostsListener() {
        // hooked to the posts part of the tree db
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference(KEY_POST);
        // when new item added, the event listener will have callback method called childAdded
        // also when app started the first time, it will call on the method on all the existing items
        // so will be called 10 times if there are 10 posts already in the db
        // this is why it's a real-time database
        postsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // datasnapshot holds the posts object that sbd uploaded
                // will automatically map the tree into java post object
                Post newPost = dataSnapshot.getValue(Post.class);
                // new post doesn't have the key yet because key is the parent object of the post
                // travels separately
                postsAdapter.addPost(newPost, dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        // catch backbutton pressed event
        // default = finishes the activity, but here you override to handle drawer status
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // calls finish method and kills activity
            // but we gotta sign out the user
            FirebaseAuth.getInstance().signOut();
            super.onBackPressed();
        }
    }


    // for the toolbar -- you can delete for this application
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.posts, menu);
        return true;
    }

    // for the toolbar -- you can delete for this application
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            // Handle the logout action
            FirebaseAuth.getInstance().signOut();
            finish(); // remove from backstack, go back to login activity
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
