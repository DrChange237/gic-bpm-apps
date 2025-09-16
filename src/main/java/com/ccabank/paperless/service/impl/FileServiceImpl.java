package com.ccabank.paperless.service.impl;

import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.entity.DocumentType;
import com.ccabank.paperless.entity.File;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.mappers.FileMapper;
import com.ccabank.paperless.openfeign.FileRestClient;
import com.ccabank.paperless.repository.DocumentTypeRepository;
import com.ccabank.paperless.repository.FileRepository;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.FileService;
import com.ccabank.paperless.specification.FileSpecifications;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final RequestRepository requestRepository;

    private final FileRepository fileRepository;

    private final FileRestClient fileRestClient;

    private final FileMapper fileMapper;

    private final DocumentTypeRepository documentTypeRepository;


    @Value("${server_url}")
    private String serverUrl;

    private final String pathFile = "/api/files/";


    @Override
    public Page<FileDto> search(String reference, String type, String staff, int page, int size) {
        // Commencez avec une spécification "vide" ou "vraie"
        Specification<File> spec = Specification.where(null);
        DocumentType documentType = documentTypeRepository.findOneByStructure(type);
        spec = FileSpecifications.withDynamicQuery(reference, documentType, staff);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "addDate"));
        Page<File> files = fileRepository.findAll(pageable);
        if(spec != null){
            files = fileRepository.findAll(spec, pageable);
        }
        // Convert Page<Element> to Page<ElementDto> using the map() method
        return files.map(fileMapper::toDto);
    }

    @Override
    public byte[] export(String reference, String type, String staff, LocalDate startDate, LocalDate endDate) {

        Specification<File> spec = Specification.where(null);
        if(endDate != null && startDate != null){
            spec = Specification.where(FileSpecifications.dateBetween(startDate, endDate));
        }
        DocumentType documentType = documentTypeRepository.findOneByStructure(type);
        spec = FileSpecifications.withDynamicQuery(reference, documentType, staff);
        Pageable pageable = PageRequest.of(0, fileRepository.findAll().size(), Sort.by(Sort.Direction.DESC, "addDate"));
        List<File> files = fileRepository.findAll(spec);
        List<FileDto> fileDtos = fileMapper.toDto(files);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();



        switch (documentType.getName()){

            case "mission":
                workbook = this.generateExport(files, "Export_Mission_Paperless");
            break;
            case "vacation":
                workbook = this.generateExport(files, "Export_Vacation_Bank");
            break;
        }

        try {
            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();
            return excelBytes;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<FileDto> getAllFiles(String businessKey, String staff) {

        Request request = requestRepository.findOneByReference(businessKey);

        if(request != null){
            List<FileDto> fileDtos = new ArrayList<>();
            List<File> files = fileRepository.findByRequest(request);
            for(File file : files){
                FileDto fileDto = new FileDto();
                fileDto.setAddDate(file.getAddDate());
                fileDto.setName(file.getName());
                fileDto.setUrl(file.getUrl());
                fileDtos.add(fileDto);
            }
            return fileDtos;
        }
        return null;
    }

    @Override
    public void saveFile(Request request, FileDto fileDto) throws IOException {

            FileDto fileFinal = fileRestClient.uploadFileToFolder("paperless", "paperless", fileDto.getMultipartFile());
            File file = new File();
            file.setRequest(request);
            file.setName(fileDto.getName());
            file.setType(fileDto.getType());
            file.setUrl(serverUrl + pathFile + fileFinal.getUrl());
            file.setAddDate(LocalDateTime.now());
            fileRepository.save(file);
    }

    public XSSFWorkbook generateExport(List<File> fileDtos, String sheetName){

        XSSFWorkbook workbook = new XSSFWorkbook();
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



        // Écrire l'en-tête
        Row headerRow = sheet.createRow(0);

        Cell cell = headerRow.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Type de Document");

        cell = headerRow.createCell(1);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Réference");

        cell = headerRow.createCell(2);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Noms du Staff");

        cell = headerRow.createCell(3);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Date de Publication");


        int i = 1;

        for (File fileDto : fileDtos) {

            if(fileDto.getRequest() == null){
                continue;
            }

            headerRow = sheet.createRow(i);
            cell = headerRow.createCell(0);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(fileDto.getRequest().getType().getName());


            cell = headerRow.createCell(1);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(fileDto.getRequest().getReference());

            cell = headerRow.createCell(2);
            cell.setCellStyle(cellStyle2);
            cell.setCellValue(fileDto.getRequest().getStaff());

            cell = headerRow.createCell(3);
            cell.setCellStyle(cellStyle2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            cell.setCellValue(fileDto.getAddDate().format(formatter));

            i = i + 1 ;
        }

        sheet.setColumnWidth(0, 75 * 256);

        return  workbook;

    }
}
