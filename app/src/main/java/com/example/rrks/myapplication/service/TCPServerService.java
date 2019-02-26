package com.example.rrks.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.rrks.myapplication.utils.MyUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPServerService extends Service {
    private boolean mIsServiceDestroyed = false;
    private String[] mDefinedMessages = new String[]{
            "来了，老弟",
            "爱滴魔力转圈圈,想你想到心花怒放黑夜白天",
            "回首,掏!吆!鬼刀一开看不见,走位,走位",
            "好嗨哟 感觉人生已经到达了高潮",
            "..........."
    };

    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed = true;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class TcpServer implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                //监听本地8688端口
                serverSocket = new ServerSocket(8688);
//                System.out.println("=======监听本地8688端口");
            } catch (IOException e) {
                System.out.println("====establish tcp server falied,port:8688");
                e.printStackTrace();
                return;
            }

            while (!mIsServiceDestroyed) {
                try {
//                    System.out.println("===============");
                    //接受客户端请求
                    final Socket client = serverSocket.accept();
                    System.out.println("====accept");
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
//                    System.out.println("=============="+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseClient(Socket client) throws IOException {
        //用于接受客户端消息
        BufferedReader in = new BufferedReader(new InputStreamReader(
                client.getInputStream()));
        //用于向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                client.getOutputStream())), true);
        out.println("欢迎来到聊天室");
        while (!mIsServiceDestroyed) {
            String str = in.readLine();
            System.out.println("====msg from client:" + str);
            if (str == null) {
                //客户端断开连接
                break;
            }
            int i = new Random().nextInt(mDefinedMessages.length);
            String msg = mDefinedMessages[i];
            out.println(msg);
            System.out.println("====send :" + msg);
        }
        System.out.println("====client quit");
        //关闭流
        MyUtils.close(out);
        MyUtils.close(in);
        client.close();
    }

}
