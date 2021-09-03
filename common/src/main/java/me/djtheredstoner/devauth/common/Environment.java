package me.djtheredstoner.devauth.common;

public enum Environment {

    FABRIC("fabric"),
    FORGE("forge");

    private final String name;

    Environment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
