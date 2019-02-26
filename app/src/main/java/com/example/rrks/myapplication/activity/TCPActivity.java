package com.example.rrks.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.rrks.myapplication.R;
import com.example.rrks.myapplication.service.TCPServerService;
import com.example.rrks.myapplication.utils.MyUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TCPActivity extends AppCompatActivity {

    private static final int MESSAGE_SOCKET_CONNECTED = 1;
    private static final int MESSAGE_RECEIVE_NEW_MSG = 2;
    @BindView(R.id.edit)
    AppCompatEditText edit;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.btn_send)
    AppCompatButton btnSend;

    private Socket mClientSocket;
    private PrintWriter mPrintWriter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_RECEIVE_NEW_MSG:
                    text.setText(text.getText() + (String) msg.obj);
                    break;
                case MESSAGE_SOCKET_CONNECTED:
                    btnSend.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        btnSend.setEnabled(false);
        Intent intentService = new Intent(this, TCPServerService.class);
        startService(intentService);
        new Thread() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                connectTCPServer();

            }
        }.start();

    }

    @OnClick(R.id.btn_send)
    public void onViewClicked() {
        final String msg = edit.getText().toString();
        if (!TextUtils.isEmpty(msg) && mPrintWriter != null) {
            Observable.just(msg)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Function<String, String>() {
                        @Override
                        public String apply(String s) throws Exception {
                            mPrintWriter.println(s);
                            return s;
                        }
                    }).subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(String s) {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });

            edit.setText("");
            String time = formatDataTime(System.currentTimeMillis());
            final String showedMsg = "YK" + time + ":" + msg + "\n";
            text.setText(text.getText() + showedMsg);
        }
    }


    private void connectTCPServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", 8688);
                mClientSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
            } catch (IOException e) {
                SystemClock.sleep(1000);
                System.out.println("====" + e.toString());
                System.out.println("====connect tcp server failed, retry...");
            }
        }
        try {
            //接收服务器端的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!TCPActivity.this.isFinishing()) {
                String msg = br.readLine();
                System.out.println("====receive :" + msg);
                if (msg != null) {
                    String time = formatDataTime(System.currentTimeMillis());
                    final String showedMsg = "服务器" + time + ":" + msg + "\n";
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showedMsg).sendToTarget();
                }
            }
            System.out.println("====quit...");
            MyUtils.close(mPrintWriter);
            MyUtils.close(br);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String formatDataTime(long time) {
        return new SimpleDateFormat("(HH:mm:ss)").format(new Date(time));
    }

    @Override
    protected void onDestroy() {
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
