package com.ri.msquare;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

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
import java.util.List;

public class TaskDetailsActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                task =(Task) extras.get("TASK");
            }
        }

        setTitle("Task: " + task.getName());

        GetTasksAsyncTask testAsyncTask = new GetTasksAsyncTask(TaskDetailsActivity.this, "https://api.myjson.com/bins/2z1ua");
        testAsyncTask.execute();

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
            mProgressDialog = new ProgressDialog(TaskDetailsActivity.this,ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
            //android.R.style.Theme_DeviceDefault_Dialog_Alert);
            mProgressDialog.setTitle("Please wait");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Retrieving Task Details...");
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
            final ListView listView = (ListView) findViewById(R.id.lv_task_details);

            Gson gson = new Gson();

            //manually parsing until get the "project" element...
            JsonParser parser = new JsonParser();
            JsonObject rootObejct = parser.parse(json).getAsJsonObject();
            JsonElement rootElement = rootObejct.get("task");

            Type projectListType = new TypeToken<List<Task>>() {}.getType();
            List<Task> tasksList = gson.fromJson(rootElement, projectListType);

            tasksList.add(new Task());

            for(Task task: tasksList) {
                Log.d("RESULT", task.toString());
            }
            TaskDetailsAdapter adapter = new TaskDetailsAdapter(mContext,  tasksList);
            listView.setAdapter(adapter);

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
}
