package com.example.blizzardprofiles.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.dementh.lib.battlenet_oauth2.BnConstants;
import com.dementh.lib.battlenet_oauth2.activities.BnOAuthAccessTokenActivity;
import com.dementh.lib.battlenet_oauth2.connections.BnOAuth2Helper;
import com.dementh.lib.battlenet_oauth2.connections.BnOAuth2Params;
import com.example.blizzardprofiles.R;
import com.example.blizzardprofiles.URLConstants;
import com.example.blizzardprofiles.connection.Servers;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    public static String selectedRegion = "";
    private ArrayList<String> servers = new ArrayList<>();
    private Button login;
    private Button clearCredentials;
    BnOAuth2Params bnOAuth2Params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner regions = findViewById(R.id.spinner);
        login = findViewById(R.id.buttonLogin);
        clearCredentials = findViewById(R.id.clear_credentials);
        String [] REGION_LIST={"Select Region", "CN", "US", "EU", "KR", "TW"};
        getRandomServer();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, REGION_LIST) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                }
                return true;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setBackgroundColor(Color.BLACK);
                tv.setTextSize(20);
                tv.setGravity(Gravity.CENTER);
                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                }else{
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };

        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        regions.setAdapter(arrayAdapter);

         regions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRegion = (String) parent.getItemAtPosition(position);
                ((TextView) view).setTextColor(Color.WHITE);
                ((TextView) view).setTextSize(20);
                ((TextView) view).setGravity(Gravity.CENTER);
                bnOAuth2Params = new BnOAuth2Params(Servers.SERVER1.getClientKey(),Servers.SERVER1.getSecretKey(), selectedRegion.toLowerCase(),
                        URLConstants.CALLBACK_URL, "Blizzard Profiles", BnConstants.SCOPE_WOW, BnConstants.SCOPE_SC2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setGravity(Gravity.CENTER);
                ((TextView) parent.getChildAt(0)).setTextColor(000000);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedRegion.equals("Select Region")){
                    Toast.makeText(getApplicationContext(),"Please select a region", Toast.LENGTH_SHORT).show();
                }else{
                    CreateToken(bnOAuth2Params);
                    setContentView(R.layout.activity_games);
                }
            }
        });



        clearCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedRegion.equals("Select Region")){
                    Toast.makeText(getApplicationContext(),"Please select a region", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Login information cleared", Toast.LENGTH_SHORT).show();
                    clearCredentials(bnOAuth2Params);
                }
            }
        });
    }

    private void CreateToken(BnOAuth2Params bnOAuth2Params) {
        startOauthFlow(bnOAuth2Params);
    }

    private void startOauthFlow(final BnOAuth2Params bnOAuth2Params) {
        final Intent intent = new Intent(this, BnOAuthAccessTokenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(BnConstants.BUNDLE_BNPARAMS, bnOAuth2Params);
        intent.putExtra(BnConstants.BUNDLE_REDIRECT_ACTIVITY, GamesActivity.class);
        startActivity(intent);
    }

    //For future usage
    private void getRandomServer(){
        Random random = new Random();
        int serverNumber = random.nextInt(4);
        Servers getServers = Servers.fromOrdinal(serverNumber);
        servers.add(getServers.getClientKey());
        servers.add(getServers.getSecretKey());
    }

    private void clearCredentials(final BnOAuth2Params bnOAuth2Params)  {
        try {
            new BnOAuth2Helper(sharedPreferences, bnOAuth2Params).clearCredentials();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
