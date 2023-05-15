package com.example.sonicnewsapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sonicnewsapplication.Models.NewsApiResponse;
import com.example.sonicnewsapplication.Models.NewsHeadlines;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectListener, View.OnClickListener{
    RecyclerView recyclerView;
    CustomAdapter adapter;
    ProgressDialog dialog;
    Button b1,b2,b3,b4,b5,b6,b7;
    SearchView searchView;
    BottomNavigationView bottomNavBar;

    @Override
    public void registerForContextMenu(View view) {
        super.registerForContextMenu(bottomNavBar);
    }

    private long pressedTime=0;
    //Api data display functions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override

                    public void onRefresh() {
                        recreate();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

        );



        searchView=findViewById(R.id.search_view);
        //Search result display
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Fetching news articles of "+query);
                dialog.show();
                RequestManager manager=new RequestManager(MainActivity.this);
                manager.getNewsHeadlines(listener,"general",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        dialog=new ProgressDialog(this);
        dialog.setTitle("Fetching News Articles");
        dialog.show();

        //Category button Initialization
        b1=findViewById(R.id.btn_1);
        b1.setOnClickListener(this);
        b2=findViewById(R.id.btn_2);
        b2.setOnClickListener(this);
        b3=findViewById(R.id.btn_3);
        b3.setOnClickListener(this);
        b4=findViewById(R.id.btn_4);
        b4.setOnClickListener(this);
        b5=findViewById(R.id.btn_5);
        b5.setOnClickListener(this);
        b6=findViewById(R.id.btn_6);
        b6.setOnClickListener(this);
        b7=findViewById(R.id.btn_7);
        b7.setOnClickListener(this);




        RequestManager manager=new RequestManager(this);
        manager.getNewsHeadlines(listener,"general",null);

        //navigation bar
        bottomNavBar=findViewById(R.id.bottom_nav_bar);
        bottomNavBar.setSelectedItemId(R.id.news);
        //bottomNavBar.setSelectedItemId(preferencesHelper.getSelectedItemId());
        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.news:
//                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:

                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        bottomNavBar.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.news:
/*                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                          overridePendingTransition(0,0);*/

                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        overridePendingTransition(0,0);
                }
            }
        });
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            bottomNavBar.setBackgroundColor(getResources().getColor(R.color.turquiose));
        } else {
            bottomNavBar.setBackgroundColor(getResources().getColor(R.color.turquiose));
        }
    }
    //Data fetch interface
    private final OnFetchDataListener<NewsApiResponse> listener=new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            if(list.isEmpty()){
                Toast.makeText(MainActivity.this,"No Data Found",Toast.LENGTH_SHORT).show();
            }
            else {
                showNews(list);
                dialog.dismiss();
            }
        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this,"An error has occurred",Toast.LENGTH_SHORT).show();
        }
    };


    //News Display function
    private void showNews(List<NewsHeadlines> list) {
        recyclerView=findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        adapter=new CustomAdapter(this,list,this);
        recyclerView.setAdapter(adapter);

    }
    //News clicked Function
    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {
        startActivity(new Intent(MainActivity.this,DetailsActivity.class)
                .putExtra("data",headlines));
    }

    //Category Switch Function
    @Override
    public void onClick(View v) {

        Button button=(Button) v;
        button.setBackgroundColor(getResources().getColor(R.color.turquiose));
        String category=button.getText().toString();
        dialog.setTitle("Fetching news articles of "+category);
        dialog.show();
        RequestManager manager=new RequestManager(this);
        manager.getNewsHeadlines(listener,category,null);

    }
    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(MainActivity.this, "You have already logged in!!\nDouble tap to exit!!", Toast.LENGTH_LONG).show();
        }
        pressedTime=System.currentTimeMillis();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // Inflate the context menu layout
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }
    public boolean onContextItemSelected(MenuItem item) {
        // Handle context menu item clicks
        switch (item.getItemId()) {
            case R.id.profileCm:
                startActivity(new Intent(MainActivity.this,UserDetails.class));
                return true;
            case R.id.logoutCm:

                startActivity(new Intent(MainActivity.this,Login.class));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}