package com.cug.lab.util.GridUtil;

import java.io.*;
import java.util.HashMap;

/**
 * Created by xh on 2018/7/12.
 */
public class checkgrd {

    public static void main(String[] args) {
        String path = "\\\\Desktop-q19nm18\\d\\NORTH AMERICA\\";
        check(path);
    }
    public static void check(String path){
        File file = new File(path);
        File[] file_arr = file.listFiles();
        for(int i=0;i<file_arr.length;i++){
            //System.out.println(file_arr[i].getName());
            TileSplitDataInputStream mydis = null;
            try {
                mydis = new TileSplitDataInputStream(new FileInputStream(path+file_arr[i].getName()));
                mydis.skip(4);
                short nx = mydis.readShort();
                short ny = mydis.readShort();
                double x1 = mydis.readDouble();
                double x2 = mydis.readDouble();
                double y1 = mydis.readDouble();
                double y2 = mydis.readDouble();
                double z1 = mydis.readDouble();
                double z2 = mydis.readDouble();

                if(z1<-1000){
                    System.out.println(file_arr[i].getName());
                    System.out.println(z1);
                    System.out.println(z2);
                }
                if(z2>7000){
                    System.out.println(file_arr[i].getName());
                    System.out.println(z1);
                    System.out.println(z2);
                }
                mydis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    mydis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }
        }

    public static void fun(InputStream in){
        try {
            //读头文件
            HashMap head = RasHead.getRasHead(in);
            short nx = Short.valueOf(head.get("nx").toString());
            short ny = Short.valueOf(head.get("ny").toString());
            double x1 = Double.valueOf(head.get("x1").toString());
            double x2 = Double.valueOf(head.get("x2").toString());
            double y1 = Double.valueOf(head.get("y1").toString());
            double y2 = Double.valueOf(head.get("y2").toString());
            double z1 = Double.valueOf(head.get("z1").toString());
            double z2 = Double.valueOf(head.get("z2").toString());
        }catch (Exception e){
        }
    }

}
