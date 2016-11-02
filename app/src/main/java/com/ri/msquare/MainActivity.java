package com.ri.msquare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ImageButton imageButton;
    TextView currentStatus;
    private ProgressDialog mProgressDialog;
    private static final int SPEECH_REQUEST_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list_container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GetTasksAsyncTask testAsyncTask = new GetTasksAsyncTask(MainActivity.this, "https://api.myjson.com/bins/146cu");
        testAsyncTask.execute();

        // Construct the data source
        final ArrayList<Task> tasks = new ArrayList<Task>();

//        tasks.add(new Task().setName("Task1").setTotalHours(8).setStartDate("20-Oct-2016").setEndDate("20-Oct-2016").setCurrentHours(2).setStatus("New"));
//        tasks.add(new Task().setName("Task2").setTotalHours(16).setStartDate("24-Oct-2016").setEndDate("25-Oct-2016").setCurrentHours(9.5f).setStatus("WIP"));

        TasksAdapter adapter = new TasksAdapter(this, tasks);
        final ListView listView = (ListView) findViewById(R.id.lv_tasks);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.d("TT","Clicked");
                Task task = (Task) listView.getItemAtPosition(position);
                String title = task.getName();
                Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
                intent.putExtra("TASK", task);
                startActivity(intent);
            }
        });
        setTitle("Assigned Tasks");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             displaySpeechRecognizer();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            Log.d("RES",spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tasks_list) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_dateview) {
            Intent intent = new Intent(MainActivity.this, DateViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class GetTasksAsyncTask extends AsyncTask<Void, Void, String> {
        private Context mContext;
        private String mUrl;

        public GetTasksAsyncTask(Context context, String url) {
            mContext = context;
            mUrl = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this,ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
            //android.R.style.Theme_DeviceDefault_Dialog_Alert);
            mProgressDialog.setTitle("Please wait");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Retrieving Tasks...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setInverseBackgroundForced(true);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String resultString = null;
            resultString = getJSON(mUrl);

            return resultString;
        }

        @Override
        protected void onPostExecute(String json) {
            Log.d("Received", json);
            super.onPostExecute(json);
            final ListView listView = (ListView) findViewById(R.id.lv_tasks);

            Gson gson = new Gson();

            //manually parsing until get the "project" element...
            JsonParser parser = new JsonParser();
            JsonObject rootObejct = parser.parse(json).getAsJsonObject();
            JsonElement rootElement = rootObejct.get("task");

            Type projectListType = new TypeToken<List<Task>>() {}.getType();
            List<Task> tasksList = gson.fromJson(rootElement, projectListType);

            for(Task task: tasksList) {
                Log.d("RESULT", task.toString());
            }
            TasksAdapter adapter = new TasksAdapter(mContext,  tasksList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                    Log.d("TT","Clicked");
                    Task task = (Task) listView.getItemAtPosition(position);
                    String title = task.getName();
                    Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
                    intent.putExtra("TASK", task);
                    startActivity(intent);
                }
            });
           mProgressDialog.dismiss();
        }

        private String getJSON(String url) {

            HttpURLConnection c = null;
            try {
                URL u = new URL(url);
                c = (HttpURLConnection) u.openConnection();
                c.connect();
                int status = c.getResponseCode();
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        return sb.toString();
                }

            } catch (Exception ex) {
                return ex.toString();
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        //disconnect error
                    }
                }
            }
            return null;
        }
    }

    public void completeTaskOnClickHandler(View v) {
        TasksAdapter.ViewHolder holder = (TasksAdapter.ViewHolder) v.getTag();
        Log.d("MAIN","Trying to change status " + holder.name.getText().toString());
        imageButton = (ImageButton)v;
        currentStatus = holder.currentStatus;
        holder.modifyTaskStatus.setEnabled(false);
        currentStatus.setText("");
        holder.status.setText("Complete");
        holder.border.setBackgroundColor(Color.GRAY);
        holder.modifyTaskStatus.setImageResource(R.drawable.ic_play_arrow_black_36dp);
        v.setEnabled(false);
    }

    public void modifyTaskStatusOnClickHandler(View v) {
        TasksAdapter.ViewHolder holder = (TasksAdapter.ViewHolder) v.getTag();

        Log.d("MAIN","Trying to change status " + holder.name.getText().toString());

        imageButton = (ImageButton)v;

        currentStatus = holder.currentStatus;

        String currentImage = (String) imageButton.getTag(R.id.current_image);
        if(currentImage == null){
            currentImage = "";
        }

        if(currentImage.equalsIgnoreCase("PAUSE")) {
            showDialog();
        }else {
            currentStatus.setText("Working");
            imageButton.setTag(R.id.current_image, "PAUSE");
            imageButton.setImageResource(R.drawable.ic_pause_black_36dp);
        }



//        Intent intent = new Intent(MainActivity.this, AddDictationActivity.class);

//        intent.putExtra("DICTATION", dictation);

//        startActivity(intent);

    }


    private void showDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle("Select Reason:");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("PTO");
        arrayAdapter.add("Lunch Break");
        arrayAdapter.add("Snacks");
        arrayAdapter.add("Holiday");
        arrayAdapter.add("Training");

        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        String strName = arrayAdapter.getItem(which);

                        imageButton.setTag(R.id.current_image, "PLAY");
                        imageButton.setImageResource(R.drawable.ic_play_arrow_black_36dp);
                        currentStatus.setText("Paused");


//                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
//                                MainActivity.this);
//                        builderInner.setMessage(strName);
//                        builderInner.setTitle("Your Selected Item is");
//                        builderInner.setPositiveButton(
//                                "Ok",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(
//                                            DialogInterface dialog,
//                                            int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                        builderInner.show();
                    }
                });
        builderSingle.show();
    }

}
