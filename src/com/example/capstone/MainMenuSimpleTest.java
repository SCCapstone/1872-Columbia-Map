package com.example.capstone;
import android.content.Intent;
import android.widget.Button;

public class MainMenuSimpleTest extends android.test.ActivityUnitTestCase<MainMenu> 
{
	private MainMenu activity;
	private int buttonIdAdminLogin;
	private int buttonIdEmailBackup;
	private int buttonIdViewMap;

	/**
	 * @param name
	 */
	public MainMenuSimpleTest() 
	{
		super(MainMenu.class);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception 
	{
		super.setUp();
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
			        MainMenu.class);
	    startActivity(intent, null, null);
	    activity = getActivity();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception 
	{
		super.tearDown();
	}

	/**
	 * Test method for {@link com.example.capstone.MainMenu#onCreate(android.os.Bundle)}.
	 * Makes sure the three buttons are created in the main menu
	 */
	public void testOnCreateBundle() 
	{
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.example.capstone.MainMenu#onCreateOptionsMenu(android.view.Menu)}.
	 */
	public void testOnCreateOptionsMenuMenu() 
	{
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.example.capstone.MainMenu#ViewMap(android.view.View)}.
	 */
	public void testViewMap() 
	{
	    buttonIdViewMap = com.example.capstone.R.id.ViewMap;
	    assertNotNull(activity.findViewById(buttonIdViewMap));
	    Button view = (Button) activity.findViewById(buttonIdViewMap);
	    assertEquals("Wrong label for the View Map button", "View Map", view.getText());
	}
	/**
	 * Test method for {@link com.example.capstone.MainMenu#AdminLogin(android.view.View)}.
	 */
	public void testAdminLogin() 
	{
		 buttonIdAdminLogin = com.example.capstone.R.id.AdminLogin;
		 assertNotNull(activity.findViewById(buttonIdAdminLogin));
		 Button view = (Button) activity.findViewById(buttonIdAdminLogin);
		 assertEquals("Wrong label for the Admin Login button", "Admin Login", view.getText());
	}

	/**
	 * Test method for {@link com.example.capstone.MainMenu#EmailBackup(android.view.View)}.
	 */
	public void testEmailBackup() 
	{
		 buttonIdEmailBackup = com.example.capstone.R.id.EmailBackup;
		 assertNotNull(activity.findViewById(buttonIdEmailBackup));
		 Button view = (Button) activity.findViewById(buttonIdEmailBackup);
		 assertEquals("Wrong label for the Email Backup button", "Email Options", view.getText());
	}

}
