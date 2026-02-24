package com.change.gic.modules.business.service.faces;


import com.change.gic.modules.business.info.MoneyMovementInfo;

import java.util.List;

public interface MoneyMovementService {
    List<MoneyMovementInfo> findMovementByReference(String reference);
}
