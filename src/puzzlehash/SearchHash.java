/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzlehash;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.Arrays;
import javax.swing.*;

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
    String TimerEnd;
    JLabel lblIntentos;
    JButton btnFinish;
    
    public SearchHash(String BaseText, String Criterial, int intentosMax, String nameTread, JLabel lblIntentos, JButton btnFinsh) {
        super(nameTread);
        StartDate = LocalDateTime.now();
        this.BaseText = BaseText;
        this.Criterial = Criterial;
        this.MaxIntent =intentosMax;
        this.lblIntentos = lblIntentos;
        this.btnFinish = btnFinsh;
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
        TimerEnd = "Tiempo:  ms: "+res;
        btnFinish.dispatchEvent(new MouseEvent(btnFinish, MouseEvent.MOUSE_CLICKED,System.currentTimeMillis(),0,10,10,1,false));
    }

    private void OtroIntento() {
        Count++;
        lblIntentos.setText(String.valueOf(Count));
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            flagTread = false;
        } finally {
            super.finalize();
        }
    }

    public void destroy() {
        flagTread = false;
    }

}
