package day01;

import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("mariadb");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot reach the database!");
        }

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
       // actorsRepository.saveActor("Jack Doe");

        System.out.println(actorsRepository.findActorsWithPrefix("Ja"));

    }
}
