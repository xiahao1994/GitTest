package com.cug.lab.util.GridUtil;

import com.cug.lab.util.HdfsOperate;
import org.apache.hadoop.fs.FSDataInputStream;

import java.io.*;
import java.util.HashMap;

/**
 * Created by xh on 2018/12/10.
 */
public class BigRasToSmall {
    //定义预览图的长宽最大大小
    public static final int Max_X = 500;
    public static final int Max_Y = 400;


    private int nx;
    private int ny;
    private double z1;
    private double z2;
    private float[] data;
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

    public float[] getData() {
        return data;
    }

    public void setData(float[] data) {
        this.data = data;
    }

    public static void main(String[] args) throws Exception {
        InputStream in = new FileInputStream(new File("\\\\Admin-s-pc\\数据盘\\Australia\\Ras\\Australia.Ras"));
        RasView s =BigRasToSmall.getHDFSRasviewTest(in);
        int x=1;
    }

    /**
     *本函数是根据HDFS文件获取文件可视化对象
     * @param in  HDFS文件输入流
     * @return  RasView前端可视化对象
     */
    public static RasView getHDFSRasviewTest(InputStream in){
        RasView rasView = new RasView();
        // File rasfile = new File("E:\\RAS数据\\Africa.Ras");
        try {
            //源文件头
            HashMap map = RasHead.getRasHead(in);
            int nx=Integer.valueOf(map.get("nx").toString());
            int ny=Integer.valueOf(map.get("ny").toString());
            double x1=Double.valueOf(map.get("x1").toString());
            double x2=Double.valueOf(map.get("x2").toString());
            double y1=Double.valueOf(map.get("y1").toString());
            double y2=Double.valueOf(map.get("y2").toString());
            double z1=Double.valueOf(map.get("z1").toString());
            double z2=Double.valueOf(map.get("z2").toString());

            //缩放比例(每scala行取一行，每scala列取一列)
            int scala = ((nx-1)/Max_X>(ny-1)/Max_Y?(nx-1)/Max_X:(ny-1)/Max_Y)+1;//-1其实只是让500在里面

            //取样的数量
            int num_x = nx/scala;//{0,scala,2scala,..,(num_x-1)*scala}
            int num_y = ny/scala;//{0,scala,2scala,..,(num_y-10)*scala}
            float data[] = new float[num_x*num_y];

            int loc =0;//预览数组全局下标
            for(int row = 0;row<ny;row++) {


                    //准备读第row行的数据
                    float row_data[] = new float[nx];//存放一行的数据数组
                    byte row_byte[] = new byte[4*nx];
                    in.read(row_byte);
                    for (int i = 0; i <nx; i++) {
                        int ch4 = row_byte[4 *i    ]& 0xff;
                        int ch3 = row_byte[4 *i + 1]& 0xff;
                        int ch2 = row_byte[4 *i + 2]& 0xff;
                        int ch1 = row_byte[4 *i + 3]& 0xff;
                        if ((ch1 | ch2 | ch3 | ch4) < 0)System.out.println("xx");
                        row_data[i] = Float.intBitsToFloat(((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0)));
                    }
                    //抽取一行中的数据
                if((row+1)%scala==0){
                    for(int l =0 ;l<num_x;l++){
                        data[loc]=row_data[l*scala];
                        loc++;
                    }
                }

            }
            //数据存放发哦Rasview准备返回
            rasView.setNx(num_x);
            rasView.setNy(num_y);
            rasView.setZ1(Double.valueOf(map.get("z1").toString()));
            rasView.setZ2(Double.valueOf(map.get("z2").toString()));
            rasView.setData(data);
            OutputStream out = new FileOutputStream(new File("E:\\地理计算框架文档\\栅格格式转换\\RasToGrd\\cck.grd"));
            byte[]dsbb = {'D','S','B','B'};
            out.write(dsbb);
            //写入文件头到Grd
            TileSplitDataOutputStream o = new TileSplitDataOutputStream(new DataOutputStream(out));
            o.writeShort((short) num_x);
            o.writeShort((short) num_y);
            o.writeDouble(x1);
            o.writeDouble(x2);
            o.writeDouble(y1);
            o.writeDouble(y2);
            o.writeDouble(z1);
            o.writeDouble(z2);
            for(int i=0;i<data.length;i++){
                o.writeFloat(data[i]);
            }
            o.close();
            out.close();
            in.close();

        }catch (Exception e){
            System.out.println("fail");
            return null;
        }
        try {
            in.close();//关闭输入流
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rasView;
    }
}