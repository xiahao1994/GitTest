package com.cug.lab.util.GridUtil;

import com.cug.lab.util.GridUtil.RasHead;
import com.cug.lab.util.GridUtil.TileSplitDataInputStream;

import java.io.*;

public class SplitGridFile {
    //private static final short START=60;//数据头偏移量
    private static final float INVAILDDATA=1.70141E38f;//填充的无用数据，与地图中无用数据一致，为1.70141E38

    private  short tileSize;
    private  int COL;//grd数据的像素列
    private  int ROW;//grd数据的像素行
    private  int COL_NUM=0;//数据块的列数
    private  int ROW_NUM=0;//数据块的行数
    private  boolean stateX;//数据块是否需要填充X的标志
    private  boolean stateY;//数据头偏移量否需要填充Y的标志

    private short COL_ADD;//数据需要填充的像素列
    private short ROW_ADD;//数据需要填充的像素行

    private double x1,x2,y1,y2,z1,z2;
    private RandomAccessFile raf = null;

    /*public ArrayList<Integer> doSplit(short START, short TILE_SIZE, String RAS_PATH, String DES_PATH)  {
        // TODO Auto-generated method stub
        tileSize = TILE_SIZE;
        String rowKey = this.getRowKey(DES_PATH);
        ArrayList<Integer> invaildTileNoList = new ArrayList<Integer>();
        try {
            raf = new RandomAccessFile(RAS_PATH,"r");
            this.init(TILE_SIZE,RAS_PATH,rowKey);//初始化
            this.split(START,TILE_SIZE,DES_PATH,rowKey,invaildTileNoList);//分片
            this.writeHeadFile(DES_PATH+"/headMsg");//写新的头文件信息
        } catch (Exception e) {
            System.out.println("分片异常！文件转换失败");
            e.printStackTrace();
        }finally{
            try {
                raf.close();
                System.out.println("转换为tile文件完成！");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return invaildTileNoList;
    }
*/
    public static void main(String[] args) {
        //short a = Short.valueOf("10");
        //SplitGridFile.init(a,"E:\\hgtfile\\Dem47.Ras");
        //HgtHead rashead = new HgtHead("E:\\hgtfile\\N29E108.hgt");
        try {
            TileSplitDataInputStream mydis = new TileSplitDataInputStream(new FileInputStream("E:\\\\hgtfile\\\\N29E108.hgt"));
            long i = mydis.skip(4);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 初始化，得到文件相validNo关信息
     * @param raf
     * @throws IOException
     */
    public void init(short TILE_SIZE,String RAS_PATH) throws IOException{//读入数据初始化
        RasHead rashead = new RasHead(RAS_PATH);
        this.ROW = rashead.getNx();
        this.COL = rashead.getNy();
        this.x1 = rashead.getX1();
        this.x2 = rashead.getX2();
        this.y1 = rashead.getY1();
        this.y2 = rashead.getY2();
        this.z1 = rashead.getZ1();
        this.z2 = rashead.getZ2();
        System.out.println("ROW="+ROW);
        System.out.println("COL="+COL);

       // saveHeadMsgtoHBase(rowKey);
        //元信息写入HBase数据
    }
}