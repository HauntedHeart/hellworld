package cn.edu.pku.yangguanbin.yangguanbin.myweather;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yangg.myweather.R;

import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.yangguanbin.yangguanbin.app.MyApplication;
import cn.edu.pku.yangguanbin.yangguanbin.bean.City;

/**
 * Created by yangg on 2018/10/4.
 *选择城市界面
 */

public class SelectCity extends Activity implements View.OnClickListener{

    private String CityNow;
    private String CodeNow;

    private ImageView mBackBtn;
    private List<City> CityList;

    @Override

    //修改选择城市界面的标题
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        Intent res=getIntent();
        CityNow=res.getStringExtra("city");
        String CityNowShow="当前城市："+CityNow;
        CodeNow=res.getStringExtra("code");
        TextView tv = (TextView)findViewById(R.id.title_name);
        tv.setText(CityNowShow);

        //初始化选择城市界面
        initViews();

        mBackBtn = (ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
    }


    @Override
    //响应点击返回
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            default:
                break;
        }
    }

    //初始化界面程序
    private void initViews() {

            ListView mList = (ListView) findViewById(R.id.title_list);

            MyApplication myApplication = (MyApplication) getApplication();

            final List<City> CityList = myApplication.getCityList();
            final ArrayList<String> cityListName = new ArrayList<>();
            for(City city :CityList){
                String cityListNameStr = city.getProvince() + "-" + city.getCity();
                cityListName.add(cityListNameStr);
            }

            //配置适配器
            ArrayAdapter myadapter = new ArrayAdapter(SelectCity.this, android.R.layout.simple_list_item_1, cityListName);
            mList.setAdapter(myadapter);
            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    City city = CityList.get(position);
                    Intent i = new Intent();
                    i.putExtra("CityCode", city.getNumber());  //推送数据，用于主界面更新天气信息
                    i.putExtra("CityName", city.getCity());
                    setResult(RESULT_OK, i);
                    finish();
                }
            });


    }
}
