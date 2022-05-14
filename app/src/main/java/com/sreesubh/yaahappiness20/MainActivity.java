package com.sreesubh.yaahappiness20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SignInButton signInButton;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    int RC_SIGN_IN = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInButton = findViewById(R.id.gsignin);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren())
                {
                    Log.d("Value", String.valueOf(snap.getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("950769705211-4i4flfoorphqqcjd5l754cjo27683i30.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser f= firebaseAuth.getCurrentUser();

        SharedPreferences preferences = getSharedPreferences("id",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String dat = preferences.getString("_id","null");
        Log.d("LOGD123",dat);
        if(dat.equals("null"))
        {
        if(f!=null)
        {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
                Task<DataSnapshot> dataSnapshotTask = ref.get();
                dataSnapshotTask.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        String srt = "";
                        DataSnapshot snapshot = task.getResult();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Log.d("LOGD", data.getKey());
                            User user = data.getValue(User.class);
                            if (f.getEmail().equals(user.getUserUuid())) {
                                srt = data.getKey();
                                break;
                            }
                        }
                        editor.putString("_id", srt);
                        editor.apply();
                        Log.d("data", srt);
                        startActivity(new Intent(MainActivity.this, MainDashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

        }
        else {
            startActivity(new Intent(MainActivity.this, MainDashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(task.isSuccessful())
            {
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                    if(account!=null)
                    {
                        AuthCredential authCredential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    final int[] flag = {0};
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
                                    Task<DataSnapshot> dataSnapshotTask = reference.get();
                                    dataSnapshotTask.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull  Task<DataSnapshot> task) {
                                            DataSnapshot dataSnapshot = task.getResult();
                                            Log.d("Childern", String.valueOf(dataSnapshot.getChildrenCount()));
                                            String srt = "";
                                            for (DataSnapshot snapshot:dataSnapshot.getChildren())
                                            {
                                                Log.d("Childern", snapshot.getKey());
                                                Log.d("Childern",snapshot.getValue().toString());
                                                User user = snapshot.getValue(User.class);
                                                Log.d("DATARetrive",user.getUserUuid());
                                                if(account.getEmail().equals(user.getUserUuid()))
                                                {
                                                    srt= snapshot.getKey();
                                                    Log.d("LOG1223",srt);
                                                    flag[0] =1;
                                                    break;
                                                }
                                            }
                                            if(flag[0]==0)
                                            {
                                                startActivity(new Intent(MainActivity.this,AgeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("id",account.getEmail()));
                                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                            else
                                            {
                                                SharedPreferences preferences = getSharedPreferences("id",MODE_PRIVATE);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("_id",srt);
                                                editor.apply();
                                                startActivity(new Intent(MainActivity.this,MainDashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("id",account.getId()));
                                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                    });


                                }else
                                {
                                    Log.d("API 1","Error-> "+task.getException());
                                    Toast.makeText(MainActivity.this, "Auth Fail!!*(1)-> "+task.getException(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("TAG", "Google sign in failed", e);
                }
            }
            else
            {
                Log.d("API 2","Error->"+task.getException());
                Toast.makeText(MainActivity.this, "Auth Fail!!-> *(2)->"+task.getException(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}