package org.roehampton.dataaccess;

import org.roehampton.domain.PricePoint;
import org.roehampton.domain.PriceSeries;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShareDatabase implements IShareDatabase {

    private final Path dbPath = Path.of("src", "main", "resources", "db", "shareinfo.db");
    private final String jdbcUrl = "jdbc:sqlite:" + dbPath.toString();

    public ShareDatabase() {

        initialiseDatabase();
    }

    private void initialiseDatabase() {
        try {

            Files.createDirectories(dbPath.getParent());

            try (Connection connection = DriverManager.getConnection(jdbcUrl);
                 Statement statement = connection.createStatement()) {

                String createTableSql = """
                        CREATE TABLE IF NOT EXISTS share_prices (
                            symbol TEXT NOT NULL,
                            price_date TEXT NOT NULL,
                            price REAL NOT NULL,
                            PRIMARY KEY (symbol, price_date)
                        )
                        """;

                statement.execute(createTableSql);
            }

        } catch (Exception e) {

            throw new RuntimeException("Database failed to initialise.", e);
        }
    }

    private Connection connect() throws SQLException {

        return DriverManager.getConnection(jdbcUrl);
    }


    @Override
    // Checks if data for a graph is found, not found, or partially found in database, and sets the status
    public DataFound dbCheck(String symbol, LocalDate from, LocalDate to) {

        String sql = """
                SELECT COUNT(*) AS match_count
                FROM share_prices
                WHERE symbol = ?
                  AND price_date BETWEEN ? AND ?
                """;

        try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Substitute '?' in sql string
            preparedStatement.setString(1, symbol);
            preparedStatement.setString(2, from.toString());
            preparedStatement.setString(3, to.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next() && resultSet.getInt("match_count") > 0) {

                    return DataFound.FOUND;
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException("Failed to check database for stored data.", e);
        }

        return DataFound.NOT_FOUND;
    }

    public void storeData(PriceSeries priceSeries) {

        String sql = """
                INSERT OR REPLACE INTO share_prices (symbol, price_date, price)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);

            String symbol = priceSeries.getSymbol();

            for (PricePoint point : priceSeries.getPoints()) {
                preparedStatement.setString(1, symbol);
                preparedStatement.setString(2, point.getDate().toString());
                preparedStatement.setDouble(3, point.getClosePrice());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to store price series data", e);
        }
    }

    public PriceSeries getStoredData(String symbol, LocalDate to, LocalDate from) {
        String sql = """
                SELECT price_date, price
                FROM share_prices
                WHERE symbol = ?
                  AND price_date BETWEEN ? AND ?
                ORDER BY price_date
                """;

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, symbol);
            preparedStatement.setString(2, from.toString());
            preparedStatement.setString(3, to.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<PricePoint> points = new ArrayList<>();

                while (resultSet.next()) {
                    LocalDate date = LocalDate.parse(resultSet.getString("price_date"));
                    double price = resultSet.getDouble("price");
                    points.add(new PricePoint(date, price));
                }

                return new PriceSeries(symbol, points);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve stored data", e);
        }
    }

    private final List<String> watchlist = new ArrayList<>();

    @Override
    public void saveWatchlistItem(String symbol) {
        String upperSymbol = symbol.trim().toUpperCase();
        if (!watchlist.contains(upperSymbol)) {
            watchlist.add(upperSymbol);
        }
    }

    @Override
    public List<String> getWatchlist() {
        return new ArrayList<>(watchlist);
    }
}



