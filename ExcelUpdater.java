import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUpdater {

    // Updates a specific cell in the Excel sheet based on header, new value, and hospitalID
    public void updater(String filePath, String hospitalID, String header, String newValue) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        // Locate the column index for the specified header
        Row headerRow = sheet.getRow(0);
        int headerColumnIndex = -1;
        for (Cell cell : headerRow) {
            if (cell.getStringCellValue().equalsIgnoreCase(header)) {
                headerColumnIndex = cell.getColumnIndex();
                break;
            }
        }

        // If header column was not found, throw an exception
        if (headerColumnIndex == -1) {
            workbook.close();
            fis.close();
            throw new IllegalArgumentException("Header \"" + header + "\" not found in the Excel file.");
        }

        // Find the row with the matching hospitalID
        boolean rowFound = false;
        for (Row row : sheet) {
            Cell idCell = row.getCell(0); // Assuming the hospitalID is in the first column
            if (idCell != null && idCell.getCellType() == CellType.STRING && idCell.getStringCellValue().equals(hospitalID)) {
                Cell cellToUpdate = row.getCell(headerColumnIndex);
                if (cellToUpdate == null) {
                    cellToUpdate = row.createCell(headerColumnIndex);
                }
                cellToUpdate.setCellValue(newValue); // Update the cell with the new value
                rowFound = true;
                break;
            }
        }

        // If no row with the specified hospitalID was found, throw an exception
        if (!rowFound) {
            workbook.close();
            fis.close();
            throw new IllegalArgumentException("Hospital ID \"" + hospitalID + "\" not found in the Excel file.");
        }

        // Write the updated workbook back to the file
        fis.close(); // Close the input stream before writing
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}