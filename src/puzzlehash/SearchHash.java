/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzlehash;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 *
 * @author furid
 */
public class SearchHash extends Thread {

    String BaseText;
    String Criterial;
    String HashFinding;
    char[] key = {0};
    int Count = 0;
    boolean flagTread = true;
    int MaxIntent = 0;
    LocalDateTime StartDate;    
    LocalDateTime EndDate;
    
    
    public SearchHash(String BaseText, String Criterial, int intentosMax, String nameTread) {
        super(nameTread);
        StartDate = LocalDateTime.now();
        this.BaseText = BaseText;
        this.Criterial = Criterial;
        this.MaxIntent =intentosMax;
    }

    @Override
    public void run() {
        try {
            while (flagTread && (MaxIntent >= Count || MaxIntent == 0)) {
                this.OtroIntento();
               HashFinding = Hashing.sha256(BaseText + Arrays.toString(key));
                if (HashFinding.matches(Criterial)) {
                    break;
                }
                key = Utils.GetNextAscii(key, 0);
            }
            System.out.println("Hash:" + HashFinding + "  KEY:" + Arrays.toString(key) + " Count:" + String.valueOf(Count));
        } catch (Exception e) {
            System.err.println(e);
            System.out.println("ERROR! Hash:" + HashFinding + "  KEY:" + Arrays.toString(key) + " Count:" + String.valueOf(Count));
        }
        EndDate = LocalDateTime.now();
        long res = Utils.GetMiliTime(StartDate, EndDate); 
        System.out.println(this.getName()+ " Time ms: "+res);
    }

    private void OtroIntento() {
        Count++;
    }

    @Override
    protected void finalize() throws Throwable {
        flagTread = false;
    }

    @Override
    public void destroy() {
        flagTread = false;
    }

}
