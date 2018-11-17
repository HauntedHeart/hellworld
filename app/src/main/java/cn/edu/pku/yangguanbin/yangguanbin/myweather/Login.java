package cn.edu.pku.yangguanbin.yangguanbin.myweather;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangg.myweather.R;

import static com.example.yangg.myweather.R.id.user_key;
import static com.example.yangg.myweather.R.id.user_name;

/**
 * Created by yangg on 2018/11/1.
 */

public class Login extends Activity implements View.OnClickListener {

    private ImageView mBackBtn;
    private Button mCancelBtn;
    private Button mEnterBtn;

    private CheckBox checkBox1;

    private EditText mEdit_userkey, mEdit_username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        mCancelBtn = (Button) findViewById(R.id.button_cancel);
        mCancelBtn.setOnClickListener(this);

        mEnterBtn = (Button) findViewById(R.id.button_Enter);
        mEnterBtn.setOnClickListener(this);

        mEdit_userkey = (EditText) findViewById(user_key);
        mEdit_userkey.setTransformationMethod(PasswordTransformationMethod.getInstance());

        mEdit_username = (EditText) findViewById(user_name);

        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    // /如果选中，显示密码
                    mEdit_userkey.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    mEdit_userkey.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.title_back:
                        finish();
                        break;
                    case R.id.button_cancel:
                        finish();
                        break;
                    case R.id.button_Enter:
                        if (mEdit_username.getText().toString().equals("yang") && mEdit_userkey.getText().toString().equals("000000")) {
                            Toast.makeText(Login.this, "登录成功", Toast.LENGTH_LONG).show();
                            finish();
                            break;
                        } else {
                            Toast.makeText(Login.this, "用户名或密码错误，请重新输入！" , Toast.LENGTH_LONG).show();
                        }
                    default:
                        break;
                }
            }


        }
