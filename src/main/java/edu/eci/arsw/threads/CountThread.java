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

    public CountThread(int a,int b){
        this.a=a;
        this.b=b;
    }

    public void run() {
        this.showNumbers(this.a, this.b);
    }

    public void showNumbers(int a, int b) {
        for (int i=this.a; i<=this.b; i++) {
            System.out.println(i);
        }
    }
}
