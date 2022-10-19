/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzlehash;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import static java.time.temporal.ChronoUnit.MILLIS;
import java.util.Date;

/**
 *
 * @author furid
 */
public class Utils {
    public static char[] GetNextAscii(char[] actual, int index){
        char[] next = actual;
        if(next[index]+1 > 255){
            next[index] = 0;
            if(actual.length <= index+1)
                next = (String.valueOf(next)+(char)0).toCharArray();
            next = GetNextAscii(next, 1);
        }else {
            next[index]++;
        }   
        return next;
    }
    
    public static long GetMiliTime(LocalDateTime dateOne, LocalDateTime dateTwo){
      long res = MILLIS.between(dateOne, dateTwo);
      return res;
    }
}
