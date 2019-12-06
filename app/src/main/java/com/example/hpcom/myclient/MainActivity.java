package com.example.hpcom.myclient;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    public static String firstN,secondN;
    public int flag;
    public EditText edT1;
    public EditText edT2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hjh
                Intent i = new Intent();
                i.setComponent(new ComponentName("com.android.contacts", "com.android.contacts.DialtactsContactsEntryActivity"));
                i.setAction("android.intent.action.MAIN");
                i.addCategory("android.intent.category.LAUNCHER");
                i.addCategory("android.intent.category.DEFAULT");
                Toast.makeText(getApplicationContext(),"Copy the particular contact's number",Toast.LENGTH_LONG).show();
                startActivity(i);
            }
        });
        final Button serviceB=(Button)findViewById(R.id.serviceB);
        flag=1;
        serviceB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 1) {
                    Toast.makeText(MainActivity.this, "ACTIVATED!", Toast.LENGTH_LONG).show();
                    if(startService(new Intent(getApplicationContext(), ShakeService.class))!=null)
                        Log.d("Service","It started!!");
                    flag = 0;
                } else {
                    Toast.makeText(MainActivity.this, "DEACTIVATED!", Toast.LENGTH_LONG).show();
                    stopService(new Intent(getApplicationContext(), ShakeService.class));
                    flag = 1;
                }

            }
        });
        Button doneB=(Button)findViewById(R.id.doneButton);
        if (doneB != null) {
            doneB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edT1 = (EditText) findViewById(R.id.firstNumber);
                    edT2 = (EditText) findViewById(R.id.secondNumber);
                    if(edT1.getText()!=null)
                        firstN=edT1.getText().toString();
                    if(edT2.getText()!=null)
                        secondN=edT2.getText().toString();
                    try {
                        File myFile = new File("/sdcard/.emergencyNumbers.txt");
                        myFile.createNewFile();
                        FileOutputStream fOut = new FileOutputStream(myFile);
                        OutputStreamWriter myOutWriter =
                                new OutputStreamWriter(fOut);
                        myOutWriter.append(firstN);
                        myOutWriter.append("\n");
                        myOutWriter.append(secondN);
                        myOutWriter.close();
                        fOut.close();
                        Toast.makeText(getApplicationContext(),
                                "The emergency contact numbers have been saved.",
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                    Log.d(getPackageName(), "Done! button pressed.");
                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.aboutM) {
            startActivity(new Intent(MainActivity.this,About.class));
        }
        else if(id == R.id.close){
            System.exit(1);
        }

        return super.onOptionsItemSelected(item);
    }
}
