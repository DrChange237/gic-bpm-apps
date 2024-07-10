package com.ccabank.signservice.security;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : cca-bank-microservices
 * @Package : com.ccabank.entityservice.security
 * <p>
 * @date: 22/02/2024
 * @time: 10:47
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class Authority {

    public static final String IS_AUTHENTICATED = "hasAuthority('isAuthenticated()')";

    /**
     * Privileges pour gestion des comptes
     */
    public static class FeedBack{
        public static final String VIEWALL_FEEDBACK = "hasAuthority('VIEWALL_FEEDBACK')";
        public static final String VIEW_FEEDBACK = "hasAuthority('VIEW_FEEDBACK')";
        public static final String ADD_FEEDBACK = "hasAuthority('ADD_FEEDBACK')";
    }
}
