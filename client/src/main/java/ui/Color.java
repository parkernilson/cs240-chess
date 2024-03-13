package ui;


import java.util.Map;
import java.util.Map.Entry;
import static ui.EscapeSequences.*;

public class Color {
    static Map<String, String> colorCodes = Map.ofEntries(
        Map.entry("%black ", BLACK),
        Map.entry("%light_grey ", LIGHT_GREY),
        Map.entry("%dark_grey ", DARK_GREY),
        Map.entry("%red ", RED),
        Map.entry("%green ", GREEN),
        Map.entry("%yellow ", YELLOW),
        Map.entry("%blue ", BLUE),
        Map.entry("%magenta ", MAGENTA),
        Map.entry("%white ", WHITE),
        Map.entry("%reset ", RESET),
        Map.entry("%bg_black ", BG_BLACK),
        Map.entry("%bg_light_grey ", BG_LIGHT_GREY),
        Map.entry("%bg_dark_grey ", BG_DARK_GREY),
        Map.entry("%bg_red ", BG_RED),
        Map.entry("%bg_green ", BG_GREEN),
        Map.entry("%bg_yellow ", BG_DARK_GREEN),
        Map.entry("%bg_blue ", BG_BLUE),
        Map.entry("%bg_magenta ", BG_MAGENTA),
        Map.entry("%bg_white ", BG_WHITE)
    );
    
    public static String format(String str) {
        for (Entry<String, String> entry : colorCodes.entrySet()) {
            str = str.replace(entry.getKey(), entry.getValue());
        }
        return str;
    }
}
