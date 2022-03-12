/**
 * @ClassName DealFile
 * @Description TODO
 * @Author Wenkun Lei
 * @Date 2022/3/10 14:53
 * @Version 1.0
 **/

import java.io.*;

public class DealFile {
    //  处理文件格式，输入参数依次为文件的编码格式、待处理的输入文件的路径、处理完成的输出文件的路径
    public void dealTxtFile(String encoding, String inputFile, String outputFile) {
        try {
            File file = new File(inputFile);
            //  判断文件是否存在
            if (file.isFile() && file.exists()) {
                //  考虑到编码格式，使用对应编码格式将文件输入流加载为输入读取流
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                //  创建文件读取缓冲区
                BufferedReader br = new BufferedReader(read);
                //  创建文件输出流
                FileWriter fw = new FileWriter(outputFile);
                //  创建文件写入缓冲区
                BufferedWriter bw = new BufferedWriter(fw);
                //  创建读取缓冲区的每行的字符串变量
                String lineTxt = null;
                //  创建每行数据的省份字符串变量
                String currentProvince = "0";
                //  缓冲区有数据就一直一行一行地读取
                while ((lineTxt = br.readLine()) != null) {
                    //  使用空格作为分割符，将每行的字符串分割为字符串数组
                    String[] lineArray = lineTxt.split("\\s+");
                    //  略去“待明确地区”
                    if(lineArray[1].equals("待明确地区")) {
                        continue;
                    }
                    //  将当前城市的省份进行比较，如果省份不一样了，那么将对当前省份进行替换
                    if (!currentProvince.equals(lineArray[0])) {
                        if (!currentProvince.equals("0")) {
                            //  写入一个换行符
                            bw.newLine();
                        }
                        currentProvince = lineArray[0];
                        bw.write(lineArray[0]);
                        //  写入一个换行符
                        bw.newLine();
                    }
                    //  向缓冲区写入除了省份的其余数据
                    bw.write(lineArray[1] + "\t" + lineArray[2]);
                    //  写入一个换行符
                    bw.newLine();
                    bw.flush();
                }
                // 关闭io流
                bw.close();
                read.close();
                System.out.println("执行完毕");
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            //  抛出异常
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }


    //  对文件的编码格式进行判断
    public String getFileFormat(String filePath) throws Exception {
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        byte[] head = new byte[3];
        inputStream.read(head);
        String code = "GBK";
        if (head[0] == -1 && head[1] == -2) {
            code = "UTF-16";
        }
        if (head[0] == -2 && head[1] == -1) {
            code = "Unicode";
        }
        if (head[0] == -17 && head[1] == -69 && head[2] == -65) {
            code = "UTF-8";
        }
        System.out.println("The format of this file is " + code);
        inputStream.close();
        return code;
    }


    public static void main(String[] args) throws Exception {
        String inputFile = "src/yq_in.txt";
        String outputFile = "src/my_yq_out.txt";
        DealFile rf = new DealFile();
        String encoding = rf.getFileFormat(inputFile);
        rf.dealTxtFile(encoding, inputFile, outputFile);
    }
}
