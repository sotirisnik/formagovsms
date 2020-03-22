package com.example.sotirisnik.smsgov;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView txt1;
    Button send_btn, preview_btn, setting_btn;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences( getBaseContext() );

        final Spinner spin1 = findViewById(R.id.spinner1);

        String one="1) Μετάβαση σε φαρμακείο ή επίσκεψη στον γιατρό, εφόσον αυτό συνιστάται μετά από σχετική επικοι-νωνία.";
        String two="2) Μετάβαση σε εν λειτουργία κατάστημα προμη-θειών  αγαθών  πρώτης  ανάγκης,  όπου  δεν  είναι δυνατή η αποστολή τους";
        String three="3) Μετάβαση στην τράπεζα, στο μέτρο που δεν είναι δυνατή η ηλεκτρονική συναλλαγή";
        String four = "4) Κίνηση για παροχή βοήθειας σε ανθρώπους που βρίσκονται σε ανάγκη.";
        String five = "5) Μετάβαση σε τελετή (π.χ. κηδεία, γάμος, βάφτιση ή ανάλογες τελετές) υπό τους όρους που προβλέπει ο νόμος";
        String six = "6) Σωματική άσκηση σε εξωτερικό χώρο ή κίνηση με κατοικίδιο ζώο, ατομικά ή ανά δύο άτομα, τηρώντας στην τελευταία αυτή περίπτωση την αναγκαία από-σταση 1,5 μέτρου";

        String[] items = new String[]{ one, two, three, four, five, six };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spin1.setAdapter(adapter);

        txt1 = (TextView)findViewById(R.id.txt1);//utoCompleteTextView;

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String msg = spin1.getSelectedItem().toString();
                txt1.setText( msg );//  null );//true);
                //findViewById( R.id.editT
                Toast.makeText(getApplicationContext(), "" + msg ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

        setting_btn = (Button) findViewById(R.id.btn_settings);
        setting_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent( getBaseContext(), SettingsActivity.class );
                startActivity( intent );
            }
        });

        preview_btn = (Button) findViewById(R.id.button_preview);
        preview_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String reason = spin1.getSelectedItem().toString();
                String fullname = preferences.getString("storedFullname", "NotSet");
                String address = preferences.getString("storedAddress", "NotSet");

                String msg = reason.charAt(0) + " " + fullname + " " + address;
                Toast.makeText(getApplicationContext(), "" + msg ,Toast.LENGTH_LONG).show();

            }
        });

        send_btn = (Button) findViewById(R.id.send_button);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {

                String reason = spin1.getSelectedItem().toString();
                String fullname = preferences.getString("storedFullname", "NotSet");
                String address = preferences.getString("storedAddress", "NotSet");

                String msg = reason.charAt(0) + " " + fullname + " " + address;

                if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getApplicationContext(),  "permission denied to SEND_SMS - requesting it", Toast.LENGTH_LONG).show();
                    String[] permissions = {Manifest.permission.SEND_SMS};
                    int PERMISSION_REQUEST_CODE = 1;
                    requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                }

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("13033", null, msg, null, null);
                    Toast.makeText(getApplicationContext(), "Το sms" + msg + " στάλθηκε", Toast.LENGTH_LONG).show();
                }catch ( Exception e ) {
                    Toast.makeText(getApplicationContext(), "Το sms δεν μπόρεσε να σταλθεί" + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}
