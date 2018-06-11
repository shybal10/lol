package com.dairam.android.fragments.activity.database;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OpenHelper extends SQLiteOpenHelper 
{
	static final String dbname     = "dairam";
	static final String Tablename  = "dairamaddressbook";
	static final String key_id  = "key_id";
	
	static final String addressone = "addressone";
	static final String addresstwo = "addresstwo";	
	static final String addressthree = "addressthree";
	static final String addressfour  = "addressfour";
	static final String addressfive  = "addressfive";
	
	public OpenHelper(Context context) 
	{
		
		super(context, dbname, null, 33);
		Log.e("DataBase", "opened");
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("CREATE TABLE "+Tablename+"("+key_id+" INTEGER NOT NULL PRIMARY KEY, "+addressone+" TEXT  , "+addresstwo+" TEXT, "+addressthree+" TEXT, "+addressfour+" TEXT, "+addressfive+" TEXT)");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) 
	{	
		
	}
	// Image Insertion here
	
	public void addressDetailsInsertion(String address1,String address2,String address3,String address4,String address5) 
	{
		      SQLiteDatabase db=this.getWritableDatabase();
		      ContentValues cv=new ContentValues();
		
		      cv.put(addressone, address1);
		      cv.put(addresstwo, address2);
		      cv.put(addressthree, address3);
		      cv.put(addressfour, address4);
		      cv.put(addressfive, address5);
		      
  
		      db.insert(Tablename, addressone, cv);
		      db.close();
		      
		      Log.v("DB", "Inset_User OK"); 
	}
	
	// Image Data retrieve here
	public ArrayList<HashMap<String, String>> address_Records() 
	{
		
		 SQLiteDatabase db=getReadableDatabase();
	   	 ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	     Cursor cursor=db.rawQuery("SELECT  "+key_id+" as _id,  "+addressone+", "+addresstwo+" , "+addressthree+" , "+addressfour+" , "+addressfive+" from "+Tablename,new String [] {});
	    /* Cursor cursor=db.rawQuery("SELECT  "+key_id+" as _id,  "+date+", "+smstext+" from "+Tablename,new String [] {});*/
	        
	         if(cursor!=null)             
	         {
	             if(cursor.moveToFirst())  // movies first column 
	             {
	                 do
	                 {
	                	 HashMap<String, String> map = new HashMap<String, String>();
	                	
	                	 String  fnam        =       cursor.getString(cursor.getColumnIndex("addressone"));
	                	 String  lnam        =       cursor.getString(cursor.getColumnIndex("addresstwo"));
	                	 String  lnam1        =       cursor.getString(cursor.getColumnIndex("addressthree"));
	                	 String  lnam2        =       cursor.getString(cursor.getColumnIndex("addressfour"));
	                	 String  lnam3        =       cursor.getString(cursor.getColumnIndex("addressfive"));
	                	 
	                	 map.put (addressone          ,     fnam);
	                     map.put (addresstwo          ,     lnam); 
	                     map.put (addressthree        ,     lnam1);
	                     map.put (addressfour         ,     lnam2);
	                     map.put (addressfive         ,     lnam3);
	                     
	                     list.add(map);
	                 }
	                 while(cursor.moveToNext()); // move to next row
	             }
	         }
	         if (cursor != null && !cursor.isClosed()) 
	         
	         {
	            cursor.close();
	         }         
	         return list;
	         
	   }	
	  
	// Delete Audio Row
	  public void DeletrowImage(String rolls)
	  {
		    SQLiteDatabase db=this.getWritableDatabase();
	        db.delete(Tablename,addressone+"=?", new String [] {rolls});
	        db.close();
	        Log.e("Delete", "this token row" + rolls); 
	        
	  }	
}