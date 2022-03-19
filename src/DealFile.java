/**
 * @ClassName DealFile
 * @Description TODO
 * @Author Wenkun Lei
 * @Date 2022/3/19 14:26
 * @Version 3.0
 **/

import java.io.*;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

class CityComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        CityData e1 = (CityData) o1;
        CityData e2 = (CityData) o2;
        if (e1.getPeopleNum() < e2.getPeopleNum()) {
            return 1;
        } else if (e1.getPeopleNum() > e2.getPeopleNum()) {
            return -1;
        } else {
            return Collator.getInstance(Locale.CHINESE).compare(e1.getCityName(), e2.getCityName());
        }
    }
}

class ProvinceComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        ProvinceData e1 = (ProvinceData) o1;
        ProvinceData e2 = (ProvinceData) o2;
        if (e1.getPeopleNum() < e2.getPeopleNum())
            return 1;
        else if (e1.getPeopleNum() > e2.getPeopleNum()) {
            return -1;
        } else {
            return Collator.getInstance(Locale.CHINESE).compare(e1.getProvinceName(), e2.getProvinceName());
        }
    }
}

public class DealFile {
    public String inputFile;
    public String outputFile;
    public String outputFile2;

    public DealFile() {

    }

    public DealFile(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public DealFile(String inputFile, String outputFile, String outputFile2) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.outputFile2 = outputFile2;
    }

    //  处理文件格式，输入参数依次为文件的编码格式、待处理的输入文件的路径、处理完成的输出文件的路径
    public void dealTxtFile(String designatedProvince) throws IOException {
        ArrayList provinceDataList = null;
        ArrayList singleDataList = null;
        try {
            File file = new File(this.inputFile);
            //  判断文件是否存在
            if (file.isFile() && file.exists()) {
                String encoding = getFileFormat(this.inputFile);
                //  考虑到编码格式，使用对应编码格式将文件输入流加载为输入读取流
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                //  创建文件读取缓冲区
                BufferedReader br = new BufferedReader(read);
                //  创建文件输出流
                FileWriter fw = new FileWriter(this.outputFile);
                //  创建文件写入缓冲区
                BufferedWriter bw = new BufferedWriter(fw);
                //  创建读取缓冲区的每行的字符串变量
                String lineTxt = null;
                //  创建每行数据的省份字符串变量
                String currentProvince = "0";

                int cityPeopleNum;
                int totalPeopleNum = 0;

                int countFlag = 0;

                provinceDataList = new ArrayList<>();
                //  缓冲区有数据就一直一行一行地读取
                while ((lineTxt = br.readLine()) != null) {
                    //  使用空格作为分割符，将每行的字符串分割为字符串数组
                    String[] lineArray = lineTxt.split("\\s+");
                    if (!lineArray[0].equals(designatedProvince) && !designatedProvince.equals("")) {
                        continue;
                    }
                    //  略去“待明确地区”
                    if (lineArray[1].equals("待明确地区")) {
                        continue;
                    }
                    //  将当前城市的省份进行比较，如果省份不一样了，那么将对当前省份进行替换
                    if (!currentProvince.equals(lineArray[0])) {
                        if (countFlag == 0) {
                            singleDataList = new ArrayList<>();
                            countFlag = 1;
                        } else {
                            bw.write("总计: " + totalPeopleNum);
//                            System.out.println(currentProvince);
                            ProvinceData provinceData = new ProvinceData(currentProvince, totalPeopleNum, singleDataList);
                            provinceDataList.add(provinceData);
                            singleDataList = new ArrayList<>();
                            totalPeopleNum = 0;
                            bw.newLine();
                        }
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
                    cityPeopleNum = Integer.parseInt(lineArray[2]);
                    totalPeopleNum += cityPeopleNum;
                    CityData cityData = new CityData(lineArray[1], cityPeopleNum);
                    singleDataList.add(cityData);
                    //  写入一个换行符
                    bw.newLine();
                    bw.flush();
                }
                bw.write("总计: " + totalPeopleNum);
//                System.out.println(currentProvince);
                ProvinceData provinceData = new ProvinceData(currentProvince, totalPeopleNum, singleDataList);
                provinceDataList.add(provinceData);

                bw.flush();
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
        sortCity(provinceDataList);
    }


    public void sortCity(ArrayList provinceDataList) throws IOException {
        //  创建文件输出流
        FileWriter fw = new FileWriter(this.outputFile2);
        //  创建文件写入缓冲区
        BufferedWriter bw = new BufferedWriter(fw);

        //必须是Comparator中的compare方法和Collections.sort方法配合使用才管用
        ProvinceComparator pct = new ProvinceComparator();
        CityComparator cct = new CityComparator();
        Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
        Collections.sort(provinceDataList, pct);

        ArrayList cityList = null;

        for (Object o : provinceDataList) {
            ProvinceData provinceData = (ProvinceData) o;
            cityList = provinceData.getCityDataList();

            cityList.sort(cct);
            bw.write(provinceData.getProvinceName() + "\t" + provinceData.getPeopleNum());
            //  写入一个换行符
            bw.newLine();

            for (Object value : cityList) {
                CityData cityData = (CityData) value;
                bw.write(cityData.getCityName() + "\t" + cityData.getPeopleNum());
                //  写入一个换行符
                bw.newLine();
            }
            //  写入一个换行符
            bw.newLine();
            bw.flush();
        }
        // 关闭io流
        bw.close();
    }


    //  对文件的编码格式进行判断
    public String getFileFormat(String filePath) throws Exception {
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        byte[] head = new byte[3];
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


    public static void main(String[] args) throws IOException {
        String inputFile = args[0];
        String outputFile = args[1];
        String outputFile2 = args[2];
        String designatedProvince = args[3];
//        String inputFile = "src/yq_in_03.txt";
//        String outputFile = "src/my_yq_out_03.txt";
//        String outputFile2 = "src/my_yq_out_04.txt";
//        String designatedProvince = "";
        DealFile rf = new DealFile(inputFile, outputFile, outputFile2);
        rf.dealTxtFile(designatedProvince);
    }
}