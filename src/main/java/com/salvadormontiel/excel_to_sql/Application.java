package com.salvadormontiel.excel_to_sql;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

/**
 * Created by Salvador Montiel on 14/nov/2018.
 */
public class Application {
    public static final String SAMPLE_XLSX_FILE_PATH = "/home/salvador/Escritorio/Productos.xlsx";

    public static void main(String[] args) throws IOException, InvalidFormatException {
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));

        // Retrieving the number of sheets in the Workbook
        // System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

        /*
           =============================================================
           Iterating over all the sheets in the workbook
           =============================================================
        */

        /*System.out.println("Retrieving Sheets");
        workbook.forEach(sheet -> {
            System.out.println("=> " + sheet.getSheetName());
        });*/

        /*
           ==================================================================
           Iterating over all the rows and columns in a Sheet (Multiple ways)
           ==================================================================
        */

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        // 1. You can obtain a rowIterator and columnIterator and iterate over them
        /*System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Now let's iterate over the columns of the current row
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = dataFormatter.formatCellValue(cell);
                System.out.print(cellValue + "\t");
            }
            System.out.println();
        }

        // 2. Or you can use a for-each loop to iterate over the rows and columns
        System.out.println("\n\nIterating over Rows and Columns using for-each loop\n");
        for (Row row: sheet) {
            for(Cell cell: row) {
                String cellValue = dataFormatter.formatCellValue(cell);
                System.out.print(cellValue + "\t");
            }
            System.out.println();
        }*/

        // 3. Or you can use Java 8 forEach loop with lambda
        //System.out.println("\n\nIterating over Rows and Columns using Java 8 forEach with lambda\n");

        sheet.forEach(row -> {
            int i = 0;

            String barcode = "";
            String name = "";
            double precioCompra = 0D;
            double precioVenta = 0D;
            int porcentajeDescuento = 0;
            String category = "";
            String description = "";
            String formato = "";
            String url = "";

            for (Cell cell : row) {
                String cellValue = dataFormatter.formatCellValue(cell);

                if (i == 1) barcode = cellValue;
                if (i == 2) name = cellValue;

                if (i == 3) {
                    String aa = cellValue.substring(1).replace(",", "");
                    precioCompra = Double.parseDouble(aa);
                }
                if (i == 4) {
                    String cc = cellValue.substring(1).replace(",", "");
                    precioVenta = Double.parseDouble(cc);
                }
                if (i == 5) porcentajeDescuento = !hasDiscount() ? 0 : (int) (StrictMath.random() * 30);
                if (i == 6) category = cellValue;
                if (i == 7) description = cellValue;
                if (i == 8) formato = cellValue;
                if (i == 9) url = cellValue;
                //System.out.print(cellValue + " ");
                //String template = "INSERT INTO producto (codigo_barras, nombre, descripcion, url_foto, formato) " +
                //        "VALUES () RETURNING id;";
                //String eee = ;
                i++;
            }
            String idProduct = "with rows as ( INSERT INTO producto (codigo_barras, nombre, descripcion, url_foto, formato, precio_compra, precio_venta, porcentaje_descuento) " +
                            "VALUES ('"+barcode+"', '"+name+"', '"+description+"', '"+url+"', '"+formato+"', "+precioCompra+", "+precioVenta+", "+porcentajeDescuento+") RETURNING id )";
            String idCategoria = "SELECT id FROM categoria WHERE nombre = '" + category + "'";

            String insert = "INSERT INTO categoria_producto(id_categoria, id_producto) VALUES((" + idCategoria + "), ( SELECT id FROM rows ) );";
            System.out.println(idProduct + " " + insert);

            //System.out.println();
        });

        // Closing the workbook
        workbook.close();
    }

    private static boolean hasDiscount() {
        int res = (int) (StrictMath.random() * 10);
        return res == 1;
    }
}
