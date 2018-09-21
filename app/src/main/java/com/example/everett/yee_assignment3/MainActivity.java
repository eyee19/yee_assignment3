package com.example.everett.yee_assignment3;

//Resources:
//Header above ListView: https://stackoverflow.com/questions/18368010/listview-with-title
//Toast alert dialog message: https://stackoverflow.com/questions/35827559/how-to-toast-a-message-if-edittext-is-empty-by-clicking-button
//Using camera: https://developer.android.com/training/camera/photobasics#java
//Android documentation

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView classList;
    Button addClass;
    String[] ListElements = new String[] {};
    final List<String> ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        classList = (ListView) findViewById(R.id.classList1);
        addClass = (Button) findViewById(R.id.addClass1);

        TextView textView = new TextView(MainActivity.this);
        textView.setText("Class List");
        textView.setTextSize(20);

        classList.addHeaderView(textView);

        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, addClass.class);
                startActivityForResult(i,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (MainActivity.this, android.R.layout.simple_list_item_1, ListElementsArrayList);

        classList.setAdapter(adapter);

        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                //Log.d("MainActivity","I got here");
                String classNameReturn = data.getStringExtra("classNameReturn");
                String classNumberReturn = data.getStringExtra("classNumberReturn");
                //Log.d("MainActivity", "Names returned successfully");

                ListElementsArrayList.add(classNameReturn + " " + classNumberReturn);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
