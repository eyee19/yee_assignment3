package com.example.everett.yee_assignment3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class addStudent extends AppCompatActivity {

    ImageView picture;
    Button addPicture;
    EditText firstName;
    EditText lastName;
    EditText studentID;
    Button saveStudent;
    RelativeLayout addStudentParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        picture = (ImageView) findViewById(R.id.picture);
        addPicture = (Button) findViewById(R.id.addPicture);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        studentID = (EditText) findViewById(R.id.studentID);
        saveStudent = (Button) findViewById(R.id.saveStudent);

        findViewById(R.id.addStudentParent).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

        saveStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstName.getText().toString().trim().length() <= 0 ||
                        lastName.getText().toString().trim().length() <= 0 ||
                        studentID.getText().toString().trim().length() <= 0) {
                    Toast.makeText(addStudent.this, "Student Name/ID Empty", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent toClass = new Intent();
                    String firstNameReturn = firstName.getText().toString();
                    String lastNameReturn = lastName.getText().toString();

                    toClass.putExtra("firstNameReturn", firstNameReturn);
                    toClass.putExtra("lastNameReturn", lastNameReturn);

                    setResult(Activity.RESULT_OK, toClass);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            picture.setImageBitmap(imageBitmap);
        }
    }
}
