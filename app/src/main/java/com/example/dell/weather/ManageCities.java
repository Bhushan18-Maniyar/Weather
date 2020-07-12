package com.example.dell.weather;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.dell.weather.MainActivity.mSQLiteHelper;

public class ManageCities extends AppCompatActivity {

    EditText search;
    ListView listofcity;
    RelativeLayout listlayout;
    LinearLayout showList;
    SimpleArcLoader loader;
    RecordListAdapter recordListAdapter;

    //    ArrayList<Model> mList;
    public static ArrayList<Model> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_cities);

        getSupportActionBar().hide();

        listlayout = findViewById(R.id.listlayout);
        showList = findViewById(R.id.showList);
        loader = findViewById(R.id.loader);
        showList.setVisibility(View.GONE);

        Calendar current = Calendar.getInstance();
        int timeOfDay = current.get(Calendar.HOUR_OF_DAY);

        final int[] backGround = {R.mipmap.daynew, R.mipmap.night123};


        if (timeOfDay >= 0 && timeOfDay < 4) {
            listlayout.setBackgroundResource(backGround[1]);//night
//            Toast.makeText(this, "Good Morning", Toast.LENGTH_SHORT).show();
        } else if (timeOfDay >= 4 && timeOfDay < 16) {
            listlayout.setBackgroundResource(backGround[0]);//day
//            Toast.makeText(this, "Good Afternoon", Toast.LENGTH_SHORT).show();
        } else if (timeOfDay >= 16 && timeOfDay < 20) {
            listlayout.setBackgroundResource(backGround[0]);//day
//            Toast.makeText(this, "Good Evening", Toast.LENGTH_SHORT).show();
        } else if (timeOfDay >= 20 && timeOfDay < 24) {
            listlayout.setBackgroundResource(backGround[1]);
//            Toast.makeText(this, "Good Night", Toast.LENGTH_SHORT).show();
        }

        search = findViewById(R.id.search);
        listofcity = findViewById(R.id.listofcity);

        loader.start();

        mList = new ArrayList<>();

        mSQLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);

        //        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS " +
        //       "RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR)");
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS " +
                "HomeCityRecord(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR)");

        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String s = String.valueOf(search.getText());
                            if (!s.trim().equals("")) {
                                searchData();

                            } else {
                                Toast.makeText(ManageCities.this, "Enter City Name", Toast.LENGTH_SHORT).show();
                            }
                        default:
                            break;
                    }
                }
                return false;
            }
        });


        loader.stop();
        loader.setVisibility(View.GONE);
        showList.setVisibility(View.VISIBLE);

        listofcity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(ManageCities.this, CityWeatherDetail.class).putExtra("position", position));
            }
        });



        /* * ** ** ** ** ** ** ** ** **  *
        * getting list of cities from DB *
        * * ** ** ** ** ** ** ** ** ** * */
        getList();




                                                                    /*/     To Delete Data       /*/

        listofcity.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(ManageCities.this).setTitle("Warning...").setMessage("Are you sure want to delete").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor c = mSQLiteHelper.getData("SELECT id FROM HomeCityRecord");
                        List<Integer> l = new ArrayList<>();
                        while (c.moveToNext()) {
                            l.add(c.getInt(0));
                        }
                        mSQLiteHelper.deleteData(l.get(position));
                        Toast.makeText(ManageCities.this, "City Removed", Toast.LENGTH_SHORT).show();
                        getList();
                    }
                }).setNegativeButton("Cancel", null).setCancelable(false).show();

                return true;
            }
        });


    }

    private void updateList() {
        Toast.makeText(ManageCities.this, "Update function", Toast.LENGTH_SHORT).show();
        recordListAdapter = new RecordListAdapter(ManageCities.this, mList);
        recordListAdapter.notifyDataSetChanged();
        listofcity.setAdapter(recordListAdapter);

    }

    private void getList() {

        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM HomeCityRecord");
        mList.clear();

            /*      for fetching list of cities from db     *
            *       through while loop.................     */

        while (cursor.moveToNext()) {

            String city = cursor.getString(1);
            String api = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=f514e820dd384c03aa31d42f15938443";

            final StringRequest request = new StringRequest(Request.Method.GET, api, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {

                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("weather");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        JSONObject sys = jsonObject.getJSONObject("sys");
                        JSONObject wind = jsonObject.getJSONObject("wind");
                        JSONObject main = jsonObject.getJSONObject("main");

                        String windspeed = wind.getString("speed");
                        String forcast = jsonObject1.getString("description");
                        String temp = main.getString("temp");
                        String tempMin = main.getString("temp_min");
                        String tempMax = main.getString("temp_max");
                        String humidity = main.getString("humidity");
                        String city = jsonObject.getString("name");


                        Long rise = sys.getLong("sunrise");
                        String sunrisestring = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(rise * 1000));
                        Long set = sys.getLong("sunset");
                        String sunsetstring = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(set * 1000));

                        Model model = new Model(city, temp, forcast, tempMin, tempMax, windspeed, humidity, sunrisestring, sunsetstring);
                        mList.add(model);

                        recordListAdapter = new RecordListAdapter(ManageCities.this, mList);
                        listofcity.setAdapter(recordListAdapter);


                    } catch (Exception e) {
                        Toast.makeText(ManageCities.this, " ERROR ", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ManageCities.this, "Error... Enter Valid City Name...!!", Toast.LENGTH_SHORT).show();

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);


        }
    }

    private synchronized void searchData() {
//        Toast.makeText(ManageCities.this,"searchData()",Toast.LENGTH_SHORT).show();

//        Toast.makeText(MainActivity.this,"search Data",Toast.LENGTH_LONG).show();
        String api = "https://api.openweathermap.org/data/2.5/weather?q=" + search.getText() + "&units=metric&appid=f514e820dd384c03aa31d42f15938443";

        final StringRequest request = new StringRequest(Request.Method.GET, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(MainActivity.this,"onResponse",Toast.LENGTH_LONG).show();
                try {

                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONObject sys = jsonObject.getJSONObject("sys");
                    JSONObject wind = jsonObject.getJSONObject("wind");
                    JSONObject main = jsonObject.getJSONObject("main");

                    jsonObject.getString("name");
                    wind.getString("speed");
                    jsonObject1.getString("description");
                    main.getString("temp");
                    main.getString("temp_min");
                    main.getString("temp_max");
                    main.getString("humidity");
//                    Toast.makeText(ManageCities.this,"try ...!!",Toast.LENGTH_SHORT).show();

                    mSQLiteHelper.insertData(search.getText().toString().trim());
                    Toast.makeText(ManageCities.this, "Record Inserted..", Toast.LENGTH_SHORT).show();
                    getList();
                } catch (Exception e) {
                    Toast.makeText(ManageCities.this, "Enter Valid City Name...!!", Toast.LENGTH_SHORT).show();
//                    falseFunction();
//                  Toast.makeText(ManageCities.this," Enter Valid City Name ",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ManageCities.this, "Error... Enter Valid City Name...!!", Toast.LENGTH_SHORT).show();
//                Toast.makeText(ManageCities.this,"Error ...!!",Toast.LENGTH_SHORT).show();

//                Toast.makeText(ManageCities.this," Enter Valid City Name ",Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    // this function can be used but we fetch data in while loop in getList() function
    private synchronized void getData(String city) {
//        Toast.makeText(ManageCities.this,"searchData()",Toast.LENGTH_SHORT).show();

        String api = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=f514e820dd384c03aa31d42f15938443";

        final StringRequest request = new StringRequest(Request.Method.GET, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(MainActivity.this,"onResponse",Toast.LENGTH_LONG).show();
                try {

                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONObject sys = jsonObject.getJSONObject("sys");
                    JSONObject wind = jsonObject.getJSONObject("wind");
                    JSONObject main = jsonObject.getJSONObject("main");

                    String windspeed = wind.getString("speed");
                    String forcast = jsonObject1.getString("description");
                    String temp = main.getString("temp");
                    String tempMin = main.getString("temp_min");
                    String tempMax = main.getString("temp_max");
                    String humidity = main.getString("humidity");
                    String city = jsonObject.getString("name");


                    Long rise = sys.getLong("sunrise");
                    String sunrisestring = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(rise * 1000));
                    Long set = sys.getLong("sunset");
                    String sunsetstring = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(set * 1000));

                    Model model = new Model(city, temp, forcast, tempMin, tempMax, windspeed, humidity, sunrisestring, sunsetstring);
                    mList.add(model);
//                    RecordListAdapter recordListAdapter = new RecordListAdapter(ManageCities.this,mList);
//                    listofcity.setAdapter(recordListAdapter);
                    return;

                } catch (Exception e) {
//                  Toast.makeText(ManageCities.this," Enter Valid City Name ",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ManageCities.this, "Error... Enter Valid City Name...!!", Toast.LENGTH_SHORT).show();
//                Toast.makeText(ManageCities.this,"Error ...!!",Toast.LENGTH_SHORT).show();

//                Toast.makeText(ManageCities.this," Enter Valid City Name ",Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

}
