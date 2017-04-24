package io.github.soojison.aitforum;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.soojison.aitforum.data.Post;

public class CreatePostActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_TAKE_PHOTO = 101;
    @BindView(R.id.etTitle)
    EditText etTitle;

    @BindView(R.id.etBody)
    EditText etBody;

    @BindView(R.id.imgAttach)
    ImageView imgAttach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnAttach)
    public void attachClick() {
        Intent intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentTakePhoto, REQUEST_CODE_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_TAKE_PHOTO) {
            Bitmap img = (Bitmap) data.getExtras().get("data");

            imgAttach.setImageBitmap(img);
            imgAttach.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btnSend)
    public void sendClick() {
        if(imgAttach.getVisibility() == View.GONE) {
            uploadPost();
        } else {
            try {
                uploadPostWithImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // optional parameter (...)
    private void uploadPost(String... imageURL) {
        String key = FirebaseDatabase.getInstance().getReference().
                child("posts").push().getKey();
        Post newPost = new Post(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                etTitle.getText().toString(),
                etBody.getText().toString()
        );

        if(imageURL != null && imageURL.length > 0) {
            newPost.setImgURL(imageURL[0]);
        }

        // navigating inside the tree
        FirebaseDatabase.getInstance().getReference().
                child("posts").child(key).setValue(newPost);

        // go back to the list of posts
        finish();
    }

    // Tools > firebase > storage for image access
    public void uploadPostWithImage() throws Exception {
        imgAttach.setDrawingCacheEnabled(true);
        imgAttach.buildDrawingCache();
        Bitmap bitmap = imgAttach.getDrawingCache(); // get bitmap from image view
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // create JPEG from bitmap
        byte[] imageInBytes = baos.toByteArray(); // byte array = image itself

        // connect to firebase storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        // filename = random string.jpg
        String newImage = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8")+".jpg";
        // the file will be added to storage
        StorageReference newImageRef = storageRef.child(newImage);
        // under the images folder
        StorageReference newImageImagesRef = storageRef.child("images/"+newImage);
        // path where the image view will be uploaded
        newImageRef.getName().equals(newImageImagesRef.getName());    // true
        newImageRef.getPath().equals(newImageImagesRef.getPath());    // false

        // upload task which will upload the image
        UploadTask uploadTask = newImageImagesRef.putBytes(imageInBytes);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(CreatePostActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                uploadPost(taskSnapshot.getDownloadUrl().toString());
            }
        });
    }
}
