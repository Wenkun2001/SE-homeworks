/**
 * @ClassName CityData
 * @Description TODO
 * @Author Wenkun Lei
 * @Date 2022/3/19 15:24
 * @Version 1.0
 **/
public class CityData {
    private String cityName;
    private int peopleNum;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String empNo) {
        this.cityName = cityName;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public CityData() {

    }

    public CityData(String cityName, int peopleNum) {
        this.cityName = cityName;
        this.peopleNum = peopleNum;
    }
}
