package com.example.bigassignment;

public class AdapterItems
{
    public long ID;
    public String DateTime;
    public String Title;
    public String Description;
    public String Time;
    public String Date;
    public long Account_ID;


    AdapterItems(long ID, String DateTime, String Title, String Description, String Time, String Date, long Account_ID)
    {
        this.ID = ID;
        this.DateTime = DateTime;
        this.Title = Title;
        this.Description = Description;
        this.Time = Time;
        this.Date = Date;
        this.Account_ID = Account_ID;
    }
}

