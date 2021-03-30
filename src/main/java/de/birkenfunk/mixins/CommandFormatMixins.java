package de.birkenfunk.mixins;

import de.birkenfunk.Reader.ReadFile;

public interface CommandFormatMixins {
    /*todo Documentation*/
    default boolean isChat (String message) {
        return !message.startsWith(ReadFile.getReadFile().getPrefix()+"");
    }
}
