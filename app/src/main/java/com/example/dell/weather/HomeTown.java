package com.example.dell.weather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeTown extends AppCompatActivity {

    EditText cityname;
    Button addbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_town);

        cityname = findViewById(R.id.cityname);
        addbtn = findViewById(R.id.addbtn);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityname.getText().toString().equals("")){
                    Toast.makeText(HomeTown.this,"Enter City Name",Toast.LENGTH_LONG).show();
                } else {
                    MainActivity.mSQLiteHelper.insertData(cityname.getText().toString().trim());
                    startActivity(new Intent(HomeTown.this,MainActivity.class));
                    Toast.makeText(HomeTown.this,"Inserted",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
