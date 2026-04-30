package org.roehampton.dataaccess;

import org.roehampton.domain.PricePoint;
import org.roehampton.domain.PriceSeries;
import org.roehampton.domain.Watchlist;
import org.roehampton.domain.WatchlistItem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShareDatabase implements IShareDatabase {

    private final Path dbPath = Path.of("src", "main", "resources", "db", "shareinfo.db");
    private final String jdbcUrl = "jdbc:sqlite:" + dbPath.toString();
    private final List<String> watchlist = new ArrayList<>();

    public ShareDatabase() {

        initialiseDatabase();
    }

    // Create sqlite db file and table inside if they don't already exist
    private void initialiseDatabase() {
        try {

            Files.createDirectories(dbPath.getParent());

            try (Connection connection = DriverManager.getConnection(jdbcUrl);
                 Statement statement = connection.createStatement()) {

                statement.execute("""
                        CREATE TABLE IF NOT EXISTS share_prices (
                            symbol TEXT NOT NULL,
                            price_date TEXT NOT NULL,
                            price REAL NOT NULL,
                            PRIMARY KEY (symbol, price_date)
                        )
                        """);

                statement.execute("""
                        CREATE TABLE IF NOT EXISTS saved_graphs (
                            graph_id INTEGER PRIMARY KEY AUTOINCREMENT,
                            symbol TEXT NOT NULL,
                            start_date TEXT NOT NULL,
                            end_date TEXT NOT NULL,
                            created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            UNIQUE(symbol, start_date, end_date)
                        )
                        """);

                statement.execute("""
                        CREATE TABLE IF NOT EXISTS watchlist (
                            watchlist_id INTEGER PRIMARY KEY AUTOINCREMENT,
                            graph_id INTEGER NOT NULL UNIQUE,
                            FOREIGN KEY (graph_id) REFERENCES saved_graphs(graph_id)
                                ON DELETE CASCADE
                        )
                        """);
            }

        } catch (Exception e) {

            throw new RuntimeException("Database failed to initialise.", e);
        }
    }

    // Creates a connection to database when communication is required
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

        // Tries connecting and running sql statement in db
        try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Values that substitute '?' in sql string
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

    public PriceSeries getStoredData(String symbol, LocalDate from, LocalDate to) {
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
            throw new RuntimeException("Failed to retrieve stored data.", e);
        }
    }

    private int saveGraph(String symbol, LocalDate from, LocalDate to) {
        String insertSql = """
                INSERT OR IGNORE INTO saved_graphs (symbol, start_date, end_date)
                VALUES (?, ?, ?)
                """;

        String selectSql = """
                SELECT graph_id
                FROM saved_graphs
                WHERE symbol = ?
                  AND start_date = ?
                  AND end_date = ?
                """;

        try (Connection connection = connect()) {

            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                insertStatement.setString(1, symbol.trim().toUpperCase());
                insertStatement.setString(2, from.toString());
                insertStatement.setString(3, to.toString());
                insertStatement.executeUpdate();
            }

            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setString(1, symbol.trim().toUpperCase());
                selectStatement.setString(2, from.toString());
                selectStatement.setString(3, to.toString());

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("graph_id");
                    }
                }
            }

            throw new RuntimeException("Saved graph could not be found.");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save graph.", e);
        }
    }

    @Override
    public void saveWatchlistItem(WatchlistItem item) {

        int graphId = saveGraph(
                item.getSymbol(),
                item.getStartDate(),
                item.getEndDate()
        );

        String sql = """
                INSERT OR IGNORE INTO watchlist (graph_id)
                VALUES (?)
                """;

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, graphId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save watchlist item.", e);
        }
    }

    @Override
    public Watchlist getWatchlist() {

        String sql = """
                SELECT g.symbol, g.start_date, g.end_date
                FROM watchlist w
                JOIN saved_graphs g ON w.graph_id = g.graph_id
                ORDER BY w.watchlist_id ASC
                """;

        List<WatchlistItem> items = new ArrayList<>();

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                items.add(new WatchlistItem(
                        resultSet.getString("symbol"),
                        LocalDate.parse(resultSet.getString("start_date")),
                        LocalDate.parse(resultSet.getString("end_date"))
                ));
            }

            return new Watchlist(items);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve watchlist.", e);
        }
    }
}



