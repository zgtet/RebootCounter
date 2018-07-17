package com.symbol.rebootcounter.rebootcounter;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {


    private boolean isReboot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();
        int value = 0;
        if (b != null)
            value = b.getInt("reboot");

        if (value > 0)
            isReboot = true;
       // String outp = Executer("settings get global boot_count");
        //Toast.makeText(MainActivity.this, outp, Toast.LENGTH_LONG ).show();
       // Log.d("isReboot", "isReboot = " + Boolean.toString(isReboot));
    }


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String rebootcounter = sharedPreferences.getString("rebootcounter", "");

        //first reboot, set counter to 1
        if (rebootcounter.isEmpty() && isReboot) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("rebootcounter", "1");
            editor.commit();
            rebootcounter = "1";
            isReboot = false;
        }
        else if (!rebootcounter.isEmpty() && isReboot)
        {
            int counter = Integer.parseInt(rebootcounter);
            counter++;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("rebootcounter", String.valueOf(counter));
            editor.commit();
            isReboot = false;
            rebootcounter = sharedPreferences.getString("rebootcounter", "");
        }

        TextView tvRecord = (TextView)  findViewById(R.id.txtReboot);
        String strMsg = "Total Reboot Count : ";

        if(rebootcounter.isEmpty())
        {
            strMsg = strMsg + "0";
        }
        else
        {
            strMsg = strMsg + rebootcounter;
        }

        tvRecord.setText(strMsg);


    }

    public String Executer(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String response = output.toString();
        return response;

    }
}
