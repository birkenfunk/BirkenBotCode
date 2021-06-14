package de.birkenfunk.birkenbotcode.application.mixins;

public interface CommandFormatMixins {
    /*todo Documentation*/
    default boolean isChat (String message) {
        return false;
    }
}
