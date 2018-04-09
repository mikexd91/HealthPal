package com.mikexd.healthpal.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikexd.healthpal.Utilities.ViewPagerAdapter;
import com.mikexd.healthpal.Fragment.ChatFragment;
import com.mikexd.healthpal.Fragment.MapFragment;
import com.mikexd.healthpal.Fragment.ProfileFragment;
import com.mikexd.healthpal.R;

import static com.mikexd.healthpal.Activity.SignUpActivity.MY_SHAREDPREF_NAME1;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private String name;

    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    MenuItem prevMenuItem;
    ChatFragment fragmentChat;
    MapFragment fragmentMap;
    ProfileFragment fragmentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        // this listener will be called when there is change in firebase user session
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }else{
                    // Write a message to the database
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference().child("users").child(user.getUid()).child("name");
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if(nameView.getText().toString().isEmpty()){
//                                Toast.makeText(getApplicationContext(),"email "+ dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                            name = dataSnapshot.getValue().toString();
                            SharedPreferences.Editor editor = getSharedPreferences(MY_SHAREDPREF_NAME1,MODE_PRIVATE).edit();
                            editor.putString("name",dataSnapshot.getValue().toString());
                            editor.commit();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //Initializing the bottomNavigationView
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_chat:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_map:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_profile:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentChat = new ChatFragment();
        fragmentMap = new MapFragment();
        fragmentProfile = new ProfileFragment();
        viewPagerAdapter.addFragment(fragmentChat);
        viewPagerAdapter.addFragment(fragmentMap);
        viewPagerAdapter.addFragment(fragmentProfile);
        viewPager.setAdapter(viewPagerAdapter);
    }

}
