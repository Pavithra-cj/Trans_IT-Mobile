package lk.nibm.furious5.scorpio.transit.Model;

public class PackageItem {

    private String iPkgName;
    private String iCreditPoints;
    private String iPrice;

    public PackageItem(String iPkgName, String iCreditPoints, String iPrice) {
        this.iPkgName = iPkgName;
        this.iCreditPoints = iCreditPoints;
        this.iPrice = iPrice;
    }

    public String getiPkgName() {
        return iPkgName;
    }

    public void setiPkgName(String iPkgName) {
        this.iPkgName = iPkgName;
    }

    public String getiCreditPoints() {
        return iCreditPoints;
    }

    public void setiCreditPoints(String iCreditPoints) {
        this.iCreditPoints = iCreditPoints;
    }

    public String getiPrice() {
        return iPrice;
    }

    public void setiPrice(String iPrice) {
        this.iPrice = iPrice;
    }
}
