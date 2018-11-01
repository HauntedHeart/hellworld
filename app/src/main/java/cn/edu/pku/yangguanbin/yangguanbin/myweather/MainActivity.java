package cn.edu.pku.yangguanbin.yangguanbin.myweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangg.myweather.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.edu.pku.yangguanbin.yangguanbin.bean.TodayWeather;
import cn.edu.pku.yangguanbin.yangguanbin.util.NetUtil;


/**
 * Created by yangg on 2018/10/4.
 */

public class MainActivity extends Activity implements View.OnClickListener {

    private static final int UPDATE_TODAY_WEATHER = 1;

    private ImageView mUpdateBtn;
    private ProgressBar mUpdateProcess;

    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv, temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView weatherImg, pmImg;

    private String Now_CityName="北京";
    private String Now_CityCode="101010100";


    //Handler模块，用来传递天气信息
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };


    private ImageView mCitySelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        mUpdateProcess = (ProgressBar) findViewById(R.id.title_update_progress);
        mUpdateProcess.setOnClickListener(this);


        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK！", Toast.LENGTH_LONG).show();
        } else {
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }

        initView();

        mCitySelect = (ImageView) findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);

        queryWeatherCode(Now_CityCode);
    }

//首页天气信息初始化
    void initView() {
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
    }


    /**
     * * @param cityCode
     */
    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);

        //次线程，用来获取处理网络上的天气数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                TodayWeather todayWeather = null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather", responseStr);

                    parseXML(responseStr);

                    todayWeather = parseXML(responseStr);
                    if (todayWeather != null) {
                        Log.d("myWeather", todayWeather.toString());

                        Message msg = new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj = todayWeather;
                        mHandler.sendMessage(msg);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).

                start();
    }


    //为title_city_manager添加响应事件
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.title_city_manager) {
            Intent i = new Intent(MainActivity.this, SelectCity.class);
            i.putExtra("city",Now_CityName);    //要求携带消息
            i.putExtra("code",Now_CityCode);    //
            startActivityForResult(i,1);
        }

        //为title_update_btn添加响应事件
        if (view.getId() == R.id.title_update_btn) {
//            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);   //使用SharedPreferences存储城市代码信息
//            String cityCode = sharedPreferences.getString("main_city_code", "101010200");
            mUpdateBtn.setVisibility(View.INVISIBLE);
            mUpdateProcess.setVisibility(View.VISIBLE);
            //Log.d("myWeather", cityCode);

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(Now_CityCode);

                //设置延迟1.5秒后停止旋转更新图标
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mUpdateBtn.setVisibility(View.VISIBLE);
                        mUpdateProcess.setVisibility(View.INVISIBLE);
                    }
                }, 1800);//1.5秒后执行Runnable中的run方法

            }
            else {
                //设置延迟5秒后停止旋转更新图标
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mUpdateBtn.setVisibility(View.VISIBLE);
                        mUpdateProcess.setVisibility(View.INVISIBLE);
                    }
                }, 5400);//1.5秒后执行Runnable中的run方法

                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }

//        if (view.getId() == R.id.title_update_progress) {
//            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
//                Log.d("myWeather", "网络OK");
//                queryWeatherCode(Now_CityCode);
//                mUpdateBtn.setVisibility(View.VISIBLE);
//                mUpdateProcess.setVisibility(View.INVISIBLE);  }
//            else {
//                Log.d("myWeather", "网络挂了");
//                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
//            }
//            queryWeatherCode(Now_CityCode);
//        }
    }

   //解析网页XML的天气数据
    private TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather = null;
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                                Log.d("myWeather", "city:    " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                                Log.d("myWeather", "updatetime:    " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                                Log.d("myWeather", "shidu:    " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                                Log.d("myWeather", "wendu:    " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                                Log.d("myWeather", "pm25:    " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                                Log.d("myWeather", "quality:    " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                Log.d("myWeather", "fengxiang:    " + xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                Log.d("myWeather", "fengli:    " + xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText());
                                Log.d("myWeather", "high:    " + xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText());
                                Log.d("myWeather", "low:    " + xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                Log.d("myWeather", "type:    " + xmlPullParser.getText());
                                typeCount++;
                            }
                        }
                        break;
                    // 判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                // 进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
         return todayWeather;
    }

    //根据返回的数据更新城市代码
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==1&&resultCode==RESULT_OK){
            String newCityCode=data.getStringExtra("CityCode");
            Now_CityName=data.getStringExtra("CityName");
            Now_CityCode=newCityCode;
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                queryWeatherCode(Now_CityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }

    }

    //根据解析的网络数据更新天气信息
    void updateTodayWeather(TodayWeather todayWeather) {
        city_name_Tv.setText(todayWeather.getCity() + "天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidityTv.setText("湿度：" + todayWeather.getShidu());

        if(todayWeather.getQuality()==null) {
            pmDataTv.setText("无数据" );}
        else{
            pmDataTv.setText(todayWeather.getPm25());
        }

        if(todayWeather.getQuality()==null) {
             pmQualityTv.setText("无数据");}
        else{
             pmQualityTv.setText(todayWeather.getQuality());
            }

        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh() + "~" + todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:" + todayWeather.getFengli());

        //根据空气质量选择相应图片
        if(todayWeather.getQuality()!=null) {                   //增加判断PM2.5的质量数据是否存在，防止switch语句判断null值，导致程序崩溃
            switch (todayWeather.getQuality().trim()) {
                case "优":
                    pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
                    break;
                case "良":
                    pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
                    break;
                case "轻度污染":
                    pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
                    break;
                case "中度污染":
                    pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
                    break;
                case "重度污染":
                    pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
                    break;
                default:
                    pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
                    break;
            }
        }

        //根据天气类型选择相应图片
       if(todayWeather.getType()!=null) {
           switch (todayWeather.getType().trim()) {
               case "暴雪":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
                   break;
               case "大雪":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
                   break;
               case "中雪":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
                   break;
               case "小雪":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
                   break;
               case "阵雪":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
                   break;
               case "雨夹雪":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
                   break;
               case "特大暴雨":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
                   break;
               case "大雨":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
                   break;
               case "中雨":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
                   break;
               case "小雨":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
                   break;
               case "阵雨":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
                   break;
               case "雷阵雨":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
                   break;
               case "雷阵雨冰雹":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
                   break;
               case "多云":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
                   break;
               case "晴":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
                   break;
               case "雾":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
                   break;
               case "阴":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
                   break;
               case "沙尘暴":
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
                   break;
               default:
                   weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
                   break;
           }
       }
        Toast.makeText(MainActivity.this, "更新成功！", Toast.LENGTH_SHORT).show();
    }


}
