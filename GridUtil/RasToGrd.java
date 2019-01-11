package com.cug.lab.util.GridUtil;

import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.util.HashMap;

/**
 * Created by xh on 2018/6/22.
 */
public class RasToGrd {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream in_ras= new FileInputStream(new File("E:\\坡度算子\\数据\\Dem47.Ras"));

        OutputStream out = new FileOutputStream(new File("E:\\坡度算子\\数据\\Dem47.grd"));
        fun(in_ras,out);
}
    public static void fun(InputStream in_ras,OutputStream out){
        try {

            //读头文件
            HashMap head = RasHead.getRasHead(in_ras);
            short nx = Short.valueOf(head.get("nx").toString());
            short ny = Short.valueOf(head.get("ny").toString());
            double x1 = Double.valueOf(head.get("x1").toString());
            double x2 = Double.valueOf(head.get("x2").toString());
            double y1 = Double.valueOf(head.get("y1").toString());
            double y2 = Double.valueOf(head.get("y2").toString());
            double z1 = Double.valueOf(head.get("z1").toString());
            double z2 = Double.valueOf(head.get("z2").toString());

            //文件头先写入"DBSS"标识
            byte[]dsbb = {'D','S','B','B'};
            out.write(dsbb);
            //写入文件头到Grd
            TileSplitDataOutputStream o = new TileSplitDataOutputStream(new DataOutputStream(out));
            o.writeShort(nx);
            o.writeShort(ny);
            o.writeDouble(x1);
            o.writeDouble(x2);
            o.writeDouble(y1);
            o.writeDouble(y2);
            o.writeDouble(z1);
            o.writeDouble(z2);

            //复制剩下的数据
            IOUtils.copyBytes(in_ras,out,4096);
            in_ras.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
