package net.ncie.cutemap.bean;

public class ChannelStatusBean {

    private String  id = "10018";
    private String co;
    private String cou;
    private String ag;
    private String ul;

    public ChannelStatusBean(String id, String co, String cou, String ag, String ul) {
        this.id = id;
        this.co = co;
        this.cou = cou;
        this.ag = ag;
        this.ul = ul;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getCou() {
        return cou;
    }

    public void setCou(String cou) {
        this.cou = cou;
    }

    public String getAg() {
        return ag;
    }

    public void setAg(String ag) {
        this.ag = ag;
    }

    public String getUl() {
        return ul;
    }

    public void setUl(String ul) {
        this.ul = ul;
    }

    @Override
    public String toString() {
        return "ChannelStatusBean{" +
                "id='" + id + '\'' +
                ", co='" + co + '\'' +
                ", cou='" + cou + '\'' +
                ", ag='" + ag + '\'' +
                ", ul='" + ul + '\'' +
                '}';
    }
}
