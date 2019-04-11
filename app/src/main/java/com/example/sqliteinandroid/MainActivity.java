package com.example.sqliteinandroid;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button addBtn, readBtn, editBtn, deleteBtn, serverBtn;
    EditText idTxt, nameTxt;
    DBHelper dbHelper;
    JSONObject json_data;
    HttpURLConnection con;
    String query, results;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DB_CODE
        dbHelper = new DBHelper(this);

        addBtn = findViewById(R.id.btnAdd);
        readBtn = findViewById(R.id.btnRead);
        editBtn = findViewById(R.id.btnEdit);
        deleteBtn = findViewById(R.id.btnDelete);
        idTxt = findViewById(R.id.txtIDVal);
        nameTxt = findViewById(R.id.txtNameVal);
        serverBtn = findViewById(R.id.serverBTN);

        serverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Create().execute();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.writeData(idTxt.getText().toString(), nameTxt.getText().toString());
            }
        });

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Cursor crs = dbHelper.readData(idTxt.getText().toString());
                if (crs.moveToFirst()) {
                    do {
                        nameTxt.setText(crs.getString(0));
                    }
                    while (crs.moveToNext());
                }
                crs.close();*/

                //Remote read:
                new Read().execute();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dbHelper.updateData(idTxt.getText().toString(), nameTxt.getText().toString());

                //Server code:
                new Update().execute();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dbHelper.deleteData(idTxt.getText().toString());

                //Server code:
                new Delete().execute();
            }
        });
    }


    //server thread
    final class Create extends AsyncTask<String, Void, Void> {
        ProgressDialog mProgressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("Creating record please wait..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("id", idTxt.getText().toString().trim()).appendQueryParameter("name", nameTxt.getText().toString().trim());
                query = builder.build().getEncodedQuery();
                String url = "https://citrus-realignments.000webhostapp.com/student/create.php";
                URL obj = new URL(url);
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
                con.setRequestProperty("Accept-Language", "UTF-8");
                con.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
                outputStreamWriter.write(query);
                outputStreamWriter.flush();
            } catch (Exception e) {
                android.util.Log.e("Fail 1", e.toString());
            }
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    sb.append(line + "\n");
                }
                results = sb.toString();
            } catch (Exception e) {
                android.util.Log.e("Fail 2", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                json_data = new JSONObject(results);
                int code = (json_data.getInt("code"));
                if (code == 1) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Success");
                    alert.setMessage("Student Record Created");
                    alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                } else {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Failed");
                    alert.setMessage("Creating Student Failed");
                    alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }

            mProgressDialog.dismiss();
        }
    }
    final class Read extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("Reading record please wait..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCancelable(false); mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("id", idTxt.getText().toString().trim());
                query = builder.build().getEncodedQuery();
                String url = "https://citrus-realignments.000webhostapp.com/student/read.php";
                URL obj = new URL(url);
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
                con.setRequestProperty("Accept-Language", "UTF-8");
                con.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
                outputStreamWriter.write(query);
                outputStreamWriter.flush();
            }
            catch (Exception e) {
                android.util.Log.e("Fail 1", e.toString());
            }
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    sb.append(line + "\n");
                }
                results = sb.toString();
            }
            catch (Exception e) {
                android.util.Log.e("Fail 2", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                json_data = new JSONObject(results);
                int code = (json_data.getInt("code"));
                if (code == 1) {
                    nameTxt.setText(json_data.getString("name"));
                }
                else {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Failed");
                    alert.setMessage("Reading Student Failed");
                    alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
            }
            catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
            mProgressDialog.dismiss();
        }
    }

    final class Update extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("Updating record please wait..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Uri.Builder builder = new Uri.Builder() .appendQueryParameter("id", idTxt.getText().toString().trim()) .appendQueryParameter("name", nameTxt.getText().toString().trim());
                query = builder.build().getEncodedQuery();
                String url = "https://citrus-realignments.000webhostapp.com/student/update.php";
                URL obj = new URL(url);
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
                con.setRequestProperty("Accept-Language", "UTF-8");
                con.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
                outputStreamWriter.write(query);
                outputStreamWriter.flush();
            }
            catch (Exception e) {
                android.util.Log.e("Fail 1", e.toString());
            }
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    sb.append(line + "\n");
                }
                results = sb.toString();
            }
            catch (Exception e) {
                android.util.Log.e("Fail 2", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try { json_data = new JSONObject(results);
                int code = (json_data.getInt("code"));
                if (code == 1) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Success");
                    alert.setMessage("Student Record Updated");
                    alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
                else {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Failed");
                    alert.setMessage("Updating Student Failed");
                    alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
            }
            catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
            mProgressDialog.dismiss();
        }
    }

    final class Delete extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("Deleting record please wait..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Uri.Builder builder = new Uri.Builder() .appendQueryParameter("id", idTxt.getText().toString().trim());
                query = builder.build().getEncodedQuery();
                String url = "https://citrus-realignments.000webhostapp.com/student/delete.php";
                URL obj = new URL(url);
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
                con.setRequestProperty("Accept-Language", "UTF-8");
                con.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
                outputStreamWriter.write(query);
                outputStreamWriter.flush();
            }
            catch (Exception e) {
                android.util.Log.e("Fail 1", e.toString());
            }
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    sb.append(line + "\n");
                }
                results = sb.toString();
            }
            catch (Exception e) {
                android.util.Log.e("Fail 2", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                json_data = new JSONObject(results);
                int code = (json_data.getInt("code"));
                if (code == 1) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Success");
                    alert.setMessage("Student Record Deleted");
                    alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
                else {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Failed");
                    alert.setMessage("Deleting Student Failed");
                    alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
            }
            catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
            mProgressDialog.dismiss();
        }
    }
}