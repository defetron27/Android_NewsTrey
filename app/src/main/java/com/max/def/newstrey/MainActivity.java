package com.max.def.newstrey;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;


import com.google.gson.Gson;
import com.max.def.newstrey.Adapter.NewsQueryAdapter;
import com.max.def.newstrey.Client.RetrofitClient;
import com.max.def.newstrey.Interface.NewsService;
import com.max.def.newstrey.Models.NewsQueryArticlesModel;
import com.max.def.newstrey.Models.NewsQueryResponseModel;
import com.max.def.newstrey.Utils.PermissionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{
    private static final String NEWS_API_KEY = "280d1a7b22af44beb5800dd37ed0b874";

    private RecyclerView newsQueryRecycler;

    private NewsService newsService;

    private NewsQueryAdapter newsQueryAdapter;

    private PermissionUtil permissionUtil;

    private AppCompatEditText queryEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionUtil = new PermissionUtil(this);

        if (checkPermission(PermissionUtil.READ_INTERNET) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,android.Manifest.permission.INTERNET))
            {
                showPermissionExplanation(PermissionUtil.READ_INTERNET);
            }
            else if (permissionUtil.checkPermissionPreference(PermissionUtil.PERMISSION_INTERNET))
            {
                requestPermission(PermissionUtil.READ_INTERNET);
                permissionUtil.updatePermissionPreference(PermissionUtil.PERMISSION_INTERNET);
            }
            else
            {
                Toast.makeText(MainActivity.this, "Please allow internet permission in your app settings", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",MainActivity.this.getPackageName(),null);
                intent.setData(uri);
                this.startActivity(intent);
            }
        }

        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        Paper.init(this);

        queryEditText = findViewById(R.id.query_edit_text);
        AppCompatButton searchQueryBtn = findViewById(R.id.search_query_btn);
        newsQueryRecycler = findViewById(R.id.results_recycler_view);
        newsQueryRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        newsService = RetrofitClient.getClient().create(NewsService.class);

        if (Paper.book().read("cache") != null)
        {
            String cache = Paper.book().read("cache");

            if (cache != null)
            {
                NewsQueryResponseModel newsQueryResponseModel = new Gson().fromJson(cache,NewsQueryResponseModel.class);

                if (newsQueryResponseModel != null && newsQueryResponseModel.getStatus().equals("ok"))
                {
                    List<NewsQueryArticlesModel> articlesModelList = newsQueryResponseModel.getArticles();

                    if (articlesModelList != null && articlesModelList.size() > 0)
                    {
                        Paper.book().write("cache",new Gson().toJson(newsQueryResponseModel));

                        newsQueryAdapter = new NewsQueryAdapter(MainActivity.this,articlesModelList);
                        newsQueryRecycler.setAdapter(newsQueryAdapter);

                        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(MainActivity.this,R.anim.layout_slide_bottom);

                        newsQueryRecycler.setLayoutAnimation(controller);
                        newsQueryRecycler.scheduleLayoutAnimation();
                    }
                }
            }
        }

        searchQueryBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String query = queryEditText.getText().toString();

                if (query.equals("") || query.equals(" "))
                {
                    Toast.makeText(MainActivity.this, "Please enter any title...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    queryEditText.setText("");

                    Call<NewsQueryResponseModel> responseModelCall = newsService.getQueryNews(query,NEWS_API_KEY);

                    responseModelCall.enqueue(new Callback<NewsQueryResponseModel>()
                    {
                        @Override
                        public void onResponse(@NonNull Call<NewsQueryResponseModel> call, @NonNull Response<NewsQueryResponseModel> response)
                        {
                            NewsQueryResponseModel responseModel = response.body();

                            if (responseModel != null && responseModel.getStatus().equals("ok"))
                            {
                                List<NewsQueryArticlesModel> articlesModelList = responseModel.getArticles();

                                if (articlesModelList != null && articlesModelList.size() > 0)
                                {
                                    Paper.book().write("cache",new Gson().toJson(responseModel));

                                    newsQueryAdapter = new NewsQueryAdapter(MainActivity.this,articlesModelList);
                                    newsQueryRecycler.setAdapter(newsQueryAdapter);

                                    LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(MainActivity.this,R.anim.layout_slide_bottom);

                                    newsQueryRecycler.setLayoutAnimation(controller);
                                    newsQueryRecycler.scheduleLayoutAnimation();
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<NewsQueryResponseModel> call, @NonNull Throwable t)
                        {
                            Log.e("MainActivity",t.getLocalizedMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (checkPermission(PermissionUtil.READ_INTERNET) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,android.Manifest.permission.INTERNET))
            {
                showPermissionExplanation(PermissionUtil.READ_INTERNET);
            }
            else if (permissionUtil.checkPermissionPreference(PermissionUtil.PERMISSION_INTERNET))
            {
                requestPermission(PermissionUtil.READ_INTERNET);
                permissionUtil.updatePermissionPreference(PermissionUtil.PERMISSION_INTERNET);
            }
            else
            {
                Toast.makeText(MainActivity.this, "Please allow internet permission in your app settings", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",MainActivity.this.getPackageName(),null);
                intent.setData(uri);
                this.startActivity(intent);
            }
        }

    }

    private int checkPermission(int permission)
    {
        int status = PackageManager.PERMISSION_DENIED;

        switch (permission)
        {
            case PermissionUtil.READ_INTERNET:
                status = ContextCompat.checkSelfPermission(this,android.Manifest.permission.INTERNET);
                break;
        }
        return status;
    }

    private void requestPermission(int permission)
    {
        switch (permission)
        {
            case PermissionUtil.READ_INTERNET:
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.INTERNET},PermissionUtil.REQUEST_INTERNET);
                break;
        }
    }

    private void showPermissionExplanation(final int permission)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        switch (permission)
        {
            case PermissionUtil.READ_INTERNET:
                builder.setMessage("This app need to access your internet..");
                builder.setTitle("Internet Permission Needed..");
                break;
        }

        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (permission)
                {
                    case PermissionUtil.READ_INTERNET:
                        requestPermission(PermissionUtil.READ_INTERNET);
                        break;
                }
            }
        });

        builder.setNegativeButton("Deny", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
