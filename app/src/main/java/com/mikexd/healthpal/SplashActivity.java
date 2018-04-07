package com.mikexd.healthpal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.mikexd.healthpal.SignUpActivity.MY_SHAREDPREF_NAME1;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        SharedPreferences prefs = getSharedPreferences(MY_SHAREDPREF_NAME1,MODE_PRIVATE);
        final String name = prefs.getString("name", null);
        String email = prefs.getString("email", null);
        String password = prefs.getString("password", null);

        if(password!=null || email != null){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    // Name, email address, and profile photo Url
                                    // String name = user.getDisplayName();

                                    String email = user.getEmail();
                                    Uri photoUrl = user.getPhotoUrl();

                                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                                    // authenticate with your backend server, if you have one. Use
                                    // FirebaseUser.getToken() instead.
                                    String uid = user.getUid();

                                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                                    i.putExtra("name",name);
                                    i.putExtra("email", email);
                                    i.putExtra("photoUrl", photoUrl);
                                    i.putExtra("uid", uid);
                                    startActivity(i);
                                    finish();
                                }


                            }

                            // ...
                        }
                    });

        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
