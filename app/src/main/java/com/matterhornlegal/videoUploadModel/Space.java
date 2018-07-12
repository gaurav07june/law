package com.matterhornlegal.videoUploadModel;

/**
 * Created by gaurav.singh on 6/1/2018.
 */

public class Space {
    private long free;

    private long max;

    private String showing;

    private long used;

    public long getFree() {
        return free;
    }

    public void setFree(long free) {
        this.free = free;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public String getShowing() {
        return showing;
    }

    public void setShowing(String showing) {
        this.showing = showing;
    }

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }
}
