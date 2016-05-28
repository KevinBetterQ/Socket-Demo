package com.kevin.socketclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
//如果某个类 后面使用 implements，并指定了相应的接口，那在该类下面就需要实现相应接口的方法。
    EditText ip;
    EditText editText;
    TextView text;
    Button connect;
    Button send;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = (EditText) findViewById(R.id.ip);
        editText = (EditText) findViewById(R.id.edit);
        text = (TextView) findViewById(R.id.text);
        connect = (Button) findViewById(R.id.connect);
        send = (Button) findViewById(R.id.send);
        connect.setOnClickListener(this);
        send.setOnClickListener(this);

        /*connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.connect:
                connect();
                break;
            case R.id.send:
                send();
                break;
            default:
                break;
        }
    }

    Socket socket = null;
    BufferedWriter writer = null;
    BufferedReader reader = null;

    private void send() {
        text.append("speak:"+editText.getText().toString()+"\n");
        try {
            writer.write(editText.getText().toString()+"\n");
            writer.flush();// 刷新该流的缓冲
        } catch (IOException e) {
            e.printStackTrace();
        }

        editText.setText("");
    }

    String ipString ;

    private void connect() {
        ipString = ip.getText().toString();
        AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    socket = new Socket(ipString, 6666);
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    publishProgress("connect success");
                } catch (UnknownHostException e1) {
                    Toast.makeText(MainActivity.this, "连接错误", Toast.LENGTH_SHORT).show();
                } catch (IOException e1) {
                    Toast.makeText(MainActivity.this, "连接错误", Toast.LENGTH_SHORT).show();
                }
                try {
                    String line;
                    while ((line = reader.readLine())!= null) {
                        publishProgress(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if (values[0].equals("connect success")) {
                    Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();

                }
                text.append(values[0]+"\n");
                super.onProgressUpdate(values);
            }
        };
        read.execute();
    }
}
