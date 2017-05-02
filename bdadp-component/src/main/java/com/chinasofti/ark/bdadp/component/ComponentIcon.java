package com.chinasofti.ark.bdadp.component;

/**
 * Created by White on 2016/10/31.
 */
public class ComponentIcon {

    public static final String ICON_XL = "xl";
    public static final String ICON_XS = "xs";

    private String xl;
    private String xs;

    public ComponentIcon() {
    }

    public ComponentIcon(String xl, String xs) {
        this.xl = xl;
        this.xs = xs;
    }

    public String getXs() {
        return xs;
    }

    public void setXs(String xs) {
        this.xs = xs;
    }

    public String getXl() {
        return xl;
    }

    public void setXl(String xl) {
        this.xl = xl;
    }

}
