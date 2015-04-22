package simgesel;

import java.util.*;
import java.io.*;

abstract class Instruction {
    String instructionName;
    String desen;

    public enum tip 
    {
        I,R,J,NOP
    }

    public Instruction (String instructionName, String desen)
    {
        this.instructionName = instructionName;
        this.desen = desen;
    }

    public abstract String getBinary();
    public abstract tip getTip();

}

class R extends Instruction {
    String d,s,t;
    public R (String instructionName , String desen, String d, String s, String t)
    {
        super (instructionName, desen);
        this.d=d;
        this.s=s;
        this.t=t;
    }

    public String getBinary()
    {
        desen = (d!=null) ? (desen =desen.replace("dddd d",d)): desen;
        desen = (s!=null) ? (desen =desen.replace("ss sss",s)): desen;
        desen = (t!=null) ? (desen =desen.replace("t tttt",t)): desen; 

        desen= desen.replace(" ","");
        String idesen="";
        for (int i=0; i < desen.length();i++){
              if (i > 0 && i % 4 == 0) {idesen +=" ";}    
              idesen += desen.charAt(i);
        }
        desen = idesen;

        return desen;
    }
    
    public tip getTip()
    {
        return tip.R;
    }
}

class I extends Instruction {
    String c,s,t;
    public I (String instructionName , String desen, String t, String s, String c)
    {
        super (instructionName, desen);
        this.c=c;
        this.s=s;
        this.t=t;
    }

    public String getBinary()
    {

        desen = (c!=null) ? (desen =desen.replace("iiii iiii iiii iiii","00000000000"+c)): desen;
        desen = (s!=null) ? (desen =desen.replace("ss sss",s)): desen;
        desen = (t!=null) ? (desen =desen.replace("t tttt",t)): desen; 

        desen= desen.replace(" ","");
        String idesen="";
        for (int i=0; i < desen.length();i++){
              if (i > 0 && i % 4 == 0) {idesen +=" ";}    
              idesen += desen.charAt(i);
        }
        desen = idesen;

        return desen;

    }
    
    public tip getTip()
    {
        return tip.I;
    }
}


class J extends Instruction {
    String c;
    public J (String instructionName , String desen, String c)
    {
        super (instructionName, desen);
        this.c=c;
    }

    public String getBinary()
    {
        desen = (c!=null) ? (desen =desen.replace("ii iiii iiii iiii iiii iiii iiii","000000000000000000000"+c)): desen;

        desen= desen.replace(" ","");
        String idesen="";
        for (int i=0; i < desen.length();i++){
            if (i > 0 && i % 4 == 0) {idesen +=" ";}    
            idesen += desen.charAt(i);
        }
    desen = idesen;

    return desen;
    }

    public tip getTip()
    {
        return tip.J;
    }
}


class NOP extends Instruction {
    String c;
    public NOP (String instructionName , String desen)
    {
        super (instructionName, desen);
    }

    public String getBinary()
    {
        return desen;   
    }
    
    public tip getTip()
    {
        return tip.NOP;
    }
}


public class Simgesel {

    public static Dictionary desen = new Hashtable();
    public static Dictionary type = new Hashtable(); 
    public static Dictionary ins = new Hashtable();
    public static Dictionary kayitci = new Hashtable();
    public static Dictionary bin2hex = new Hashtable();
    public static String [] anla;

    public static void main(String[] args) {

        desen.put("add", "0000 00ss ssst tttt dddd d000 0010 0000");
        desen.put("sub", "0000 00ss ssst tttt dddd d000 0010 0010");
        desen.put("addi","0010 00ss ssst tttt iiii iiii iiii iiii");
        desen.put("lw",  "1000 11ss ssst tttt iiii iiii iiii iiii");
        desen.put("lb",  "1000 00ss ssst tttt iiii iiii iiii iiii");
        desen.put("sw",  "1010 11ss ssst tttt iiii iiii iiii iiii");
        desen.put("sb",  "1010 00ss ssst tttt iiii iiii iiii iiii");
        desen.put("lui", "0011 1100 000t tttt iiii iiii iiii iiii");
        desen.put("slt", "0000 00ss ssst tttt dddd d000 0010 1010");
        desen.put("slti","0010 10ss ssst tttt iiii iiii iiii iiii");
        desen.put("beq", "0001 00ss ssst tttt iiii iiii iiii iiii");
        desen.put("bne", "0001 01ss ssst tttt iiii iiii iiii iiii");
        desen.put("j",   "0000 10ii iiii iiii iiii iiii iiii iiii");
        desen.put("jr",  "0000 00ss sss0 0000 0000 0000 0000 1000");
        desen.put("jal", "0000 11ii iiii iiii iiii iiii iiii iiii");
        desen.put("nop", "0000 0000 0000 0000 0000 0000 0000 0000");

        type.put("add", "dst");
        type.put("sub", "dst");
        type.put("addi","tsc");
        type.put("lw",  "tcs");
        type.put("lb",  "tcs");
        type.put("sw",  "tcs");
        type.put("sb",  "tcs");
        type.put("lui", "tc");
        type.put("slt", "dst");
        type.put("slti","tsc");
        type.put("beq", "stc");
        type.put("bne", "stc");
        type.put("j",   "c");
        type.put("jr",  "s");
        type.put("jal", "c");

        ins.put("add", 1);
        ins.put("sub", 1);
        ins.put("addi",2);
        ins.put("lw",  2);
        ins.put("lb",  2);
        ins.put("sw",  2);
        ins.put("sb",  2);
        ins.put("lui", 2);
        ins.put("slt", 1);
        ins.put("slti",2);
        ins.put("beq", 2);
        ins.put("bne", 2);
        ins.put("j",   3);
        ins.put("jr",  1);
        ins.put("jal", 3);
        ins.put("nop", 4);

        kayitci.put("$0",      "00000");
        kayitci.put("$zero",   "00000");
        kayitci.put("$1",      "00001");
        kayitci.put("$at",     "00001");
        kayitci.put("$2",      "00010");
        kayitci.put("$v0",     "00010");
        kayitci.put("$3",      "00011");
        kayitci.put("$v1",     "00011");
        kayitci.put("$4",      "00100");
        kayitci.put("$a0",     "00100");
        kayitci.put("$5",      "00101");
        kayitci.put("$a1",     "00101");
        kayitci.put("$6",      "00110");
        kayitci.put("$a2",     "00110");
        kayitci.put("$7",      "00111");
        kayitci.put("$a3",     "00111");
        kayitci.put("$8",      "01000");
        kayitci.put("$t0",     "01000");
        kayitci.put("$9",      "01001");
        kayitci.put("$t1",     "01001");
        kayitci.put("$10",     "01010");
        kayitci.put("$t2",     "01010");
        kayitci.put("$11",     "01011");
        kayitci.put("$t3",     "01011");
        kayitci.put("$12",     "01100");
        kayitci.put("$t4",     "01100");
        kayitci.put("$13",     "01101");
        kayitci.put("$t5",     "01101");
        kayitci.put("$14",     "01110");
        kayitci.put("$t6",     "01110");
        kayitci.put("$15",     "01111");
        kayitci.put("$t7",     "01111");
        kayitci.put("$16",     "10000");
        kayitci.put("$s0",     "10000");
        kayitci.put("$17",     "10001");
        kayitci.put("$s1",     "10001");
        kayitci.put("$18",     "10010");
        kayitci.put("$s2",     "10010");
        kayitci.put("$19",     "10011");
        kayitci.put("$s3",     "10011");
        kayitci.put("$20",     "10100");
        kayitci.put("$s4",     "10100");
        kayitci.put("$21",     "10101");
        kayitci.put("$s5",     "10101");
        kayitci.put("$22",     "10110");
        kayitci.put("$s6",     "10110");
        kayitci.put("$23",     "10111");
        kayitci.put("$s7",     "10111");
        kayitci.put("$24",     "11000");
        kayitci.put("$t8",     "11000");
        kayitci.put("$25",     "11001");
        kayitci.put("$t9",     "11001");
        kayitci.put("$26",     "11010");
        kayitci.put("$k0",     "11010");
        kayitci.put("$27",     "11011");
        kayitci.put("$k1",     "11011");
        kayitci.put("$28",     "11100");
        kayitci.put("$gp",     "11100");
        kayitci.put("$29",     "11101");
        kayitci.put("$sp",     "11101");
        kayitci.put("$30",     "11110");
        kayitci.put("$fp",     "11110");
        kayitci.put("$31",     "11111");
        kayitci.put("$ra",     "11111");
        
        // Sembolik Bellek Adresi
        kayitci.put("0", "00000");
        
        bin2hex.put("0000", "0");
        bin2hex.put("0001", "1");
        bin2hex.put("0010", "2");
        bin2hex.put("0011", "3");
        bin2hex.put("0100", "4");
        bin2hex.put("0101", "5");
        bin2hex.put("0110", "6");
        bin2hex.put("0111", "7");
        bin2hex.put("1000", "8");
        bin2hex.put("1001", "9");
        bin2hex.put("1010", "A");
        bin2hex.put("1011", "B");
        bin2hex.put("1100", "C");
        bin2hex.put("1101", "D");
        bin2hex.put("1110", "E");
        bin2hex.put("1111", "F");
        
        
        execute();
    }

    public static String rParser () {
        int d,s,t;
        String dd,ss,tt;
        String pattern = type.get(anla[0]).toString();

        d= pattern.indexOf("d") !=-1 ? pattern.indexOf("d")+1 : 99 ;
        s= pattern.indexOf("s") !=-1 ? pattern.indexOf("s")+1 : 99 ;
        t= pattern.indexOf("t") !=-1 ? pattern.indexOf("t")+1 : 99 ;

        dd = (d== 99) ? null : kayitci.get(anla[d]).toString();
        ss = (s== 99) ? null : kayitci.get(anla[s]).toString();
        tt = (t== 99) ? null : kayitci.get(anla[t]).toString();

        R t1 = new R(anla[0],desen.get(anla[0]).toString(),dd,ss,tt);
        System.out.println(desen.get(anla[0]));
        System.out.println(t1.getBinary()+" "+bin2hex(t1.getBinary()));
        System.out.println(t1.getTip());
        System.out.println("");
        return t1.getBinary()+" "+bin2hex(t1.getBinary());
    }

    public static String iParser () {
        int t,s,c;
        String tt,ss,cc;
        String pattern = type.get(anla[0]).toString();

        t= pattern.indexOf("t") !=-1 ? pattern.indexOf("t")+1 : 99 ;
        s= pattern.indexOf("s") !=-1 ? pattern.indexOf("s")+1 : 99 ;
        c= pattern.indexOf("c") !=-1 ? pattern.indexOf("c")+1 : 99 ;

        tt = (t== 99) ? null : kayitci.get(anla[t]).toString();
        ss = (s== 99) ? null : kayitci.get(anla[s]).toString();
        cc = (c== 99) ? null : kayitci.get(anla[c]).toString();

        I t1 = new I(anla[0],desen.get(anla[0]).toString(),tt,ss,cc);
        System.out.println(desen.get(anla[0]));
        System.out.println(t1.getBinary()+" "+bin2hex(t1.getBinary()));
        System.out.println(t1.getTip());
        System.out.println("");
        return t1.getBinary()+" "+bin2hex(t1.getBinary());
    }

    public static String jParser () {
        int c;
        String cc;
        String pattern = type.get(anla[0]).toString();

        c= pattern.indexOf("c") !=-1 ? pattern.indexOf("c")+1 : 99 ;

        cc = (c== 99) ? null : kayitci.get(anla[c]).toString();

        J t1 = new J(anla[0],desen.get(anla[0]).toString(),cc);
        System.out.println(desen.get(anla[0]));
        System.out.println(t1.getBinary()+" "+bin2hex(t1.getBinary()));
        System.out.println(t1.getTip());
        System.out.println("");
        return t1.getBinary()+" "+bin2hex(t1.getBinary());
    }

    public static String nop () {
        NOP t1 = new NOP(anla[0],desen.get(anla[0]).toString());
        System.out.println(desen.get(anla[0]));
        System.out.println(t1.getBinary()+" "+bin2hex(t1.getBinary()));
        System.out.println(t1.getTip());
        System.out.println("");
        return t1.getBinary()+" "+bin2hex(t1.getBinary());
    }
    
    public static String bin2hex (String bin) {
        String [] tempb = bin.split("[\\s\\xA0]+");
        String ret="";
        for (int i = 0; i < tempb.length; i++){
            ret+=bin2hex.get(tempb[i]).toString();
        }    
        return ret;
    }
    
    public static void execute () {
        String ptest=null;
        String set= "";
        try {
            FileInputStream giris = new FileInputStream ("giris.txt");
            InputStreamReader stream = new InputStreamReader (giris,"ISO-8859-9");
            BufferedReader buffer = new BufferedReader (stream);
            ptest = buffer.readLine();

            FileWriter cikis = new FileWriter("cikis.txt");
            BufferedWriter bw = new BufferedWriter(cikis);

            while (ptest!=null) {

                if (ptest.indexOf("#") != -1) {
                    ptest = buffer.readLine();
                    continue;
                }
                String test = test = ptest.replace(",", " ");
                test = test.replace("(", " ");
                test = test.replace(")", "").trim();

                anla = test.split("[\\s\\xA0]+");

                int tart =  Integer.parseInt (ins.get(anla[0]).toString());
                switch (tart) 
                {
                     case 1:
                        set+=rParser();
                        break;
                     case 2:
                        set+=iParser();
                        break;
                     case 3:
                        set+=jParser();
                        break;
                     case 4:
                        set+=nop();
                        break;    
                }

                ptest = buffer.readLine();
                set+="\r\n";
            }
            buffer.close();
            bw.write(set);
            bw.close();
        } catch (Exception e) {System.err.println("Error: "+e.getMessage());}
    }

}
