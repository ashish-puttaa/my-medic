import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.lang.SuppressWarnings;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

// iText API - PDF
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Element;

// Apache POI API - XLS
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@WebServlet("/Download")
public class DownloadServlet extends HttpServlet {

public static byte[] createPDF(ArrayList<String[]> table_contents, String file_heading) {
        ByteArrayOutputStream baos = null;
        Document document = new Document();

        try {
                baos = new ByteArrayOutputStream();
                PdfWriter writer = PdfWriter.getInstance(document, baos);
                document.open();

                // Setting attributes
                document.addAuthor("Ashish");
                document.addCreationDate();
                document.addCreator("Doctor Appointment App");
                document.addTitle(file_heading);
                // document.addSubject("List of all appointments");

                int no_of_cols = table_contents.get(0).length;
                PdfPTable table = new PdfPTable(no_of_cols);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);


                float[] columnWidths = new float[no_of_cols];
                Arrays.fill(columnWidths, 1f);
                table.setWidths(columnWidths);

                Iterator<String[]> table_contents_iter = table_contents.iterator();

                while(table_contents_iter.hasNext()) {
                        String[] table_record = table_contents_iter.next(); // Note : First table record is the heading

                        for(String data : table_record) {
                                PdfPCell cell = new PdfPCell(new Paragraph(data));
                                cell.setPaddingLeft(10);
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                table.addCell(cell);
                        }

                }

                document.add(table);

                document.close();
                writer.close();
        }
        catch(DocumentException e) {
                e.printStackTrace();
        }

        return baos.toByteArray();
}


public static byte[] createXLS(ArrayList<String[]> table_contents, String file_heading) {

        // System.out.println("Creating XLSS");
        XSSFWorkbook workbook = new XSSFWorkbook();
        // System.out.println("Created Workbook");
        XSSFSheet sheet = workbook.createSheet(file_heading);
        // System.out.println("Created sheet");
        ByteArrayOutputStream baos = null;

        int rowNum = 0;

        Iterator<String[]> table_contents_iter = table_contents.iterator();

        while(table_contents_iter.hasNext()) {
                // System.out.println("Writing rows");
                String[] table_record = table_contents_iter.next();

                Row row = sheet.createRow(rowNum++);

                int colNum = 0;
                for(String data : table_record) {
                        // System.out.println("Writing column");
                        Cell cell = row.createCell(colNum++);
                        cell.setCellValue(data);
                }
        }

        try {
                baos = new ByteArrayOutputStream();
                workbook.write(baos);
                workbook.close();
        }
        catch(IOException e) {
                e.printStackTrace();
        }

        // System.out.println("Content : " + baos.toString());
        return baos.toByteArray();

}

public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        @SuppressWarnings("unchecked")
        ArrayList<String[]> table_contents = (ArrayList<String[]>) request.getAttribute("list");
        String download_format = request.getParameter("download_format");

        String file_name = (String) request.getAttribute("file_name");
        String file_heading = (String) request.getAttribute("file_heading");

        if(download_format.equals("pdf")) {
                response.setContentType("application/pdf");
                response.setHeader("Content-disposition", "attachment; filename=" + file_name + LocalDateTime.now() + ".pdf");
                // System.out.println("Content-disposition, attachment; filename=" + file_name + LocalDateTime.now() + ".pdf");

                try(OutputStream out = response.getOutputStream()) {
                        byte[] buffer = createPDF(table_contents, file_heading);
                        int numBytesRead;
                        out.write(buffer);
                }
                return;
        }

        if(download_format.equals("xls")) {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-disposition", "attachment; filename=" + file_name + LocalDateTime.now() + ".xlsx");

                try(OutputStream out = response.getOutputStream()) {
                        byte[] buffer = createXLS(table_contents, file_heading);
                        int numBytesRead;
                        out.write(buffer);
                }
                return;
        }

}

public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
}
}
