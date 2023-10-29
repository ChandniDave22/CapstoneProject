package Capstoneproject_3_java.Capstoneproject_3_java;

import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelConfig {
	public static XSSFWorkbook wb;
	public static XSSFSheet sheet;

	public ExcelConfig(String excelPath) {
		try {
			FileInputStream fis = new FileInputStream(excelPath);
			wb = new XSSFWorkbook(fis);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String readData(String sheetname, int row, int column) {

		sheet = wb.getSheet(sheetname);
		String data = sheet.getRow(row).getCell(column).getStringCellValue();
		return data;
	}

	public int getRowCount(int sheetIndex) {
		int row = wb.getSheetAt(sheetIndex).getLastRowNum();
		row = row + 1;
		return row;
	}

}