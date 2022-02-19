package day01;

import java.util.Arrays;
import java.util.Optional;

public class MoviesRatingsService {

    private MoviesRepository moviesRepository;
    private RatingsRepository ratingsRepository;

    public MoviesRatingsService(MoviesRepository moviesRepository, RatingsRepository ratingsRepository) {
        this.moviesRepository = moviesRepository;
        this.ratingsRepository = ratingsRepository;
    }

    public void addRatings(String title, Integer... ratings) {
        Optional<Movie> actual = moviesRepository.findMovieByTitle(title);
        if (actual.isPresent()) {
            Long movieId = actual.get().getId();
            ratingsRepository.insertRating(movieId, Arrays.asList(ratings));
            Double avgRating = moviesRepository.calculateAvgRating(movieId);
            moviesRepository.updateAvgRating(movieId, avgRating);
        } else {
            throw new IllegalArgumentException("Cannot find movie: " + title);
        }
    }
}
