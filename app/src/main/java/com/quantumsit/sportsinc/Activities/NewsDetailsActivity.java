package com.quantumsit.sportsinc.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.quantumsit.sportsinc.Entities.NewsEntity;
import com.quantumsit.sportsinc.R;
import com.squareup.picasso.Picasso;

public class NewsDetailsActivity extends AppCompatActivity {

    ImageView imageView;
    TextView Content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details_trial);
        getSupportActionBar().setTitle(R.string.news_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageView = findViewById(R.id.newsDetailsImage);
        Content = findViewById(R.id.newsDetailsContent);

        NewsEntity myNews = (NewsEntity) getIntent().getSerializableExtra("MyNews");

        if (myNews != null) {
            fillView(myNews);
        }

    }

    private void fillView(NewsEntity newsEntity) {
        String ImageUrl = newsEntity.getImg();

        if(!ImageUrl.equals("")) {
            Picasso.with(getApplicationContext()).load(ImageUrl).into(imageView);
        }
        Content.setText(newsEntity.getContent());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}