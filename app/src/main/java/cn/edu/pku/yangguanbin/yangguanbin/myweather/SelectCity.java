package cn.edu.pku.yangguanbin.yangguanbin.myweather;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangg.myweather.R;

import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.yangguanbin.yangguanbin.app.MyApplication;
import cn.edu.pku.yangguanbin.yangguanbin.bean.City;

import static com.example.yangg.myweather.R.id.city;

/**
 * Created by yangg on 2018/10/4.
 * 选择城市界面
 */

public class SelectCity extends Activity implements View.OnClickListener {

    private String CityNow;
    private String CodeNow;

    private ImageView mBackBtn;
    private List<City> CityList;

    private TextView mTextView;
    private EditText mEditText;
    private ListView mListView;

    private ArrayAdapter myadapter;

    private String cityListNameStr;

    private String cityListCodeStr;

    private String temp_cityListNameStr;
    private String temp_cityListCodeStr;


    ArrayList<String> cityListName = new ArrayList<>();

    private CharSequence temp;

    @Override

    //修改选择城市界面的标题
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        Intent re_city = getIntent();
        CityNow = re_city.getStringExtra("city");
        String CityNowShow = "当前城市：" + CityNow;
        CodeNow = re_city.getStringExtra("code");
        TextView tv = (TextView) findViewById(R.id.title_name);
        tv.setText(CityNowShow);

        mTextView = (TextView) findViewById(R.id.t);
        mListView=(ListView)findViewById(R.id.title_list) ;
        mEditText = (EditText) findViewById(R.id.search_edit);

//        myadapter = new ArrayAdapter(SelectCity.this, android.R.layout.simple_list_item_1, cityListName);
//        mListView.setAdapter(myadapter);
        //初始化选择城市界面
//        initViews();____________________________-______________________________________________________________________
        //ListView mList = (ListView) findViewById(R.id.title_list);
        MyApplication myApplication = (MyApplication) getApplication();
        CityList = myApplication.getCityList();
//        final ArrayList<String> cityListName = new ArrayList<>();
        for (City city : CityList) {
//           cityListNameStr = city.getProvince() + "-" + city.getCity();
            cityListNameStr = city.getCity();
            cityListName.add(cityListNameStr);
        }
        //配置适配器
         myadapter = new ArrayAdapter(SelectCity.this, android.R.layout.simple_list_item_1, cityListName);
         mListView.setAdapter(myadapter);

//_______________________________________________________________________________________________________________________________
        mEditText.addTextChangedListener(mTextWatcher);
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for(City city:CityList){
                    if(city.getCity().equals(cityListName.get(position))){
                        temp_cityListNameStr=city.getCity();
                        temp_cityListCodeStr=city.getNumber();
                    }
                }
                Intent i = new Intent();
               // i.putExtra("CityCode", city.getNumber());  //推送数据，用于主界面更新天气信息
//                i.putExtra("CityName", city.getCity());
                City city = CityList.get(position);
                i.putExtra("CityCode", city.getNumber());  //推送数据，用于主界面更新天气信息
                i.putExtra("CityName", city.getCity());

                if (temp!="") {
                    i.putExtra("CityCode", temp_cityListCodeStr);
                    i.putExtra("CityName", temp_cityListNameStr);
                }
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }


    TextWatcher mTextWatcher = new TextWatcher() {
//        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            temp = charSequence;
            Log.d("myapp", "beforeTextChanged:" + temp);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            temp = charSequence;
            mTextView.setText(charSequence);
            cityListName.clear();
                String cityListNameStr = "";
                Log.d("Tag", charSequence.toString());
                for (City city : CityList) {
                    if (city.getCity().contains(charSequence.toString())) {
                        cityListNameStr = city.getCity();
                        cityListName.add(cityListNameStr);
                    }
                }
                myadapter.notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            editStart = mEditText.getSelectionStart();
            editEnd = mEditText.getSelectionEnd();
            if (temp.length() > 10) {
                Toast.makeText(SelectCity.this, "你输⼊入的字数已经超过了限制！", Toast.LENGTH_SHORT).show();
                editable.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                mEditText.setText(editable);
                mEditText.setSelection(tempSelection);
            }
            Log.d("myapp", "afterTextChanged:");
        }
    };


    @Override
    //响应点击返回
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            default:
                break;
        }
    }

    //初始化界面程序
//    private void initViews() {
//
//        ListView mList = (ListView) findViewById(R.id.title_list);
//
//        MyApplication myApplication = (MyApplication) getApplication();
//
//        final List<City> CityList = myApplication.getCityList();
//        final ArrayList<String> cityListName = new ArrayList<>();
//        for (City city : CityList) {
//            String cityListNameStr = city.getProvince() + "-" + city.getCity();
//            cityListName.add(cityListNameStr);
//        }
//
//        //配置适配器
//        ArrayAdapter myadapter = new ArrayAdapter(SelectCity.this, android.R.layout.simple_list_item_1, cityListName);
//        mList.setAdapter(myadapter);
//        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                City city = CityList.get(position);
//                Intent i = new Intent();
//                i.putExtra("CityCode", city.getNumber());  //推送数据，用于主界面更新天气信息
//                i.putExtra("CityName", city.getCity());
//                setResult(RESULT_OK, i);
//                finish();
//            }
//        });
//
//
//    }


}
