package test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import utilities.DBType;
import utilities.DBUtility;
import utilities.ExcelUtility;

public class CountriesDBTest {

	@BeforeClass
	public void setUp() throws SQLException {
		DBUtility.establishConnection(DBType.ORACLE);
	}

	@AfterClass
	public void tearDown() {
		DBUtility.closeConnections();
	}

	@Test
	public void countTest() throws SQLException {
		String sql = "select * from employees where job_id = 'IT_PROG'";
		int rowsCount = DBUtility.rowsCount(sql);
		assertTrue(rowsCount > 0);
		System.out.println(rowsCount);
	}

	@Test
	public void nameTestByID() throws SQLException {
		String sql = "select first_name, last_name from employees where employee_id = 105";
		List<Map<String, String>> empData = DBUtility.runSQLQuery(sql);
		assertEquals("David", empData.get(0).get("FIRST_NAME"));
		assertEquals("Austin", empData.get(0).get("LAST_NAME"));

		System.out.println(empData.get(0).get("FIRST_NAME"));
		System.out.println(empData.get(0).get("LAST_NAME"));

	}

	
	
	@Test
	public void getColTypes() throws SQLException {

		String sql = "select * from COUNTRIES";
		List<String> dbRead = DBUtility.colTypes(sql);
		System.out.println(dbRead);
	}

	
	@Test
	public void getColNames() throws SQLException {

		String sql = "select * from COUNTRIES";
		List<String> dbRead = DBUtility.columnName(sql);
		System.out.println(dbRead);

	}

	@Test
	public void getColNumber() throws SQLException {

		String sql = "select * from COUNTRIES";
		int dbRead = DBUtility.columnCount(sql);
		System.out.println(dbRead);

	}

	@Test
	public void getCellData() throws SQLException {

		String sql = "select * from COUNTRIES";
		String dbRead = DBUtility.getCellData(sql, 24, 3);
		System.out.println(dbRead);
	}

	@Test
	public void compareDBandExcel() throws SQLException {

		ExcelUtility excelObject = new ExcelUtility("./Country.xlsx", "Sheet1");

		List<Map<String, String>> exelRead = excelObject.getDataList();
		System.out.println(exelRead);
		System.out.println(exelRead.size());

		String sql = "select * from COUNTRIES";
		List<Map<String, String>> dbRead = DBUtility.runSQLQuery(sql);
		System.out.println(dbRead);
		System.out.println(dbRead.size());

		System.out.println(dbRead.equals(exelRead));

		assertEquals(exelRead, dbRead);

	}
}
