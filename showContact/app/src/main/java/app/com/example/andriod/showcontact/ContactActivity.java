package app.com.example.andriod.showcontact;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class ContactActivity extends Activity {

    URL url=null;
    String text_of_audio;
    String whattodo,name,callto;
    String[] personname = new String[1000];
    String[][] contactnumbers = new String[1000][9];
    int i = 0, j = 0;

    StringBuilder result=new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    //record audio by google api
    public void record_audio(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
        startActivityForResult(intent,10);

    }

    //audio to text by response of google api and call build url
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 10:

                if(resultCode==RESULT_OK && data!=null ){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text_of_audio=result.get(0);

                    BuildUrl();
                }
                break;
        }
    }

    //build url to send request to wit api
    private void BuildUrl() {
        String wit = "https://api.wit.ai/message";
        String google = "https://www.google.com/";
        Uri sendRequest_wit = Uri.parse(wit).buildUpon()
                .appendQueryParameter("q",text_of_audio).build();


        try{
            url=new URL(sendRequest_wit.toString());
        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        TextView showurl=(TextView) findViewById(R.id.showUrl);
        showurl.setText(url.toString());

        try {
            callto=new WitQueryTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView showName=(TextView) findViewById(R.id.showName);
        showName.setText(callto);

        i=0;
        contactnumbers[0][0]=null;
        ContentResolver contentResolver = getContentResolver();
        String man = callto;
        String mSelectionClause = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + "= ?";
        String[] mColunmProjection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.CONTACT_STATUS,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, mColunmProjection, mSelectionClause, new String[]{man}, null);
//            Cursor cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,mColunmProjection,null,null,null);

        TextView showContact = (TextView) findViewById(R.id.contact);
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                j = 0;
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                int hasphonenumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if (hasphonenumber > 0) {
                    if (name == null) {
                        break;
                    } else {
                        personname[i] = name;
                    }

                    Cursor phonecursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                            new String[]{id},
                            null);

                    while (phonecursor.moveToNext()) {
                        String phonenumber = phonecursor.getString(phonecursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        int flag = 0;
                        int q = j;
                        while (j >= 1) {
                            if (phonenumber.equals(contactnumbers[i][j - 1])) {
                                flag = 1;
                                break;
                            } else {
                                j--;
                            }
                        }
                        if (flag == 0) {
                            contactnumbers[i][q] = phonenumber;
                            j++;
                        }
                    }
                    phonecursor.close();
                }
                if (j > 0) {
                    i++;
                }

            }

        } else {
            showContact.setText("no Contact is present sorry\n");
        }

        StringBuilder stringBuilderQuryResult = new StringBuilder("");

        if(contactnumbers[0][0]!=null) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + contactnumbers[0][0]));
            stringBuilderQuryResult.append(contactnumbers[0][0]);
            showContact.setText(stringBuilderQuryResult);
            if (ActivityCompat.checkSelfPermission(ContactActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }
    }


    //url is sent to wit api and extracting the name and what to do means call or message
    public class WitQueryTask extends AsyncTask<URL,Void,String> {

        @Override
        protected String doInBackground(URL... urls){
            URL searchUrl = urls[0];
            String witSerachResult = null;
            try{
                witSerachResult=getResponseFromHttpUrl(searchUrl);
            }catch (IOException e){
                e.printStackTrace();
            }

            String s= witSerachResult;
            TextView showurl=(TextView) findViewById(R.id.showUrl);


            JSONArray t = null;
            if(s!=null && !s.equals("")){
                try {
                    JSONObject contact= new JSONObject(s);
                    JSONObject entity=contact.getJSONObject("entities");
                    JSONArray doit=entity.getJSONArray("call");
                    JSONObject what=doit.getJSONObject(0);
                    whattodo=what.getString("value");
                    JSONArray Name=entity.getJSONArray("contact");
                    JSONObject uname=Name.getJSONObject(0);
                    name=uname.getString("value");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showurl.setText(whattodo+" "+name);
            }

            return name;
        }
    }

    //set up to send request to wit api
    private String getResponseFromHttpUrl(URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.addRequestProperty("Authorization","Bearer JVNDXWXVDDQN2IJUJJY7NQXCPS23M7DX");
        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }else{
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }

}
