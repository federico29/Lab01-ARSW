/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import edu.eci.arsw.threads.HostBlackThread;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;

    public static int getBlackListAlarmCount() {
        return BLACK_LIST_ALARM_COUNT;
    }

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
    public List<Integer> checkHost(String ipaddress){

        LinkedList<Integer> blackListOcurrences=new LinkedList<>();

        LinkedList<HostBlackThread> hilos=new LinkedList<>();

        LinkedList<Integer> blackListOcurrences2=new LinkedList<>();

        int ocurrencesCount;
        int ocurrencesCount2=0;

        HostBlacklistsDataSourceFacade skds= HostBlacklistsDataSourceFacade.getInstance();

        int checkedListsCount=0;

        int num=skds.getRegisteredServersCount()/80;
        int cont=0;

        for (int i=1;i<skds.getRegisteredServersCount()/1000;i++){
            HostBlackThread threadMini = new HostBlackThread(skds, cont, cont+num, ipaddress);
            hilos.add(threadMini);
            cont=cont+num;
        }

        for(HostBlackThread hilo:hilos){
            hilo.start();
        }

        for(HostBlackThread hilo:hilos){
            try {
                hilo.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ocurrencesCount = hilo.getOcurrencesCount();
            checkedListsCount+=hilo.getCheckedListsCount();
            ocurrencesCount2+=ocurrencesCount;
            blackListOcurrences = hilo.getBlackListOcurrences();
            for(Integer index:blackListOcurrences){
                blackListOcurrences2.add(index);
            }
            if (ocurrencesCount2>=BLACK_LIST_ALARM_COUNT){
                break;
            }
        }
        if (ocurrencesCount2>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        /*HostBlackThread threadMini = new HostBlackThread(skds, 0, 8000, ipaddress);

        threadMini.start();

        for (int i=0;i<skds.getRegisteredServersCount() && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){
            checkedListsCount++;

            if (skds.isInBlackListServer(i, ipaddress)){

                blackListOcurrences.add(i);

                ocurrencesCount++;
            }
        }
        try {
            threadMini.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ocurrencesCount = threadMini.getOcurrencesCount();
        blackListOcurrences = threadMini.getBlackListOcurrences();

        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }

        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
*/
        return blackListOcurrences2;
    }


    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());


}
