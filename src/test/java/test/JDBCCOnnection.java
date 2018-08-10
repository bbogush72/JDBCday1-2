package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import utilities.DBUtility;

public class JDBCCOnnection {
 
	String oracleDBUrl = "jdbc:oracle:thin:@ec2-34-219-73-53.us-west-2.compute.amazonaws.com:1521:xe"; 
	String OracleDbUsername = "hr";
	String oracleDbPassword = "hr";
	
	@Test
  public void oracleJDBC() throws SQLException {
		
		Connection connection = DriverManager.getConnection(oracleDBUrl, OracleDbUsername, oracleDbPassword);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * from countries");
		
		//System.out.println(resultSet.getRow());
		
		resultSet.next();
		System.out.println(resultSet.getString(1));
		System.out.println(resultSet.getString("country_id"));
//		
//		resultSet.next();
//		System.out.println(resultSet.getString(1));
//		System.out.println(resultSet.getString("country_id"));
		resultSet.last();
		
		resultSet.first();
		System.out.println(resultSet.getRow());
		
		while(resultSet.next()) {
			System.out.println(resultSet.getString(1));
		}
		
		resultSet.close();
		statement.close();
		connection.close();
		
  }
	
	
	@Test
	  public void oracleJDBC2() throws SQLException {
			
			Connection connection = DriverManager.getConnection(oracleDBUrl, OracleDbUsername, oracleDbPassword);
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = statement.executeQuery("SELECT * from countries");

			ResultSetMetaData rMdata = resultSet.getMetaData();
			//System.out.println("ColumnName: "+ rMdata.getColumnName(1));
			//System.out.println("Column number: "+ rMdata.getColumnCount());
			
			//System.out.println("Column Types: +"DBUtility.colTypes("SELECT * from countries"));
			
			List<Map<String, Object>> list = new ArrayList<>();
			int colCount = rMdata.getColumnCount();
			
			while(resultSet.next()) {
				Map<String, Object> rowMap = new HashMap<>();
				
					for (int i = 1; i <= colCount; i++) {
						rowMap.put(rMdata.getColumnName(i), resultSet.getObject(i));
					}
				
				list.add(rowMap);
			}
			
			System.out.println(list);
			
			
	  }

}
