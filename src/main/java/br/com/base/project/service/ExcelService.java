package br.com.base.project.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import br.com.base.project.model.Atendimento;

@Service
public class ExcelService {

    public void excelAtendimento(HttpServletResponse response, List<Atendimento> atendimentos, LocalDate dataInicial, LocalDate dataFinal) throws IOException {
    	
    	XSSFWorkbook workbook = new XSSFWorkbook();
    	XSSFSheet sheet = workbook.createSheet("Atendimento");
    	writeInfoHeaderLine(workbook, sheet, dataInicial, dataFinal);
    	
    	if (atendimentos.size() > 0) {
    		writeHeaderLine(workbook, sheet);
    		writeDataLines(atendimentos, workbook, sheet, this.atendimentoProcessor(atendimentos));
		}else {
		    CellStyle style = workbook.createCellStyle();
	        XSSFFont font = workbook.createFont();
	        font.setBold(true);
	        font.setFontHeight(16);
	        style.setFont(font);
			createCell(sheet.createRow(2), 0, "Sem dados para a data selecionada!", style, sheet);
		}
    	
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    	
    }
     
 
 
    private Integer atendimentoProcessor(List<Atendimento> atendimentos) {
    	
    	Integer valorTotal = 0;
    	
    	Map<Integer, List<Atendimento>> monthsOfYear = atendimentos.stream().collect(Collectors.groupingBy(d -> d.getDataAtendimento().get(ChronoField.MONTH_OF_YEAR)));
   	
	   	for (List<Atendimento> monthOfMonth : monthsOfYear.values()) {
	   		
	   		Map<Integer, List<Atendimento>> daysOfMonth = monthOfMonth.stream().collect(Collectors.groupingBy(d -> d.getDataAtendimento().get(ChronoField.DAY_OF_MONTH)));
			
	   		for (List<Atendimento> dayOfMonth : daysOfMonth.values()) {
	   			
	   			Map<Integer, List<Atendimento>> hoursOfDay = dayOfMonth.stream().collect(Collectors.groupingBy(d -> d.getDataAtendimento().get(ChronoField.HOUR_OF_DAY)));
	   			
	   			for (List<Atendimento> list1 : hoursOfDay.values()) {
	   				if (list1.size() == 1) {
	   					if (isAtendimentoValorUnico(list1)) {
							valorTotal+=25;
						}else {
							valorTotal += 15;
						}
	   				}else if (list1.size() == 2) {
	   					valorTotal += 20;
	   				}else {
	   					valorTotal += 25;
	   				}
	   			}
	   		}
		}
	   	
	   	return valorTotal;
	}

	private boolean isAtendimentoValorUnico(List<Atendimento> list1) {
		return list1.get(0).getTipoAtendimento().equalsIgnoreCase("Massagem") ||
				list1.get(0).getTipoAtendimento().equalsIgnoreCase("Fisioterapia") ||
				list1.get(0).getTipoAtendimento().equalsIgnoreCase("Pilates individual") ||
				list1.get(0).getTipoAtendimento().equalsIgnoreCase("Avalia????o");
	}

	private void writeHeaderLine(XSSFWorkbook workbook, XSSFSheet sheet) {
         
	    Row row = sheet.createRow(2);
	    
	    CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Nome do Fisioterapeuta", style, sheet);      
        createCell(row, 1, "Tipo do Atendimento", style, sheet);
        createCell(row, 2, "Data do Atendimento", style, sheet);    
        createCell(row, 3, "Horario do Atendimento", style, sheet);    
        createCell(row, 4, "Nome do Aluno", style, sheet);       
         
    }
    
    private void writeInfoHeaderLine(XSSFWorkbook workbook, XSSFSheet sheet, LocalDate dataInicial, LocalDate dataFinal) {
        
	    Row row = sheet.createRow(0);
	    sheet.addMergedRegion(CellRangeAddress.valueOf("A1:J1"));
	    CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Parametros da pesquisa para a Data de : "
        		.concat(dataInicial.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        		.concat(" At?? ")
        		.concat(dataFinal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))), style, sheet);      
         
         
    }
     
    private void createCell(Row row, int columnCount, Object value, CellStyle style, XSSFSheet sheet) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof LocalDate) {
        	cell.setCellValue((String) LocalDate.parse(value.toString()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else if (value instanceof LocalTime) {
        	cell.setCellValue((String) LocalTime.parse(value.toString()).format(DateTimeFormatter.ofPattern("HH:mm")));
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines(List<Atendimento> atendimentos, XSSFWorkbook workbook, XSSFSheet sheet, Integer valorTotalAtendimentos) {
        int rowCount = 4;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        
        for (Atendimento atendimento : atendimentos) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            
            createCell(row, columnCount++, atendimento.getNomeFisioterapeuta(), style, sheet);
            createCell(row, columnCount++, atendimento.getTipoAtendimento(), style, sheet);
            createCell(row, columnCount++, atendimento.getDataAtendimento().toLocalDate(), style, sheet);
            createCell(row, columnCount++, atendimento.getDataAtendimento().toLocalTime(), style, sheet);
            createCell(row, columnCount++, atendimento.getNomeAluno(), style, sheet);
             
        }
        
        Row row = sheet.createRow(rowCount+=2);

        CellStyle styleAux = workbook.createCellStyle();
        XSSFFont fontAux = workbook.createFont();
        fontAux.setBold(true);
        fontAux.setFontHeight(16);
        styleAux.setFont(fontAux);
        
        createCell(row, 0, "Total de registros:", styleAux, sheet);
        createCell(row, 3, "Valor Total a Receber:", styleAux, sheet);
        styleAux.setAlignment(HorizontalAlignment.LEFT);
        createCell(row, 1, atendimentos.size(), styleAux, sheet);
        createCell(row, 4, NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(new BigDecimal(valorTotalAtendimentos)), styleAux, sheet);
    }
}
