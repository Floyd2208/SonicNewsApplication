package com.example.sonicnewsapplication;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sonicnewsapplication.Models.NewsHeadlines;
import com.squareup.picasso.Picasso;
//Displays news articles once article is clicked
public class DetailsActivity extends AppCompatActivity {
    NewsHeadlines headlines;
    TextView txt_title,txt_author,txt_time,txt_detail,txt_content,txt_url,txt_webView;
    ImageView img_news;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        txt_title=findViewById(R.id.text_detail_news);
        txt_author=findViewById(R.id.text_detail_author);
        txt_time=findViewById(R.id.text_detail_time);
        txt_detail=findViewById(R.id.text_detail_detail);
        txt_content=findViewById(R.id.text_detail_content);
        img_news=findViewById(R.id.img_detail_news);
        txt_url=findViewById(R.id.text_detail_url);
        webView=findViewById(R.id.webView);
        txt_webView=findViewById(R.id.txt_webView);


        //Retrieves data from News headlines array list
        headlines= (NewsHeadlines) getIntent().getSerializableExtra("data");

        txt_title.setText(headlines.getTitle());
        txt_author.setText(headlines.getAuthor());
        txt_time.setText(headlines.getPublishedAt());
        txt_detail.setText(headlines.getDescription());
        txt_content.setText(headlines.getContent());
        Picasso.get().load(headlines.getUrlToImage()).into(img_news);
        txt_url.setText(headlines.getUrl());
        String text= txt_url.getText().toString();
        txt_webView.setText("Click here to read the article in App!!");
        txt_webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(text);

            }
        });
    }
}