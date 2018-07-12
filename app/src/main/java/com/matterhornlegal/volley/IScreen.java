package com.matterhornlegal.volley;


public interface IScreen {
    /**
     * Subclass should over-ride this method to update the UI with response. <br/>
     * Subclass should note that it might being called from non-UI thread.
     *
     * @param serviceResponse
     */
    void updateUi(final boolean status, final int actionID, final Object serviceResponse, int statusCode);

    void getData(final int actionID);
}
