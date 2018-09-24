package com.example.everett.yee_assignment3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class addClass extends AppCompatActivity {

    EditText className;
    EditText classNumber;
    ListView studentList;
    Button addStudent;
    Button save;
    RelativeLayout addClassParent;
    String[] studentListElements = new String[] {};
    final ArrayList<String> studentListElementsArrayList = new ArrayList<String>(Arrays.asList(studentListElements));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        className = (EditText) findViewById(R.id.className);
        classNumber = (EditText) findViewById(R.id.classNumber);
        studentList = (ListView) findViewById(R.id.studentList);
        addStudent = (Button) findViewById(R.id.addStudent);
        save = (Button) findViewById(R.id.saveButton);

        TextView textView = new TextView(addClass.this);
        textView.setText("Student List");
        textView.setTextSize(20);

        studentList.addHeaderView(textView, null, false);

        findViewById(R.id.addClassParent).setOnTouchListener(new View.OnTouchListener() { //Makes keyboard disappear when user clicks outside of text boxes
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(addClass.this, addStudent.class);
                startActivityForResult(i,1);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (className.getText().toString().trim().length() <= 0 || classNumber.getText().toString().trim().length() <= 0) {
                    Toast.makeText(addClass.this, "Class Name/Number Empty", Toast.LENGTH_LONG).show(); //If input is empty
                }
                else {
                    Intent backHome = new Intent();
                    String classNameReturn = className.getText().toString();
                    String classNumberReturn = classNumber.getText().toString();
                    int count = studentList.getAdapter().getCount();
                    String studentCount = Integer.toString(count);

                    backHome.putExtra("classNameReturn", classNameReturn);
                    backHome.putExtra("classNumberReturn", classNumberReturn);
                    backHome.putExtra("studentCount", studentCount);
                    //backHome.putStringArrayListExtra("studentList", studentListElementsArrayList);
                    //Log.d("MainActivity", "RETURNED VALUE: " + backHome.putStringArrayListExtra("studentList", studentListElementsArrayList));
                    //Log.d("addClass", "STUDENT DATA TEST: " + studentList.getAdapter().getItem(1));

                    setResult(Activity.RESULT_OK, backHome);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        final ArrayAdapter<String> classAdapter = new ArrayAdapter<String>
                (addClass.this, android.R.layout.simple_list_item_1, studentListElementsArrayList);

        studentList.setAdapter(classAdapter);

        if(requestCode == 1) { //Displays the student in the list
            if(resultCode == Activity.RESULT_OK) {
                String firstNameReturn = data.getStringExtra("firstNameReturn");
                String lastNameReturn = data.getStringExtra("lastNameReturn");
                String studentIDReturn = data.getStringExtra("studentIDReturn");
                String completeInfo = firstNameReturn + " " + lastNameReturn + " | ID #: " + studentIDReturn;
                //Log.d("addClass", "RETURNED STRING: " + completeInfo);

                studentListElementsArrayList.add(firstNameReturn + " " + lastNameReturn + " | ID #: " + studentIDReturn);
                classAdapter.notifyDataSetChanged();

                studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Click on a student to delete them
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                addClass.this);

                        alertDialogBuilder
                                .setMessage("Delete student?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        //Object index = studentListElementsArrayList.getAdapter().getItem(position);
                                        studentListElementsArrayList.remove(position-1);
                                        classAdapter.notifyDataSetChanged();
                                        //finish();
                                    }
                                })
                                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });
            }
        }
    }
}
