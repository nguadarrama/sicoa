package mx.gob.segob.dgtic.web.mvc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class Excel extends AbstractExcelView {
    @SuppressWarnings("unchecked")
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, 
    		HttpServletRequest request, HttpServletResponse response) {
    	
        //configuración archivo excel
        String nombreHoja = (String) model.get("nombreHoja");
        List<String> cabeceras = (List<String>)model.get("cabeceras");
        List<List<String>> registros = (List<List<String>>) model.get("registros");
        List<String> numericColumns = new ArrayList<String>();
        
        if (model.containsKey("numericcolumns")) {
            numericColumns = (List<String>)model.get("numericcolumns");
        }
        
        //construye el documento
        HSSFSheet sheet = workbook.createSheet(nombreHoja);
        sheet.setDefaultColumnWidth((short) 25);
        int filaActual = 0;
        short columnaActual = 0;
        
        //estilo para cabecera
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        HSSFFont headerFont = workbook.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont); 
        
        //pobla las cabeceras
        HSSFRow headerRow = sheet.createRow(filaActual);
        for(String header : cabeceras){
            HSSFRichTextString text = new HSSFRichTextString(header);
            HSSFCell cell = headerRow.createCell(columnaActual); 
            cell.setCellStyle(headerStyle);
            cell.setCellValue(text);            
            columnaActual++;
        }
        
        //pobla los registros
        filaActual++;//exclude header
        
        for(List<String> registro: registros) {
            columnaActual = 0;
            HSSFRow fila = sheet.createRow(filaActual);
            
            for(String value : registro) { //para contar número de columnas
                
            	HSSFCell cell = fila.createCell(columnaActual);
                
            	if (numericColumns.contains(cabeceras.get(columnaActual))) {
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                } else {
                    HSSFRichTextString text = new HSSFRichTextString(value);                
                    cell.setCellValue(text);                    
                }
            	
                columnaActual++;
            }
            
            filaActual++;
        }
    }
}
