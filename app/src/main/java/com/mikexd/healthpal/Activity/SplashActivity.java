package com.mikexd.healthpal.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikexd.healthpal.R;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            updateUI(i);
        }else{
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            updateUI(i);
        }
    }

    public void updateUI(Intent i) {
        startActivity(i);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

//    public void loginWithFacebook(){
////        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
//    }
//
//    public void loginWithEmail(String email, String password){
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        // If sign in fails, display a message to the user. If sign in succeeds
//                        // the auth state listener will be notified and logic to handle the
//                        // signed in user can be handled in the listener.
//                        if (!task.isSuccessful()) {
//                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }else{
//                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                            if (user != null) {
//                                // Name, email address, and profile photo Url
//                                // String name = user.getDisplayName();
//
//                                String email = user.getEmail();
//                                Uri photoUrl = user.getPhotoUrl();
//
//                                // The user's ID, unique to the Firebase project. Do NOT use this value to
//                                // authenticate with your backend server, if you have one. Use
//                                // FirebaseUser.getToken() instead.
//                                String uid = user.getUid();
//
//                                Intent i = new Intent(SplashActivity.this, MainActivity.class);
//                                i.putExtra("email", email);
//                                i.putExtra("photoUrl", photoUrl);
//                                i.putExtra("uid", uid);
//                                startActivity(i);
//                                finish();
//                            }
//                        }
//                    }
//                });
//    }
}
