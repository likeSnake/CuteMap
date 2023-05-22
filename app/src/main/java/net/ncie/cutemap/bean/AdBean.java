package net.ncie.cutemap.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class AdBean implements Parcelable {

    /**
     * aab : abed7ccd48a8dcf5f117ef5267efdba39c5214e0eeb8aa84b53454145878bff706fa1e101f76152fa854f4a466613edc4e8635d84f47e526122069f93e617c07d5683543668baba0eec068627b0c2b93b7e6e1fd090ff1d48ad6780c9f9f3b2b91bfb97933ae8dca2ebb37ce7d6e5908cda643a7bc573cef3762963f6783595461585bff5ce3c7bc68ebc7506a2c2a1cb7430def20ead3d96d97ff9097dce9ccc54a80987cd5da79b46f308c632c43619c6fed675797891b1c0ee93dda88425f3ec75d9257db0c6c4c210a5eb2e2a74f18aca03c1a58d292e1990e62e8ff5ac7ba3e432c5c6ecb5e583c451a29d8b9e12a7e7db70a06909cf153826ea5f3aafb6b8c70acf4c4324d8b9aa6b964589ca9a1cce2b9eafb1f88470b1c98da0336830410af8852dcfd94129e3ef2c8781807d7c30a57d946b70c26c4c8f4b8fc5f8f5088d3670f6404d5ff0a674976479067d9c7bc5b7b3f970dd0f8db871443e90c047a311ee4bd92d7d4a0cbe21788715884e07cbc2a395bc6fefac7ba4bd8865ff0bcb1d3f7dc9dd1b342ca19c82dceca832b59467472046645f887de13c5f9ae
     * my : https://accounts.google.com/
     * my1 : https://myaccount.google.com/
     * pli : https://myaccount.google.com/?pli=1
     * pli1 : pli=1
     * yup : [""]
     * yupj :
     * adIcon :
     */

    private String aab;
    private String my;
    private String my1;
    private String pli;
    private String pli1;
    private String yupj;
    private String adIcon;
    private List<String> yup;


    public AdBean(String aab, String my, String my1, String pli, String pli1, String yupj, String adIcon, List<String> yup) {
        this.aab = aab;
        this.my = my;
        this.my1 = my1;
        this.pli = pli;
        this.pli1 = pli1;
        this.yupj = yupj;
        this.adIcon = adIcon;
        this.yup = yup;
    }

    protected AdBean(Parcel in) {
        aab = in.readString();
        my = in.readString();
        my1 = in.readString();
        pli = in.readString();
        pli1 = in.readString();
        yupj = in.readString();
        adIcon = in.readString();
        yup = in.createStringArrayList();
    }

    public static final Creator<AdBean> CREATOR = new Creator<AdBean>() {
        @Override
        public AdBean createFromParcel(Parcel in) {
            return new AdBean(in);
        }

        @Override
        public AdBean[] newArray(int size) {
            return new AdBean[size];
        }
    };

    public String getAab() {
        return aab;
    }

    public void setAab(String aab) {
        this.aab = aab;
    }

    public String getMy() {
        return my;
    }

    public void setMy(String my) {
        this.my = my;
    }

    public String getMy1() {
        return my1;
    }

    public void setMy1(String my1) {
        this.my1 = my1;
    }

    public String getPli() {
        return pli;
    }

    public void setPli(String pli) {
        this.pli = pli;
    }

    public String getPli1() {
        return pli1;
    }

    public void setPli1(String pli1) {
        this.pli1 = pli1;
    }

    public String getYupj() {
        return yupj;
    }

    public void setYupj(String yupj) {
        this.yupj = yupj;
    }

    public String getAdIcon() {
        return adIcon;
    }

    public void setAdIcon(String adIcon) {
        this.adIcon = adIcon;
    }

    public List<String> getYup() {
        return yup;
    }

    public void setYup(List<String> yup) {
        this.yup = yup;
    }


    @Override
    public String toString() {
        return "AdBean{" +
                "aab='" + aab + '\'' +
                ", my='" + my + '\'' +
                ", my1='" + my1 + '\'' +
                ", pli='" + pli + '\'' +
                ", pli1='" + pli1 + '\'' +
                ", yupj='" + yupj + '\'' +
                ", adIcon='" + adIcon + '\'' +
                ", yup=" + yup +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(aab);
        dest.writeString(my);
        dest.writeString(my1);
        dest.writeString(pli);
        dest.writeString(pli1);
        dest.writeString(yupj);
        dest.writeString(adIcon);
        dest.writeList(yup);
    }
}
