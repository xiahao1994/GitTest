package com.cug.lab.util.GridUtil;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by xh on 2018/10/30.
 * 裁剪出一个小的Ras文件用于计算
 */
public class CuttingRas {
    public static void main(String[] args) {
        File rasfile = new File("E:\\temp\\Dem47.Ras");
        TileSplitDataInputStream mydis = null;
        HashMap map = new HashMap();
        try {
            InputStream in = new FileInputStream(rasfile);
            mydis = new TileSplitDataInputStream(in);
            long dbss = mydis.skip(4);
            map.put("nx",mydis.readInt());
            map.put("ny",mydis.readInt());
            map.put("x1",mydis.readDouble());
            map.put("x2",mydis.readDouble());
            map.put("y1",mydis.readDouble());
            map.put("y2",mydis.readDouble());
            map.put("z1",mydis.readDouble());
            map.put("z2",mydis.readDouble());

            //源文件头
            int nx=Integer.valueOf(map.get("nx").toString());
            int ny=Integer.valueOf(map.get("ny").toString());
            double x1=Double.valueOf(map.get("x1").toString());
            double x2=Double.valueOf(map.get("x2").toString());
            double y1=Double.valueOf(map.get("y1").toString());
            double y2=Double.valueOf(map.get("y2").toString());
            double z1=Double.valueOf(map.get("z1").toString());
            double z2=Double.valueOf(map.get("z2").toString());


            float[]data = new float[Integer.valueOf(map.get("nx").toString())*Integer.valueOf(map.get("ny").toString())];
            byte[]temp = new byte[4096];
            int len=0;
            int j =0;//数组下标
            while((len=in.read(temp))>0){
                for(int i =0;i<len/4;i++){
                    short a;
                    int ch4 = temp[4*i];
                    int ch3 = temp[4*i+1];
                    int ch2 = temp[4*i+2];
                    int ch1 = temp[4*i+3];
                    data[j] =Float.intBitsToFloat(((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0)));
                    j++;
                }
            }


            float[]newdata = new float[400*500];
            for(int n=0;n<400*500;n++){
                int y = n/500;
                int x = n%500;
                newdata[n] = data[y*nx+x];
            }

            OutputStream out = new FileOutputStream(new File("E:\\temp\\cutDem47.Ras"));
            byte[]dsbb = {'D','S','B','B'};
            out.write(dsbb);
            TileSplitDataOutputStream o = new TileSplitDataOutputStream(new DataOutputStream(out));
            o.writeInt(500);
            o.writeInt(400);
            o.writeDouble(x1);
            o.writeDouble(x2);
            o.writeDouble(y1);
            o.writeDouble(y2);
            o.writeDouble(z1);
            o.writeDouble(z2);
            for(int n=0;n<400*500;n++){
                o.writeFloat(newdata[n]);
            }
            o.close();
            in.close();
            System.out.println("over");
        }catch (Exception e){
            System.out.println("fail");
        }
    }

}
