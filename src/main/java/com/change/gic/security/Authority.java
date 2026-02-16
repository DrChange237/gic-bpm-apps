package com.change.gic.security;

public class Authority {

    public static final String IS_AUTHENTICATED = "hasAuthority('isAuthenticated()')";

    /**
     * Privileges pour gestion des approbations
     */
    public static class ProcessUnity{
        public static final String VIEWALL_PROCESSUNITY = "hasAuthority('VIEWALL_PROCESSUNITY')";
        public static final String VIEW_PROCESSUNITY = "hasAuthority('VIEW_PROCESSUNITY')";
        public static final String ADD_PROCESSUNITY = "hasAuthority('ADD_PROCESSUNITY')";
        public static final String UPDATE_PROCESSUNITY = "hasAuthority('UPDATE_PROCESSUNITY')";
    }

    /**
     * Privileges pour gestion des processus
     */
    public static class DocumentType{
        public static final String VIEWALL_DOCUMENT_TYPE = "hasAuthority('VIEWALL_DOCUMENT_TYPE')";
        public static final String VIEW_DOCUMENT_TYPE = "hasAuthority('VIEW_DOCUMENT_TYPE')";
    }

    /**
     * Privileges pour la demande de congés
     */
    public static class Vacation{
        public static final String ADD_VACATION = "hasAuthority('ADD_VACATION')";
    }

    /**
     * Privileges pour la Fiche de reprise de service
     */
    public static class Resumption{
        public static final String ADD_RESUMPTION = "hasAuthority('ADD_RESUMPTION')";
    }

    /**
     * Privileges pour la demande d'autorisation d'absence
     */
    public static class Absence{
        public static final String ADD_ABSENCE = "hasAuthority('ADD_ABSENCE')";
    }

    /**
     * Privileges pour la demande de Mémo
     */
    public static class Memo{
        public static final String ADD_MEMO = "hasAuthority('ADD_MEMO')";
    }

    /**
     * Privileges pour la demande de Travail
     */
    public static class WorkForm{
        public static final String ADD_WORKFORM = "hasAuthority('ADD_WORKFORM')";
    }

    /**
     * Privileges pour la demande d'achat
     */
    public static class Purchase{
        public static final String ADD_PURCHASE = "hasAuthority('ADD_PURCHASE')";
    }


}
