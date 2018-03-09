package bwei.com.huanxinsix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import bwei.com.huanxinsix.fragment.PersonFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.frame_layout)
    FrameLayout mFrameLayout;
    @BindView(R.id.chat_lists)
    TextView mChatList;
    @BindView(R.id.contact_list)
    TextView mContactList;
    @BindView(R.id.person_center)
    TextView mPersonCenter;
    @BindView(R.id.linear)
    LinearLayout mLinear;
    private EaseConversationListFragment conversationListFragment;
    private EaseContactListFragment easeContactListFragment;
    private PersonFragment personFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                new Thread() {//需要在子线程中调用
                    @Override
                    public void run() {
                        //需要设置联系人列表才能启动fragment
                        easeContactListFragment.setContactsMap(getContact());
                        easeContactListFragment.refresh();
                    }
                }.start();
            }

            @Override
            public void onContactDeleted(String s) {

            }

            @Override
            public void onContactInvited(String s, String s1) {

            }

            @Override
            public void onFriendRequestAccepted(String s) {

            }

            @Override
            public void onFriendRequestDeclined(String s) {

            }
        });
        easeContactListFragment = new EaseContactListFragment();
        new Thread() {//需要在子线程中调用
            @Override
            public void run() {
                //需要设置联系人列表才能启动fragment
                easeContactListFragment.setContactsMap(getContact());
            }
        }.start();
        easeContactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                startActivity(new Intent(MainActivity.this,ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID,user.getUsername()));
            }
        });
        personFragment = new PersonFragment();

        conversationListFragment = new EaseConversationListFragment();
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                //进入聊天页面
                startActivity(new Intent(MainActivity.this,ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, conversationListFragment).commit();
    }


    @OnClick({R.id.chat_lists, R.id.contact_list, R.id.person_center})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.chat_lists:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,conversationListFragment).commit();

                break;
            case R.id.contact_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,easeContactListFragment).commit();

                break;
            case R.id.person_center:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,personFragment).commit();
                break;
        }
    }

    public Map<String,EaseUser> getContact() {
               Map<String,EaseUser> map = new HashMap<>();
        try {
            List<String> allContactsFromServer = EMClient.getInstance().contactManager().getAllContactsFromServer();
            for (String user : allContactsFromServer){
                map.put(user,new EaseUser(user));
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return map;
    }
}
