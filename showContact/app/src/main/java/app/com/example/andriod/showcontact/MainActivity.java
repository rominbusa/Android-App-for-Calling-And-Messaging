package app.com.example.andriod.showcontact;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    String []personname=new String[1000];
    String [][]contactnumbers=new String[1000][9];
    int i=0,j=0;
    StringBuilder stringBuilderQuryResult=new StringBuilder("");
    public void showContact(View view) {
        ContentResolver contentResolver=getContentResolver();
        String man="Savan";
        String mSelectionClause=ContactsContract.Contacts.DISPLAY_NAME_PRIMARY+"= ?";
        String[] mColunmProjection=new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.CONTACT_STATUS,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };
        Cursor cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,mColunmProjection,mSelectionClause,new String[]{man},null);
//        Cursor cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,mColunmProjection,null,null,null);

        TextView showContact=(TextView) findViewById(R.id.contact);
        if(cursor!=null && cursor.getCount()>0){

            while(cursor.moveToNext()) {
                j=0;
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                int hasphonenumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if (hasphonenumber > 0) {
                    if(name==null){
                        break;
                    }else{
                        personname[i]=name;
                    }

                    Cursor phonecursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                            new String[]{id},
                            null);

                    while (phonecursor.moveToNext()) {
                        String phonenumber = phonecursor.getString(phonecursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        int flag=0;int q=j;
                        while(j>=1){
                            if(phonenumber.equals(contactnumbers[i][j-1])){
                                flag=1;
                                break;
                            }else{
                                j--;
                            }
                        }
                        if(flag==0){
                            contactnumbers[i][q]=phonenumber;
                            j++;
                        }
                    }
                    phonecursor.close();
                }
                if(j>0){
                    i++;
                }

            }

        }else{
            showContact.setText("no Contact is present sorry\n");
        }

        for(int k=0;k<i;k++){
            stringBuilderQuryResult.append(k+" "+personname[k]+"  ");

            for(int m=0;m<5;m++){
                if(contactnumbers[k][m]==null){
                    break;
                }
                stringBuilderQuryResult.append(contactnumbers[k][m]+" "+k+" "+m);
            }
            stringBuilderQuryResult.append("\n\n\n");
        }
        showContact.setText(stringBuilderQuryResult);

        TextView num=(TextView) findViewById(R.id.num);

        num.setText(contactnumbers[0][0]);
//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse("tel:"+contactnumbers[0][0]));
//        stringBuilderQuryResult.append(contactnumbers[0][0]);
//        showContact.setText(stringBuilderQuryResult);
//        if (ActivityCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        startActivity(callIntent);

    }
}
