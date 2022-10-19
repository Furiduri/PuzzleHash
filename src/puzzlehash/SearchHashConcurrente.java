/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzlehash;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public SearchHashConcurrente(String BaseText, String Criterial, int intentosMax, String nameTread, int nThreads) {
        super(nameTread);
        StartDate = LocalDateTime.now();
        this.BaseText = BaseText;
        this.Criterial = Criterial;
        this.MaxIntent = intentosMax;
        this.NThreads = nThreads;
    }

    @Override
    public void run() {
        System.out.println();
        ExecutorService executor = Executors.newFixedThreadPool(NThreads);
        char[] startKey = {0};
        for (int i = 0; i < NThreads; i++) {
            int MaxKeyLeng = 0;
            if(NThreads > i+1)
               MaxKeyLeng = i + 2;
            executor.execute(new SearchHash(
                    this, startKey, "SubHilo"+String.valueOf(i), MaxKeyLeng)
            );
            startKey = (String.valueOf(startKey) +(char)0+(char)0).toCharArray();
        }
        while (flagTread && HashFinding == null && !executor.isTerminated());
        executor.shutdown();
        System.out.println("Main! Hash:" + HashFinding + "  KEY:" + Arrays.toString(key) + " Count:" + String.valueOf(Count));
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

    private class SearchHash extends Thread {

        SearchHashConcurrente Context;
        String HashFinding;
        char[] key = {0};
        int Count = 0;
        boolean flagTread = true;
        int MaxKeyLeng;   

        public SearchHash(SearchHashConcurrente context, char[] StartKey, String nameTread, int MaxKey) {
            super(nameTread);
            this.Context = context;
            this.key = StartKey;
            this.MaxKeyLeng = MaxKey;
        }

        @Override
        public void run() {
            try {
                while (Context.flagTread 
                    && (Context.MaxIntent >= Count || Context.MaxIntent == 0)
                    && (MaxKeyLeng >= key.length || MaxKeyLeng == 0)
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
            } catch (Exception e) {
                System.err.println(e);
                System.out.println("ERROR! Hash:" + HashFinding + "  KEY:" + Arrays.toString(key) + " Count:" + String.valueOf(Count));
            }
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
}
