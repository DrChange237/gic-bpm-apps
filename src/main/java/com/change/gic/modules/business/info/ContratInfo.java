package com.change.gic.modules.business.info;


import com.change.gic.modules.business.enumeration.ContratStatus;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ContratInfo {

    private String id;

    private ConsultationInfo consultation;

    private String reference;

    private ContratStatus status;

    private BigDecimal firstAmount;

    private BigDecimal secondAmount;

    private BigDecimal lastAmount;

    public BigDecimal getTotalAmount() {
        return this.firstAmount.add(this.secondAmount).add(this.lastAmount);
    }

}
