package org.roehampton.presentation;

public interface ICompanySearchView {

    void displaySearchForm();
    String getSymbol();
    String[] getTwoSymbols();
    boolean validateSymbol(String symbol);

}
