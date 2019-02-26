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

        ContactList contactList = (ContactList)getApplicationContext();
        final ArrayList<Person> arr = contactList.getPerson();

        Log.d("arraylist size",String.valueOf(arr.size()));
        String[] names =new String[arr.size()-1];
        try{
            for(int i = 0 ; i<arr.size() - 1 ; i++){
                Log.d("names of " + i + "person is " , arr.get(i).getName());
                if(arr.get(i).getName() != null) {
                    names[i] = arr.get(i).getName();
                }
            }
        }catch(NullPointerException e){
            Log.d("Exceptions :","occures");
        }
        ArrayAdapter adapter=new ArrayAdapter<String>(this,R.layout.contact_detail,names);
        final ListView listView = (ListView)findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected_name = arr.get(position).getName();
                Toast.makeText(DisplayContactListActivity.this, "You selected " + selected_name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DisplayContactListActivity.this, TimerActivity.class);
                intent.putExtra("Selected_name", selected_name);
                intent.putExtra("option", getIntent().getStringExtra("option"));
                startActivity(intent);

            }
        });
    }
}
