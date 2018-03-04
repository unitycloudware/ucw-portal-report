/* Copyright 2017, 2018 Unity{Cloud}Ware - UCW Industries Ltd. All rights reserved.
 */

package com.unitycloudware.portal.tutorial.report.controller.report;

import java.util.ArrayList;
import java.util.Date;
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

import org.nsys.util.RandomRange;
import org.nsys.util.TimeUtils;
import org.nsys.portal.chart.ChartConfig;
import org.nsys.portal.chart.ChartData;
import org.nsys.portal.chart.ChartPoint;
import org.nsys.portal.chart.ChartSeries;
import org.nsys.portal.controller.AbstractReportController;

import com.unitycloudware.portal.tutorial.report.model.HumidityData;

/**
 * Humidity Report
 *
 * @author Tomas Hrdlicka <tomas@hrdlicka.co.uk>
 * @see <a href="http://unitycloudware.com">Unity{Cloud}Ware</a>
 */
@Controller
@RequestMapping("/ucw-report/humidity")
public class HumidityReport extends AbstractReportController {
    private List<HumidityData> cachedData;

    public static final String REPORT_NAME = "UCW Report - Humidity";
    public static final String REPORT_TEMPLATE = "/templates/report/humidity-report.vm";
    public static final String CHART_TITLE = "UCW Report - Humidity Chart (%)";
    public static final int CHART_WIDTH = 640;
    public static final int CHART_HEIGHT = 480;
    public static final String CHART_DATA_URL = "/ucw-report/humidity/chart-data";

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
        setChartAxisXTimeUnit(ChartConfig.TimeUnit.HOUR_2);
    }

    @RequestMapping(method = RequestMethod.GET)
    @Override
    public ModelAndView showReport(
            final HttpServletRequest request,
            final HttpServletResponse response) {

        if (cachedData == null) {
            cachedData = getData();
        }

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("data", cachedData);

        return render(context, request, response);
    }

    @RequestMapping(value = "/chart-data", method = RequestMethod.GET)
    @ResponseBody
    public List<ChartSeries> getChartData(
            final HttpServletRequest request,
            final HttpServletResponse response) {

        ChartData data = new ChartData();

        if (cachedData == null) {
            cachedData = getData();
        }

        data.addSeries(
                getHumidityData(
                        "humidityData",
                        "DHT22 Sensor # 1",
                        ChartConfig.ChartColor.BLUE,
                        cachedData));

        return data.getSeries();
    }

    protected ChartSeries getHumidityData(
            final String key,
            final String name,
            final String color,
            final List<HumidityData> data) {

        ChartSeries humidityData = new ChartSeries();
        humidityData.setKey(key);
        humidityData.setName(name);
        humidityData.setColor(color);

        for (HumidityData hd : data) {
            humidityData.addPoint(ChartPoint.<Long, Integer>create(hd.getTimestamp() / 1000, hd.getValue()));
        }

        return humidityData;
    }

    protected List<HumidityData> getData() {
        List<HumidityData> humidityData = new ArrayList<HumidityData>();

        Date date = TimeUtils.getStartOfDay(TimeUtils.getNow());

        for (int i = 0; i < 24; i++) {
            int value = RandomRange.getRandomInt(1, 100);
            long timestamp = date.getTime() + TimeUtils.getTimezoneUTCAndDSTOffset(date);
            humidityData.add(HumidityData.create(value, timestamp));
            date = TimeUtils.addHours(date, 1);
        }

        return humidityData;
    }
}