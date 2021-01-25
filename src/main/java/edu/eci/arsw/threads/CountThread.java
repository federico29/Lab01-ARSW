/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThread extends Thread{
    public int a;
    public int b;

    /***
     * Método constructor de un hilo que cuenta los numero entre a y b.
     * @param a Entero.
     * @param b Entero.
     */
    public CountThread(int a,int b){
        this.a=a;
        this.b=b;
    }

    /***
     * Método para correr el hilo.
     */
    public void run() {
        this.showNumbers(this.a, this.b);
    }

    /***
     * Método en donde se desarrolla el ciclo para ver los números consecutivos.
     * @param a Entero.
     * @param b Entero.
     */
    public void showNumbers(int a, int b) {
        for (int i=this.a; i<=this.b; i++) {
            System.out.println(i);
        }
    }
}
