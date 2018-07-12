package com.matterhornlegal.models;

/**
 * Created by karan.kalsi on 9/28/2017.
 */

public class DemoPageScrollEvent {

    public float getPositionOffset() {
        return positionOffset;
    }

    public void setPositionOffset(float positionOffset) {
        this.positionOffset = positionOffset;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private float positionOffset;
    private int position;


}
