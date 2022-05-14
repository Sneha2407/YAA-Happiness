package com.sreesubh.yaahappiness20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class AgeActivity extends AppCompatActivity {
    DatePicker datePicker;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age);
        datePicker = findViewById(R.id.age_date_picker);
        button = findViewById(R.id.age_submit);
        String id = getIntent().getStringExtra("id");
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String dp = datePicker.getYear()+"/"+datePicker.getMonth()+"/"+datePicker.getDayOfMonth();
                try {
                    FirebaseDatabase db= FirebaseDatabase.getInstance();
                    DatabaseReference reference = db.getReference("User");
                    User user = new User();
                    user.setUserUuid(id);
                    user.setAggHappinessScore(0.0);
                    user.setDob(dp);
                    reference.push().setValue(user);
                    final String[] da = {""};
                    SharedPreferences preferences = getSharedPreferences("id",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
                    Task<DataSnapshot> dataSnapshotTask = ref.get();
                    dataSnapshotTask.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser user1 = auth.getCurrentUser();
                            DataSnapshot snapshot = task.getResult();
                            for (DataSnapshot data:snapshot.getChildren())
                            {
                                User user = data.getValue(User.class);
                                Log.d("LOGD",data.getKey());
                                if(user1.getEmail().equals(user.getUserUuid()))
                                {
                                    da[0] = data.getKey();
                                    break;
                                }
                            }
                            editor.putString("_id",da[0]);
                            editor.apply();
                            Log.d("data",da[0]);
                            startActivity(new Intent(AgeActivity.this,MainDashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            Toast.makeText(AgeActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}