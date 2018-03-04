/* Copyright 2017, 2018 Unity{Cloud}Ware - UCW Industries Ltd. All rights reserved.
 */

package com.unitycloudware.portal.tutorial.report.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Humidity Data
 *
 * @author Tomas Hrdlicka <tomas@hrdlicka.co.uk>
 * @see <a href="http://unitycloudware.com">Unity{Cloud}Ware</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HumidityData {
    private int value;
    private long timestamp;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "HumidityData{" +
                "value=" + value +
                ", timestamp=" + timestamp +
                '}';
    }

    public static HumidityData create(final int value, final long timestamp) {
        HumidityData humidityData = new HumidityData();
        humidityData.setValue(value);
        humidityData.setTimestamp(timestamp);
        return humidityData;
    }
}