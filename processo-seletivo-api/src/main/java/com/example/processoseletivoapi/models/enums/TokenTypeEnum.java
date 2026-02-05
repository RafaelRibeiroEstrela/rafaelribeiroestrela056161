package com.example.processoseletivoapi.models.enums;

public enum TokenTypeEnum {
    TOKEN(300), REFRESH_TOKEN(86400), TOKEN_PRE_ASSINADO(1800);

    private final int expiration;


    TokenTypeEnum(int expiration) {
        this.expiration = expiration;
    }

    public int getExpiration() {
        return expiration;
    }
}
