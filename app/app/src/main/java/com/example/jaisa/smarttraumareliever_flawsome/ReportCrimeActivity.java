package com.example.jaisa.smarttraumareliever_flawsome;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.bing.speech.Conversation;
import com.microsoft.bing.speech.SpeechClientStatus;
import com.microsoft.cognitiveservices.speechrecognition.DataRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionStatus;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;


public class ReportCrimeActivity extends AppCompatActivity implements ISpeechRecognitionServerEvents {

    Button record, send, helpButton;
    EditText report;
    TextView helpText;
    int helpVisibility = 0;
    static boolean gotPermission = false, button_stop=false;
    String result="",displayText="";

    MicrophoneRecognitionClient micClient = null;
    int m_waitSeconds = 0;
    DataRecognitionClient dataClient = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_crime);

        getSupportActionBar().setTitle("Report Crime");
        record = findViewById(R.id.record_button);
        send = findViewById(R.id.report_crime_button);
        report = findViewById(R.id.report_text);
        helpButton = findViewById(R.id.helpButton);
        helpText = findViewById(R.id.helpText);
        helpText.setVisibility(View.INVISIBLE);

        if(!gotPermission)//Keep requesting until granted
        {
            requestPermissionAudio();
        }
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts recording
                //first check if audio permission is granted or not
                if(!button_stop) {
                    startRecording(v);
                    record.setBackgroundResource(R.drawable.stop);
                    button_stop = true;
                }
                else
                {
                    stopRecording(v);
                    record.setBackgroundResource(R.drawable.record);
                    button_stop = false;
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valueText = report.getText().toString();
                if(!valueText.equals("")) {
                    AsyncFetch asyncFetch = new AsyncFetch();
                    asyncFetch.execute(new String[]{valueText});
                }
                //startActivity(new Intent(ReportCrimeActivity.this, CrimeDetailsActivity.class));

            }
        });
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helpVisibility == 0){
                    helpText.setVisibility(View.VISIBLE);
                    helpVisibility = 1;
                }
                else {
                    helpText.setVisibility(View.INVISIBLE);
                    helpVisibility = 0;
                }

            }
        });
    }
    private void requestPermissionAudio()
    {
        if(Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.RECORD_AUDIO)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Microphone access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please confirm Microphone access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.RECORD_AUDIO}
                                    , 1);
                        }
                    });
                    builder.show();

                } else {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            1);
                }
            }
            else//permission already granted
            {
                gotPermission = true;
            }
        }
    }
    private void startRecording(View v)
    {
        //first request permission if not granted
        if(!gotPermission)//Keep requesting until granted
        {
            requestPermissionAudio();
        }
         //Proceed to recording speech
        m_waitSeconds = 200;
        if(this.micClient == null)
        {
           this.micClient= SpeechRecognitionServiceFactory.createMicrophoneClient(
                    this,
                    SpeechRecognitionMode.LongDictation,
                    getDefaultLocale(),
                    this,
                    getPrimaryKey());
            //this.micClient.setAuthenticationUri(this.getAuthenticationUri());
        }
        //Toast.makeText(this,"Recording",Toast.LENGTH_SHORT).show();
        this.micClient.startMicAndRecognition();

    }
    private void stopRecording(View view)
    {
        if(micClient != null && gotPermission)
        {
            micClient.endMicAndRecognition();
            report.setText(displayText);
            report.post(new Runnable() {
                @Override
                public void run() {
                    report.setSelection(report.getText().toString().length());
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 1)
        {
            if (grantResults.length!=0 &&(grantResults[0] == PackageManager.PERMISSION_GRANTED))//Good! We may proceed with speech to text conversion now!
            {
                gotPermission = true;
            }
            else
            {
                //Toast.makeText(getApplicationContext(),"Please grant permission to proceed",Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public String getPrimaryKey() {
        return this.getString(R.string.primary_key);
    }

    private String getDefaultLocale() {
        return "en-in";
    }

    private String getAuthenticationUri() {
        return this.getString(R.string.authenticationUri);
    }

    @Override
    public void onPartialResponseReceived(String s) {
        //display while speaking
        result = s;
        Log.e("Writing",s);
    }

    @Override
    public void onFinalResponseReceived(RecognitionResult response) {

    Log.e("called","called now");
    for(int i=0;i<response.Results.length;i++)
    {
        Log.e("arr",response.Results[i].DisplayText+"......."+response.Results[i].Confidence);
        displayText = displayText +" "+ response.Results[i].DisplayText;
    }
        if(response.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                response.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout)//end
        {
           // Toast.makeText(getApplicationContext(),"Audio stopped",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onIntentReceived(String s) {

    }

    @Override
    public void onError(int i, String s) {
    Log.e("error",s);
    }

    @Override
    public void onAudioEvent(boolean b) {

        Toast.makeText(this,"listening",Toast.LENGTH_SHORT).show();
    }

    private class AsyncFetch extends AsyncTask {

        ProgressDialog pdLoading = new ProgressDialog(ReportCrimeActivity.this);
        private char ch;
        private String res,value="";

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            //Toast.makeText(getActivity(), tabNum+"", Toast.LENGTH_SHORT).show();
            value = (String)params[0];
            //aa="hiiiiHello";

            try{
                String link = "http://websiteyo.pythonanywhere.com/";
                String data="";
                try {
                    data = URLEncoder.encode("ind", "UTF-8")+"="+URLEncoder.encode(value,"UTF-8")+"&"+URLEncoder.encode("work", "UTF-8")+"="+URLEncoder.encode("retrieve","UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //String data = "status=registered";

                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String sb = "";
                String line = null;
                // Read Server Response

                int c = 1;
                //String name="", address="", phone="", comments="";
                while((line = rd.readLine()) != null) {
                    //sb.append(line);
                    sb = sb+line;
                    //break;
                }
                //aa=sb+"    "+sb.length();
                res=sb;
                return sb;

            } catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(Object result){
            pdLoading.hide();
            String r[] = new String[16];
            int size=0;
            String display="";
            try{
                JSONArray jsonArray = new JSONArray(res);
                for(int i=0; i<jsonArray.length(); i++){
                    r[i] = jsonArray.getString(i);
                    display += "\n"+r[i];
                }
                size = jsonArray.length();
            }catch (JSONException e){

            }
            Intent intent = new Intent(ReportCrimeActivity.this, CrimeDetailsActivity.class);
            Bundle b=new Bundle();
            b.putStringArray("laws", r);
            b.putInt("size", size);
            b.putString("incident", value);
            intent.putExtras(b);
            //Toast.makeText(ReportCrimeActivity.this, ""+display, Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }

    }
}
