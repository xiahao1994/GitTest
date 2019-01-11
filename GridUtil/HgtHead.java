package com.cug.lab.util.GridUtil;

import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;




//头文件类
public class HgtHead {

    private int nx;
    private int ny;
    //private int no_use;
    private double x1;
    private double x2;
    private double y1;
    private double y2;
    private short z1;
    private short z2;

    public static void main(String[] args) throws Exception {
        InputStream in = new FileInputStream(new File("\\\\Admin-s-pc\\hpc测试数据\\测试hgt\\N15W092.hgt"));
        HgtHead h = new HgtHead(in,"\\\\Admin-s-pc\\hpc测试数据\\测试hgt\\N15W092.hgt");
        System.out.println(h);
    }

    public HgtHead(InputStream in,String path) throws IOException {
        this.readHeadFile(in,path);
    }
    //计算文件中z值得最大值、最小值
    public  static short[] getZSt(InputStream in) throws IOException {
        //初始化最大值和最小值
        short Zmax =Short.MIN_VALUE;
        short Zmin =Short.MAX_VALUE;

        byte[]temp = new byte[4096];
            int len=0;//读近缓冲区的长度
            while((len=in.read(temp))>0){
              // System.out.println(len);
                for(int i =0;i<len/2;i++){
                    short a = (short) (((temp[2*i] << 8) | temp[2*i+1] & 0xff));
                    if(a>Zmax)if(a<10000)Zmax =a;
                    if(a<Zmin)if(a>-1000)Zmin =a;
                }
            }

        short[]re= {Zmin,Zmax};//new short[2];
//        re[0]= Zmin;
//        re[1]=Zmax;
        return re;
    }

    public void readHeadFile(InputStream in,String path) throws IOException {

        char lat[]=new char[3];
        char lon[] = new char[4];
        long Row,Col;
        double XGap,YGap,XMin,YMin;
        String BilFileName = path;//参数
        File tempFile =new File( BilFileName.trim());
        String fname = tempFile.getName();
        //左下角纬度.
        for(int i=0; i<2; i++) lat[i]=fname.charAt(i+1);
        lat[2]='\0';
        //左下角经度.
        for(int i=0; i<3; i++) lon[i]=fname.charAt(i+4);
        lon[3]='\0';
        YMin = Double.parseDouble(new String(lat));
        XMin = Double.parseDouble(new String(lon));
        if(fname.charAt(0)=='S' || fname.charAt(0)=='s') YMin*=-1;
        if(fname.charAt(3)=='W' || fname.charAt(3)=='w') XMin*=-1;
        Row =3601;
        Col=3601;
        XGap=YGap=1./3600;

        this.nx=3601;
        this.ny=3601;
        this.x1=XMin;
        this.x2=XMin+1;
        this.y1=YMin;
        this.y2=YMin+1;
        short[] z =getZSt(in);
        this.z1=z[0];
        this.z2=z[1];

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

    public void setZ1(short z1) {
        this.z1 = z1;
    }

    public double getZ2() {
        return z2;
    }

    public void setZ2(short z2) {
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