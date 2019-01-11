package com.cug.lab.util.GridUtil;

import java.io.*;

/**
 * Created by xh on 2018/12/10.
 */
public class WriteRas {
    public static void main(String[] args) throws Exception {
        //ployblockRas();
        //smallRas();
        x1000y600();
    }
    public static void writeRas() throws Exception{
        OutputStream out = new FileOutputStream(new File("E:\\地理计算框架文档\\栅格格式转换\\RasToGrd\\1000*1000.Ras"));
        byte[]dsbb = {'D','S','B','B'};
        out.write(dsbb);
        TileSplitDataOutputStream o = new TileSplitDataOutputStream(new DataOutputStream(out));
        o.writeInt(1000);
        o.writeInt(1000);
        o.writeDouble(108.0);
        o.writeDouble(113.0);
        o.writeDouble(30.0);
        o.writeDouble(33.0);
        o.writeDouble(0);
        o.writeDouble(6);
        for(int n=0;n<1000*1000;n++){
            int x = n/1000+n%1000;
            o.writeFloat((float) (n%1000+n/1000));
        }
        o.close();
    }
    public static void blockRas() throws Exception {
        OutputStream out = new FileOutputStream(new File("E:\\地理计算框架文档\\栅格格式转换\\RasToGrd\\block.Ras"));
        byte[]dsbb = {'D','S','B','B'};
        out.write(dsbb);
        TileSplitDataOutputStream o = new TileSplitDataOutputStream(new DataOutputStream(out));
        o.writeInt(1000);
        o.writeInt(1000);
        o.writeDouble(108.0);
        o.writeDouble(113.0);
        o.writeDouble(30.0);
        o.writeDouble(33.0);
        o.writeDouble(0);
        o.writeDouble(6);
        for(int n=0;n<1000*1000;n++){
            int x = n/1000+n%1000;
            o.writeFloat((float) (n%1000/256+n/1000/256));
        }
        o.close();
    }
    public static void smallRas() throws Exception {
        OutputStream out = new FileOutputStream(new File("E:\\地理计算框架文档\\栅格格式转换\\RasToGrd\\2Tile.Ras"));
        byte[]dsbb = {'D','S','B','B'};
        out.write(dsbb);
        TileSplitDataOutputStream o = new TileSplitDataOutputStream(new DataOutputStream(out));
        o.writeInt(400);
        o.writeInt(200);
        o.writeDouble(108.0);
        o.writeDouble(113.0);
        o.writeDouble(30.0);
        o.writeDouble(33.0);
        o.writeDouble(0);
        o.writeDouble(80000);
        for(int n=0;n<400*200;n++){

            o.writeFloat((float)n);
        }
        o.close();
    }
    public static void x1000y600() throws Exception {
        OutputStream out = new FileOutputStream(new File("E:\\地理计算框架文档\\栅格格式转换\\RasToGrd\\x1000y600.Ras"));
        byte[]dsbb = {'D','S','B','B'};
        out.write(dsbb);
        TileSplitDataOutputStream o = new TileSplitDataOutputStream(new DataOutputStream(out));
        o.writeInt(1000);
        o.writeInt(600);
        o.writeDouble(108.0);
        o.writeDouble(113.0);
        o.writeDouble(30.0);
        o.writeDouble(33.0);
        o.writeDouble(0);
        o.writeDouble(600000);
        for(int n=0;n<1000*600;n++){
            o.writeFloat((float)n);
        }
        o.close();
    }
    public static void ployblockRas() throws Exception {
        OutputStream out = new FileOutputStream(new File("E:\\地理计算框架文档\\栅格格式转换\\RasToGrd\\Ploy.Ras"));
        byte[]dsbb = {'D','S','B','B'};
        out.write(dsbb);
        TileSplitDataOutputStream o = new TileSplitDataOutputStream(new DataOutputStream(out));
        o.writeInt(1223);
        o.writeInt(936);
        o.writeDouble(110.0);
        o.writeDouble(113.0);
        o.writeDouble(30.0);
        o.writeDouble(33.0);
        o.writeDouble(0);
        o.writeDouble(8);
        for(int n=0;n<1223*936;n++){
            int x = n/1000+n%1000;
            o.writeFloat((float) (n%1223/256+n/1223/256+1));
        }
        o.close();
    }
}
