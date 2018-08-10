package utilities;

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

import org.apache.poi.ss.usermodel.DataFormatter;
import org.testng.annotations.Test;

public class DBUtility {

	private static Connection connection; 
	private static Statement statement; 
	private static ResultSet resultSet; 
	
	
	public static void establishConnection(DBType dbtype) throws SQLException {
		switch(dbtype) {
			case ORACLE: 
				connection = DriverManager.getConnection(ConfigurationReader.getProperty("oracleurl"),
														 ConfigurationReader.getProperty("oracledb.username"),
						                                 ConfigurationReader.getProperty("oracledb.password"));
				break;
			default:
				connection = null;
		}
	}

	
	public static int rowsCount(String sql) throws SQLException{
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		resultSet = statement.executeQuery(sql);

		resultSet.last();
		return resultSet.getRow();
	}
	
	public static List<String> colTypes(String sql) throws SQLException{
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		resultSet = statement.executeQuery(sql);
		
		ResultSetMetaData rMdata = resultSet.getMetaData();
		List<String> colList = new ArrayList<>();
		
		for (int i = 1; i <= rMdata.getColumnCount(); i++) {
			colList.add(i+":"+rMdata.getColumnName(i));
		}

		return colList;
	}

	
	public static List<String> columnName(String sql) throws SQLException{
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		resultSet = statement.executeQuery(sql);
		ResultSetMetaData rMdata = resultSet.getMetaData();
		
		List<String> colNames = new ArrayList<>();
		for (int i = 1; i <= rMdata.getColumnCount(); i++) {
			colNames.add(rMdata.getColumnName(i));
		}
			
		return colNames;
	}

	public static int columnCount(String sql) throws SQLException{
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		resultSet = statement.executeQuery(sql);
		ResultSetMetaData rMdata = resultSet.getMetaData();
		
		return rMdata.getColumnCount();
	}

	public static String getCellData(String sql, int row, int col) throws SQLException{
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		resultSet = statement.executeQuery(sql);
		ResultSetMetaData rMdata = resultSet.getMetaData();
		
		String cellData = "";
		int cnt = 1;
		while(resultSet.next() && cnt <= row) {
			cellData = resultSet.getString(col);
			cnt++;
		}
		return cellData;
	}

	public static List<Map<String,String>> runSQLQuery(String sql) throws SQLException{
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		resultSet = statement.executeQuery(sql);
		ResultSetMetaData rMdata = resultSet.getMetaData();

		List<Map<String, String>> list = new ArrayList<>();
		DataFormatter dataFormatter = new DataFormatter();
		int colCount = rMdata.getColumnCount();
		
		
		while(resultSet.next()) {
			Map<String, String> rowMap = new HashMap<>();
			
				for (int i = 1; i <= colCount; i++) {
					
					rowMap.put(rMdata.getColumnName(i), resultSet.getString(i));
				}
				
			list.add(rowMap);
		}
		return list;
	}
	
	public static void closeConnections() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
