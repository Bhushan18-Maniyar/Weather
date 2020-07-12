package com.example.dell.weather;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static SQLiteHelper mSQLiteHelper;
    RelativeLayout myLayout ;
    ConstraintLayout constraintLayout;
    Button addBtn;
    TextView city , forcast, minmaxtemp, temp, day, date, humidity, windspeed, sunrise, sunset;

    SimpleArcLoader loader;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

                                                    /*        Variable Initialization      */

        constraintLayout = findViewById(R.id.constraintLayout);
        loader = findViewById(R.id.loader);
        addBtn = findViewById(R.id.addbtn);
        city = findViewById(R.id.city);
        myLayout = findViewById(R.id.mylayout);
        forcast = findViewById(R.id.forcast);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        windspeed = findViewById(R.id.windspeed);
        minmaxtemp = findViewById(R.id.minmaxtemp);
        temp = findViewById(R.id.temp);
        day = findViewById(R.id.day);
        humidity = findViewById(R.id.humidity);
        date = findViewById(R.id.date);


                                                /*        Date and background Operation      */

        final String[] weekday = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        String currentdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        date.setText(currentdate);

        Calendar c = Calendar.getInstance();
        Date d = new Date();
        d.getDate();
        c.setTime(d);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        day.setText(weekday[dayOfWeek-1]);

        final int[] backGround = {R.mipmap.daynew, R.mipmap.night123};

        Calendar current = Calendar.getInstance();
        int timeOfDay = current.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 4){
            myLayout.setBackgroundResource(backGround[1]);//night
            Toast.makeText(this, "Good Morning", Toast.LENGTH_SHORT).show();
        }else if(timeOfDay >= 4 && timeOfDay < 16){
            myLayout.setBackgroundResource(backGround[0]);//day
            Toast.makeText(this, "Good Afternoon", Toast.LENGTH_SHORT).show();
        }else if(timeOfDay >= 16 && timeOfDay < 20){
            myLayout.setBackgroundResource(backGround[0]);//day
            Toast.makeText(this, "Good Evening", Toast.LENGTH_SHORT).show();
        }else if(timeOfDay >= 20 && timeOfDay < 24){
            myLayout.setBackgroundResource(backGround[1]);
            Toast.makeText(this, "Good Night", Toast.LENGTH_SHORT).show();
        }


                            /*        DB Operation      */

        mSQLiteHelper = new SQLiteHelper(this,"RECORDDB.sqlite",null, 1);

        //        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS " +
        //       "RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR)");
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS " +
                "HomeCityRecord(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR)");


//        Toast.makeText(MainActivity.this,"To Cursor DB",Toast.LENGTH_LONG).show();
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM HomeCityRecord");
        if (!cursor.moveToNext()) {
//            Toast.makeText(MainActivity.this,"Null DB",Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, HomeTown.class));
        } else {
            loader.start();
//            Toast.makeText(MainActivity.this,"Not NULL DB",Toast.LENGTH_LONG).show();
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            homeTownData(name);
        }


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ManageCities.class));
            }
        });


    }

    private void homeTownData(final String homeTown) {

//        Toast.makeText(MainActivity.this,"homeTownData DB",Toast.LENGTH_LONG).show();
//        Toast.makeText(MainActivity.this,"search Data",Toast.LENGTH_LONG).show();
        String api = "https://api.openweathermap.org/data/2.5/weather?q="+homeTown+"&units=metric&appid=f514e820dd384c03aa31d42f15938443";

        StringRequest request = new StringRequest(Request.Method.GET, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(MainActivity.this,"onResponse",Toast.LENGTH_LONG).show();
                try {
//                    Toast.makeText(MainActivity.this,"Try",Toast.LENGTH_LONG).show();
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject jsonObject1= jsonArray.getJSONObject(0);
                    JSONObject sys = jsonObject.getJSONObject("sys");
                    JSONObject wind = jsonObject.getJSONObject("wind");
                    JSONObject main = jsonObject.getJSONObject("main");

                    windspeed.setText(" "+wind.getString("speed"));
                    forcast.setText(jsonObject1.getString("description"));
                    temp.setText(main.getString("temp")+"°c");
                    minmaxtemp.setText(main.getString("temp_min") + "°c / "+main.getString("temp_max")+"°c");
                    humidity.setText(" "+main.getString("humidity"));
                    city.setText(jsonObject.getString("name"));

                    Long rise = sys.getLong("sunrise");
                    String sunrisestring = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(rise * 1000));
                    Long set = sys.getLong("sunset");
                    String sunsetstring = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(set * 1000));

                    sunrise.setText(" "+sunrisestring);
                    sunset.setText(" "+sunsetstring);

                    constraintLayout.setVisibility(View.VISIBLE);
                    loader.stop();
                    loader.setVisibility(View.GONE);


                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}
