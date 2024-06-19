package com.ccabank.feedbackservice.service.impl;

import com.ccabank.feedbackservice.constant.AppError;
import com.ccabank.feedbackservice.domain.AppServiceResult;
import com.ccabank.feedbackservice.dto.feedback.*;
import com.ccabank.feedbackservice.entity.Agency;
import com.ccabank.feedbackservice.entity.Answer;
import com.ccabank.feedbackservice.entity.Feedback;
import com.ccabank.feedbackservice.entity.Staff;
import com.ccabank.feedbackservice.mappers.AgencyMapper;
import com.ccabank.feedbackservice.mappers.FeedbackMapper;
import com.ccabank.feedbackservice.mappers.StaffMapper;
import com.ccabank.feedbackservice.openfeign.UserRestClient;
import com.ccabank.feedbackservice.repository.AgencyRepository;
import com.ccabank.feedbackservice.repository.AnswerRepository;
import com.ccabank.feedbackservice.repository.FeedbackRepository;
import com.ccabank.feedbackservice.repository.StaffRepository;
import com.ccabank.feedbackservice.service.faces.IFeedbackService;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ccabank.feedbackservice.constant.BeanIdConstant.FEEDBACK_DETAIL_SERVICE;


@Service
@Transactional
@Qualifier(FEEDBACK_DETAIL_SERVICE)
public class FeedbackService implements IFeedbackService {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackService.class);

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


    @Override
    public AppServiceResult<List<FeedbackDto>> getAllFeedback() {
        try {
            List<Feedback> feedbacks = feedbackRepository.findAll();
            return getConvertedResult(feedbacks, "getAllFeedback ");
        } catch (Exception e) {
            e.printStackTrace();
            return new AppServiceResult<List<FeedbackDto>>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }


    @Override
    public AppServiceResult<FeedbackDto> getFeedbackById(String id) {
        try {
            Feedback feedback = feedbackRepository.findById(Long.parseLong(id)).orElse(null);
            if (feedback == null) {
                logger.warn(FEEDBACK_DETAIL_SERVICE, "getFeedbackById",
                        "Feedback not exist!, Cannot further process!");
                return new AppServiceResult<FeedbackDto>(false, AppError.Validattion.errorCode(),
                        "Feedback not exist!", null);
            }
            return new AppServiceResult<FeedbackDto>(true, 0, "Succeed!", feedbackMapper.toDto(feedback));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(FEEDBACK_DETAIL_SERVICE, "getFeedbackById : Exception ", e.getMessage());
            return new AppServiceResult<FeedbackDto>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<List<FeedbackDto>> getFeedbackByStaffAndCreatedAt(String staff, LocalDate startAt, LocalDate endAt) {
        try {
            List<Feedback> feedbacks = feedbackRepository.findFeedbackByStaffUsernameAndCreatedAtBetween(staff, startAt.atStartOfDay(), endAt.atStartOfDay());

            return getConvertedResult(feedbacks, "getFeedbackByStaffAndCreatedAt ");
        } catch (Exception e) {
            e.printStackTrace();
            return new AppServiceResult<List<FeedbackDto>>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<List<FeedbackDto>> getFeedbackByAgencyAndCreatedAt(String agencyCode, LocalDate startAt, LocalDate endAt) {
        try {
            List<Feedback> feedbacks = feedbackRepository.findFeedbackByAgencyAndCreatedAtBetween(agencyCode, startAt.atStartOfDay(), endAt.atStartOfDay());

            return getConvertedResult(feedbacks, "getFeedbackByStaffAndCreatedAt ");
        } catch (Exception e) {
            e.printStackTrace();
            return new AppServiceResult<List<FeedbackDto>>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public Page<Feedback> findRank(LocalDateTime startAt, LocalDateTime endAt, String property, int limit) {
        Pageable pageable = PageRequest.of(1, limit);
        try {
            Page<Feedback> feedbacks = feedbackRepository.findRank(pageable, startAt, endAt, property);
            return feedbacks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AppServiceResult<List<FeedbackDto>> getFeedbackByStaffUsername(String staffUsername) {
        try {
            List<Feedback> feedbacks = feedbackRepository.findFeedbackByStaff(staffUsername);
            return getConvertedResult(feedbacks, "getFeedbackByStaffUsername");
        } catch (Exception e) {
            e.printStackTrace();
            return new AppServiceResult<List<FeedbackDto>>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<FeedbackDto> addFeedback(FeedbackDto feedbackDto) {
        try {
            logger.info(FEEDBACK_DETAIL_SERVICE + "addFeedback : methode invocation");
            Feedback feedback = feedbackMapper.toEntity(feedbackDto);
            Staff  staff = staffRepository.findByUsername(feedbackDto.getStaff().getUsername());
            if(staff == null) {
                staff = staffMapper.toEntity(feedbackDto.getStaff());
                UserRestDto userDto = userRestClient.getAgencyByStaffUsername(staff.getUsername(), "key", "secret");
                Agency agency = agencyRepository.findAgencyByAgencyCode(userDto.getAgencyCode());
                if(agency == null){
                    agency = new Agency();
                    agency.setAgencyCode(userDto.getAgencyCode());
                    agency.setAgencyName(userDto.getAgencyName());
                    agency = agencyRepository.save(agency);
                }
                staff.setPosition(userDto.getFunction());
                staff.setAgency(agency);
                staff = staffRepository.save(staff);
            }
            feedback.setStaff(staff);
            feedback.setCreatedAt(LocalDateTime.now());
            for (Answer answer : feedback.getAnswerCollection()) {
                answer.setFeedback(feedback);
            }
            feedback = feedbackRepository.save(feedback);
            FeedbackDto dto = feedbackMapper.toDto(feedback);
            return new AppServiceResult<FeedbackDto>(true, 0, "Succeed!", dto );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(FEEDBACK_DETAIL_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<FeedbackDto>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }


    @Override
    public AppServiceResult<EvaluationPeriodStaffDto> getEvaluationStaff(String staffUsername, LocalDate startAt, LocalDate endAt) {

        List<Feedback> feedbacks = feedbackRepository.findFeedbackByStaffUsernameAndCreatedAtBetween(staffUsername, startAt.atStartOfDay(), endAt.atStartOfDay());

        logger.info(FEEDBACK_DETAIL_SERVICE + " Feedbacks retrieve" + String.valueOf(feedbacks.size()), "" );

        EvaluationPeriodStaffDto evaluation = new EvaluationPeriodStaffDto();
        evaluation.setUsername(staffUsername);
        evaluation.setStartAt(startAt);
        evaluation.setEndAt(endAt);

        List<EvaluationItem> evaluations = evaluation.getEvaluations();

        List<QuestionDto> questionDtos = questionService.getAllQuestions("fr");

        for(QuestionDto questionDto: questionDtos){

            if(!questionDto.getType().equals("1-5")){
                continue;
            }

            EvaluationItem item = new EvaluationItem();
            item.setElement(questionDto.getProperty());
            item.setLabel(questionDto.getLabel());
            int score = 0;
            int total = 0;
            int count = 0;
            float percentage = 0;

            for (Feedback feedback : feedbacks) {
                    // Itérer sur chaque question et calculer le pourcentage
                    for (Answer answer : feedback.getAnswerCollection()) {



                        if(!questionDto.getProperty().equals(answer.getQuestion())){
                            continue;
                        }

                        if(answer.getAnswer().equals("-1")){
                            continue;
                        }

                        item.addNote(answer.getAnswer());


                        score =  score + Integer.parseInt(answer.getAnswer());
                        count = count + 1;
                        total = total + 5;
                    }
                    if(total > 0){
                        percentage = ((float) score /total) * 100;
                    }
            }
            item.setPourcent(percentage);
            item.setCount(count);
            evaluations.add(item);
            evaluation.setEvaluations(evaluations);
        }

        return new AppServiceResult<EvaluationPeriodStaffDto>(true, 0, "Succeed!", evaluation);
    }

    @Override
    public AppServiceResult<EvaluationPeriodStaffDto> getEvaluationAgency(String agencyCode, LocalDate startAt, LocalDate endAt) {

        List<Feedback> feedbacks = feedbackRepository.findFeedbackByAgencyAndCreatedAtBetween(agencyCode, startAt.atStartOfDay(), endAt.atStartOfDay());

        EvaluationPeriodStaffDto evaluation = new EvaluationPeriodStaffDto();
        evaluation.setUsername(agencyCode);
        evaluation.setStartAt(startAt);
        evaluation.setEndAt(endAt);

        List<EvaluationItem> evaluations = evaluation.getEvaluations();

        List<QuestionDto> questionDtos = questionService.getAllQuestions("fr");

        for(QuestionDto questionDto: questionDtos){
            if(!questionDto.getType().equals("1-5")){
                continue;
            }

            EvaluationItem item = new EvaluationItem();
            item.setElement(questionDto.getProperty());
            item.setLabel(questionDto.getLabel());
            int score = 0;
            int total = 0;
            int count = 0;
            float percentage = 0;
            for (Feedback feedback : feedbacks) {
                // Itérer sur chaque question et calculer le pourcentage
                for (Answer answer : feedback.getAnswerCollection()) {
                    if(!questionDto.getProperty().equals(answer.getQuestion())){
                        continue;
                    }
                    if(answer.getAnswer() == "-1"){
                        continue;
                    }

                    item.addNote(answer.getAnswer());

                    score =  score + Integer.parseInt(answer.getAnswer());
                    count = count + 1;
                    total = total + 5;
                }
                if(total > 0){
                    percentage = ((float) score /total) * 100;
                }
            }
            item.setPourcent(percentage);
            item.setCount(count);
            evaluations.add(item);
            evaluation.setEvaluations(evaluations);
        }

        return new AppServiceResult<EvaluationPeriodStaffDto>(true, 0, "Succeed!", evaluation);
    }

    @Override
    public byte[] exportExcelFeedbacks(List<FeedbackDto> feedbackDtos){

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("FEEDBACK");


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

            // Écrire le fichier Excel dans un tableau d'octets
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();

            return excelBytes;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public byte[] exportExcelEvaluationFeedbacks(EvaluationPeriodStaffDto evaluation){

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("FEEDBACK_EVALUATION");


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

            // Écrire le fichier Excel dans un tableau d'octets
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();

            return excelBytes;

        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];

        }

    }



    private float calculatePercentage(int value, int max) {
        return (float) value / max * 100;
    }


    private AppServiceResult<List<FeedbackDto>> getConvertedResult(List<Feedback> feedbacks, String functionName) {
        if (feedbacks == null) {
            logger.warn(FEEDBACK_DETAIL_SERVICE, functionName,
                    "Feedback not exist!, Cannot further process!");
            return new AppServiceResult<List<FeedbackDto>>(false, AppError.Validattion.errorCode(),
                    "Feedback not exist!", null);
        }
        List<FeedbackDto> result =  new ArrayList<FeedbackDto>();
        if (feedbacks.size() > 0) {
            for (Feedback feedback : feedbacks) {
                result.add(feedbackMapper.toDto(feedback));
            }
        }
        return new AppServiceResult<List<FeedbackDto>>(true, 0, "Succeed!", result);
    }
}
