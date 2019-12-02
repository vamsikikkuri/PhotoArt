package com.example.vamsikikkuriproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<String> imageCategory = new ArrayList<>();
    private DatabaseReference dbRef;
    private ArrayList<String> filteredList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbRef = FirebaseDatabase.getInstance().getReference("links");


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Query query = dbRef.orderByChild("category");
                final AlertDialog.Builder filter = new AlertDialog.Builder(view.getContext());
                final String itemList[] = getResources().getStringArray(R.array.filterList);
                filteredList.clear();
                filter.setTitle("Add Filters for better results");
                filter.setMultiChoiceItems(itemList, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        String selectedItem = itemList[i];
                        if (b) {
                            filteredList.add(selectedItem);
                        } else {
                            filteredList.remove(selectedItem);
                        }
                    }
                }).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        imageUrls.clear();
                        descriptions.clear();
                        imageCategory.clear();
                        for (int list = 0; list < filteredList.size(); list++) {
                            String finalFilters = filteredList.get(list);
                            Log.v("Selected Items", finalFilters);
                            query.equalTo(finalFilters).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot imageShot : dataSnapshot.getChildren()) {
                                            Images images = imageShot.getValue(Images.class);
                                            imageUrls.add(images.getLink());
                                            descriptions.add(images.getDescription());
                                            imageCategory.add(images.getCategory());
                                        }
                                        initRecyclerView(imageUrls, descriptions, imageCategory);
                                    } else {
                                        Toast.makeText(HomePage.this, "Sorry no Images in Selected Category!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(HomePage.this, "Filtered Results reset to null, Nothing selected", Toast.LENGTH_LONG).show();

                    }
                }).setNeutralButton("clear filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                imageUrls.clear();
                                descriptions.clear();
                                imageCategory.clear();
                                for (DataSnapshot imageShot : dataSnapshot.getChildren()) {
                                    Images images = imageShot.getValue(Images.class);
                                    imageUrls.add(images.getLink());
                                    descriptions.add(images.getDescription());
                                    imageCategory.add(images.getCategory());
                                }
                                initRecyclerView(imageUrls, descriptions, imageCategory);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                filter.show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageUrls.clear();
                descriptions.clear();
                imageCategory.clear();
                for (DataSnapshot imageShot : dataSnapshot.getChildren()) {
                    Images images = imageShot.getValue(Images.class);
                    imageUrls.add(images.getLink());
                    descriptions.add(images.getDescription());
                    imageCategory.add(images.getCategory());
                }
                initRecyclerView(imageUrls, descriptions, imageCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRecyclerView(ArrayList<String> finalImageUrls, ArrayList<String> finalImageDescs, ArrayList<String> finalCategories) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        HomePageAdapter adapter = new HomePageAdapter(this, finalImageDescs, finalImageUrls, finalCategories);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_resume) {
            Intent intentResume = new Intent(HomePage.this, Resume.class);
            startActivity(intentResume);
        }
        return super.onOptionsItemSelected(item);
    }
}
