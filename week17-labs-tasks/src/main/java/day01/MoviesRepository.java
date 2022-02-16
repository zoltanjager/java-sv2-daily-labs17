package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MoviesRepository {

    private DataSource dataSource;

    public MoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveMovie(String title, LocalDate releaseDate) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement("insert into movies(title, release_date) values (?,?)")) {
            statement.setString(1, title);
            statement.setDate(2, Date.valueOf(releaseDate));
            statement.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect'", sqle);
        }
    }

    public List<Movie> findAllMovies() {
        List<Movie> movies = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM movies")
        ) {
            while (rs.next()) {
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                LocalDate localDate = rs.getDate("release_date").toLocalDate();
                movies.add(new Movie(id, title, localDate));
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query!", sqle);
        }

        return movies;
    }

}
