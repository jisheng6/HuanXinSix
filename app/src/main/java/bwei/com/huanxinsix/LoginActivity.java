package bwei.com.huanxinsix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.sql.CallableStatement;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.admin_edit)
    EditText mAdminEdit;
    @BindView(R.id.pwd_edit)
    EditText mPwdEdit;
    @BindView(R.id.login_btn)
    Button mLoginBtn;
    @BindView(R.id.register_btn)
    Button mRegisterBtn;
        private Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                       String string = (String) msg.obj;
                       Toast.makeText(LoginActivity.this,string,Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        String strings = (String) msg.obj;
                        Toast.makeText(LoginActivity.this,strings,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


    }

    @OnClick({R.id.login_btn, R.id.register_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.login_btn:
                login();
                break;
            case R.id.register_btn:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
        }
    }
    public void login(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().login(mAdminEdit.getText().toString().trim(), mPwdEdit.getText().toString().trim(), new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        getSharedPreferences("userinfo",MODE_PRIVATE).edit().putString("username",mAdminEdit.getText().toString().trim()).commit();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));

                        Message msg = Message.obtain();
                        msg.what=0;
                        msg.obj="登录成功";
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onError(int i, String s) {
                        Message msg = Message.obtain();
                        msg.what=1;
                        msg.obj="登录失败"+"code:"+i+"-msg"+s;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
         handler.removeCallbacksAndMessages(this);
    }
}
