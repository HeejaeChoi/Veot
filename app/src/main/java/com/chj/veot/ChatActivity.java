package com.chj.veot;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;

import com.chj.veot.chatbot.ChatMessage;
import com.chj.veot.chatbot.ChatAdapter;
import com.chj.veot.chatbot.BotProcessor;


public class ChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mTextInput;
    private Button mSendButton;
    private RecyclerView mRecyclerView;
    private ChatAdapter mChatAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<ChatMessage> mMessageList;
    private BotProcessor mBotProcessor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Veot");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_32dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_chat);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(false);
        mRecyclerView.setLayoutManager(layoutManager);

        mMessageList = new ArrayList<ChatMessage>();
        mChatAdapter = new ChatAdapter(this, mMessageList);
        mRecyclerView.setAdapter(mChatAdapter);

        mBotProcessor = new BotProcessor(this);
        DefaultMessage("XSTART");
        DefaultMessage("XMENU");

        mTextInput = (EditText) findViewById(R.id.text_input_chat);
        mSendButton = (Button) findViewById(R.id.button_send_chat);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mTextInput.getText().toString();
                UserMessage(input);
                mTextInput.setText("");
            }
        });
    }

    public void DefaultMessage(String keyword) {
        mBotProcessor.request(keyword);
        Log.v("request default message", keyword);

        BotMessage(keyword);
    }

    public void UserMessage(String usertext) {      // bot에게 메세지 전달 + user input 화면에 띄우기
        ChatMessage message = new ChatMessage(usertext, true);
        mMessageList.add(message);
        mChatAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mMessageList.size() - 1);

        mBotProcessor.request(usertext);
        Log.v("send user message", usertext);

        BotMessage(usertext);
    }

    public void BotMessage(String usertext) {                  // bot response를 화면에 띄우기
        String response = mBotProcessor.response(usertext);
        ChatMessage message = new ChatMessage(response, false);
        mMessageList.add(message);
        mChatAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mMessageList.size() - 1);

        Log.v("request bot message", response);
    }
}
