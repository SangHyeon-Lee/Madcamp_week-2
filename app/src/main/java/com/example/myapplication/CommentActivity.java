package com.example.myapplication;

import android.content.Intent;
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
import java.util.Date;

public class CommentActivity extends AppCompatActivity {
    Intent i;
    TextView name;
    TextView post;
    ImageView imageView;
    Button button;
    String posted;
    EditText comment;
    String user_name;
    String commented;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        i = getIntent();
        name = (TextView)findViewById(R.id.name);
        post = (TextView)findViewById(R.id.post22);
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.basic);
        comment = (EditText)findViewById(R.id.editText11);
        commented = comment.getText().toString();
        user_name = i.getStringExtra("user_name");
        posted = i.getStringExtra("post");
        Log.i("comment_activity",user_name);
        Log.i("comment_activity",posted);

        name.setText(user_name);
       post.setText(posted);
    }

    public void onClick(View v){
        
        finish();
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        //time column을 위해 만듦
        // 현재시간을 msec 으로 구한다.
        //long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        //Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        //SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        //String formatDate = sdfNow.format(date);
        //String formatDate = date.toString();


        @Override
        protected String doInBackground(String... urls) {
            try {

                HttpURLConnection con = null;
                BufferedReader reader = null;

                //JSONObject를 만들고 key-value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("name", user_name);
                jsonObject.accumulate("time", "1");
                jsonObject.accumulate("post", post);
                jsonObject.accumulate("comment",commented);
                Log.i("comment", commented);
                //Log.i("info_PostActivity", user_name);


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

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "서버로 부터 값을 받습니다: ";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    Log.i("info_MainActivity190", buffer.toString());
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
            //Log.i("info_server","__"+result);//서버로 부터 받은 값을 출력해주는 부
        }
    }
}
