package com.chj.veot.chatbot;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.Graphmaster;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

import com.chj.veot.ChatActivity;


public class BotProcessor {
    String botName = "Veot";
    String action = "aiml2csv";
    public Bot bot;
    private Chat chat;

    String request;
    String[] response;

    Context context;


    public BotProcessor(Context context) {
        this.context = context;
        setBotProcessor();
    }

    public void request(String message) {
        request = message;
    }

    public String[] response(String usertext) {
        response = new String[2];
        response[0] = chat.multisentenceRespond(usertext);
        OOBProcessor oob = new OOBProcessor(response[0], context);
        if (request != null) {
            if (oob.checkOOB()) {
                response[0] = oob.OOBAction();
                response[1] = actionResponse(oob.OOBActionRequest());
                return response;
            }
            else {
                return response;
            }
        }
        return null;
    }

    public String actionResponse(String botText) {
        String bot = chat.multisentenceRespond(botText);
        return bot;
    }

    public void setBotProcessor() {
        AssetManager assets = context.getResources().getAssets();
        File jayDir = new File(context.getExternalFilesDir(null).getAbsolutePath() + "/bots/Veot");
        boolean dir_check = jayDir.mkdirs();
        Log.v("test", jayDir.getAbsolutePath());

        if (jayDir.exists()) {
            try {
                for (String dir : assets.list("Veot")) {
                    File subdir = new File(jayDir.getPath() + "/" + dir);
                    boolean subdir_check = subdir.mkdirs();
                    for (String file : assets.list("Veot/" + dir)) {
                        File f = new File(jayDir.getPath() + "/" + dir + "/" + file);
                        InputStream in = null;
                        OutputStream out = null;
                        in = assets.open("Veot/" + dir + "/" + file);
                        out = new FileOutputStream(jayDir.getPath() + "/" + dir + "/" + file);
                        copyFile(in, out);
                        Log.v("test", file);
                        in.close();
                        in = null;
                        out.flush();
                        out.close();
                        out = null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MagicStrings.root_path = context.getExternalFilesDir(null).getAbsolutePath();
        System.out.println("Working Directory = " + MagicStrings.root_path);
        AIMLProcessor.extension = new PCAIMLProcessorExtension();

        this.bot = new Bot(botName, MagicStrings.root_path, action);
        this.chat = new Chat(bot);

        MagicBooleans.trace_mode = false;
        System.out.println("trace mode = " + MagicBooleans.trace_mode);
        Graphmaster.enableShortCuts = true;
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        Log.v("test", "copy file");
    }
}

