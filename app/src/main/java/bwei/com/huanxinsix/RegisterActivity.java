package bwei.com.huanxinsix;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.admin_edits)
    EditText mAdminEdit;
    @BindView(R.id.pwd_edits)
    EditText mPwdEdit;
    @BindView(R.id.register_btns)
    Button mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_btns)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.register_btns:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().createAccount(
                                    mAdminEdit.getText().toString().trim(),
                                    mPwdEdit.getText().toString().trim());
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            Log.i("lf","注册失败  "+e.getErrorCode()+" , "+e.getMessage());
                        }
                    }
                }).start();
                break;
        }
    }

}

