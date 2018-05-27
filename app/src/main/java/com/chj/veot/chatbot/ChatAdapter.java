package com.chj.veot.chatbot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.chj.veot.R;

public class ChatAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_USER = 1;
    private static final int VIEW_TYPE_MESSAGE_BOT = 2;
    private Context context;
    private ArrayList<ChatMessage> mChatList;

    public ChatAdapter(Context context, ArrayList<ChatMessage> ml) {
        this.context = context;
        this.mChatList = ml;
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = (ChatMessage) mChatList.get(position);
        if (message.isUser()) {
            return VIEW_TYPE_MESSAGE_USER;
        } else {
            return VIEW_TYPE_MESSAGE_BOT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_USER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_message_user, parent, false);
            return new UserMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_BOT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_message_bot, parent, false);
            return new BotMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = (ChatMessage) mChatList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_USER:
                ((UserMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_BOT:
                ((BotMessageHolder) holder).bind(message);
                break;
        }
    }

    private class UserMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        UserMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_user);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessage());
        }
    }

    private class BotMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        BotMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_bot);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessage());
        }
    }
}

