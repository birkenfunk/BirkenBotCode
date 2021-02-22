package mixins;

public interface CommandFormatMixins {
    /*todo Documentation*/
    default boolean isChat (String message) {
        return !message.startsWith("!");
    }
}
