package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoviesRepository {

    private DataSource dataSource;

    public MoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long saveMovie(String title, LocalDate releaseDate) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("insert into movies(title, release_date) values (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
            stmt.setDate(2, Date.valueOf(releaseDate));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                throw new IllegalStateException("Insert failed to movies!");
            }

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect'", sqle);
        }
    }

    public List<Movie> findAllMovies() {
        List<Movie> movies = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
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

    public Optional<Movie> findMovieByTitle(String title) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("select * from movies where title=?")) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Movie(rs.getLong("id"), rs.getString("title"), rs.getDate("release_date").toLocalDate()));
                }
                return Optional.empty();
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect to movies!", sqle);
        }
    }

    public Double calculateAvgRating(Long movieId) {
        try(Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("select round(avg(rating),2) as avg_rating from ratings where movie_id=?")){
            stmt.setLong(1, movieId);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()) {
                    Double avgRating = rs.getDouble("avg_rating");
                    return avgRating;
                }
                return 0.0;
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect to ratings!", sqle);
        }
    }

    public void updateAvgRating(Long movieId, Double avgRating) {
        try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("update movies set avg_rating=? where id=?")){
            stmt.setDouble(1, avgRating);
            stmt.setLong(2, movieId);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect to movies!", sqle);
        }
    }

}
