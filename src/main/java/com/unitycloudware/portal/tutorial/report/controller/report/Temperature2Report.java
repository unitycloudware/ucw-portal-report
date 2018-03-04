/* Copyright 2017, 2018 Unity{Cloud}Ware - UCW Industries Ltd. All rights reserved.
 */

package com.unitycloudware.portal.tutorial.report.controller.report;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.nsys.util.JsonUtils;
import org.nsys.system.ComponentProvider;
import org.nsys.portal.chart.ChartConfig;
import org.nsys.portal.chart.ChartData;
import org.nsys.portal.chart.ChartPoint;
import org.nsys.portal.chart.ChartSeries;
import org.nsys.portal.controller.AbstractReportController;

import com.unitycloudware.core.model.DataMessage;
import com.unitycloudware.core.model.DataStream;
import com.unitycloudware.core.model.DataStreamItem;
import com.unitycloudware.core.model.DataStreamType;
import com.unitycloudware.core.model.Device;
import com.unitycloudware.core.service.DataManager;
import com.unitycloudware.core.service.DeviceManager;

import com.unitycloudware.portal.tutorial.report.model.DataItem;
import com.unitycloudware.portal.tutorial.report.util.TestDataUtils;

/**
 * Temperature Report
 *
 * @author Tomas Hrdlicka <tomas@hrdlicka.co.uk>
 * @see <a href="http://unitycloudware.com">Unity{Cloud}Ware</a>
 */
@Controller
@RequestMapping("/ucw-report/temperature2")
public class Temperature2Report extends AbstractReportController {
    private DeviceManager deviceManager;
    private DataManager dataManager;
    private TestDataUtils testDataUtils;

    public static final String REPORT_NAME = "UCW Report - Temperature 2";
    public static final String REPORT_TEMPLATE = "/templates/report/temperature-report.vm";
    public static final String CHART_TITLE = "UCW Report - Temperature Chart (&deg;C)";
    public static final int CHART_WIDTH = 640;
    public static final int CHART_HEIGHT = 480;
    public static final String CHART_SECOND_10 = "10 second";
    public static final String CHART_DATA_URL = "/ucw-report/temperature2/chart-data";

    public DeviceManager getDeviceManager() {
        if (deviceManager == null) {
            deviceManager = ComponentProvider.getInstance().getComponent(DeviceManager.class);
        }

        return deviceManager;
    }

    public DataManager getDataManager() {
        if (dataManager == null) {
            dataManager = ComponentProvider.getInstance().getComponent(DataManager.class);
        }

        return dataManager;
    }

    public TestDataUtils getTestDataUtils() {
        if (testDataUtils == null) {
            testDataUtils = ComponentProvider.getInstance().getComponent(TestDataUtils.class);
        }

        return testDataUtils;
    }

    @Override
    protected void configure() {
        setReportName(REPORT_NAME);
        setReportImageUrl("${portalResourcesUrl}/resources/images/ucw_logo.png");
        setReportTemplate(REPORT_TEMPLATE);
        setChartTitle(CHART_TITLE);
        setChartType(ChartConfig.ChartType.LINE);
        setChartWidth(CHART_WIDTH);
        setChartHeight(CHART_HEIGHT);
        setChartDataUrl(CHART_DATA_URL);
        setChartLegend(true);
        setChartStack(false);
        setChartHoverDetail(true);
        setChartAxisXMode(ChartConfig.AxisXMode.TIME_SERIES);
        setChartAxisXTimeUnit(CHART_SECOND_10);
    }

    @RequestMapping(method = RequestMethod.GET)
    @Override
    public ModelAndView showReport(
            final HttpServletRequest request,
            final HttpServletResponse response) {

        Map<String, Object> context = new HashMap<String, Object>();
        return render(context, request, response);
    }

    @RequestMapping(value = "/chart-data", method = RequestMethod.GET)
    @ResponseBody
    public List<ChartSeries> getChartData(
            final HttpServletRequest request,
            final HttpServletResponse response) {

        ChartData data = new ChartData();

        DataStream dataStream = getTestDataUtils().getDataStream();

        if (dataStream == null) {
            return data.getSeries();
        }

        List<String> colors = Arrays.asList(
                ChartConfig.ChartColor.BLUE, ChartConfig.ChartColor.GREEN, ChartConfig.ChartColor.VIOLET);

        int counter = 0;

        for (Device device : getTestDataUtils().getDevices()) {
            data.addSeries(getTemperatureData(dataStream, device, colors.get(counter)));
            counter++;
        }

        return data.getSeries();
    }

    protected ChartSeries getTemperatureData(
            final DataStream dataStream,
            final Device device,
            final String color) {

        ChartSeries temperatureData = new ChartSeries();
        temperatureData.setKey(String.format("%s_id", device.getName()));
        temperatureData.setName(device.getName());
        temperatureData.setColor(color);

        // Load 18 last records
        List<DataStreamItem> items = getDataManager().loadStream(dataStream, device, 0, 18);

        if (items == null || items.isEmpty()) {
            return temperatureData;
        }

        for (DataStreamItem item : items) {
            // Process only data for data stream with type of DATA_MESSAGE
            if (item.getType() != DataStreamType.DATA_MESSAGE) {
                continue;
            }

            DataMessage dataMessage = (DataMessage) item.getData();

            // Transform JSON payload to DataItem object
            DataItem di = JsonUtils.fromJson(dataMessage.getData(), DataItem.class);

            temperatureData.addPoint(ChartPoint.<Long, Double>create(di.getTimestamp() / 1000, di.getTemperature()));
        }

        return temperatureData;
    }
}