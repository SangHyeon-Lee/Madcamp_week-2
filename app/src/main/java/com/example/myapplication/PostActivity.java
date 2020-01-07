package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class PostActivity extends AppCompatActivity {
    EditText textView;
    TextView name;
    ImageView imgView;
    Button button;

    String post;
    String user_name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        textView = (EditText)findViewById(R.id.textView);
        name = (TextView)findViewById(R.id.name);
        imgView = (ImageView)findViewById(R.id.imageView);
        button = (Button)findViewById(R.id.button);
        user_name = name.getText().toString();
        //post = textView.getText().toString();
        imgView.setImageResource(R.drawable.basic);
    }

    public void onclick(View v){
        post = textView.getText().toString();

        try {
            String response = new JSONTask().execute("http://192.249.19.253:3680/").get();
            //Log.i("response", response);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        finish();
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        //time column을 위해 만듦
        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMddHHmmss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(date);
        //String formatDate = date.toString();



        @Override
        protected String doInBackground(String... urls) {
            try {

                HttpURLConnection con = null;
                BufferedReader reader = null;

                //JSONObject를 만들고 key-value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("name", user_name);
                jsonObject.accumulate("time", formatDate);
                jsonObject.accumulate("post", post);

                Log.i("info_PostActivity", user_name);


                try{
                    //URL url = new URL("http://192.249.19.253:3680/");
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    Log.i("info_PostActivity", "URL접속");

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    Log.i("info_PostActivity", "con.connect()전");

                    con.connect();

                    Log.i("info_PostActivity", "con.connect()후");


                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 닫아줌
                    Log.i("info_PostActivity","write(jsonObject) 했음");

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "서버로 부터 값을 받습니다: ";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    Log.i("info_PostActivity132", buffer.toString());
                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 (아마 OK!!가 들어올것임)

                } catch (MalformedURLException e){
                    Log.i("info_PostActivity", "에러1");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("info_PostActivity", "에러2");
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        Log.i("info_PostActivity", "에러3");
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                Log.i("info_PostActivity", "에러4");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("info_server","__"+result);//서버로 부터 받은 값을 출력해주는 부
        }
    }


}
