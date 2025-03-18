package me.spencernold.transformer.visitors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class VisitorTool {

    static int getFirstLocalPos(String desc) {
        Pattern pattern = Pattern.compile("(Z|B|S|I|J|F|D|L[^(L|;)]+?;)"); // (B|S|I|J|F|D|L[^(L|;)]+?;)
        Matcher matcher = pattern.matcher(desc);
        int size = 1;
        while (matcher.find()) {
            String s = matcher.group(1);
            if (s.equals("D") || s.equals("J"))
                size += 2;
            else
                size++;
        }
        return size;
    }

    static String getRawClassName(Class<?> clazz) {
        return clazz.getName().replace('.', '/');
    }
}
