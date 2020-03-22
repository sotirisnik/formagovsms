package com.example.sotirisnik.smsgov;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    EditText fullname_txt, address_txt;
    Button save_btn;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String fullname, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences( getBaseContext() );
        editor = preferences.edit();

        fullname_txt = (EditText)findViewById(R.id.fullname_txt);
        address_txt = (EditText)findViewById(R.id.address_txt);

        fullname = preferences.getString("storedFullname", "");
        address = preferences.getString("storedAddress", "");

        fullname_txt.setText( fullname );
        address_txt.setText( address );

        save_btn = (Button) findViewById(R.id.save_button);
        save_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fullname = fullname_txt.getText().toString();
                address = address_txt.getText().toString();

                try {
                    editor.putString("storedFullname", fullname);
                    editor.putString("storedAddress", address);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Τα στοιχεία αποθηκεύθηκαν επιτυχώς:\nΟνοματεπώνυμο:" + fullname + "\nΟδός:" + address ,Toast.LENGTH_SHORT).show();
                }catch ( Exception e ) {
                    Toast.makeText(getApplicationContext(), "Τα στοιχεία δεν μπόρεσαν να αποθηκευτούν" ,Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
