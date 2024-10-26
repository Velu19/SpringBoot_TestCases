package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewRepositoryTests {

    private ReviewRepository reviewRepository;

    @Autowired
    public  ReviewRepositoryTests(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    @Test
    public  void ReviewRepository_save_ReturnSaved(){
        Review review = Review.builder()
                .title("title")
                .content("Is good")
                .stars(5)
                .build();

        Review savedReview = reviewRepository.save(review);

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);

    }

    @Test
    public  void ReviewRepository_saveAll_ReturnSaved(){
        Review review = Review.builder()
                .title("title")
                .content("Is good")
                .stars(5)
                .build();

        Review review2 = Review.builder()
                .title("title2")
                .content("Is good")
                .stars(5)
                .build();

        reviewRepository.save(review);
        reviewRepository.save(review2);

        List<Review> savedReviewList = reviewRepository.findAll();


        Assertions.assertThat(savedReviewList).isNotNull();
        Assertions.assertThat(savedReviewList.size()).isEqualTo(2);

    }

    @Test
    public  void ReviewRepository_findById_ReturnSaved(){
        Review review = Review.builder()
                .title("title")
                .content("Is good")
                .stars(5)
                .build();


        reviewRepository.save(review);

        Review savedReview = reviewRepository.findById(review.getId()).get();


        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isEqualTo(review.getId());

    }


    @Test
    public  void ReviewRepository_UpdateById_ReturnSaved(){
        Review review = Review.builder()
                .title("title")
                .content("Is good")
                .stars(5)
                .build();


        reviewRepository.save(review);

        Review savedReview = reviewRepository.findById(review.getId()).get();
        savedReview.setContent("Is not good");
        savedReview.setStars(3);


        Review updatedReview = reviewRepository.save(savedReview);


        Assertions.assertThat(updatedReview.getContent()).isNotNull();
        Assertions.assertThat(updatedReview.getStars()).isNotNull();
        Assertions.assertThat(updatedReview.getContent()).isEqualTo("Is not good");
        Assertions.assertThat(updatedReview.getStars()).isEqualTo(3);

    }

    @Test
    public  void ReviewRepository_DeleteById_Success(){
        Review review = Review.builder()
                .title("title")
                .content("Is good")
                .stars(5)
                .build();


        reviewRepository.save(review);

        reviewRepository.deleteById(review.getId());

        Optional<Review> deletedReview = reviewRepository.findById(review.getId());



        Assertions.assertThat(deletedReview).isEmpty() ;

    }



}
