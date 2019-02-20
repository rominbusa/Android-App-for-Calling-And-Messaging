package com.example.callingandmessaging;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayContactListActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        //Bundle b=getIntent().getExtras();
        //final String arr[]=b.getStringArray("Contact_name");

        ContactList contactList = (ContactList)getApplicationContext();
        final ArrayList<Person> arr = contactList.getPerson();

        Log.d("arraylist size",String.valueOf(arr.size()));
        String[] names =new String[arr.size()];
        for(int i=0;i<arr.size();i++){
            names[i]=arr.get(i).getName();
        }

        ArrayAdapter adapter=new ArrayAdapter<String>(this,R.layout.contact_detail,names);
        final ListView listView=(ListView)findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected_name= arr.get(position).getName();
                Toast.makeText(DisplayContactListActivity.this,"You selected "+selected_name,Toast.LENGTH_SHORT).show();
                //if(getIntent().getStringExtra("option").equals("Call")) {
                    Intent intent = new Intent(DisplayContactListActivity.this, TimerActivity.class);
                    intent.putExtra("Selected_name", selected_name);
                    intent.putExtra("option", getIntent().getStringExtra("option"));
                    startActivity(intent);
                /*}
                else {
                    Intent intent = new Intent(DisplayContactListActivity.this, TimerActivity.class);
                    intent.putExtra("Selected_name", selected_name);
                    intent.putExtra("option", getIntent().getStringExtra("option"));
                    startActivity(intent);
                }*/
//                Bundle b = getIntent().getExtras();
                //intent.putExtra("Contact_name",selected_name);
//                intent.putExtra("Contact_numbers",b.getStringArray("Contact_numbers"));
//                intent.putExtra("name",selected_name);

            }
        });
    }
}
