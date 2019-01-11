package com.cug.lab.util.GridUtil;

import com.cug.lab.util.HdfsOperate;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.yarn.proto.YarnServerCommonServiceProtos;

import java.io.*;
import java.util.HashMap;

/**
 * Created by xh on 2018/10/30.
 */
public class RasView {
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
        getHDFSTileView("/lyc123/upload_file/TestRas/tile");
    }

    /**
     * 本函数没有实际意义
     * b比较连个文件流的局别，结论不要用read 使用deadFully更好,read到一定程度会断
     * @throws Exception
     */
    public static void testequel()throws Exception{
        FSDataInputStream in1= HdfsOperate.inputStreamDDDDD("/lyc123/upload_file/Ras/ggg.ras");
        InputStream in2 = new FileInputStream(new File("E:\\地理计算框架文档\\栅格格式转换\\RasToGrd\\ggg.ras"));

        int len1 = in1.available();//返回总的字节数
        int len2 = in2.available();
        if (len1 == len2) {//长度相同，则比较具体内容
            //建立两个字节缓冲区
            byte[] data1 = new byte[len1];
            byte[] data2 = new byte[len2];

            //分别将两个文件的内容读入缓冲区
            in1.read(data1);
            in2.read(data2);

            //依次比较文件中的每一个字节
            for (int i=0; i<len1; i++) {
                //只要有一个字节不同，两个文件就不一样
                if (data1[i] != data2[i]) {
                    System.out.println(i+" 文件内容不一样");
                }
            }
            System.out.println("两个文件完全相同");
        } else {
            System.out.println("两个文件不相同");
        }

        in1.close();
        in2.close();
    }
    /**
     *本函数是根据HDFS文件获取文件可视化对象
     * @param in  HDFS文件输入流
     * @return  RasView前端可视化对象
     */
    public static RasView getHDFSRasviewTest(FSDataInputStream in){
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

                if((row+1)%scala==0){
                    //准备读第row行的数据
                    long position = (long)row*(long)nx*4+60;//row行的开始位置(从文件头算起)，int会有越界的问题用
                    float row_data[] = new float[nx];//存放一行的数据数组
                    byte row_byte[] = new byte[4*nx];
                    in.readFully(position,row_byte);
                    for (int i = 0; i <nx; i++) {
                        int ch4 = row_byte[4 *i    ]& 0xff;
                        int ch3 = row_byte[4 *i + 1]& 0xff;
                        int ch2 = row_byte[4 *i + 2]& 0xff;
                        int ch1 = row_byte[4 *i + 3]& 0xff;
                        if ((ch1 | ch2 | ch3 | ch4) < 0)System.out.println("xx");
                        row_data[i] = Float.intBitsToFloat(((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0)));
                    }
                    //抽取一行中的数据
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

    /**
     *本函数是根据HDFS Tile文件获取文件可视化对象
     * @param tilefolderpath  HDFS文件输入流
     * @return  RasView前端可视化对象
     */
    public static RasView getHDFSTileView(String tilefolderpath){
        //tilefolderpath ="/lyc123/upload_file/TestRas/tile";
        tilefolderpath = tilefolderpath.trim();
        String path = tilefolderpath.substring(0,tilefolderpath.lastIndexOf("tile"));
        RasView rasView = new RasView();
        short TILE_SIZE=0;
        int nx=0,ny=0;
        int ROW=0,COL=0;
        double x1 = 0;
        double x2 = 0;
        double y1 = 0;
        double y2 = 0;
        double z1 = 0;
        double z2 = 0;
        int ROW_NUM = 0;
        int COL_NUM=0;
        //第一步：读headMsg文件，确定文件大小
        try {
            myDataInputStream mydis = null;
            mydis = new myDataInputStream(HdfsOperate.inputStreamDownload(path+"headMsg"));
            TILE_SIZE = mydis.readShort();
            ROW = mydis.readInt();
            COL = mydis.readInt();
            x1 = mydis.readDouble();
            x2 = mydis.readDouble();
            y1 = mydis.readDouble();
            y2 = mydis.readDouble();
            z1 = mydis.readDouble();
            z2 = mydis.readDouble();
            ROW_NUM = mydis.readInt();//一行多少瓦片
            COL_NUM = mydis.readInt();//一列多少瓦片
            short ROW_ADD = mydis.readShort();
            short COL_ADD = mydis.readShort();
            //总行数和列数
            nx=COL+COL_ADD;
            ny=ROW+ROW_ADD;

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(nx==0||ny==0)return null;
        int scala = 1;//((nx-1)/Max_X>(ny-1)/Max_Y?(nx-1)/Max_X:(ny-1)/Max_Y)+1;//-1其实只是让500在里面
        while(nx/scala>Max_X||ny/scala>Max_Y){
            scala = scala*2;
        }
        //取样的数量
        int num_x = nx/scala;//{0,scala,2scala,..,(num_x-1)*scala}
        int num_y = ny/scala;//{0,scala,2scala,..,(num_y-10)*scala}
        float data[] = new float[num_x*num_y];

        int loc =0;//预览数组全局下标

        //第二步：读tile文件，准备抽取样点
        try {
            FSDataInputStream tile_inputinsteam = HdfsOperate.inputStreamDDDDD(path+"tile");

                //tile_row瓦片所在行数（从0开始）
                for(int tile_row=0;tile_row<ROW_NUM;tile_row++){
                    //tile_col瓦片所在列数（从0开始）
                    for(int tile_col=0;tile_col<COL_NUM;tile_col++){
                        //读一个瓦片
                        long position = (long)(tile_row*COL_NUM+tile_col)*((long)TILE_SIZE*TILE_SIZE)*4;//row行的开始位置(从文件头算起)，int会有越界的问题用
                        float tile[] = new float[TILE_SIZE*TILE_SIZE];
                        byte tile_byte[] = new byte[4*TILE_SIZE*TILE_SIZE];
                        tile_inputinsteam.readFully(position,tile_byte);
                        for (int i = 0; i <TILE_SIZE*TILE_SIZE; i++) {
                            int ch4 = tile_byte[4 *i   ]& 0xff;
                            int ch3 = tile_byte[4 *i + 1]& 0xff;
                            int ch2 = tile_byte[4 *i + 2]& 0xff;
                            int ch1 = tile_byte[4 *i + 3]& 0xff;
                            if ((ch1 | ch2 | ch3 | ch4) < 0)System.out.println("floatdata 4 bytes fail");
                            tile[i] = Float.intBitsToFloat(((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0)));
                        }
                        //tile[i]赋值给data[]
                        for(int i=0;i<TILE_SIZE/scala;i++){
                            for(int j=0;j<TILE_SIZE/scala;j++){
                                //关键转化部分第i行j列
                                int xxx = (tile_row*TILE_SIZE/scala+i)*num_x+(tile_col*TILE_SIZE/scala+j);
                                int yyy = i*TILE_SIZE*scala+j*scala;
                                data[xxx]=tile[yyy];
                            }
                        }
                    }
                }

        }catch (Exception e){
            System.out.println("tile文件解析失败");
            return null;
        }
        //测试，png输出图像
        /*
        String outputfilepng = "E:\\1.png";
        int rgbArray[][] = new int[num_y][num_x];
        for(int i=0;i<num_x*num_y;i++){
            int value = (new Double(((double)data[i]-z1)*255/(z2-z1))).intValue();
            rgbArray[num_y-1-i/num_x][ i%num_x]=value*256*256+value*256+value+255*256*256*256;
        }
        ImageWithArray.writeImageFromArray(outputfilepng, "png", rgbArray);//这里写你要输出的绝对路径+文件名
        */

        //第三步：生成RasView对象，返回
        //数据存放发哦Rasview准备返回
        rasView.setNx(num_x);
        rasView.setNy(num_y);
        rasView.setZ1(z1);
        rasView.setZ2(z2);
        rasView.setData(data);
        return rasView;
    }
}
