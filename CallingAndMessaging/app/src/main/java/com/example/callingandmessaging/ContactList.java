package com.example.callingandmessaging;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import android.os.Handler;

public class ContactList extends Application implements Runnable {

    private ArrayList<Person> personList = new ArrayList<Person>();

    private Handler mainHandler = new Handler();

    //get and set for person array
    public void setPerson(ArrayList<Person> personList)
    {
        this.personList = personList;
    }
    public ArrayList<Person> getPerson()
    {
        return personList;
    }


        @Override
        public void run (){


            ContentResolver contentResolver=getContentResolver();
            int j,q = 0;

            String[] mColunmProjection = new String[]{
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.CONTACT_STATUS,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER
            };

            //fetch contact list
            Cursor cursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,mColunmProjection,null,null,"DISPLAY_NAME ASC");

            if(cursor != null && cursor.getCount() > 0){

                while(cursor.moveToNext()) {
                    Person person = new Person();
                    String contact_number[] = new String[10];
                    j = 0;
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    int hasphonenumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                    if (hasphonenumber > 0) {
                        if (name == null) {
                            break;
                        } else {
                            person.setName(name);
                        }

                        //fetch contact of perticular person
                        Cursor phonecursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                                new String[]{id},
                                null);

                        while (phonecursor.moveToNext()) {
                            String phonenumber = phonecursor.getString(phonecursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            int flag = 0;
                            q = j;
                            while (j >= 1) {
                                if (phonenumber.equals(contact_number[j - 1])) {
                                    flag = 1;
                                    break;
                                } else {
                                    j--;
                                }
                            }
                            if (flag == 0) {
                                contact_number[q] = phonenumber;

                                j++;
                            }
                        }
                        phonecursor.close();
                    }

                    person.setContact_no(contact_number);
                    if (q != 0) {
                        if(person.getName() != null) {
                            personList.add(person);
                            Log.d("name", person.getName());
                        }
                    }
                }
                mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"contact fetched",Toast.LENGTH_SHORT).show();
                    Log.d("fetched ","contact dectshded");
                }
            });
            }else{
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"no Contact is present sorry",Toast.LENGTH_SHORT);
                        Log.d("not fetched","no contact");
                    }
                });
            }
        }
}
