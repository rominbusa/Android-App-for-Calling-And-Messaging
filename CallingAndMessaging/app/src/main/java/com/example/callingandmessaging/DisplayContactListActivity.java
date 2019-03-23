package com.example.callingandmessaging;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayContactListActivity extends Activity {

    private RecyclerView recyclerView;
    private ContactListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText editText;
    private ArrayList<Person> arr;
    private ArrayList<Person> result = new ArrayList<Person>();
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        ContactList contactList = (ContactList)getApplicationContext();
        arr = contactList.getPerson();

        editText = findViewById(R.id.editText);

        //for Toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Select Contact");

        //for recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.contactListRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ContactListAdapter(arr);
        mAdapter.setOnItemClickListener(new ContactListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String selected_name = arr.get(position).getName();
//                Toast.makeText(DisplayContactListActivity.this, "You selected " + selected_name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DisplayContactListActivity.this, TimerActivity.class);
                intent.putExtra("Selected_name", selected_name);
                intent.putExtra("option", getIntent().getStringExtra("option"));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);

        Log.d("arraylist size",String.valueOf(arr.size()));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//                searchContact(s.toString());
                mAdapter = new ContactListAdapter(searchContact(s.toString()));
                recyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new ContactListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        String selected_name = result.get(position).getName();
//                        Toast.makeText(DisplayContactListActivity.this, "You selected " + selected_name, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DisplayContactListActivity.this, TimerActivity.class);
                        intent.putExtra("Selected_name", selected_name);
                        intent.putExtra("option", getIntent().getStringExtra("option"));
                        startActivity(intent);
                    }
                });
            }
        });


//        String[] names =new String[arr.size()-1];
//        try{
//            for(int i = 0 ; i<arr.size() - 1 ; i++){
//                Log.d("names of " + i + "person is " , arr.get(i).getName());
//                if(arr.get(i).getName() != null) {
//                    names[i] = arr.get(i).getName();
//                }
//            }
//        }catch(NullPointerException e){
//            Log.d("Exceptions :","occures");
//        }
//        ArrayAdapter adapter=new ArrayAdapter<String>(this,R.layout.contact_detail,names);
//        final ListView listView = (ListView)findViewById(R.id.listview);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String selected_name = arr.get(position).getName();
//                Toast.makeText(DisplayContactListActivity.this, "You selected " + selected_name, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(DisplayContactListActivity.this, TimerActivity.class);
//                intent.putExtra("Selected_name", selected_name);
//                intent.putExtra("option", getIntent().getStringExtra("option"));
//                startActivity(intent);

//            }
//        });
    }

    private ArrayList<Person> searchContact(String s){
//        ArrayList<Person> result = null;
//        result.clear();
        if( result != null){
            result.clear();
        }
        for (Person p : arr ) {
           if (p != null && p.getName().toLowerCase().contains(s.toLowerCase()) ){
               Log.d("names",p.getName());
               result.add(p);
           }
        }
        return result;
    }
}
