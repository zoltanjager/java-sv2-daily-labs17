package day01;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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
        flyway.migrate();

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        //actorsRepository.saveActor("Jack Doe");

        //System.out.println(actorsRepository.findActorsWithPrefix("Ja"));

        MoviesRepository moviesRepository = new MoviesRepository(dataSource);
        moviesRepository.saveMovie("Titanic", LocalDate.of(1997, 12,11));

                moviesRepository.findAllMovies().stream()
                        .map(Movie::getTitle)
                        .peek(System.out::println).collect(Collectors.toList());

    }
}
