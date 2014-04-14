package com.example.capstone;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Vector;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EditLocation extends ListActivity {
	
	private static final String FILENAME = "DataFile.txt";
	private static String TAG = "Location DATA";
	private Context context;
	private String finalimage,finaloutput,title,description,xLocation,yLocation;
	private Bitmap loadedImage;
	private static int RESULT_LOAD_IMAGE = 1;
	private String getTitle = "";
	private ListView titlesList;
	private Vector<String> titleVector;
	ItemsAdapter adapter;

	public EditLocation()
	{
		
	}
	
	public EditLocation(Context context) 
	{
        this.context = context;
    }
    
	@Override	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);	
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.location_edit);
		
		final Button LoadImage = (Button) findViewById(R.id.LoadImage);
		final Button DoneEditing = (Button) findViewById(R.id.DoneEditing);
		final Button SelectLocationButton = (Button)findViewById(R.id.selectLocationButton);
		TextView EditScreenTitle = (TextView) findViewById(R.id.editscreentitle);
		TextView locationsList = (TextView) findViewById(R.id.locationsList);
		Typeface TradeGothic = Typeface.createFromAsset(getAssets(),"TradeGothic.ttf");
		Typeface TradeGothic18 = Typeface.createFromAsset(getAssets(),"TradeG18.ttf");
		EditScreenTitle.setTypeface(TradeGothic18);
		DoneEditing.setTypeface(TradeGothic);
		LoadImage.setTypeface(TradeGothic);
		locationsList.setTypeface(TradeGothic);
		SelectLocationButton.setTypeface(TradeGothic);
											
		context = getApplicationContext();
		
		Intent i1 = getIntent();
		Double xLoc = i1.getDoubleExtra("xLocation", 0.0);
		Double yLoc = i1.getDoubleExtra("yLocation", 0.0);
		xLocation = new Double(xLoc).toString();
		yLocation = new Double(yLoc).toString();
		//FILENAME = xLocation+"."+yLocation+"."+"DATA.txt";
		
		//Begin another syncTask to browse and attach an image file
		LoadImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadImageAsync loadImageTask = new LoadImageAsync();
				loadImageTask.execute();
			}
		});
		
	    titlesList = (ListView) findViewById(android.R.id.list);
	    titlesList.setScrollbarFadingEnabled(false);
	    
	    titleVector = new Vector<String>();	    
	    
	    adapter = new ItemsAdapter(this, titleVector);
	   
	    SelectLocationButton.setOnClickListener(new OnClickListener() 
	    {
	        @Override
	        public void onClick(View v) 
	        {
	        	adapter.add(title);
	        	adapter.notifyDataSetChanged();
	        	
//	        	new Runnable()
//	        	{
//	        		public void run()
//	        		{
//		        		 for(int i=0; i<titleVector.size(); i++)
//		   	        	 {
//		   	        		 adapter.add(titleVector.get(i));
//		   	        	 }
//		        		 adapter.add(title);
//	   	        	     adapter.notifyDataSetChanged();
//	        		}		
//	        	};
	        }
	    });

	    setListAdapter(adapter);
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
	}
	
	//called when user clicks on Done Editing button i
	public void DoneEditing(View view) 
	{
		EditText titleedit = (EditText) findViewById(R.id.titleedit);
		title=titleedit.getText().toString();
		EditText descriptionedit = (EditText) findViewById(R.id.descriptionedit);
		description=descriptionedit.getText().toString();
		
		if(loadedImage!=null)
		{
			String image = BitMapToString(loadedImage);
			StringBuffer stringBuffer = new StringBuffer();
			
			for (int i = 0; i < image.length(); i++) 
			{
				if (image.charAt(i) != '\n' && image.charAt(i) != '\r') 
				{
					stringBuffer.append(image.charAt(i));
				}
			}
			finalimage = stringBuffer.toString();
			
			finaloutput=xLocation+"\n"+yLocation+"\n"+title+"\n"+description+"\n"+finalimage+"\n";
				
			//Write location data to an instance of the location object class
			createLocationObject();
		}
		else
		{
			finaloutput=xLocation+"\n"+yLocation+"\n"+title+"\n"+description;
			createLocationObject();
		}
		
		WritetoFile(finaloutput);
		Toast.makeText(context.getApplicationContext(), "Location Saved", Toast.LENGTH_LONG).show();
	}

	/**
	 * Writes location data to a location object 
	 */
	private void createLocationObject() 
	{
		Vector<LocationObject> locationVector = new Vector();
		LocationObject saveLocation = new LocationObject(xLocation, yLocation, title, description, finalimage);
		
		//save the object to a vector
		if (saveLocation != null)
		{
			locationVector.add(saveLocation);
		}
				
		//check to see what is inside the vector
     	for(int i=0; i<locationVector.size(); i++)
		{
			getTitle = locationVector.get(i).getLocTitle();
			Log.v("LOCATION",getTitle);
		}
		
	}
	
	private class LoadImageAsync extends AsyncTask<Void, Void, Void> 
	 {
		    @Override
		    protected Void doInBackground(Void...params) 
		    {
		    	 Intent load = new Intent(
		                    Intent.ACTION_PICK,
		                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		            startActivityForResult(load, RESULT_LOAD_IMAGE);
		    	
		    
		        try 
		        {			  
					  EditLocation.this.runOnUiThread(new Runnable()
						{
							  @Override
							  public void run() 
							  {
							      Toast.makeText(EditLocation.this, "Loading image", Toast.LENGTH_SHORT).show();
							  }
					     });
		
				} 
		        catch (Exception e) 
				{
					e.printStackTrace();
				}       
		    	
		    	return null;
		    }
	 }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	 {
	     super.onActivityResult(requestCode, resultCode, data);

	     if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) 
	     {
	         Uri selectedImage = data.getData();
	         String[] filePathColumn = {MediaStore.Images.Media.DATA};

	         Cursor cursor = getContentResolver().query(selectedImage,
	                 filePathColumn, null, null, null);
	         
	         cursor.moveToFirst();

	         int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	         String picturePath = cursor.getString(columnIndex);
	         cursor.close();
	         
	         //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
	         loadedImage = BitmapFactory.decodeFile(picturePath);
	         ImageView imageView = (ImageView) findViewById(R.id.imageView1);
	         
	         if(loadedImage != null)
	         {
	        	 imageView.setImageBitmap(loadedImage);	 
	        	 Toast.makeText(EditLocation.this, "Image loaded", Toast.LENGTH_SHORT).show();
	         }
	         else if (loadedImage == null)
	         {
	        	 Toast.makeText(EditLocation.this, "Image not loaded", Toast.LENGTH_SHORT).show();
	         }
	     }
	     
	 }
	
	//convert bitmap to string
	public String BitMapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		String temp = Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}
	
	//convert string to bitmap
	/*public Bitmap StringToBitMap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}*/
	
	public static String getFilename() 
	{
		return FILENAME;
	}
	
	private void WritetoFile(String output) 
	 {
		//String textToSaveString = "Testing Token Method";
		 //StringTokenizer st = new StringTokenizer(output);

		 try {
			 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_APPEND));
			 //outputStreamWriter.write("Location Test");
			 //while (st.hasMoreElements()) {
				 //outputStreamWriter.write(st.nextElement().toString()+ "\n");
			 //}
			 outputStreamWriter.write(output);
			 //Toast.makeText(context.getApplicationContext(), "writing: " + output, Toast.LENGTH_LONG).show();
			 outputStreamWriter.close();
			 Toast.makeText(context.getApplicationContext(), "Location Saved", Toast.LENGTH_LONG).show();
		 }
		 catch (IOException e) {
			 Log.e(TAG, "File write failed: " + e.toString());
		 }

		 //WRITE SPLIT WITH COMMA
		 //System.out.println("---- Split by comma ',' ------");
		 //StringTokenizer st2 = new StringTokenizer(str, ",");

		 //while (st2.hasMoreElements()) {
		 //	System.out.println(st2.nextElement());
		 //}
	 }
	
	class ItemsAdapter extends ArrayAdapter<String> 
	{

	    public ItemsAdapter(Context context, List<String> list) 
	    {
	        super(context, R.layout.location_adapter, list);
	    }

	    @Override
	    public View getView(final int position, View row, final ViewGroup parent) 
	    {
	        final String item = getItem(position);

	        ItemWrapper wrapper = null;
	        if (row == null) {
	            row = getLayoutInflater().inflate(R.layout.location_adapter, parent, false);
	            wrapper = new ItemWrapper(row);

	            row.setTag(wrapper);
	        } else {
	            wrapper = (ItemWrapper) row.getTag();
	        }
	        wrapper.refreshData(item);

	        return row;
	    }

	    class ItemWrapper {

	        TextView text;

	        public ItemWrapper(View row) {
	            text = (TextView) row.findViewById(R.id.elementLista);
	        }

	        public void refreshData(String item) {
	            text.setText(item);
	        }

	    }
	    }    
} 
	 

