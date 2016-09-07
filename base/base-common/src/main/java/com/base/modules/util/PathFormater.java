package com.base.modules.util;

import static java.io.File.separator;
import static java.util.regex.Matcher.quoteReplacement;

/**
 * Created by micheal on 15/7/10.
 */
public class PathFormater {

    private PathFormater(){

    }
    
    public static String formate(String path){
        return path.replaceAll("[/|\\\\]+", quoteReplacement(separator));
    }
}
