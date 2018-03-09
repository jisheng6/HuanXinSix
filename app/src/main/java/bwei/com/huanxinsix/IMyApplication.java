package bwei.com.huanxinsix;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

/**
 * Created by ShiLei on 2018/3/3.
 */

public class IMyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(true);
        EMClient.getInstance().init(this,options);
    }
}
