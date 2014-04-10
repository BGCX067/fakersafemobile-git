package com.faker.mobilesafe.bean;

/**
 * User:LichFaker
 * Date:14-4-9
 * Time:下午7:26
 * Email:lichfaker@gmail.com
 */
public class TrafficBean {
    private long mobileTx;
    private long mobileRx;
    private long wifiTx;
    private long wifiRx;
    private long offset;


    public TrafficBean() {
    }

    public TrafficBean(long mobileTx, long mobileRx, long wifiTx, long wifiRx,long offset) {
        this.mobileTx = mobileTx;
        this.mobileRx = mobileRx;
        this.wifiTx = wifiTx;
        this.wifiRx = wifiRx;
        this.offset = offset;
    }

    public long getMobileTx() {
        return mobileTx;
    }

    public void setMobileTx(long mobileTx) {
        this.mobileTx = mobileTx;
    }

    public long getMobileRx() {
        return mobileRx;
    }

    public void setMobileRx(long mobileRx) {
        this.mobileRx = mobileRx;
    }

    public long getWifiTx() {
        return wifiTx;
    }

    public void setWifiTx(long wifiTx) {
        this.wifiTx = wifiTx;
    }

    public long getWifiRx() {
        return wifiRx;
    }

    public void setWifiRx(long wifiRx) {
        this.wifiRx = wifiRx;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "TrafficBean{" +
                "mobileTx=" + mobileTx +
                ", mobileRx=" + mobileRx +
                ", wifiTx=" + wifiTx +
                ", wifiRx=" + wifiRx +
                '}';
    }
}
