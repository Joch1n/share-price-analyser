package org.roehampton.presentation;

import org.roehampton.controller.IChartData;

import java.util.Map;

public class ChartDisplay implements IChartDisplay
{

    @Override
    public void displayChart(IChartData data)
    {

        System.out.println("Share Price Graph Display:");

        for (String company : data.getChartData().keySet())
        {

            System.out.println("Company: " + company);

            Map<String, Double> prices = data.getChartData().get(company);

            for (String date : prices.keySet())
            {
                System.out.println(date + " : " + prices.get(date));
            }

        }
    }
}
