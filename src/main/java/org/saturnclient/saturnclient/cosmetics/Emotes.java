package org.saturnclient.saturnclient.cosmetics;

import java.util.ArrayList;
import java.util.List;

public class Emotes {
    public static List<String> availableEmotes = new ArrayList<>();

    public static void initialize() {
        availableEmotes.add("tpose");
        availableEmotes.add("back_flip");
        availableEmotes.add("facepalm");
        availableEmotes.add("cry");
        availableEmotes.add("giga_chad");
        availableEmotes.add("front_flip");
    }
}
