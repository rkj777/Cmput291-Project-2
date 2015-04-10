import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Scanner;
import com.sleepycat.db.*;

public class Hash implements DataBaseType {

	Database database;
	//File name for the table
	private static final String MY_DB_TABLE = "/tmp/egsmith_db";
	private static final int NUM_RECORDS = 1000;
	@Override
	public void populate() {

		int range;
		DatabaseEntry kdbt, ddbt;
		String s;
		
		try {
			DatabaseConfig dbConfig = new DatabaseConfig();
		    dbConfig.setType(DatabaseType.HASH);
		    dbConfig.setAllowCreate(true);
		    database = new Database(MY_DB_TABLE, null, dbConfig);
			System.out.println(MY_DB_TABLE + "successfully created!");
			/*  Generate a random string with the length between 64 and 127,
			 *  inclusive.
			 *
			 *  Seeded once only.
			 *  
			 *  Population maintains nearly the same code as the sample population code
			 */
			Random random = new Random(1000000);

		        try {
		            for (int i = 0; i < NUM_RECORDS; i++) {
		
						/* to generate a key string */
						range = 64 + random.nextInt( 64 );
						s = "";
						for ( int j = 0; j < range; j++ ) {
						  s+=(new Character((char)(97+random.nextInt(26)))).toString();
						}
			
						/* to create a DBT for key */
						kdbt = new DatabaseEntry(s.getBytes());
						kdbt.setSize(s.length()); 
			
				        // to print out the key/data pair
				        // System.out.println(s);	
			
						/* to generate a data string */
						range = 64 + random.nextInt( 64 );
						s = "";
						for ( int j = 0; j < range; j++ ) { 
							s+=(new Character((char)(97+random.nextInt(26)))).toString();
					        // to print out the key/data pair
			                // System.out.println(s);	
			                // System.out.println("");
						}
						
						/* to create a DBT for data */
						ddbt = new DatabaseEntry(s.getBytes());
						ddbt.setSize(s.length()); 
			
						/* to insert the key/data pair into the database */
			            database.putNoOverwrite(null, kdbt, ddbt);
		            }
		        }
		        catch (DatabaseException dbe) {
		            System.err.println("Populate the table: "+dbe.toString());
		            System.exit(1);
		        }		
		    
			database.close();
			database.remove(MY_DB_TABLE,null,null);
		    
		}
		catch (Exception e1) {
			System.err.println("Create Database Failed" + e1.toString());
			System.exit(1);
		}	
		
	}

	@Override
	public void retrieveByKey() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retrieveByData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retrieveByRange() {
		if(database == null){
			System.out.println("Please populate the database before continung");
			return;
		}
		
		// prompt user for range
		System.out.println("Please enter search range.");
		System.out.print("Start: ");
		Scanner s = new Scanner(System.in).useDelimiter(" ");
		String start;
		start = s.next();
		DatabaseEntry startKey;
		try {
			startKey = new DatabaseEntry(start.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			System.err.println("Encoding Exception:" + e1.toString());
			return;
		}
				
		System.out.print("End: ");
		s = new Scanner(System.in).useDelimiter(" ");
		String end = s.next();
		try {
			DatabaseEntry endKey = new DatabaseEntry(end.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			System.err.println("Encoding Exception:" + e1.toString());
			return;
		}
		
		// Create cursor
		
		try {
			Cursor cursor = database.openCursor(null, null);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();
			
			OperationStatus retVal = cursor.getSearchKey(startKey, foundData, LockMode.DEFAULT);
			
		    if (retVal == OperationStatus.NOTFOUND) {
		        System.out.println(foundKey + "/" + foundData + 
		                           " not matched in database " + 
		                           database.getDatabaseName());
		    }
		    
			while(cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				//Right now the loop iterates through entire data structure.
				//cursor.getNext(foundKey, foundData, LockMode.DEFAULT);
				
				
			}
		} catch (DatabaseException e) {
			System.err.println("Create Database Failed" + e.toString());
			System.exit(1);	
		}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
