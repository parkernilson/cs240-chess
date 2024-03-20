package ui;


import java.util.Map;
import java.util.Map.Entry;
import static ui.EscapeSequences.*;

public class Color {
    static Map<String, String> colorCodes = Map.ofEntries(
        Map.entry("{BLACK} ", BLACK),
        Map.entry("{LIGHT_GREY} ", LIGHT_GREY),
        Map.entry("{DARK_GREY} ", DARK_GREY),
        Map.entry("{RED} ", RED),
        Map.entry("{GREEN} ", GREEN),
        Map.entry("{YELLOW} ", YELLOW),
        Map.entry("{BLUE} ", BLUE),
        Map.entry("{MAGENTA} ", MAGENTA),
        Map.entry("{WHITE} ", WHITE),
        Map.entry("{RESET} ", RESET),
        Map.entry("{BG_BLACK} ", BG_BLACK),
        Map.entry("{BG_LIGHT_GREY} ", BG_LIGHT_GREY),
        Map.entry("{BG_DARK_GREY} ", BG_DARK_GREY),
        Map.entry("{BG_RED} ", BG_RED),
        Map.entry("{BG_GREEN} ", BG_GREEN),
        Map.entry("{BG_YELLOW} ", BG_DARK_GREEN),
        Map.entry("{BG_BLUE} ", BG_BLUE),
        Map.entry("{BG_MAGENTA} ", BG_MAGENTA),
        Map.entry("{BG_WHITE} ", BG_WHITE)
    );
    
    public static String format(String str) {
        for (Entry<String, String> entry : colorCodes.entrySet()) {
            str = str.replace(entry.getKey(), entry.getValue());
        }
        return str;
    }
}
