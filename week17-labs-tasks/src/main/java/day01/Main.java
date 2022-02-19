package day01;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("mariadb");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot reach the database!", sqle);
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        //actorsRepository.saveActor("Jack Doe");

        //System.out.println(actorsRepository.findActorsWithPrefix("Ja"));

        MoviesRepository moviesRepository = new MoviesRepository(dataSource);
        //moviesRepository.saveMovie("Titanic", LocalDate.of(1997, 12,11));

        //        moviesRepository.findAllMovies().stream()
        //                .map(Movie::getTitle)
        //               .peek(System.out::println).collect(Collectors.toList());

        ActorsMoviesRepository actorsMoviesRepository = new ActorsMoviesRepository(dataSource);
        RatingsRepository ratingsRepository = new RatingsRepository(dataSource);

        ActorsMoviesService actorsMoviesService = new ActorsMoviesService(actorsRepository, moviesRepository, actorsMoviesRepository);
        MoviesRatingsService moviesRatingsService = new MoviesRatingsService(moviesRepository, ratingsRepository);

        actorsMoviesService.insertMovieWithActors("Titanic", LocalDate.of(1997, 11,13), List.of("Leonardo Dicaprio", "Kate Winslet"));
        actorsMoviesService.insertMovieWithActors("Great Gatsby", LocalDate.of(2012, 12,11), List.of("Leonardo Dicaprio", "Toby"));
        actorsMoviesService.insertMovieWithActors("The Lord of the Rings", LocalDate.of(2000, 10,11), List.of("Orlando Bloom", "Liv Tyler"));

        moviesRatingsService.addRatings("Titanic", 5,3,2);
        moviesRatingsService.addRatings("Great Gatsby", 1, 3, 2, 5);
        moviesRatingsService.addRatings("Great Gatsby", 1, 2, 2, 2);
        moviesRatingsService.addRatings("The Lord of the Rings", 4, 4, 5, 5);

    }
}
