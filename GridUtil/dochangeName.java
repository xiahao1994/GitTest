
package com.cug.lab.util.GridUtil;

        import java.io.*;
        import java.lang.reflect.Array;
        import java.text.DecimalFormat;
        import java.util.ArrayList;

/**
 * Created by xh on 2018/6/29.
 * 用于批量修改文件名（HGT文件查找）
 */
public class dochangeName {

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


        String fileroot= "\\\\Desktop-q19nm18\\HGT\\";
       if(true){dochangeName.restort(fileroot);return;}
//        double Ymin=-46.1;
//        double Ymax=37;
//        double Xmin=108.123;
//        double Xmax=109.223;

        //经纬度数组
        int xmin = 113;//(int)Xmin;
        int xmax = 154;//(int)Xmax;
        int ymin =-39;//(int)Ymin;
        int ymax = -11;//(int)Ymax;

        //X,Y 数量
        int Xcount = xmax-xmin+1;
        int Ycount = ymax-ymin+1;

        //创建文件名二维数组 filenameArr[0][0]="E29E108"
        String[][] filenameArr = new String[Ycount][Xcount];
        for (int j=0;j<Ycount;j++){
            for (int i=0;i<Xcount;i++){
                filenameArr[j][i]=getName(j,i,ymin,xmin);
                File oldfile=new File(fileroot+"/"+filenameArr[j][i]);
                File newfile=new File(fileroot+"/1"+filenameArr[j][i]);
                System.out.println(filenameArr[j][i]);
                if(!oldfile.exists()){
                    System.out.println(filenameArr[j][i]+"不存在");
                    //return;//重命名文件不存在
                }
                if(newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                    System.out.println("1"+filenameArr[j][i]+"已经存在！");
                else{
                    oldfile.renameTo(newfile);
                }

            }
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
        return filename+".hgt";
    }
    public static void restort(String fileroot){
        File file = new File(fileroot);
        File[] file_arr = file.listFiles();
        for(int i=0;i<file_arr.length;i++){
           System.out.println(file_arr[i].getName());
            if(file_arr[i].getName().startsWith("1")){
                File oldfile=new File(fileroot+file_arr[i].getName());
                File newfile=new File(fileroot+file_arr[i].getName().substring(1));
                oldfile.renameTo(newfile);

            }
        }
    }
}

