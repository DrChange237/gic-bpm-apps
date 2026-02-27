package com.change.gic.modules.business.service.faces;

import com.change.gic.modules.business.info.InscriptionInfo;
import com.change.gic.modules.core.info.DocumentInfo;
import com.change.gic.modules.file.dto.FileDto;

import java.util.List;

public interface InscriptionService {
    List<InscriptionInfo> search(String search);

    FileDto downloadDocument(String reference, String tag);
}
