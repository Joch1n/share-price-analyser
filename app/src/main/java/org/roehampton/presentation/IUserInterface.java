package org.roehampton.presentation;

import java.time.LocalDate;
import java.util.List;

public interface IUserInterface
{
    void start();
    void selectCompanies(List<String> companies);
    void selectDateRange(LocalDate start, LocalDate end);

}
