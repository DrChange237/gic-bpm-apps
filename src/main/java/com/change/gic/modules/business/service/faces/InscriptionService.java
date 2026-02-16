package com.change.gic.modules.business.service.faces;

import com.change.gic.modules.business.info.InscriptionInfo;

import java.util.List;

public interface InscriptionService {
    List<InscriptionInfo> search(String search);
}
