/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzlehash;

import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 *
 * @author furid
 */
public class SearchHashConcurrente extends Thread {

    String BaseText;
    String Criterial;
    String HashFinding = null;
    char[] key = {0};
    int Count = 0;
    boolean flagTread = true;
    int MaxIntent = 0;
    int NThreads;
    LocalDateTime StartDate;    
    LocalDateTime EndDate;
    JButton btnFinish;
    JTextArea txtLog;
    String TimerEnd;
    JLabel lblIntentos;

    public SearchHashConcurrente(String BaseText, String Criterial, int intentosMax, String nameTread, int nThreads, JTextArea txtLog, JButton btnFinsh, JLabel lblIntentos) {
        super(nameTread);
        StartDate = LocalDateTime.now();
        this.BaseText = BaseText;
        this.Criterial = Criterial;
        this.MaxIntent = intentosMax;
        this.NThreads = nThreads;
        this.btnFinish = btnFinsh;
        this.txtLog = txtLog;        
        this.lblIntentos = lblIntentos;
    }


    @Override
    @SuppressWarnings("empty-statement")
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(NThreads);
        char[] startKey = {0};
        int step = Math.round(256 / NThreads);
        for (int i = 0; i < NThreads; i++) {            
            char MaxKey = (char)(step*(i+1));
            char[] NewstartKey = {0,0,MaxKey};
            if(NThreads < i+1)
                NewstartKey = null;
            executor.execute(new SearchHash(
                    this, startKey.clone(), "SubHilo"+String.valueOf(i), NewstartKey)
            );
            startKey =  NewstartKey;
        }
        while (flagTread && HashFinding == null && !executor.isTerminated());
        executor.shutdown();
        System.out.println("Main! Hash:" + HashFinding + "  KEY:" + Arrays.toString(key) + " Count:" + String.valueOf(Count));
        EndDate = LocalDateTime.now();
        long res = Utils.GetMiliTime(StartDate, EndDate); 
        System.out.println(this.getName()+ " Time ms: "+res);
        TimerEnd = "Tiempo:  ms: "+res;
        
        btnFinish.dispatchEvent(new MouseEvent(btnFinish, MouseEvent.MOUSE_CLICKED,System.currentTimeMillis(),0,10,10,1,false));
    }

    /**
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {        
            flagTread = false;        
    }

    public void destroy() {
        flagTread = false;
    }

    private class SearchHash extends Thread {

        SearchHashConcurrente Context;
        String HashFinding;
        char[] key = {0};
        int Count = 0;
        char[] MaxKey;   

        public SearchHash(SearchHashConcurrente context, char[] StartKey, String nameTread, char[] MaxKey) {
            super(nameTread);
            this.Context = context;
            this.key = StartKey;
            this.MaxKey = MaxKey;
        }

        @Override
        public void run() {
            try {
                while (Context.flagTread 
                    && (Context.MaxIntent >= Count || Context.MaxIntent == 0)
                    && (!(MaxKey.equals(key)) || MaxKey == null)
                    ) {
                    this.OtroIntento();
                    HashFinding = Hashing.sha256(Context.BaseText + Arrays.toString(key));
                    if (HashFinding.matches(Context.Criterial)) {
                        Context.HashFinding = HashFinding;
                        Context.key = key;
                        Context.flagTread = false;
                        break;
                    }
                    key = Utils.GetNextAscii(key, 0);
                }
                System.out.println(this.getName()+ " Hash:" + HashFinding + "  KEY:" + Arrays.toString(key) + " Count:" + String.valueOf(Count));                
                txtLog.append( this.getName()+ " Hash:" + HashFinding + "  KEY:" + Arrays.toString(key) + " Count:" + String.valueOf(Count)+"\n");
            } catch (Exception e) {
                System.err.println(e);
                System.out.println("ERROR! Hash:" + HashFinding + "  KEY:" + Arrays.toString(key) + " Count:" + String.valueOf(Count));
            }
           
        }

        private void OtroIntento() {
            Count++;
            Context.Count++;
            Context.lblIntentos.setText(String.valueOf(Context.Count));
        }

        public void destroy() {
        }
    }
}
