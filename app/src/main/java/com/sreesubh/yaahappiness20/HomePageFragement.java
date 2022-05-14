package com.sreesubh.yaahappiness20;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomePageFragement extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomePageFragement() {
    }

    public static HomePageFragement newInstance(String param1, String param2) {
        HomePageFragement fragment = new HomePageFragement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    LinearLayout star_panel;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page_fragement, container, false);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        star_panel = view.findViewById(R.id.star_panel);
        progressBar.setVisibility(View.VISIBLE);
        star_panel.setVisibility(View.GONE);
        SharedPreferences preferences = getActivity().getSharedPreferences("id", Context.MODE_PRIVATE);
        String dam = preferences.getString("score","null");
        String arr[]= dam.split(":");
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        String dat = preferences.getString("_id","null");


        DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("User/"+dat+"/aggHappinessScore");

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User/"+dat+"/daily");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull  DataSnapshot snapshot1) {
                    int k =0,n=0;
                    int sum = 0;

                    for (DataSnapshot snap:snapshot1.getChildren())
                    {
                        DailyData data = snap.getValue(DailyData.class);
                        sum = sum+data.getScore();
                        n++;
                        if (data.getDate().equals(formattedDate))
                        {
                            progressBar.setVisibility(View.GONE);
                            star_panel.setVisibility(View.VISIBLE);
                            setR(data.getScore()-1);
                            k=1;
                            star_panel.setClickable(false);
                            for (int i = 0;i<star_panel.getChildCount();i++)
                            {
                                star_panel.getChildAt(i).setClickable(false);
                            }
                            break;
                        }
                    }
                    if (n>0) {
                        double sk = (double) sum / n;
                        db2.setValue(sk);
                    }else
                    {
                        db2.setValue(0);
                    }
                    if(k==0)
                    {
                        star_panel.setClickable(true);
                        for (int i = 0;i<star_panel.getChildCount();i++)
                        {
                            star_panel.getChildAt(i).setClickable(true);
                        }
                        setR(-1);
                    }
                    progressBar.setVisibility(View.GONE);
                    star_panel.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        int x;
        for(x = 0;x<star_panel.getChildCount();x++)
        {
            int STAR_POS = x;
            star_panel.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRating(STAR_POS);
                }
            });
        }
        return view;
    }

    private void setR(int x) {
        for (int i = 0;i<star_panel.getChildCount();i++)
        {
            ImageView sb = (ImageView) star_panel.getChildAt(i);
            sb.setImageTintList(ColorStateList.valueOf(Color.parseColor("#A7A7A7")));
            if(i<=x)
            {
                sb.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFC107")));
            }

        }
    }
    private void setRating(int x) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        Task<DataSnapshot> dataSnapshotTask = reference.get();
        dataSnapshotTask.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task!=null)
                {
                    String unm="";
                    DataSnapshot snapshot = task.getResult();
                    for (DataSnapshot sp:snapshot.getChildren())
                    {
                        User userr = sp.getValue(User.class);
                        Log.d("LOGD",sp.getKey());
                        if(userr.getUserUuid().equals(user.getEmail()))
                        {
                            unm = sp.getKey();
                            break;
                        }
                    }
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("User/"+unm+"/daily");
                    DailyData data = new DailyData();
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);
                    data.setDate(formattedDate);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                    Date d = new Date();
                    String dayOfTheWeek = sdf.format(d);
                    data.setDay(dayOfTheWeek);
                    data.setScore((x+1));
                    db.push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task!=null)
                            {
                                Toast.makeText(getContext(), "Data Uploaded", Toast.LENGTH_SHORT).show();
                                star_panel.setEnabled(false);
                            }else
                            {
                                Toast.makeText(getContext(), "Today's Happiness is uploaded Thank you!! ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Today's Happiness is uploaded Thank you!! ", Toast.LENGTH_SHORT).show();
                            Log.d("LOGD",e.getMessage());
                        }
                    });
                }
                else
                {
                    Toast.makeText(getContext(), "Today's Happiness is uploaded Thank you!! ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        for (int i = 0;i<star_panel.getChildCount();i++)
        {
            ImageView sb = (ImageView) star_panel.getChildAt(i);
            sb.setImageTintList(ColorStateList.valueOf(Color.parseColor("#A7A7A7")));
            if(i<=x)
            {
                sb.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFC107")));
            }

        }
    }
}