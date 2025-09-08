package com.ccabank.paperless.service.faces;

import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.entity.Request;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface FileService {

    Page<FileDto> search(String reference, String type, String staff, int page, int size);

    byte[] export(String reference, String type, String staff, LocalDate startDate, LocalDate endDate);

    List<FileDto> getAllFiles(String businessKey, String staff);

    void saveFile(Request request, FileDto fileDto) throws IOException;
}
