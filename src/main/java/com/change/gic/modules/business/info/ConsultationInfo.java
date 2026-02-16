package com.change.gic.modules.business.info;

import com.change.gic.modules.business.entity.Inscription;
import com.change.gic.modules.business.enumeration.ConsultationStatus;
import com.change.gic.modules.core.info.DocumentInfo;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ConsultationInfo {

    private String id;

    private Inscription inscription;

    private String observation;

    private BigDecimal firstAmount;

    private BigDecimal secondAmount;

    private BigDecimal lastAmount;

    public BigDecimal getTotalAmount() {
        return this.firstAmount.add(this.secondAmount).add(this.lastAmount);
    }

    private Boolean equivalence;

    private Boolean testLang;

    private Boolean eligible;

    private ConsultationStatus status;

    private DocumentInfo manuscrit;
}
