package com.example.everett.yee_assignment3;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.content.DialogInterface;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class classProperties extends AppCompatActivity {

    EditText tv;
    TextView classIndex;
    Button saveClass;
    Button deleteClass;
    Button addStudent;
    String oldName;
    String count;
    ListView studentListEdit;
    String[] studentListElements = new String[] {};
    final ArrayList<String> studentListElementsArrayList = new ArrayList<String>(Arrays.asList(studentListElements));
    RelativeLayout classPropertiesParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_properties);

        tv = (EditText) findViewById(R.id.editClass);
        classIndex = (TextView) findViewById(R.id.classIndex);
        saveClass = (Button) findViewById(R.id.saveClass);
        deleteClass = (Button) findViewById(R.id.deleteClass);
        addStudent = (Button) findViewById(R.id.addStudentEdit);
        studentListEdit = (ListView) findViewById(R.id.studentListEdit);

        TextView textView = new TextView(classProperties.this);
        textView.setText("Student List");
        textView.setTextSize(20);

        studentListEdit.addHeaderView(textView, null, false);

        findViewById(R.id.classPropertiesParent).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            tv.setText(bundle.getString("className"));
            classIndex.setText(bundle.getString("index"));
            oldName = tv.getText().toString();
            count = bundle.getString("count");

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
                    String delete = "noDelete";

                    //Log.d("classProperties", "RETURNED INDEX: " + returnedIndex);

                    toHome.putExtra("newName", newName);
                    toHome.putExtra("oldName", oldName);
                    toHome.putExtra("indexReturn", returnedIndex);
                    toHome.putExtra("countReturn", count);
                    toHome.putExtra("delete", delete);

                    setResult(Activity.RESULT_OK, toHome);
                    finish();
                }
            }
        });

        deleteClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        classProperties.this);

                alertDialogBuilder
                        .setMessage("Are you sure you want to delete this class?") //Verifies first
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Intent deleteToHome = new Intent();
                                String returnedIndex = classIndex.getText().toString();
                                String delete = "delete";

                                deleteToHome.putExtra("indexReturn", returnedIndex);
                                deleteToHome.putExtra("delete", delete);

                                setResult(Activity.RESULT_OK, deleteToHome);
                                finish();
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

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(classProperties.this, addStudent.class);
                startActivityForResult(i,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        final ArrayAdapter<String> classAdapter = new ArrayAdapter<String>
                (classProperties.this, android.R.layout.simple_list_item_1, studentListElementsArrayList);

        studentListEdit.setAdapter(classAdapter);

        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                String firstNameReturn = data.getStringExtra("firstNameReturn");
                String lastNameReturn = data.getStringExtra("lastNameReturn");
                String studentIDReturn = data.getStringExtra("studentIDReturn");

                studentListElementsArrayList.add(firstNameReturn + " " + lastNameReturn + " | ID #: " + studentIDReturn);
                classAdapter.notifyDataSetChanged();

                studentListEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                classProperties.this);

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
