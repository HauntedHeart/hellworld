package cn.edu.pku.yangguanbin.yangguanbin.myweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.yangg.myweather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangg on 2018/11/6.
 */

public class Guide extends Activity implements ViewPager.OnPageChangeListener{
    private ViewPagerAdapter vpAadpter;
    private ViewPager vp;
    private List<View>  views;

    private ImageView[] dots;
    private int[] dots_ids={R.id.iv1,R.id.iv2,R.id.iv3};


    private Button btn1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);

        initViews();

        initDots();

        btn1=(Button)views.get(2).findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Guide.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }



    void initDots(){
        dots = new ImageView[views.size()];
        for(int i=0;i<views.size();i++){
            dots[i]=(ImageView)findViewById(dots_ids[i]);
        }
    }



    private void initViews(){
        LayoutInflater inflater = LayoutInflater.from(this);
        views=new ArrayList<View>();
        views.add(inflater.inflate(R.layout.page1,null));
        views.add(inflater.inflate(R.layout.page2,null));
        views.add(inflater.inflate(R.layout.page3,null));
        vpAadpter = new ViewPagerAdapter(views,this);
        vp = (ViewPager)findViewById(R.id.viewpager);
        vp.setAdapter(vpAadpter);
        vp.setOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int a=0;a<dots_ids.length;a++){
            if(a==position){
                dots[a].setImageResource(R.drawable.page_indicator_focused);
            }else {
                dots[a].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

