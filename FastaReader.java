/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ash
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.io.IOUtils;

public class FastaReader {
    
    public static void main(String args[]) throws FileNotFoundException, IOException
    {
        String everything = null;
        String exp = null;
        String trial = "";
        boolean flag = false;
        boolean flag1 = false;
        String reads = "No. of Reads";
        StringBuffer sb = new StringBuffer();
        ArrayList<String> str = new ArrayList<String>();
        int count=0;
        Map<String, Integer> hmap = new HashMap<String, Integer>();
        String input = "/Users/Ash/NetBeansProjects/testing/dist/POV_reads.fa.txt";
        String output = "/Users/Ash/NetBeansProjects/testing/dist/output.txt";
    BufferedReader br = new BufferedReader(new FileReader(input));
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(output)));
    try {
        StringBuilder sb1 = new StringBuilder();
        String line = br.readLine();
        while (line != null) 
        {
            sb1.append(line);
            sb1.append("\n");
            line = br.readLine();
        }
        everything = sb1.toString();
        } 
    finally {
        br.close();
        }

    String[] splits = everything.split("\n");
    String temp = "";
    if(splits.length!=0){
    for(int i=0; i<splits.length; i++)
    { 
       if(splits[i].length() != 0)
       {
        if(splits[i].charAt(0)=='>')
       {
           if(temp!="")
           {
               str.add(temp);
               temp = "";
               count++;
           }
           flag1 = false;     
                if(hmap.containsKey(reads))
                {
                    hmap.put(reads, hmap.get(reads)+1);
                    flag1 = true;
                }
                if(flag1 == false)
                {
                    hmap.put(reads, 1);
                }
       }
       else
       {
           temp = temp + splits[i];
           if(i==splits.length-1)
           {
               if(temp!="")
               {
                   str.add(temp);
                   temp = "";
               }
           }
       }
    }
    }
    }
    if(!str.isEmpty())
    {
    for(int j=0; j<str.size(); j++)
    {
        exp = str.get(j);
        char[] exp1 = exp.toCharArray();
        for(int k=0; k<exp1.length; k++)
        {
            for(int l=k; l<k+5; l++)
            {
                if(k+5 <= exp1.length)
                {               
                    trial = trial + (Character.toString(exp1[l]));  
                }
                else
                {
                    break;
                }
            }
            if(trial != "")
            {
                flag = false;
                boolean test = false;
                char[] chartrial = trial.toCharArray();
                for(int x=0; x<chartrial.length; x++)
                {
                    if(chartrial[x] == 'A' ||  chartrial[x] == 'C' || chartrial[x] == 'G' || chartrial[x] == 'T')
                    {
                        test = true;
                    }
                    else
                    {
                        test = false;
                        break;
                    }
                }
                if(test == true)
                {
                if(hmap.containsKey(trial))
                {
                    hmap.put(trial, ((hmap.get(trial))+1));
                    trial = "";
                    flag = true;
                }
                if(flag == false)
                {
                    hmap.put(trial, 1);
                    trial = "";
                }
                }
                else
                {
                    trial = "";
                }
            }   
        }       
    }
    }
    try
    { 
    Map<String, Integer> tmap = new TreeMap<String, Integer>(hmap);
    Set set1 = tmap.entrySet(); 
    Iterator it = set1.iterator();
    while(it.hasNext())
    {
        Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) it.next();
        out.print(pair.getKey());
        out.print(" ");
        out.print(pair.getValue());
        out.print("\n");
    }    
    }
    finally
    {
        out.close();
    }
    //System.out.println(Arrays.asList(tmap));
   }
}
