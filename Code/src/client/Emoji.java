package client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Emoji {

    private static Map<String, String> emoji = new HashMap<>();

    public Emoji() {
        emoji.put("\uD83D\uDE01", "[:smile]");
        emoji.put("\uD83D\uDE02", "[:laughTear]");
        emoji.put("\uD83D\uDE04", "[:laugh]");
        emoji.put("\uD83D\uDE05", "[:laughSweat]");
        emoji.put("\uD83D\uDE06", "[:laughClosedEye]");
        emoji.put("\uD83D\uDE09", "[:wink]");
        emoji.put("\uD83D\uDE0A", "[:sweet]");
        emoji.put("\uD83D\uDE0C", "[:relief]");
        emoji.put("\uD83D\uDE0D", "[:heartEyes]");
        emoji.put("\uD83D\uDE12", "[:assume]");
        emoji.put("\uD83D\uDE14", "[:pensive]");
        emoji.put("\uD83D\uDE16", "[:confound]");
        emoji.put("\uD83D\uDE18", "[:kiss]");
        emoji.put("\uD83D\uDE1C", "[:tongue]");
        emoji.put("\uD83D\uDE22", "[:crying]");
        emoji.put("\uD83D\uDE24", "[:triumph]");
        emoji.put("\uD83D\uDE2D", "[:loudlyCrying]");
        emoji.put("\uD83D\uDE31", "[:fear]");
        emoji.put("\uD83D\uDE32", "[:astonish]");
        emoji.put("\uD83D\uDE33", "[:flush]");
        emoji.put("\uD83D\uDE37", "[:flush]");

    }

    public static String emojiToString(String string) {
        Set<Map.Entry<String, String>> emojiSet = emoji.entrySet();
        for (Map.Entry<String, String> entry : emojiSet) {
            if (string.contains(entry.getKey())) {
                string = string.replace(entry.getKey(), entry.getValue());
            }
        }
        return string;
    }

    public static String stringToEmoji(String string) {
        Set<Map.Entry<String, String>> emojiSet = emoji.entrySet();
        for (Map.Entry<String, String> entry : emojiSet) {
            if (string.contains(entry.getValue())) {
                string = string.replace(entry.getValue(), entry.getKey());
            }
        }
        return string;
    }
    public static void main(String[] args) {
        Emoji e = new Emoji();
//        emoji.forEach((x,y)-> System.out.print(x));
        System.out.println(emojiToString("\uD83D\uDE37Hello你好"));

    }
}
