package com.example.callingandmessaging;


public class Person {
    private String name;
    private String contact_no[] = new String[10];

    //set and get name
    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }

    //set and get contact no
    public void setContact_no(String Contact_no[])
    {
        this.contact_no = Contact_no;
    }
    public String[] getContact_no()
    {
        return contact_no;
    }


}
