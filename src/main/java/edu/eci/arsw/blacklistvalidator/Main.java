/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;

/**
 *
 * @author hcadavid
 */
public class Main {
    
    public static void main(String a[]){
        HostBlackListsValidator listsValidator=new HostBlackListsValidator();
        List<Integer> blackListOccurrences=listsValidator.checkHost("202.24.34.55",200);
        System.out.println("The host was found in the following blacklists:"+blackListOccurrences);
    }
    
}