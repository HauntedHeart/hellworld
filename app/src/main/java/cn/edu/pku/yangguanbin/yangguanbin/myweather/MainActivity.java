package cn.edu.pku.yangguanbin.yangguanbin.myweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.yangguanbin.yangguanbin.bean.TodayWeather;
import cn.edu.pku.yangguanbin.yangguanbin.util.NetUtil;

import static android.R.attr.start;


/**
 * Created by yangg on 2018/10/4.
 */

public class MainActivity extends Activity implements View.OnClickListener ,ViewPager.OnPageChangeListener{

    private static final int UPDATE_TODAY_WEATHER = 1;

    private ImageView mUpdateBtn;
    private ProgressBar mUpdateProcess;
    private ImageView mLocationBtn;

    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv, temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView  pmImg;
    private ImageView weatherImg,weatherImg_1,weatherImg_2,weatherImg_3,weatherImg_4,weatherImg_5,weatherImg_6;
    private ImageView[] weatherImgs={weatherImg,weatherImg_1,weatherImg_2,weatherImg_3,weatherImg_4,weatherImg_5,weatherImg_6};
    private int[] weatherImg_ids={R.id.weather_img,R.id.weather1_img,R.id.weather2_img,R.id.weather3_img,R.id.weather4_img,R.id.weather5_img,R.id.weather6_img};

    private TextView temperatureTv_1,temperatureTv_2,temperatureTv_3,temperatureTv_4,temperatureTv_5,temperatureTv_6;
    private TextView[] temperatureTvs={temperatureTv_1,temperatureTv_2,temperatureTv_3,temperatureTv_4,temperatureTv_5,temperatureTv_6};
    private int[] temperature_ids={R.id.temperature_future1,R.id.temperature_future2,R.id.temperature_future3,R.id.temperature_future4,R.id.temperature_future5,R.id.temperature_future6};

    private TextView  climateTv_1, climateTv_2, climateTv_3, climateTv_4, climateTv_5, climateTv_6;
    private TextView[] climateTvs={climateTv_1, climateTv_2, climateTv_3, climateTv_4, climateTv_5, climateTv_6};
    private int[] climate_ids={R.id.climate_future1,R.id.climate_future2,R.id.climate_future3,R.id.climate_future4,R.id.climate_future5,R.id.climate_future6};

    private TextView windTv_1,windTv_2,windTv_3,windTv_4,windTv_5,windTv_6;
    private TextView[] windTvs={windTv_1,windTv_2,windTv_3,windTv_4,windTv_5,windTv_6};
    private int[] wind_ids={R.id.wind_future1,R.id.wind_future2,R.id.wind_future3,R.id.wind_future4,R.id.wind_future5,R.id.wind_future6};

    private TextView weekTv_1,weekTv_2,weekTv_3,weekTv_4,weekTv_5,weekTv_6;
    private TextView[] weekTvs={weekTv_1,weekTv_2,weekTv_3,weekTv_4,weekTv_5,weekTv_6};
    private int[] week_ids={R.id.week_future1,R.id.week_future2,R.id.week_future3,R.id.week_future4,R.id.week_future5,R.id.week_future6};



    private String Now_CityName="北京";
    private String Now_CityCode="101010100";


    private ViewPagerAdapter vpAadpter;
    private ViewPager vp;
    private List<View> views;

    private ImageView[] dots;
    private int[] dots_ids={R.id.iv13,R.id.iv46,};

    private LocationManager locationManager;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
//原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明


//    LocationClientOption option = new LocationClientOption();
//            option.(true);
////可选，是否需要地址信息，默认为不需要，即参数为false
////如果开发者需要获得当前点的地址信息，此处必须为true
//
//           mLocationClient.set(option);
////mLocationClient为第二步初始化过的LocationClient对象
////需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
////更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明



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

    private ImageView mShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);


        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        mLocationBtn = (ImageView) findViewById(R.id.title_location);
        mLocationBtn.setOnClickListener(this);

        mUpdateProcess = (ProgressBar) findViewById(R.id.title_update_progress);
        mUpdateProcess.setOnClickListener(this);

        mLocationClient = new LocationClient(getApplicationContext());
            //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
            //注册监听函数

        initLocation();



        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK！", Toast.LENGTH_LONG).show();
        } else {
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }

        initViews();
        initView();

        initDots();

        mCitySelect = (ImageView) findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);

        mShare=(ImageView)findViewById(R.id.title_share) ;
        mShare.setOnClickListener(this);


        queryWeatherCode(Now_CityCode);

    }

//首页天气信息初始化
    void initView() {
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);

        for(int i=0;i<3;i++){
            weekTvs[i]=(TextView)views.get(0).findViewById(week_ids[i]);}
        for(int i=3;i<6;i++){
            weekTvs[i]=(TextView)views.get(1).findViewById(week_ids[i]);}
//        for(int i=0;i<6;i++){
//            weekTvs[i].setText("星期"+i);}

       pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        for(int i=0;i<3;i++){
            temperatureTvs[i]=(TextView)views.get(0).findViewById(temperature_ids[i]);}
        for(int i=3;i<6;i++){
            temperatureTvs[i]=(TextView)views.get(1).findViewById(temperature_ids[i]);}
//        for(int i=0;i<6;i++){
//            temperatureTvs[i].setText("温度"+i);}


        climateTv = (TextView) findViewById(R.id.climate);
        for(int i=0;i<3;i++){
            climateTvs[i]=(TextView)views.get(0).findViewById(climate_ids[i]);}
        for(int i=3;i<6;i++){
            climateTvs[i]=(TextView)views.get(1).findViewById(climate_ids[i]);}
//        for(int i=0;i<6;i++){
//            climateTvs[i].setText("天气类型"+i);}

        windTv = (TextView) findViewById(R.id.wind);
        for(int i=0;i<3;i++){
            windTvs[i]=(TextView)views.get(0).findViewById(wind_ids[i]);}
        for(int i=3;i<6;i++){
            windTvs[i]=(TextView)views.get(1).findViewById(wind_ids[i]);}
//        for(int i=0;i<6;i++){
//            windTvs[i].setText("风向"+i);}


        weatherImgs[0]=(ImageView)findViewById( weatherImg_ids[0]);
        for(int i=1;i<4;i++){
            weatherImgs[i]=(ImageView)views.get(0).findViewById( weatherImg_ids[i]);}
        for(int i=4;i<7;i++){
            weatherImgs[i]=(ImageView)views.get(1).findViewById( weatherImg_ids[i]);}




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
        }).start();
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

        if (view.getId() == R.id.title_share) {
            Intent i = new Intent(MainActivity.this, Login.class);//
            startActivityForResult(i,1);
        }

        if (view.getId() == R.id.title_location) {

//            public void onCreate() {
//                mLocationClient = new LocationClient(getApplicationContext());
//                //声明LocationClient类
//                mLocationClient.registerLocationListener(myListener);
//                //注册监听函数
//            }
//
//            LocationClientOption option = new LocationClientOption();
//            option.(true);
////可选，是否需要地址信息，默认为不需要，即参数为false
////如果开发者需要获得当前点的地址信息，此处必须为true
//
//            mLocationClient.set(option);
////mLocationClient为第二步初始化过的LocationClient对象
////需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
////更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
           if (mLocationClient.isStarted()) {
                mLocationClient.stop();
           }
            mLocationClient.start();

            final Handler BDHandler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case UPDATE_TODAY_WEATHER:
                        if (msg.obj != null) {
                            if (NetUtil.getNetworkState(MainActivity.this) != NetUtil.NETWORN_NONE) {
                                Log.d("location111", "定位成功");
                                Now_CityCode=myListener.cityCode;
                                queryWeatherCode(Now_CityCode);
                                Toast.makeText(MainActivity.this, "定位成功！", Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("myWeather", "网络挂了");
                                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
                            }

                        }
                        myListener.cityCode = null;
                        break;
                        default:
                            break;
                    }
                }

            };

            new Thread(new Runnable(){
                @Override
                public void run(){
                    try{
                        while (myListener.cityCode==null){
                            Thread.sleep(2000);
                        }
                        Message msg=new Message();
                        msg.what=UPDATE_TODAY_WEATHER;
                        msg.obj=myListener.cityCode;
                        BDHandler.sendMessage(msg);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();

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
                            }else if (xmlPullParser.getName().equals("fengli") && fengliCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli_1(xmlPullParser.getText());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli") && fengliCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli_2(xmlPullParser.getText());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli") && fengliCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli_3(xmlPullParser.getText());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli") && fengliCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli_4(xmlPullParser.getText());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                fengliCount++;
                            }


                            else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                dateCount++;
                            }else if (xmlPullParser.getName().equals("date") && dateCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWeek_1(xmlPullParser.getText());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                dateCount++;
                            }else if (xmlPullParser.getName().equals("date") && dateCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWeek_2(xmlPullParser.getText());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                dateCount++;
                            }else if (xmlPullParser.getName().equals("date") && dateCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWeek_3(xmlPullParser.getText());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                dateCount++;
                            }else if (xmlPullParser.getName().equals("date") && dateCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWeek_4(xmlPullParser.getText());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                dateCount++;
                         }

                            else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText());
                                Log.d("myWeather", "high:    " + xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh_1(xmlPullParser.getText().substring(2).trim());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                highCount++;
                            }else if (xmlPullParser.getName().equals("high") && highCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh_2(xmlPullParser.getText().substring(2).trim());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                highCount++;
                            }else if (xmlPullParser.getName().equals("high") && highCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh_3(xmlPullParser.getText().substring(2).trim());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                highCount++;
                            }else if (xmlPullParser.getName().equals("high") && highCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh_4(xmlPullParser.getText().substring(2).trim());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                highCount++;
                            }




                            else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText());
                                Log.d("myWeather", "low:    " + xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow_1(xmlPullParser.getText().substring(2).trim());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low") && lowCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow_2(xmlPullParser.getText().substring(2).trim());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low") && lowCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow_3(xmlPullParser.getText().substring(2).trim());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low") && lowCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow_4(xmlPullParser.getText().substring(2).trim());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                lowCount++;
                            }


                            else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                Log.d("myWeather", "type:    " + xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type") && typeCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType_1(xmlPullParser.getText());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type") && typeCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType_2(xmlPullParser.getText());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type") && typeCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType_3(xmlPullParser.getText());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type") && typeCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType_4(xmlPullParser.getText());
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
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
        weekTvs[0].setText(todayWeather.getWeek_1());
        weekTvs[1].setText(todayWeather.getWeek_2());
        weekTvs[2].setText(todayWeather.getWeek_3());
        weekTvs[3].setText(todayWeather.getWeek_4());
//        weekTvs[4].setText(todayWeather.getWeek_5());
//        weekTvs[5].setText(todayWeather.getWeek_6());

        temperatureTv.setText(todayWeather.getHigh() + "~" + todayWeather.getLow());
        temperatureTvs[0].setText(todayWeather.getHigh_1() + "~" + todayWeather.getLow_1());
        temperatureTvs[1].setText(todayWeather.getHigh_2() + "~" + todayWeather.getLow_2());
        temperatureTvs[2].setText(todayWeather.getHigh_3() + "~" + todayWeather.getLow_3());
        temperatureTvs[3].setText(todayWeather.getHigh_4() + "~" + todayWeather.getLow_4());
//        temperatureTvs[4].setText(todayWeather.getHigh_5() + "~" + todayWeather.getLow_5());
//        temperatureTvs[5].setText(todayWeather.getHigh_6() + "~" + todayWeather.getLow_6());

        climateTv.setText(todayWeather.getType());
        climateTvs[0].setText(todayWeather.getType_1());
        climateTvs[1].setText(todayWeather.getType_2());
        climateTvs[2].setText(todayWeather.getType_3());
        climateTvs[3].setText(todayWeather.getType_4());
//        climateTvs[4].setText(todayWeather.getType_5());
//        climateTvs[5].setText(todayWeather.getType_6());

        windTv.setText("风力:" + todayWeather.getFengli());
        windTvs[0].setText("风力:" + todayWeather.getFengli_1());
        windTvs[1].setText("风力:" + todayWeather.getFengli_2());
        windTvs[2].setText("风力:" + todayWeather.getFengli_3());
        windTvs[3].setText("风力:" + todayWeather.getFengli_4());
//        windTvs[4].setText("风力:" + todayWeather.getFengli_5());
//        windTvs[5].setText("风力:" + todayWeather.getFengli_6());

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

//       String types[]={todayWeather.getType().trim(),todayWeather.getType_1().trim(),todayWeather.getType_2().trim(),todayWeather.getType_3().trim(),todayWeather.getType_4().trim(),todayWeather.getType_5().trim(),todayWeather.getType_6().trim()};
      String types[]={todayWeather.getType(),todayWeather.getType_1(),todayWeather.getType_2(),todayWeather.getType_3(),todayWeather.getType_4(),todayWeather.getType_5(),todayWeather.getType_6()};
        for(int i=0;i<5;i++){
            if(types[i]!=null) {
                switch (types[i]) {
                    case "暴雪":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_baoxue);
                        break;
                    case "大雪":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_daxue);
                        break;
                    case "中雪":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_zhongxue);
                        break;
                    case "小雪":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
                        break;
                    case "阵雪":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_zhenxue);
                        break;
                    case "雨夹雪":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
                        break;
                    case "特大暴雨":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
                        break;
                    case "大雨":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_dayu);
                        break;
                    case "中雨":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_zhongyu);
                        break;
                    case "小雨":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
                        break;
                    case "阵雨":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_zhenyu);
                        break;
                    case "雷阵雨":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
                        break;
                    case "雷阵雨冰雹":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
                        break;
                    case "多云":
                        Log.d("dfasfsdsaf","dfaf________________hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_duoyun);

                        Log.d("dfasfsdsaf","dfaf________________dddddddddddddddddddddddddddddddddddddd__________________");
                        break;
                    case "晴":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_qing);
                        break;
                    case "雾":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_wu);
                        break;
                    case "阴":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_yin);
                        break;
                    case "沙尘暴":
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_shachenbao);
                        break;
                    default:
                        weatherImgs[i].setImageResource(R.drawable.biz_plugin_weather_baoxue);
                        break;
                }
            }
       }
        Toast.makeText(MainActivity.this, "更新成功！", Toast.LENGTH_SHORT).show();
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
        views.add(inflater.inflate(R.layout.future13,null));
        views.add(inflater.inflate(R.layout.future46,null));
        vpAadpter = new ViewPagerAdapter(views,this);
        vp = (ViewPager)findViewById(R.id.viewpager);
        vp.setAdapter(vpAadpter);
        vp.setOnPageChangeListener(this);

    }


    private void initLocation(){

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认GCJ02
//GCJ02：国测局坐标；
//BD09ll：百度经纬度坐标；
//BD09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        int span=1000;
        option.setScanSpan(0);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效
        option.setIsNeedAddress(true);
        option.setOpenGps(true);

//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);
//可选，V7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明



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
