/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import edu.eci.arsw.threads.HostBlackListThread;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT = 5;

    public static int getBlackListAlarmCount() { return BLACK_LIST_ALARM_COUNT; }

    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress,int n){

        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();

        LinkedList<Integer> blackListOccurrences;
        LinkedList<Integer> globalBlackListOccurrences = new LinkedList<>();
        LinkedList<HostBlackListThread> threads = new LinkedList<>();

        int globalOccurrencesCount = 0;
        int checkedListsCount = 0;
        int serversNumber = skds.getRegisteredServersCount()/n;
        int cont = 0;

        for (int i = 0; i < n; i++){
            threads.add(new HostBlackListThread(skds, cont, cont + serversNumber, ipaddress));
            cont = cont + serversNumber;
        }

        for(HostBlackListThread hilo : threads){
            hilo.start();
        }

        for(HostBlackListThread hilo : threads){
            try {
                hilo.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            globalOccurrencesCount = globalOccurrencesCount + hilo.getOccurrencesCount();
            checkedListsCount= checkedListsCount + hilo.getCheckedListsCount();
            blackListOccurrences = hilo.getBlackListOccurrences();
            for(Integer hostList : blackListOccurrences){
                globalBlackListOccurrences.add(hostList);
            }
        }

        if (globalOccurrencesCount >= BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }

        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});

        return globalBlackListOccurrences;
    }

    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());

}
