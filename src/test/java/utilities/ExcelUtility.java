package utilities;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {

	private XSSFSheet workSheet;
	private XSSFWorkbook workBook;
	private String path;

	public ExcelUtility(String path, String sheetName) {
		this.path = path;
		try {
			// Open the Excel file
			FileInputStream ExcelFile = new FileInputStream(path);
			// Access the required test data sheet
			workBook = new XSSFWorkbook(ExcelFile);
			workSheet = workBook.getSheet(sheetName);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getCellData(int rowNum, int colNum) {
		XSSFCell cell;
		try {
			cell = workSheet.getRow(rowNum).getCell(colNum);
			String cellData = cell.toString();
			return cellData;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}


	public List<Map<String, String>> getDataList() {
		// get all columns
		List<String> columns = getColumnsNames();
		// this will be returned
		List<Map<String, String>> data = new ArrayList<>();

		DataFormatter dataFormatter = new DataFormatter();
		
		for (int i = 1; i < rowCount(); i++) {
			// get each row
			Row row = workSheet.getRow(i);
			// create map of the row using the column and value
			// column map key, cell value --> map bvalue
			Map<String, String> rowMap = new HashMap<>();
			for (Cell cell : row) {
				int columnIndex = cell.getColumnIndex();
				//rowMap.put(columns.get(columnIndex), cell.toString());
				rowMap.put(columns.get(columnIndex), dataFormatter.formatCellValue(cell));
			}

			data.add(rowMap);
		}

		return data;
	}

	public List<String> getColumnsNames() {
		List<String> columns = new ArrayList<>();

		for (Cell cell : workSheet.getRow(0)) {
			columns.add(cell.toString());
		}
		return columns;
	}

	public int columnCount() {
		return workSheet.getRow(0).getLastCellNum();
	}

	public int rowCount() {
		return workSheet.getPhysicalNumberOfRows();
	}

	
	
}
