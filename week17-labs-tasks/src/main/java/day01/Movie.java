package day01;

import java.time.LocalDate;

public class Movie {
    private Long id;
    private String Title;
    private LocalDate localDate;

    public Movie(Long id, String title, LocalDate localDate) {
        this.id = id;
        Title = title;
        this.localDate = localDate;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return Title;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }
}
