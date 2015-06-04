package com.slm.ospiui.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import android.test.suitebuilder.annotation.SmallTest;

import com.slm_ospiui.ProgSumListItem;

import junit.framework.TestCase;


/*
 * Modification History:
 * 	
 * 	05/07/15 Created
 * 
 */
public class ProgSumListItemTest extends TestCase {

	public ProgSumListItemTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	/*
	 * To run this test (which is in SLM-OspiUI/tests, package name = com.slm.ospiui.tests)
	 * - Right click on folder name tests
	 * - Select Run As JUnit Test
	 * - Check "use configuration specific settings" and
	 * - select "Android JUnit Test Launcher" then
	 * - Click OK
	 * Should run all tests.
	 * 
	 * OR
	 * - Right click on package com.slm.ospiui.test
	 * - Select Run As JUnit Test
	 * - Check "use configuration specific settings" and
	 * - select "Android JUnit Test Launcher" then
	 * - Select any and all tests you wish to run and 
	 * - Click OK
	 * Should run all selected tests.
	 */
	
	/**
	 * testGetDays0String - Test Method to test the getDays0String() operation 
	 * 	of ProgSumListItem
	 */
	public final void testGetDays0String() {
		ProgSumListItem tester = new ProgSumListItem();
		
		tester.setmDays0(1);
		assertEquals("testing days0 = 1 = M", "M", tester.getDays0String());
		
		tester.setmDays0(2);
		assertEquals("testing days0 = 2 = Tu", "Tu", tester.getDays0String());
		
		tester.setmDays0(3);
		assertEquals("testing days0 = 3 = M,Tu", "M,Tu", tester.getDays0String());
		
		tester.setmDays0(4);
		assertEquals("testing days0 = 4 = W", "W", tester.getDays0String());
		
		tester.setmDays0(5);
		assertEquals("testing days0 = 5 = M,W", "M,W", tester.getDays0String());
		
		tester.setmDays0(6);
		assertEquals("testing days0 = 6 = Tu,W", "Tu,W", tester.getDays0String());
		
		tester.setmDays0(7);
		assertEquals("testing days0 = 7 = M,Tu,W", "M,Tu,W", tester.getDays0String());
		
		tester.setmDays0(8);
		assertEquals("testing days0 = 8 = Th", "Th", tester.getDays0String());

		tester.setmDays0(9);
		assertEquals("testing days0 = 9 = M,Th", "M,Th", tester.getDays0String());

		tester.setmDays0(10);
		assertEquals("testing days0 = 10 = Tu,Th", "Tu,Th", tester.getDays0String());

		tester.setmDays0(11);
		assertEquals("testing days0 = 11 = M,Tu,Th", "M,Tu,Th", tester.getDays0String());

		tester.setmDays0(12);
		assertEquals("testing days0 = 12 = W,Th", "W,Th", tester.getDays0String());

		tester.setmDays0(13);
		assertEquals("testing days0 = 13 = M,W,Th", "M,W,Th", tester.getDays0String());

		tester.setmDays0(14);
		assertEquals("testing days0 = 14 = Tu,W,Th", "Tu,W,Th", tester.getDays0String());

		tester.setmDays0(15);
		assertEquals("testing days0 = 15 = M,Tu,W,Th", "M,Tu,W,Th", tester.getDays0String());
		
		tester.setmDays0(16);
		assertEquals("testing days0 = 16 = F", "F", tester.getDays0String());

		tester.setmDays0(17);
		assertEquals("testing days0 = 17 = M,F", "M,F", tester.getDays0String());

		tester.setmDays0(31);
		assertEquals("testing days0 = 31 = M,Tu,W,Th,F", "M,Tu,W,Th,F", tester.getDays0String());
		
		tester.setmDays0(32);
		assertEquals("testing days0 = 32 = Sa", "Sa", tester.getDays0String());

		tester.setmDays0(51);
		assertEquals("testing days0 = 51 = M,Tu,F,Sa", "M,Tu,F,Sa", tester.getDays0String());

		tester.setmDays0(63);
		assertEquals("testing days0 = 63 = M,Tu,W,Th,F,Sa", "M,Tu,W,Th,F,Sa", tester.getDays0String());

		tester.setmDays0(64);
		assertEquals("testing days0 = 64 = Su", "Su", tester.getDays0String());

		tester.setmDays0(88);
		assertEquals("testing days0 = 88 = Th,F,Su", "Th,F,Su", tester.getDays0String());

		tester.setmDays0(127);
		assertEquals("testing days0 = 127 = M,Tu,W,Th,F,Sa,Su", "M,Tu,W,Th,F,Sa,Su", tester.getDays0String());
	}
	
	/**
	 * testGetCircuitsString - Test Method to test the getCircuitsString()
	 * 	operation of ProgSumListItem
	 */
	public final void testGetCircuitsString() {
		ProgSumListItem tester = new ProgSumListItem();
		
		tester.setmCircuits(1);
		assertEquals("testing mCircuits = 1", "1", tester.getCircuitsString());
		
		tester.setmCircuits(2);
		assertEquals("testing mCircuits = 2", "2", tester.getCircuitsString());
		
		tester.setmCircuits(3);
		assertEquals("testing mCircuits = 3", "1,2", tester.getCircuitsString());
		
		tester.setmCircuits(4);
		assertEquals("testing mCircuits = 4", "3", tester.getCircuitsString());
		
		tester.setmCircuits(8);
		assertEquals("testing mCircuits = 8", "4", tester.getCircuitsString());
		
		tester.setmCircuits(16);
		assertEquals("testing mCircuits = 16", "5", tester.getCircuitsString());
		
		tester.setmCircuits(32);
		assertEquals("testing mCircuits = 32", "6", tester.getCircuitsString());
		
		tester.setmCircuits(64);
		assertEquals("testing mCircuits = 64", "7", tester.getCircuitsString());
		
		tester.setmCircuits(128);
		assertEquals("testing mCircuits = 128", "8", tester.getCircuitsString());
		
		tester.setmCircuits(255);
		assertEquals("testing mCircuits = 255", "1,2,3,4,5,6,7,8", tester.getCircuitsString());
	}	
	
	
/*	
	public final void testPiRead() 
	{
		String line = "";
        String splitDelim = ",";
        BufferedReader br = null;
        
		try 
		{
//			File myFile = new File(new URI("file:///192.168.1.41/home/pi/downloads/OSPi/data/programs.json"));
			File myFile = new File("192.168.1.41:/home/pi/downloads/OSPi/data/programs.json");
//			
//			File myFile = new File("/Users/s/programs.json");
			
			
			//  InputStream targetStream = new FileInputStream(initialFile);
			 br = new BufferedReader(new InputStreamReader(new FileInputStream(myFile)));
			 
			 if (( line = br.readLine()) != null)
			 {
				 assertNotNull("programs.json is not empty", line );
			 }
			 else
				 assertNull("programs.json is empty", line );
					 
		} 
		
		catch (URISyntaxException e) {
			fail("URISyntaxException");
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		catch (FileNotFoundException e) 
		{
			fail("FileNotFoundException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) 
		{
			fail("IOException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
}
