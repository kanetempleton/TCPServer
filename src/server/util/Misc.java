/*
    miscellanious methods i wrote that are useful while coding

    Written by Kane Templeton Jan 21, 2019
 */

package server.util;

import java.awt.Rectangle;


public class Misc {
    
    public static boolean inRange(int x, int y, int minx, int miny, int maxx, int maxy) {
        return (x>=minx&&x<=maxx&&y>=miny&&y<=maxy);
    }
    
    public static boolean inRange(int x, int y, int startx, int starty, Rectangle R) {
        return (x>=startx&&y>=starty&&x<=startx+R.width&&y<=starty+R.height);
    }
    
    public static String arrayToString(String[] words, int fromIndex) {
        String build = "";
        for (int i=fromIndex; i<words.length-1; i++)
            build+=words[i]+" ";
        build+=words[words.length-1];
        return build;
    }

}
