package com.ccabank.feedbackservice.util.excel;

import com.ccabank.feedbackservice.dto.feedback.*;
import com.ccabank.feedbackservice.entity.Answer;
import com.ccabank.feedbackservice.entity.Feedback;
import com.ccabank.feedbackservice.mappers.AgencyMapper;
import com.ccabank.feedbackservice.mappers.FeedbackMapper;
import com.ccabank.feedbackservice.mappers.StaffMapper;
import com.ccabank.feedbackservice.openfeign.UserRestClient;
import com.ccabank.feedbackservice.repository.AgencyRepository;
import com.ccabank.feedbackservice.repository.FeedbackRepository;
import com.ccabank.feedbackservice.repository.StaffRepository;
import com.ccabank.feedbackservice.service.impl.AnswerService;
import com.ccabank.feedbackservice.service.impl.QuestionService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.ccabank.feedbackservice.constant.BeanIdConstant.FEEDBACK_DETAIL_SERVICE;


@Service
@Transactional
@Qualifier(FEEDBACK_DETAIL_SERVICE)
public class SheetExcelGenerator {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private AgencyMapper agencyMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UserRestClient userRestClient;

    public XSSFWorkbook generateFeedbackChoiceOccurences(XSSFWorkbook workbook, List<FeedbackDto> feedbackDtos, String property , String sheetName){

        Sheet sheet = workbook.createSheet(sheetName);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.VIOLET.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font =  workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        font.setBold(false);
        font.setColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFont(font);

        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        font =  workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 9);
        font.setItalic(true);
        font.setColor(IndexedColors.VIOLET.getIndex());
        cellStyle2.setFont(font);

        cellStyle2.setBorderTop(BorderStyle.MEDIUM);
        cellStyle2.setBorderRight(BorderStyle.MEDIUM);
        cellStyle2.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle2.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle2.setAlignment(HorizontalAlignment.LEFT);
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle cellStyle3 = workbook.createCellStyle();
        cellStyle3.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        font =  workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 9);
        cellStyle3.setFont(font);

        cellStyle3.setBorderTop(BorderStyle.MEDIUM);
        cellStyle3.setBorderRight(BorderStyle.MEDIUM);
        cellStyle3.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle3.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle3.setAlignment(HorizontalAlignment.LEFT);
        cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);


        QuestionDto question = questionService.getQuestion(property);

        // Écrire l'en-tête
        Row headerRow = sheet.createRow(0);

        Cell cell = headerRow.createCell(0);

        cell.setCellStyle(cellStyle);
        cell.setCellValue("Valeurs ");

        cell = headerRow.createCell(1);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue("Propriétés");

        cell = headerRow.createCell(2);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue("Nombres");


        int i = 1 ;

        for(QuestionChoiceDto choice : question.getChoices()){

            headerRow = sheet.createRow(i);
            cell = headerRow.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(choice.getLabel());

            cell = headerRow.createCell(1);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(choice.getValue());

            int nbChoice = 0;

            for (FeedbackDto feedbackDto : feedbackDtos) {
                for (AnswerDto answerDto : feedbackDto.getAnswerCollection()){

                    if(!answerDto.getQuestion().equals("visitCause")){
                        continue;
                    }
                    if(answerDto.getAnswer().equals(choice.getValue())){
                        nbChoice++;
                    }
                }
            }

            cell = headerRow.createCell(2);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(nbChoice);

            i = i + 1 ;
        }


        sheet.setColumnWidth(0, 75 * 256);
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 5 * 256);
        sheet.setColumnWidth(5, 5 * 256);
        sheet.setColumnWidth(6, 5 * 256);
        sheet.setColumnWidth(7, 5 * 256);
        sheet.setColumnWidth(8, 5 * 256);
        sheet.setColumnWidth(9, 5 * 256);

        return workbook;

    }

    public XSSFWorkbook generateFeedbackEvaluation(XSSFWorkbook workbook, EvaluationPeriodStaffDto evaluation, String sheetName){

        Sheet sheet = workbook.createSheet("FEEDBACKS EVALUATION");


        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.VIOLET.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font =  workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        font.setBold(false);
        font.setColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFont(font);

        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        font =  workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 9);
        font.setItalic(true);
        font.setColor(IndexedColors.VIOLET.getIndex());
        cellStyle2.setFont(font);

        cellStyle2.setBorderTop(BorderStyle.MEDIUM);
        cellStyle2.setBorderRight(BorderStyle.MEDIUM);
        cellStyle2.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle2.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle2.setAlignment(HorizontalAlignment.LEFT);
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle cellStyle3 = workbook.createCellStyle();
        cellStyle3.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        font =  workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 9);
        cellStyle3.setFont(font);

        cellStyle3.setBorderTop(BorderStyle.MEDIUM);
        cellStyle3.setBorderRight(BorderStyle.MEDIUM);
        cellStyle3.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle3.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle3.setAlignment(HorizontalAlignment.LEFT);
        cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);



        List<QuestionDto> questions = questionService.getAllQuestions("fr");
        // Écrire l'en-tête
        Row headerRow = sheet.createRow(0);

        Cell cell = headerRow.createCell(0);

        cell.setCellStyle(cellStyle);
        cell.setCellValue("Questions");

        cell = headerRow.createCell(1);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue("Valeur");

        cell = headerRow.createCell(2);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue("Nombres");

        cell = headerRow.createCell(3);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue("Pourcentages");

        cell = headerRow.createCell(4);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue("0");

        cell = headerRow.createCell(5);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue("1");

        cell = headerRow.createCell(6);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue("2");

        cell = headerRow.createCell(7);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue("3");

        cell = headerRow.createCell(8);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue("4");

        cell = headerRow.createCell(9);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue("5");

        int column = 0;
        int row = 1;



        // Écrire les données
        for (EvaluationItem item : evaluation.getEvaluations()) {

            headerRow = sheet.createRow(row);
            cell = headerRow.createCell(column);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(item.getLabel());

            cell = headerRow.createCell(column + 1);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(item.getElement());

            cell = headerRow.createCell(column + 2);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(item.getCount());

            cell = headerRow.createCell(column + 3);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(item.getPourcent());

            cell = headerRow.createCell(column + 4);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(0);
            Optional<CountNoteDto> optional = item.getCountNoteDtoByNote("0");
            if(optional.isPresent()){
                int count = optional.get().getCount();
                cell.setCellValue(count);
            }

            cell = headerRow.createCell(column + 5);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(0);
            optional = item.getCountNoteDtoByNote("1");
            if(optional.isPresent()){
                int count = optional.get().getCount();
                cell.setCellValue(count);
            }

            cell = headerRow.createCell(column + 6);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(0);
            optional = item.getCountNoteDtoByNote("2");
            if(optional.isPresent()){
                int count = optional.get().getCount();
                cell.setCellValue(count);
            }

            cell = headerRow.createCell(column + 7);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(0);
            optional = item.getCountNoteDtoByNote("3");
            if(optional.isPresent()){
                int count = optional.get().getCount();
                cell.setCellValue(count);
            }

            cell = headerRow.createCell(column + 8);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(0);
            optional = item.getCountNoteDtoByNote("4");
            if(optional.isPresent()){
                int count = optional.get().getCount();
                cell.setCellValue(count);
            }

            cell = headerRow.createCell(column + 9);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(0);
            optional = item.getCountNoteDtoByNote("5");
            if(optional.isPresent()){
                int count = optional.get().getCount();
                cell.setCellValue(count);
            }

            row++;

               /* Row dataRow = sheet.createRow(i);
                dataRow.createCell(0).setCellValue(item.getLabel());
                dataRow.createCell(1).setCellValue(item.getCount());
                dataRow.createCell(2).setCellValue(item.getPourcent());
                i = i + 1 ;*/
        }

        sheet.setColumnWidth(0, 75 * 256);
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 5 * 256);
        sheet.setColumnWidth(5, 5 * 256);
        sheet.setColumnWidth(6, 5 * 256);
        sheet.setColumnWidth(7, 5 * 256);
        sheet.setColumnWidth(8, 5 * 256);
        sheet.setColumnWidth(9, 5 * 256);

        return workbook;
    }

    public XSSFWorkbook generateFeedbackListing(XSSFWorkbook workbook, List<FeedbackDto> feedbackDtos, String sheetName){

        Sheet sheet = workbook.createSheet(sheetName);


        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.VIOLET.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font =  workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        font.setBold(false);
        font.setColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFont(font);

        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        font =  workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 9);
        font.setItalic(true);
        font.setColor(IndexedColors.VIOLET.getIndex());
        cellStyle2.setFont(font);

        cellStyle2.setBorderTop(BorderStyle.MEDIUM);
        cellStyle2.setBorderRight(BorderStyle.MEDIUM);
        cellStyle2.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle2.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle2.setAlignment(HorizontalAlignment.LEFT);
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle cellStyle3 = workbook.createCellStyle();
        cellStyle3.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        font =  workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 9);
        cellStyle3.setFont(font);

        cellStyle3.setBorderTop(BorderStyle.MEDIUM);
        cellStyle3.setBorderRight(BorderStyle.MEDIUM);
        cellStyle3.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle3.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle3.setAlignment(HorizontalAlignment.LEFT);
        cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);


        List<QuestionDto> questions = questionService.getAllQuestions("fr");
        // Écrire l'en-tête
        Row headerRow = sheet.createRow(0);

        Cell cell = headerRow.createCell(0);

        cell.setCellStyle(cellStyle);
        cell.setCellValue("Questions / Feedbacks");


        int i = 1;

        for (QuestionDto questionDto : questions) {
            /*Cell cellRow = headerRow.createCell(i);
            cellRow.setCellStyle(cellStyle);
            cellRow.setCellValue(questionDto.getProperty());*/
            headerRow = sheet.createRow(i);
            cell = headerRow.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(questionDto.getLabel());

            cell = headerRow.createCell(1);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(questionDto.getProperty());

            i = i + 1 ;
        }

        sheet.setColumnWidth(0, 75 * 256);
        sheet.setColumnWidth(1, 25 * 256);


        int column = 2;


        // Écrire les données
        for (FeedbackDto feedbackDto : feedbackDtos) {
            Row dataRow = sheet.getRow(0);
            Cell cellRow = dataRow.createCell(column);
            cellRow.setCellValue(feedbackDto.getFullname());
            cellRow.setCellStyle(cellStyle2);

            int row = 1;

            for (QuestionDto questionDto : questions) {
                Row dataRowQuestion = sheet.getRow(row);
                Feedback feedback =  feedbackRepository.getOne(feedbackDto.getId());
                Optional<Answer> answer = answerService.findByFeedbackAndQuestion(feedback, questionDto.getProperty());
                //Optional<AnswerDto> answerDto = feedback.getAnwserByProperty(questionDto.getProperty());
                Cell cellAnswer = dataRowQuestion.createCell(column);
                cellAnswer.setCellValue(answer.isPresent() ? answer.get().getAnswer() : "");
                cellAnswer.setCellStyle(cellStyle3);
                row++;

            }

            sheet.setColumnWidth(column, 25 * 256);


            column++;
        }
        return  workbook;

    }

}


