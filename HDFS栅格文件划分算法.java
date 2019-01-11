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
