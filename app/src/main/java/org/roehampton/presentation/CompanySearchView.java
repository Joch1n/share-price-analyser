package org.roehampton.presentation;

import org.roehampton.controller.IController;
import java.util.Scanner;

public class CompanySearchView implements ICompanySearchView {

    private final IController controller;
    private final Scanner scanner;

    public CompanySearchView(IController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void displaySearchForm() {
        System.out.println("Enter stock symbol:");
    }

    @Override
    public String getSymbol() {
        System.out.print("Symbol: ");
        String symbol = scanner.nextLine().trim().toUpperCase();

        if (!validateSymbol(symbol)) {
            System.out.println("Invalid symbol format");
            return null;
        }

        return symbol;
    }

    @Override
    public String[] getTwoSymbols() {
        System.out.println("Enter TWO stock symbols for comparison:");

        System.out.print("First Symbol: ");
        String symbol1 = scanner.nextLine().trim().toUpperCase();

        if (!validateSymbol(symbol1)) {
            System.out.println("Invalid first symbol");
            return null;
        }

        System.out.print("Second Symbol: ");
        String symbol2 = scanner.nextLine().trim().toUpperCase();

        if (!validateSymbol(symbol2)) {
            System.out.println("Invalid second symbol");
            return null;
        }

        if (symbol1.equals(symbol2)) {
            System.out.println("Cannot compare a stock with itself");
            return null;
        }

        return new String[]{symbol1, symbol2};
    }

    @Override
    public boolean validateSymbol(String symbol) {
        if (symbol == null || symbol.isEmpty()) {
            return false;
        }

        // Symbol should be 1-5 uppercase letters
        if (!symbol.matches("^[A-Z]{1,5}$")) {
            return false;
        }

        return true;
    }

}
