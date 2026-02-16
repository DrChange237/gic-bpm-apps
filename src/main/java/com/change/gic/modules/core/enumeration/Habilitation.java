package com.change.gic.modules.core.enumeration;


import com.change.gic.modules.core.converter.EnumConverter;

public enum Habilitation {

    DASHBOARD,
    ROLE_LIST,
    ROLE_DETAIL,
    AGENT_HISTORY,
    AGENCY_ENABLED,
    AGENCY_LIST,
    AGENCY_DETAIL,
    AGENCY_OPEN,
    AGENCY_HISTORY,
    CASHIER_HISTORY,
    CASHIER_HISTORY_DETAIL,
    CASHIER_LIST_AGENCY,
    CASHIER_LIST_AGENT,
    CASHIER_ENABLED,
    CASHIER_UPDATE_ROLE,
    ACCOUNT_OPERATION,
    BALANCE_VIEW,
    COMMISSION_VIEW,
    FUNDS_TRANSFER,
    REPORT;

    public static class Converter extends EnumConverter<Habilitation> {
        public Converter() {
            super(Habilitation.class);
        }
    }

}
