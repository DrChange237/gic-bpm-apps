package com.change.gic.modules.business.info;


import com.change.gic.modules.business.entity.TestExamStatus;
import com.change.gic.modules.business.enumeration.ContratStatus;
import com.change.gic.modules.business.enumeration.EquivalenceStatus;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ContratInfo {

    private String id;

    private ConsultationInfo consultation;

    private String reference;

    private String equivalenceStatus;

    private String testExamStatus;

    private BigDecimal restToPay;

    private ContratStatus status;

    private BigDecimal firstAmount;

    private BigDecimal secondAmount;

    private BigDecimal lastAmount;

    public BigDecimal getTotalAmount() {
        return this.firstAmount.add(this.secondAmount).add(this.lastAmount);
    }

}
