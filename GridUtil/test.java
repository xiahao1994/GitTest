package com.cug.lab.util.GridUtil;

import mil.nga.tiff.FileDirectory;
import mil.nga.tiff.Rasters;
import mil.nga.tiff.TIFFImage;
import mil.nga.tiff.TiffWriter;
import mil.nga.tiff.util.TiffConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by xh on 2018/7/16.
 */
public class test {
    public static void main(String[] args) throws Exception {

        InputStream in = new FileInputStream(new File("E:\\xxxx\\AsiaEurope.Ras"));
        HashMap headMap = RasHead.getRasHead(in);
        System.out.println("列数"+headMap.get("nx"));
        System.out.println("行数"+headMap.get("ny"));




        int width = Integer.valueOf(headMap.get("nx").toString());
        int height = Integer.valueOf(headMap.get("ny").toString());
        int samplesPerPixel = 1;
        int bitsPerSample = 32;

        Rasters rasters = new Rasters(width,height,samplesPerPixel,bitsPerSample, TiffConstants.SAMPLE_FORMAT_FLOAT);
        //Rasters rasters = new Rasters(1,1,1,1);

        int rowsPerStrip = rasters.calculateRowsPerStrip(TiffConstants.PLANAR_CONFIGURATION_CHUNKY);

        FileDirectory directory = new FileDirectory();
        directory.setImageWidth(width);
        directory.setImageHeight(height);
        directory.setBitsPerSample(bitsPerSample);
        directory.setCompression(TiffConstants.COMPRESSION_NO);
        directory.setPhotometricInterpretation(TiffConstants.PHOTOMETRIC_INTERPRETATION_BLACK_IS_ZERO);
        directory.setSamplesPerPixel(samplesPerPixel);
        directory.setRowsPerStrip(rowsPerStrip);
        directory.setPlanarConfiguration(TiffConstants.PLANAR_CONFIGURATION_CHUNKY);
        directory.setSampleFormat(TiffConstants.SAMPLE_FORMAT_FLOAT);
        directory.setWriteRasters(rasters);

        //缓冲区每次读4096个字节
        byte[]temp = new byte[4096];
       //已经读取了4096字节的次数
        int NumOfRead =0;
        int len=0;
        while((len=in.read(temp))>0){
            //每次取四个字节读成float，再存进tiff文件
            for(int i =0;i<len/4;i++){

                //short a = (short) (((temp[2*i] << 8) | temp[2*i+1] & 0xff));
                int a =  ((temp[4*i+3] << 24) + (temp[4*i+2] << 16) + (temp[4*i+1] << 8) + (temp[4*i] << 0));
                float pixelValue = Float.intBitsToFloat(a);
                //确定tiff中的位置（x,y），tiff与raster文件上下位置颠倒
                int x = (NumOfRead*4096+4*i)/4%width;
                int y = height-(NumOfRead*4096+4*i)/4/width-1;
                    rasters.setFirstPixelSample(x, y, pixelValue);
            }
            NumOfRead++;
        }

//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                float pixelValue = (float)(x+y);// Float.valueOf("0");
//                rasters.setFirstPixelSample(x, y, pixelValue);
//            }
//        }

        TIFFImage tiffImage = new TIFFImage();
        tiffImage.add(directory);

// or
        try {
            byte[] bytes = TiffWriter.writeTiffToBytes(tiffImage);
            File file = new File("E:\\xxxx\\AsiaEurope.tif");
            TiffWriter.writeTiff(file, tiffImage);
        }catch (Exception e){
            System.out.println("failed");
        };
    }
}
