package com.cug.lab.util.GridUtil;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//Grd文件头
public class GrdHead {

    private short nx;
    private short ny;
    private double x1;
    private double x2;
    private double y1;
    private double y2;
    private double z1;
    private double z2;

    public GrdHead(String path){
        this.readHeadFile(path);
    }

    public static void main(String[] args) throws Exception {
        String file = "E:\\地理计算框架文档\\测试数据\\新建文件夹\\";
        double xmin=113.1;
        double xmax=114.1;
        double ymin=32.1;
        double ymax=35.2;
        List<String> xx = GrdHead.getFileList(new File(file),"grd");
        ArrayList<HashMap>list = new ArrayList<>();
        for(int i=0;i<xx.size();i++){
            InputStream in = new FileInputStream(new File(xx.get(i)));
            HashMap grdHead = GrdHead.getGrdHead(in);
            grdHead.put("filename",xx.get(i));
            if(getRangeFileList(grdHead,xmin,xmax,ymin,ymax)){
                list.add(grdHead);
            }
            in.close();
        }
        System.out.println(list);
    }

    /**
     * 判断该grd文件是否在范围内有交集
     * @param grdHead
     * @param xmin
     * @param xmax
     * @param ymin
     * @param ymax
     * @return
     */
    public static boolean getRangeFileList(HashMap grdHead,double xmin,double xmax,double ymin,double ymax){
        double x1 = Double.valueOf(grdHead.get("x1").toString());
        double x2 = Double.valueOf(grdHead.get("x2").toString());
        double y1 = Double.valueOf(grdHead.get("y1").toString());
        double y2 = Double.valueOf(grdHead.get("y2").toString());
        return !(x2<=xmin||x1>=xmax||y2<=ymin||y1>=ymax);
    }
    /**
     * 获取文件夹下所有的某文件列表
     * @param file
     * @return
     */
    public static List<String> getFileList(File file,String Suffix ) {
        List<String> result = new ArrayList<String>();
        if (!file.isDirectory()) {
            System.out.println(file.getAbsolutePath());
            result.add(file.getAbsolutePath());
        } else {
            File[] directoryList = file.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    if (file.isFile() && file.getName().indexOf(Suffix) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            for (int i = 0; i < directoryList.length; i++) {
                result.add(directoryList[i].getPath());
            }		}
        return result;
    }

    /**
     * 读取.grd文件流中的文件头文件
     * @param in
     * @return
     */
    public static HashMap getGrdHead(InputStream in){
        TileSplitDataInputStream mydis = null;
        HashMap map = new HashMap();
        try {
            mydis = new TileSplitDataInputStream(in);
            long i = mydis.skip(4);
            map.put("nx",mydis.readShort());
            map.put("ny",mydis.readShort());
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
            this.nx = mydis.readShort();
            this.ny = mydis.readShort();
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
}