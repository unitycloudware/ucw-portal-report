/* Copyright 2017, 2018 Unity{Cloud}Ware - UCW Industries Ltd. All rights reserved.
 */

package com.unitycloudware.portal.tutorial.report.controller.report;

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

/**
 * Temperature Report
 *
 * @author Tomas Hrdlicka <tomas@hrdlicka.co.uk>
 * @see <a href="http://unitycloudware.com">Unity{Cloud}Ware</a>
 */
@Controller
@RequestMapping("/ucw-report/temperature")
public class TemperatureReport extends AbstractReportController {

    public static final String REPORT_NAME = "UCW Report - Temperature";
    public static final String REPORT_TEMPLATE = "/templates/report/temperature-report.vm";
    public static final String CHART_TITLE = "UCW Report - Temperature Chart (&deg;C)";
    public static final int CHART_WIDTH = 640;
    public static final int CHART_HEIGHT = 480;
    public static final String CHART_DATA_URL = "/ucw-report/temperature/chart-data";

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

        Map<String, Object> context = new HashMap<String, Object>();
        return render(context, request, response);
    }

    @RequestMapping(value = "/chart-data", method = RequestMethod.GET)
    @ResponseBody
    public List<ChartSeries> getChartData(
            final HttpServletRequest request,
            final HttpServletResponse response) {

        ChartData data = new ChartData();

        data.addSeries(
                getTemperatureData(
                        "temperatureData",
                        "DHT22 Sensor # 1",
                        ChartConfig.ChartColor.BLUE));

        return data.getSeries();
    }

    protected ChartSeries getTemperatureData(
            final String key,
            final String name,
            final String color) {

        ChartSeries temperatureData = new ChartSeries();
        temperatureData.setKey(key);
        temperatureData.setName(name);
        temperatureData.setColor(color);

        Date date = TimeUtils.getStartOfDay(TimeUtils.getNow());

        for (int i = 0; i < 24; i++) {
            double value = RandomRange.getRandomDouble(1, 100);
            long timestamp = date.getTime() + TimeUtils.getTimezoneUTCAndDSTOffset(date);
            temperatureData.addPoint(ChartPoint.<Long, Double>create(timestamp / 1000, value));
            date = TimeUtils.addHours(date, 1);
        }

        return temperatureData;
    }
}