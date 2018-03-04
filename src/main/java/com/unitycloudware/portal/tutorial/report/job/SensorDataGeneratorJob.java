/* Copyright 2017, 2018 Unity{Cloud}Ware - UCW Industries Ltd. All rights reserved.
 */

package com.unitycloudware.portal.tutorial.report.job;

import java.util.Map;

import org.nsys.util.JsonUtils;
import org.nsys.util.RandomRange;
import org.nsys.util.TimeUtils;
import org.nsys.daemon.scheduler.job.AbstractJob;

import com.unitycloudware.core.model.DataStream;
import com.unitycloudware.core.model.Device;
import com.unitycloudware.core.service.DataManager;

import com.unitycloudware.portal.tutorial.report.model.DataItem;
import com.unitycloudware.portal.tutorial.report.util.TestDataUtils;

/**
 * Sensor Data Generator Job
 *
 * @author Tomas Hrdlicka <tomas@hrdlicka.co.uk>
 * @see <a href="http://unitycloudware.com">Unity{Cloud}Ware</a>
 */
public class SensorDataGeneratorJob extends AbstractJob {
    private DataManager dataManager;
    private TestDataUtils testDataUtils;

    public DataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public TestDataUtils getTestDataUtils() {
        return testDataUtils;
    }

    public void setTestDataUtils(TestDataUtils testDataUtils) {
        this.testDataUtils = testDataUtils;
    }

    public void execute(final Map<String, Object> jobDataMap) {
        getLog().info("Executing sensor data generator...");

        if (!jobDataMap.containsKey(ComponentName.DATA_MANAGER)) {
            getLog().error("Unable to find DataManager component in jobDataMap!");
            return;
        }

        if (!jobDataMap.containsKey(ComponentName.TEST_DATA_UTILS)) {
            getLog().error("Unable to find TestDataUtils component in jobDataMap!");
            return;
        }

        setDataManager((DataManager) jobDataMap.get(ComponentName.DATA_MANAGER));
        setTestDataUtils((TestDataUtils) jobDataMap.get(ComponentName.TEST_DATA_UTILS));

        generateData();
    }

    public void generateData() {
        DataStream dataStream = getTestDataUtils().getDataStream();

        if (dataStream == null) {
            return;
        }

        for (Device device : getTestDataUtils().getDevices()) {
            DataItem data = DataItem.create(
                    RandomRange.getRandomDouble(0, 100),
                    RandomRange.getRandomInt(0, 100),
                    TimeUtils.getNow().getTime());

            String payload = JsonUtils.toJson(data);

            getLog().debugFormat("Storing generated sensor data for device %s... Payload: %s", device.getName(), payload);
            getDataManager().storeStream(dataStream, device, payload);
        }
    }
}