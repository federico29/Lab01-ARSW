package edu.eci.arsw.threads;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;

public class HostBlackListThread<checkHost> extends Thread {
    private HostBlacklistsDataSourceFacade skds;
    private int x;
    private int y;
    private String ipaddress;
    private int occurrencesCount;
    private LinkedList<Integer> blackListOcurrences;
    private int checkedListsCount = 0;


    public HostBlackListThread(HostBlacklistsDataSourceFacade skds,int x, int y,String ipaddress){
        this.skds = skds;
        this.x = x;
        this.y = y;
        this.ipaddress = ipaddress;
        blackListOcurrences = new LinkedList<>();
    }

    @Override
    public void run() {
        for (int i = x; i < y && occurrencesCount < 5; i++) {
            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipaddress)) {
                blackListOcurrences.add(i);
                occurrencesCount++;
            }
        }
    }

    public int getOccurrencesCount() { return occurrencesCount; }

    public int getCheckedListsCount(){ return checkedListsCount; }

    public LinkedList<Integer> getBlackListOccurrences() { return blackListOcurrences; }
}