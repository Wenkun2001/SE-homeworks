import java.util.ArrayList;

/**
 * @ClassName ProvinceData
 * @Description TODO
 * @Author Wenkun Lei
 * @Date 2022/3/19 15:40
 * @Version 1.0
 **/
public class ProvinceData {
    private String provinceName;
    private int peopleNum;
    private ArrayList cityDataList;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String empNo) {
        this.provinceName = provinceName;
    }

    public ArrayList getCityDataList() {
        return cityDataList;
    }

    public void setCityDataList(ArrayList cityDataList) {
        this.cityDataList = cityDataList;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public ProvinceData() {

    }

    public ProvinceData(String provinceName, int peopleNum, ArrayList cityDataList) {
        this.provinceName = provinceName;
        this.peopleNum = peopleNum;
        this.cityDataList = cityDataList;
    }
}
