package com.trivediheena.googlespreadsheetdemo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SpreadSheetActivity extends AppCompatActivity {

    public static final MediaType FORM_DATA_TYPE=MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    public static final String URL="https://docs.google.com/forms/d/e/1FAIpQLSeanRmMvOiPrQ10TF7e8uE9LBJVson5NxmKzen065O-SPyK2Q/formResponse";
    public static final String NAME_KEY="entry.1034896853";
    public static final String CONTACT_KEY="entry.1275630611";
    public static final String EMAIL_KEY="entry.1287076362";
    public static final String MESSAGE_KEY="entry.1460321210";
    private final Context context=this;
    private EditText txtNm;
    private EditText txtCont;
    private EditText txtEmail;
    private EditText txtMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spread_sheet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtNm=(EditText)findViewById(R.id.txtNm);
        txtCont=(EditText)findViewById(R.id.txtCont);
        txtEmail=(EditText)findViewById(R.id.txtEmail);
        txtMsg=(EditText)findViewById(R.id.txtMsg);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(txtEmail.getText().toString()) || TextUtils.isEmpty(txtNm.getText().toString())||
                        TextUtils.isEmpty(txtCont.getText().toString())|| TextUtils.isEmpty(txtMsg.getText().toString())){
                    Snackbar.make(view,"All Fields Are Mandatory",Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {
                    Snackbar.make(view, "Plz. Enter valid Email", Snackbar.LENGTH_LONG)
                            .show();
                }
                PostDataTask postDataTask=new PostDataTask();
                postDataTask.execute(URL,txtNm.getText().toString(),txtCont.getText().toString(),txtEmail.getText().toString(),txtMsg.getText().toString());
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_spread_sheet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private class PostDataTask extends AsyncTask<String,Void,Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean result=true;
            String url=params[0];
            String nm=params[1];
            String cont=params[2];
            String email=params[3];
            String msg=params[4];
            String postBody="";
            try{
                postBody=NAME_KEY+"="+ URLEncoder.encode(nm, "UTF-8")+"&"+
                        CONTACT_KEY+"="+URLEncoder.encode(cont,"UTF-8")+"&"+
                        EMAIL_KEY+"="+ URLEncoder.encode(email,"UTF-8")+"&"+
                        MESSAGE_KEY+"="+URLEncoder.encode(msg,"UTF-8");

            }catch (UnsupportedEncodingException e){
                //  e.printStackTrace();
                result=false;
            }
            try{
                OkHttpClient client=new OkHttpClient();
                RequestBody body=RequestBody.create(FORM_DATA_TYPE,postBody);
                Request request=new Request.Builder().url(url).post(body).build();
                Response response=client.newCall(request).execute();
            }catch (IOException ie){
                result=false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Toast.makeText(context, aBoolean ? "Message Successfully Sent" : "Plz. Try Again Later", Toast.LENGTH_LONG).show();
        }
    }
}
