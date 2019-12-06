package com.example.hpcom.myclient;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class CheckCertainty extends Activity implements LocationListener {
    private LocationManager lm;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Location location;
    public double latitude, longitude;
    public String No1,No2;
    protected String SENDER_ID = "your_sender_id";
    String token="";
    String app_server_url="https://accidentserver.000webhostapp.com/fcm_insert.php";
    boolean isGPSEnabled=false,isNetworkEnabled=false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_certainty);
        TextView tv = findViewById(R.id.textView4);
        try {
            File myFile = new File("/sdcard/.emergencyNumbers.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            No1=myReader.readLine();
            No2=myReader.readLine();
            myReader.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        final SmsManager sms = SmsManager.getDefault();
        lm=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

       //     lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10, this);

        //---------
        checkLocationPermission();
        location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Log.d("Location found to be:",location.getLatitude()+" $ "+location.getLongitude() );

        //---------
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    sms.sendTextMessage(No1, null, "Help! I've met with an accident at http://maps.google.com/?q=" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()), null, null);
                //    sms.sendTextMessage(No1, null, "Nearby Hospitals http://maps.google.com/maps?q=hospital&mrt=yp&sll=" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()) + "&output=kml", null, null);
                    sms.sendTextMessage(No2, null, "Help! I've met with an accident at http://maps.google.com/?q=" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()), null, null);
                //    sms.sendTextMessage(No2, null, "Nearby Hospitals http://maps.google.com/maps?q=hospital&mrt=yp&sll=" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()) + "&output=kml", null, null);

                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Your SMS sent has failed!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                System.exit(1);
            }
        }, 15000);

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        token=sharedPreferences.getString(getString(R.string.FCM_TOKEN),"");
        Log.i("TAAG", "NAGAR1");

        StringRequest stringRequest=new StringRequest(Request.Method.POST, app_server_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("fcm_token",token);
                Log.i("TAAG", token);
                return params;
            }
        };
        MySingleton.getmInstance(CheckCertainty.this).addToRequestque(stringRequest);
        try {
            JSONObject toSend = new JSONObject();
            toSend.put("msg", location.getLatitude()+" $ "+location.getLongitude());
            Log.d("Message sent to server:",location.getLatitude()+" $ "+location.getLongitude() );
            JSONTransmitter transmitter = new JSONTransmitter();
            transmitter.execute(new JSONObject[]{toSend});
            tv.setText("message sent");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Button dismiss = (Button) findViewById(R.id.dismissB);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(1);
            }
        });


    }
    @Override
    public void onLocationChanged(Location location){
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        Toast.makeText(getApplicationContext(),"Lat and Long extracted",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onProviderDisabled(String provider){
    }
    @Override
    public void onProviderEnabled(String provider){
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("Please grant Access to location!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(CheckCertainty.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Permission","granted!");
                        //Request location updates:
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1, this);

                    }

                } else {
                    Log.d("Permission","denied!");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            lm.removeUpdates(this);
        }
    }
}