package com.change.gic.modules.core.enumeration;


import com.change.gic.modules.core.converter.EnumConverter;

public enum Habilitation {

    INSCRIPTION,
    DOSSIER,
    EQUIVALENCE,
    TEST_LANG,
    PROFIL,
    RESIDENT_PERMANENT;

    public static class Converter extends EnumConverter<Habilitation> {
        public Converter() {
            super(Habilitation.class);
        }
    }

}
