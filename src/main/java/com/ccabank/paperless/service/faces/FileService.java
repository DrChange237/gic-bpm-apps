package com.ccabank.paperless.service.faces;

import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.entity.Request;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<FileDto> getAllFiles(String businessKey, String staff);

    void saveFile(Request request, FileDto fileDto) throws IOException;
}
