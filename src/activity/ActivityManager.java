package activity;

import enums.Activities;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class ActivityManager {
    /*todo Documentation*/
    private static Activity getActivity(Activities type, String message) {
        Activity activity = null;

        switch (type) {
            case PLAYING:
                activity = Activity.playing(message);
                break;
            case WATCHING:
                activity = Activity.watching(message);
                break;
            case LISTENING:
                activity = Activity.listening(message);
                break;
        }
        return activity;
    }

    private static String putTogether(String[] splittedMessage){
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < splittedMessage.length; i++) {
            message.append(splittedMessage[i]).append(" ");
        }
        return message.toString();
    }

    public static void setActivity(Activities type, PrivateMessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        String [] splittedMessage = message.split(" ");
        if (splittedMessage.length == 1) {
            event.getJDA().getPresence().setActivity(getActivity(type, "Nothing"));
            return;
        }
        event.getJDA().getPresence().setActivity(getActivity(type, putTogether(splittedMessage)));
    }
}
