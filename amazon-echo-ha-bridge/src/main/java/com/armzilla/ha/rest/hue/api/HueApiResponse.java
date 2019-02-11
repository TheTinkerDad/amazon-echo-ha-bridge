package com.armzilla.ha.rest.hue.api;

import java.util.Map;

import com.armzilla.ha.rest.hue.api.DeviceResponse;

/**
 * Created by arm on 4/14/15.
 */
public class HueApiResponse {
    private Map<String, DeviceResponse> lights;

    public Map<String, DeviceResponse> getLights() {
        return lights;
    }

    public void setLights(Map<String, DeviceResponse> lights) {
        this.lights = lights;
    }
}
