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

    /***
     * Método constructor de un hilo que hace la búsqueda de una dirección IP dentro de
     * un segmento del conjunto de servidores disponibles.
     * @param skds Objeto que contiene la información de todos los servidores disponibles.
     * @param x Limite inferior del intervalo de servidores que se va a revisar.
     * @param y Limite superior del intervalo de servidores que se va a revisar.
     * @param ipaddress Dirección IP que se desea buscar.
     */
    public HostBlackListThread(HostBlacklistsDataSourceFacade skds,int x, int y,String ipaddress){
        this.skds = skds;
        this.x = x;
        this.y = y;
        this.ipaddress = ipaddress;
        blackListOcurrences = new LinkedList<>();
    }

    /***
     * Método para correr el hilo, tiene el ciclo que realizará la busqueda en cada uno de los
     * servidores del intervalo.
     */
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

    /***
     * Retorna el número de veces que la dirección IP se encontró en los servidores dados.
     * @return El número de ocurrencias de la dirección IP en los servidores dados.
     */
    public int getOccurrencesCount() { return occurrencesCount; }

    /***
     *Retorna el número de listas que se han revisado.
     * @return El número de listas que se han revisado.
     */
    public int getCheckedListsCount(){ return checkedListsCount; }

    /***
     * Retorna el número que identifica a las listas de servidores donde se tienen ocurrencias de
     * la dirección IP dada.
     * @return El número que identifica a las listas de servidores donde se tienen ocurrencias de
     * la dirección IP dada.
     */
    public LinkedList<Integer> getBlackListOccurrences() { return blackListOcurrences; }
}