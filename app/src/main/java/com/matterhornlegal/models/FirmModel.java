package com.matterhornlegal.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karan.kalsi on 10/27/2017.
 */

public class FirmModel {

    private String firmTitle;
    private String firmLogo;
    private String firmLocation;
    private List<String> practiceAreas=new ArrayList<>();

    public String getFirmTitle() {
        return firmTitle;
    }

    public void setFirmTitle(String firmTitle) {
        this.firmTitle = firmTitle;
    }

    public String getFirmLogo() {
        return firmLogo;
    }

    public void setFirmLogo(String firmLogo) {
        this.firmLogo = firmLogo;
    }

    public String getFirmLocation() {
        return firmLocation;
    }

    public void setFirmLocation(String firmLocation) {
        this.firmLocation = firmLocation;
    }

    public List<String> getPracticeAreas() {
        return practiceAreas;
    }

    public void setPracticeAreas(List<String> practiceAreas) {
        this.practiceAreas = practiceAreas;
    }

}
