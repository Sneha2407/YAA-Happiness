package com.sreesubh.yaahappiness20;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Reports extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public Reports() {
    }
    public static Reports newInstance(String param1, String param2) {
        Reports fragment = new Reports();
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
    RecyclerView rv_show;
    Spinner spinner;
    List<TotalReportModelView> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        rv_show = view.findViewById(R.id.rv_show);
        spinner = view.findViewById(R.id.spinner);
        List<String> l = new ArrayList<>();
        l.add("Show All");
        l.add("Last 7 Days");
        l.add("Last 15 Days");
        l.add("Last 30 Days");
        l.add("Last 60 Days");
        l.add("Last 90 Days");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,l);
        spinner.setAdapter(adapter);

        rv_show.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        list = new ArrayList<>();
        SharedPreferences preferences = getActivity().getSharedPreferences("id", Context.MODE_PRIVATE);
        String dat = preferences.getString("_id","null");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User/"+dat+"/daily");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap:snapshot.getChildren())
                {
                    Log.d("LOGD", String.valueOf(snap.getValue()));
                    DailyData data = snap.getValue(DailyData.class);
                    list.add(new TotalReportModelView(data.getScore(),data.getDay(),data.getDate()));
                }
                Collections.reverse(list);

                TotalReportAdapter adapter = new TotalReportAdapter(list);
                
                rv_show.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int index = parent.getSelectedItemPosition();
                        List<TotalReportModelView> li = new ArrayList<>();
                        String s = l.get(index);
                        if(s.contains("7"))
                        {
                            if(list.size()>=6)
                            {
                                for (int i =0;i< list.size();i++)
                                {
                                    if(i<7)
                                        li.add(list.get(i));
                                    else
                                        break;
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Less Items", Toast.LENGTH_SHORT).show();
                                li.addAll(list);
                            }
                        }
                        else if(s.contains("15"))
                        {
                            if(list.size()>=14)
                            {
                                for (int i =0;i< list.size();i++)
                                {
                                    if(i<15)
                                        li.add(list.get(i));
                                    else
                                        break;
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Less Items", Toast.LENGTH_SHORT).show();
                                li.addAll(list);
                            }

                        }else if(s.contains("30"))
                        {
                            if(list.size()>=29)
                            {
                                for (int i =0;i< list.size();i++)
                                {
                                    if(i<=29)
                                        li.add(list.get(i));
                                    else
                                        break;
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Less Items", Toast.LENGTH_SHORT).show();
                                li.addAll(list);
                            }

                        }else if(s.contains("45"))
                        {
                            if(list.size()>=44)
                            {
                                for (int i =0;i< list.size();i++)
                                {
                                    if(i<45)
                                        li.add(list.get(i));
                                    else
                                        break;
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Less Items", Toast.LENGTH_SHORT).show();
                                li.addAll(list);
                            }

                        }else if(s.contains("60"))
                        {
                            if(list.size()>=59)
                            {
                                for (int i =0;i< list.size();i++)
                                {
                                    if(i<60)
                                        li.add(list.get(i));
                                    else
                                        break;
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Less Items", Toast.LENGTH_SHORT).show();
                                li.addAll(list);
                            }

                        }else if(s.contains("90"))
                        {
                            if(list.size()>=90)
                            {
                                for (int i =0;i< list.size();i++)
                                {
                                    if(i<89)
                                    li.add(list.get(i));
                                    else
                                        break;
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Less Items", Toast.LENGTH_SHORT).show();
                                li.addAll(list);
                            }

                        }else if(s.contains("All"))
                        {
                            li.addAll(list);
                        }
                        TotalReportAdapter adapter = new TotalReportAdapter(li);

                        rv_show.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}