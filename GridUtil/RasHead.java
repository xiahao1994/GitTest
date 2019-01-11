package com.cug.lab.util.GridUtil;

import java.io.*;
import java.util.HashMap;


//头文件类
public class RasHead {

    private int nx;
    private int ny;
    //private int no_use;
    private double x1;
    private double x2;
    private double y1;
    private double y2;
    private double z1;
    private double z2;

    public static void main(String[] args) throws Exception {
        InputStream in = new FileInputStream(new File("E:\\地理计算框架文档\\栅格格式转换\\RasToSmall\\SlopeOut.Ras"));
        HashMap headMap = getRasHead(in);
        System.out.println(headMap.get("nx"));
        System.out.println(headMap.get("ny"));
        byte[]kk = new byte[8];
        in.read(kk);
        System.out.println(kk);
    }

    public RasHead(String path){
        this.readHeadFile(path);
    }

    public static HashMap getRasHead(InputStream in){
        TileSplitDataInputStream mydis = null;
        HashMap map = new HashMap();
        try {
            mydis = new TileSplitDataInputStream(in);
            long i = mydis.skip(4);
            map.put("nx",mydis.readInt());
            map.put("ny",mydis.readInt());
            map.put("x1",mydis.readDouble());
            map.put("x2",mydis.readDouble());
            map.put("y1",mydis.readDouble());
            map.put("y2",mydis.readDouble());
            map.put("z1",mydis.readDouble());
            map.put("z2",mydis.readDouble());
        }catch (Exception e){
            return null;
        }

        return map;
    }
    public void readHeadFile(String path) {
        TileSplitDataInputStream mydis = null;
        try {
            mydis = new TileSplitDataInputStream(new FileInputStream(path));
            long i = mydis.skip(4);
            this.nx = mydis.readInt();
            this.ny = mydis.readInt();
            this.x1 = mydis.readDouble();
            this.x2 = mydis.readDouble();
            this.y1 = mydis.readDouble();
            this.y2 = mydis.readDouble();
            this.z1 = mydis.readDouble();
            this.z2 = mydis.readDouble();
            System.out.println(nx);
            System.out.println(ny);
            System.out.println(x1);
            System.out.println(x2);
            System.out.println(y1);
            System.out.println(y2);
            System.out.println(z1);
            System.out.println(z2);
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

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public double getZ1() {
        return z1;
    }

    public void setZ1(double z1) {
        this.z1 = z1;
    }

    public double getZ2() {
        return z2;
    }

    public void setZ2(double z2) {
        this.z2 = z2;
    }

    public int getNx() {
        return nx;
    }
    public void setNx(int nx) {
        this.nx = nx;
    }
    public int getNy() {
        return ny;
    }
    public void setNy(int ny) {
        this.ny = ny;
    }
}