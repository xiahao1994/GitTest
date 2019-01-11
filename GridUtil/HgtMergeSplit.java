package com.cug.lab.util.GridUtil;

import java.io.*;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by xh on 2018/6/29.
 */
public class HgtMergeSplit {

    public static int COL_NUM = 3601;
    public static int ROW_Num = 3601;

    /*
    东经北纬为正 其他未负
    * args[0]   维度最小值
    * args[1]   维度最大值
    * args[2]   精度最小值
    * args[3]   精度最大值
    * */
    public static void main(String[] args) throws IOException {

        String fileroot= "E:\\地理计算框架文档\\测试数据\\hgtfile\\output\\";
        double Ymin=29.1212;
        double Ymax=30.2;
        double Xmin=108.123;
        double Xmax=109.223;


        //经纬度数组
        int xmin = (int)Xmin;
        int xmax = (int)Xmax;
        int ymin = (int)Ymin;
        int ymax = (int)Ymax;

        //X,Y 数量
        int Xcount = xmax-xmin+1;
        int Ycount = ymax-ymin+1;

        //创建文件名二维数组 filenameArr[0][0]="E29E108"
        String[][] filenameArr = new String[Ycount][Xcount];
        for (int j=0;j<Ycount;j++){
            for (int i=0;i<Xcount;i++){
                filenameArr[j][i]=getName(j,i,xmin,ymin);
                System.out.println(filenameArr[j][i]);
            }
        }
        merge(xmin,ymin,Xcount,Ycount,fileroot);
    }

    public static void merge(int xmin,int ymin,int Xcount,int Ycount,String fileroot) throws IOException {

        //输出文件流
        OutputStream out = new FileOutputStream(new File("E:\\test.grd"));
        //初始化输入文件流
        InputStream[][] inputStreamArr = new InputStream[Ycount][Xcount];
        for (int j = 0; j < Ycount; j++) {
            for (int i = 0; i < Xcount; i++) {
                //获取文件流数组
                inputStreamArr[i][j] = new FileInputStream(new File(fileroot + getName(i, j, ymin, xmin)));
                byte[]head = new byte[56];
                inputStreamArr[i][j].read(head);
            }
        }
        //划分开始
        byte[] dsbb = {'D', 'S', 'B', 'B'};
        out.write(dsbb);
        //写入文件头到Grd
        TileSplitDataOutputStream o = new TileSplitDataOutputStream(new DataOutputStream(out));

        short x = (short) (Xcount * COL_NUM);
        o.writeShort((short) (Xcount * (COL_NUM-1)));// 还要减去Xcount+1的
        o.writeShort((short) (Xcount * (ROW_Num-1)));
        o.writeDouble(xmin);
        o.writeDouble(xmin + Xcount);
        o.writeDouble(ymin);
        o.writeDouble(ymin + Ycount);
        o.writeDouble(1.1);//瞎写的
        o.writeDouble(2000);//瞎写的


        int k = 1;//从第一行写起
        //7202=1024*7+34

        byte[] temp1024 = new byte[1024];//读14次
        byte[] temp68 = new byte[64];//读1次
        byte[] temp4 = new byte[4];//读1次扔掉

        while (k <=Ycount * 3601) {
            if(k%3601==0){
                k++;
                continue;
            }
            int X = (k-1)/3601;//数组第一个a【√】【】
            //arr[2][] arr[1][] arr[0][]
            for (int i = 0; i <=Xcount-1; i++) {//0,1,...
                //temp7读7次

                for(int xx7=0; xx7<14; xx7++){
                int len= inputStreamArr[X][i].read(temp1024);
                    o.write(temp1024,0,len);}
                int len2= inputStreamArr[X][i].read(temp68);
                    o.write(temp68,0,len2);
                inputStreamArr[X][i].read(temp4);
/*                for (int bt = 0; bt < len2 / 2; bt++) {

                    //short a = (short) (((temp34[2 * bt] << 8) | temp34[2 * bt + 1] & 0xff));
                   // float high = (float) a;
                  //  o.writeFloat(high);
                }*/
            }
            k++;
        }

        }

    //获取文件类型名
    public static String getName(int y,int x,int ymin,int xmin){
       //确定文件经纬度
        int lat = ymin+y;
        int lon = xmin+x;
        DecimalFormat lat_format = new DecimalFormat("00");
        DecimalFormat lon_format = new DecimalFormat("000");
        String filename = "";
        if(lat>=0){
            filename+="N"+lat_format.format(lat);
            if(lon>=0){
                filename +="E"+lon_format.format(lon);
            }else
            {
                filename +="W"+lon_format.format((-1)*lon);
            }
        }
        else {
            filename+="S"+lat_format.format((-1)*lat);
            if(lon>=0){
                filename +="E"+lon_format.format(lon);
            }else
            {
                filename +="W"+lon_format.format((-1)*lon);
            }
        }
        return filename+".grd";
    }
}
