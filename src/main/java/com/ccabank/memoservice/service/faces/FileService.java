package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.dto.memo.FileDto;
import com.ccabank.memoservice.entity.Request;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<FileDto> getAllFiles(String businessKey, String staff);

    void saveFile(Request request, FileDto fileDto) throws IOException;
}
