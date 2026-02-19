package org.roehampton.presentation;

import controller.IController;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class WebUI implements IUserInterface
{

    private IController controller;

    public WebUI(IController controller)
    {
        this.controller = controller;
    }

    @Override
    public void start()
    {
        System.out.println("Share Price Comparison Webpage Started");
    }

    @Override
    public void selectCompanies(List<String> companies)
    {

        if (companies.size() == 0 || companies.size() > 2)
        {
            throw new IllegalArgumentException("Please select the companies you want to compare.");
        }

        controller.setCompanies(companies);
    }

    @Override
    public void selectDateRange(LocalDate start, LocalDate end)
    {

        LocalDate today = LocalDate.now();
        LocalDate twoYearsAgo = today.minusYears(2);

        if (start.isBefore(twoYearsAgo) || end.isAfter(today))
        {
            throw new IllegalArgumentException("Date range must be within last 2 years.");
        }

        if (start.isAfter(end))
        {
            throw new IllegalArgumentException("Start date must be before end date.");
        }

        controller.setDateRange(start, end);
    }
}