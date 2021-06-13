package de.birkenfunk.birkenbotcode.application.mixins;

import de.birkenfunk.birkenbotcode.infrastructure.reader.ReadFile;

public interface CommandFormatMixins {
    /*todo Documentation*/
    default boolean isChat (String message) {
        return !message.startsWith(ReadFile.getReadFile().getPrefix()+"");
    }
}
