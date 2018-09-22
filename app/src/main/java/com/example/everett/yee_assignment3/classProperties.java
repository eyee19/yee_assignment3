package com.example.everett.yee_assignment3;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.awt.font.TextAttribute;

public class classProperties extends AppCompatActivity {

    EditText tv;
    TextView classIndex;
    Button saveClass;
    String oldName;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_properties);

        tv = (EditText) findViewById(R.id.editClass);
        classIndex = (TextView) findViewById(R.id.classIndex);
        saveClass = (Button) findViewById(R.id.saveClass);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            tv.setText(bundle.getString("className"));
            classIndex.setText(bundle.getString("index"));
            oldName = tv.getText().toString();
        }

        saveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv.getText().toString().trim().length() <= 0) {
                    Toast.makeText(classProperties.this, "Class Name Empty", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent toHome = new Intent();
                    String newName = tv.getText().toString();
                    String returnedIndex = classIndex.getText().toString();

                    Log.d("classProperties", "RETURNED INDEX: " + returnedIndex);

                    toHome.putExtra("newName", newName);
                    toHome.putExtra("oldName", oldName);
                    toHome.putExtra("indexReturn", returnedIndex);

                    setResult(Activity.RESULT_OK, toHome);
                    finish();
                }
            }
        });
    }
}
