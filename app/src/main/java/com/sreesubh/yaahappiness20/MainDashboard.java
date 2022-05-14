package com.sreesubh.yaahappiness20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    GoogleSignInClient googleSignInClient;
    FirebaseAuth auth;
    CircleImageView gimg;
    TextView name,user_age;
    DrawerLayout layout;
    NavigationView navigationView;
    Toolbar toolbar;
    User user ;
    String dob;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        layout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        View hview = navigationView.getHeaderView(0);
        gimg = (CircleImageView) hview.findViewById(R.id.gimg);
        name = (TextView) hview.findViewById(R.id.gname);
        user_age = hview.findViewById(R.id.user_age);
        FirebaseUser firebaseUser =auth.getCurrentUser();
        FirebaseDatabase db= FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference("User");
        SharedPreferences preferences = getSharedPreferences("id",MODE_PRIVATE);
        Log.d("LOGD",preferences.getString("_id","null"));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_view,new HomePageFragement());
        transaction.commit();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    user = dataSnapshot.getValue(User.class);
                    if (user.getUserUuid().equals(firebaseUser.getEmail()))
                    {
                        dob = user.getDob();
                        break;
                    }
                }
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Log.d("Provider",firebaseUser.getEmail());
                try {
                    Date d2 = dateFormat.parse(dateFormat.format(date));
                    Date d1 = dateFormat.parse(dob);
                    int y = (d2.getYear()-d1.getYear());
                    user_age.setText(y+" year old");

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainDashboard.this, layout, toolbar, R.string.Open, R.string.Close);
        layout.addDrawerListener(toggle);
        toggle.syncState();
        googleSignInClient = GoogleSignIn.getClient(MainDashboard.this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        if (firebaseUser != null) {
            name.setText(firebaseUser.getDisplayName());
            Glide.with(MainDashboard.this)
                    .load(firebaseUser.getPhotoUrl())
                    .into(gimg);
        }
        navigationView.setCheckedItem(R.id.home_nav);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout layout = findViewById(R.id.drawerLayout);

        if(layout.isDrawerOpen(GravityCompat.START))
        {
            layout.closeDrawer(GravityCompat.START);
        }else {

                super.onBackPressed();

        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.home_nav:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_view,new HomePageFragement());
                transaction.commit();
                break;
            case R.id.report_nav:
                FragmentTransaction transact = getSupportFragmentManager().beginTransaction();
                transact.replace(R.id.content_view,new Reports());
                transact.commit();
                break;
            case R.id.Logout:
                SharedPreferences preferences = getSharedPreferences("id",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            auth.signOut();
                            Toast.makeText(MainDashboard.this, "Successfully SignOut", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainDashboard.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }
                    }
                });
                break;
        }
        return true;
    }
}