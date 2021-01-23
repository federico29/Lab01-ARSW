package edu.eci.arsw.threads;

import edu.eci.arsw.blacklistvalidator.HostBlackListsValidator;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import java.util.LinkedList;

public class HostBlackThread<checkHost> extends Thread {
    private HostBlacklistsDataSourceFacade skds;
    private int x;
    private int y;
    private String ipaddress;
    private int ocurrencesCount;
    private LinkedList<Integer> blackListOcurrences;
    private int checkedListsCount=0;


    public HostBlackThread(HostBlacklistsDataSourceFacade skds,int x, int y,String ipaddress){
        this.skds=skds;
        this.x=x;
        this.y=y;
        this.ipaddress=ipaddress;
        blackListOcurrences=new LinkedList<Integer>();
    }
    @Override
    public void run() {
        for (int i=x  ; i < y && ocurrencesCount < 5; i++) {
            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipaddress)) {
                blackListOcurrences.add(i);
                ocurrencesCount++;
            }
        }
    }

    public int getOcurrencesCount() {
        return ocurrencesCount;
    }

    public int getCheckedListsCount(){ return checkedListsCount; }

    public LinkedList<Integer> getBlackListOcurrences() {
        return blackListOcurrences;
    }
}
