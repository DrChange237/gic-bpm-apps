package com.change.gic.modules.business.service.faces;

import com.change.gic.modules.business.enumeration.ContratStatus;
import com.change.gic.modules.business.info.ContratInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContratService {

    void archived(String reference);

    List<ContratInfo> search(String search, Boolean archived);
}
