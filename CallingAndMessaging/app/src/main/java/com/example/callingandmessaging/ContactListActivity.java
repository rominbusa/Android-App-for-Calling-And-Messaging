//package com.example.callingandmessaging;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.Toast;
//
//public class ContactListActivity extends AppCompatActivity {
//
//    String[] names = new String[10];
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_contact_list);
//
//        Bundle b = getIntent().getExtras();
//        final String arr[] = b.getStringArray("Contact_name");
//        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.contact_detail,arr);
//        final ListView listView = (ListView)findViewById(R.id.listview);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String selected_name = arr[position];
//                Toast.makeText(ContactListActivity.this,"You selected " + selected_name,Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(ContactListActivity.this,TimerActivity.class);
//                Bundle b = getIntent().getExtras();
//                intent.putExtra("Contact_name",b.getStringArray("Contact_name"));
//                intent.putExtra("Contact_numbers",b.getStringArray("Contact_numbers"));
//                intent.putExtra("name",selected_name);
//                startActivity(intent);
//            }
//        });
//    }
//}
