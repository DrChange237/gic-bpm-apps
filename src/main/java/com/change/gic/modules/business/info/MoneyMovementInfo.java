package com.change.gic.modules.business.info;

import com.change.gic.modules.business.enumeration.MovementFlow;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class MoneyMovementInfo extends  AuditableInfo{

    private String id;

    private String label;

    private String reference;

    private BigDecimal amount;

    private Boolean fees;

    private MovementFlow flow;
}
