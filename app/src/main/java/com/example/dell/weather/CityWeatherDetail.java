package com.example.dell.weather;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CityWeatherDetail extends AppCompatActivity {

    TextView city , forcast, minmaxtemp, temp, day, date, humidity, windspeed, sunrise, sunset;
    ConstraintLayout myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather_detail);

        getSupportActionBar().hide();

                                                    /*        Variable Initialization      */
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

        Intent i =  getIntent();
        int position = i.getIntExtra("position",0);


        city.setText(ManageCities.mList.get(position).getCityname());
        forcast.setText(ManageCities.mList.get(position).getForcast());
        sunrise.setText(ManageCities.mList.get(position).getSunrise());
        sunset.setText(ManageCities.mList.get(position).getSunset());
        windspeed.setText(ManageCities.mList.get(position).getWindSpeed());
        minmaxtemp.setText(ManageCities.mList.get(position).getLowTemp()+"°c / "+ManageCities.mList.get(position).getHighTemp()+"°c");
        temp.setText(ManageCities.mList.get(position).getTemp()+"°c");
        humidity.setText(ManageCities.mList.get(position).getHumidity());

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
//            Toast.makeText(this, "Good Morning", Toast.LENGTH_SHORT).show();
        }else if(timeOfDay >= 4 && timeOfDay < 16){
            myLayout.setBackgroundResource(backGround[0]);//day
//            Toast.makeText(this, "Good Afternoon", Toast.LENGTH_SHORT).show();
        }else if(timeOfDay >= 16 && timeOfDay < 20){
            myLayout.setBackgroundResource(backGround[0]);//day
//            Toast.makeText(this, "Good Evening", Toast.LENGTH_SHORT).show();
        }else if(timeOfDay >= 20 && timeOfDay < 24){
            myLayout.setBackgroundResource(backGround[1]);
//            Toast.makeText(this, "Good Night", Toast.LENGTH_SHORT).show();
        }
    }
}
