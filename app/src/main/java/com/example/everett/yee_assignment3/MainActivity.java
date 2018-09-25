package com.example.everett.yee_assignment3;

//Resources:
//Header above ListView: https://stackoverflow.com/questions/18368010/listview-with-title
//Toast alert dialog message: https://stackoverflow.com/questions/35827559/how-to-toast-a-message-if-edittext-is-empty-by-clicking-button
//Using camera: https://developer.android.com/training/camera/photobasics#java
//Alert dialog: https://www.mkyong.com/android/android-alert-dialog-example/
//Android documentation

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ListView classList;
    Button addClass;
    String[] ListElements = new String[] {};
    final ArrayList<String> ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));
    public static final String PREFS_NAME = "PreferencesFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean dialogShown = settings.getBoolean("dialogShown", false);

        if (!dialogShown) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

            alertDialogBuilder
                    .setMessage("Welcome to the class manager app! \n\nKnown issues: \n" +
                            "- List of students is not persistent \n- Student picture is rotated incorrectly \n\n" +
                            "Notes: \n- Click on a class to edit it \n\n**This dialog will only appear on first startup**")
                    .setCancelable(false)
                    .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            //dialog.dismiss();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("dialogShown", true);
            editor.commit();
        }

        setContentView(R.layout.activity_main);

        classList = (ListView) findViewById(R.id.classList1);
        addClass = (Button) findViewById(R.id.addClass1);

        TextView textView = new TextView(MainActivity.this);
        textView.setText("Class List");
        textView.setTextSize(20);

        classList.addHeaderView(textView, null, false);

        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, addClass.class);
                startActivityForResult(i,1);
            }
        });

        classList.setOnItemClickListener(new AdapterView.OnItemClickListener() { //When a class is clicked open the edit class activity
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentString = classList.getItemAtPosition(position).toString();
                String [] separated = currentString.split(Pattern.quote("|"));
                Intent i = new Intent(MainActivity.this, classProperties.class);
                i.putExtra("className", separated[0]);
                i.putExtra("count", separated[1]);
                String newPosition = Integer.toString(position);
                i.putExtra("index", newPosition);
                startActivityForResult(i, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (MainActivity.this, android.R.layout.simple_list_item_1, ListElementsArrayList);
        classList.setAdapter(adapter);

        if(requestCode == 1) { //Displaying the class name and number in the main screen list
            if(resultCode == Activity.RESULT_OK) {
                String classNameReturn = data.getStringExtra("classNameReturn");
                String classNumberReturn = data.getStringExtra("classNumberReturn");
                String studentCount = data.getStringExtra("studentCount");
                int adjusted = Integer.parseInt(studentCount);
                int adjustedCount = adjusted - 1;
                //ArrayList<String> studentListReturn = getIntent().getExtras().getStringArrayList("studentList");
                //Log.d("MainActivity", "STUDENT LIST TEST: " + studentListReturn);

                ListElementsArrayList.add(classNameReturn + " " + classNumberReturn + " | " + "Student Count: " + adjustedCount);
                adapter.notifyDataSetChanged();
            }
        }

        if(requestCode == 2) { //Result from class properties screen, verifying whether or not to delete a class
            if(resultCode == Activity.RESULT_OK) {
                String newName = data.getStringExtra("newName");
                String indexReturn = data.getStringExtra("indexReturn");
                String countReturn = data.getStringExtra("countReturn");
                int newIndex = Integer.parseInt(indexReturn);
                int newerIndex = newIndex -1;

                String deleteBool = data.getStringExtra("delete");

                if(deleteBool.equals("noDelete")) {
                    ListElementsArrayList.add(newName + " |" + countReturn);
                    ListElementsArrayList.remove(newerIndex);
                    adapter.notifyDataSetChanged();
                }

                else if (deleteBool.equals("delete")) {
                    ListElementsArrayList.remove(newerIndex);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
