package de.birkenfunk.birkenbotcode.application.mixins;

public interface AccessMixins {
    /*todo Documentation*/
    default boolean hasAdminAccess(String authorName) {
        String[] allowedUsers = {
                "U:Birkenfunk(284418478277132289)",
                "U:TerminatorCow(412763740321087490)"
        };

        for (String username : allowedUsers) {
            if (authorName.equalsIgnoreCase(username)) {
                return true;
            }
        }

        return false;
    }

    default boolean isBirkenBot(String authorName) {
        return authorName.equals("U:Birkenbot(471344280930353153)");
    }
}
