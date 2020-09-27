package ru.pudgy.vertex.srvc;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class TextService {

    public String formatSearchString(String search_string){
        return Optional.ofNullable(search_string)
                .map(str -> {
                    String ls = search_string.trim().toUpperCase();
                    if(ls.length() > 0){
                        StringBuilder builder = new StringBuilder();
                        builder.append('%');
                        String [] ss = ls.split("\\s");
                        for(String s : ss){
                            if(s.length() > 0)
                                builder.append(s).append('%');
                        }
                        return builder.toString();
                    }
                    return null;
                })
                .orElse(null);
    }

    public String createAnnotation(String text) {
        String tmp = filterHtmlString(text);
        String [] words = tmp.split(" ", 40);
        int len = words.length;
        if(len == 40)
            words[len - 1] = "";
        return String.join(" ", words);
    }

    private static String filterHtmlString(String search_string) {
        String lss = "";
        if(search_string != null){
            String tmp_s = search_string.replaceAll("\\<.*?>", "");
            lss = tmp_s.trim();
        }
        return lss;
    }
}
