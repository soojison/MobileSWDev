package io.github.soojison.aitforum;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// pekler: I'm still a child
// moments later
// pekler: I’m too old for this
// girls and guys i don’t know how to say it

// Tools > Firebase > Email > follow steps
// on the website > Authentication > Enable email/password registration
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    // registration related methods
    @OnClick(R.id.btnRegister)
    public void registerClick() {
        if (!isFormValid()) {
            return;
        }
        showProgressDialog();
        firebaseAuth.createUserWithEmailAndPassword(
                etEmail.getText().toString(), etPassword.getText().toString()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // the task param is the result of the completed work: could be success or some error
                hideProgressDialog();
                if(task.isSuccessful()) {
                    // who has just been registered
                    FirebaseUser user = task.getResult().getUser();
                    user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(
                       userNameFromEmail(user.getEmail())).build());

                    Toast.makeText(LoginActivity.this,
                            "Registration successful", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(LoginActivity.this,
                            "Not successful: " + task.getException().getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // i.e. network is down
                hideProgressDialog();
                Toast.makeText(LoginActivity.this,
                        "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                // localized message translates if the phone is in a different language
            }
        });
    }

    // pekler: I like this laser pointer...
    // chainsaw is the lightsaber of the countryside people,
    // laser pointer is the lightsaber of the teachers

    // helper method: progress dialog
    private void showProgressDialog() {
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Wait for it...");
        }
        progressDialog.show();
    }

    // code is good if you can read it and understand what it is doing like a sentence
    private void hideProgressDialog() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    // helper method: edit text validation
    private boolean isFormValid() {
        if(TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError("Should not be empty");
            return false;
        }
        if(TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Should not be empty");
            return false;
        }

        return true;
    }

    // helper method: get username from email
    private String userNameFromEmail(String email) {
        // pekler: uuh what do we do here? who knows...
        if(email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}
