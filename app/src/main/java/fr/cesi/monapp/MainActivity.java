package fr.cesi.monapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initListener();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void initListener() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Loading", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                JSONObject jsonObj = readJSONFeed(getString(R.string.activity_url));
                manageResult(jsonObj);

            }
        });

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
                MainActivity.this.startActivity(intent);

            }
        });
    }

    private void manageResult(JSONObject jsonObj) {
        try {
            String name = (String) jsonObj.get("name");
            String email = (String) jsonObj.get("email");
            String body = (String) jsonObj.get("body");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //https://jsonplaceholder.typicode.com/comments

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //PRIVATE METHODS

    private JSONObject readJSONFeed(String url) {
        String jsonArticle = new String();
        JSONArray obj = null;
        JSONObject jsonObj = null;
        try {
            URL urlObj = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(urlConnection.getInputStream())
            );
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                String line;
                while ((line = in.readLine()) != null) {
                    jsonArticle+=line;
                }
            } else {
                Log.d("JSON", "Failed to download file"+statusCode);
            }
            in.close();
            urlConnection.disconnect();
            obj = new JSONArray(jsonArticle);


            return obj.getJSONObject(0);
        }
        catch(MalformedURLException e){
            Log.d("URL",e.getStackTrace().toString());
        }
        catch (Exception e) {
            Log.d("JSON",e.toString());
        }
        return jsonObj;
    }
}
