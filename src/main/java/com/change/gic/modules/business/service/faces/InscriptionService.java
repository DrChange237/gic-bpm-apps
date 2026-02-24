package com.change.gic.modules.business.service.faces;

import com.change.gic.modules.business.info.InscriptionInfo;
import com.change.gic.modules.core.info.DocumentInfo;

import java.util.List;

public interface InscriptionService {
    List<InscriptionInfo> search(String search);

    DocumentInfo downloadDocument(String reference, String tag);
}
