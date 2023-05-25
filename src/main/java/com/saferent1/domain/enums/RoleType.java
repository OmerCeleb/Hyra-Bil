package com.saferent1.domain.enums;

public enum RoleType {

    ROLE_CUSTOMER("Kund"),  //"Kund"
    ROLE_ADMIN("Administratör");

    private String name;

    private RoleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
