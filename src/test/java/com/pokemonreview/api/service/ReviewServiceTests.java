package com.pokemonreview.api.service;


import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.exceptions.PokemonNotFoundException;
import com.pokemonreview.api.exceptions.ReviewNotFoundException;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.repository.ReviewRepository;
import com.pokemonreview.api.service.impl.ReviewServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Pokemon pokemon;
    private Review review;
    private ReviewDto reviewDto;
    private PokemonDto pokemonDto;

    @BeforeEach
    public void init(){

         pokemon = Pokemon.builder().
                name("pikachu").
                type("electric").
                build();

         pokemonDto = PokemonDto.builder()
                .name("pikachu")
                .type("electric")
                .build();

         review = Review.builder()
                .title("title")
                .content("Is good")
                .stars(5)
                .build();

         reviewDto = ReviewDto.builder()
                .title("review title")
                .content("test content")
                .stars(5)
                .build();

    }


    @Test
    public void createReview_ReturnsReviewDto_Success(){
        when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.of(pokemon));

        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);


        ReviewDto savedReview = reviewService.createReview(pokemon.getId(),reviewDto);

        Assertions.assertThat(savedReview).isNotNull();

    }

    @Test
    public void  getReviewByPokemonId_ReturnsList_Success(){
        int reviewId = 1;

        when(reviewRepository.findByPokemonId(reviewId)).thenReturn(Arrays.asList(review));

        List<ReviewDto> reviews = reviewService.getReviewsByPokemonId(reviewId);
        Assertions.assertThat(reviews).isNotNull();
        Assertions.assertThat(reviews.size()).isGreaterThan(0);

    }

    @Test
    public void getReviewById_ThrowsPokemonNotFoundException_WhenPokemonNotFound() {
        int reviewId = 1;
        int pokemonId = 999; // Non-existent Pokemon ID

        // Mock pokemon retrieval to return empty (simulating not found)
        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.empty());

        // Assert that the exception is thrown when service is called
        Assertions.assertThatThrownBy(() -> reviewService.getReviewById(reviewId, pokemonId))
                .isInstanceOf(PokemonNotFoundException.class)
                .hasMessage("Pokemon with associated review not found");
    }


    @Test
    public void getReviewById_ThrowsReviewNotFoundException_WhenReviewNotFound() {
        int reviewId = 999; // Non-existent Review ID
        int pokemonId = 1;

        // Mock pokemon retrieval to return a valid pokemon
        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));

        // Mock review retrieval to return empty (simulating not found)
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // Assert that the exception is thrown when service is called
        Assertions.assertThatThrownBy(() -> reviewService.getReviewById(reviewId, pokemonId))
                .isInstanceOf(ReviewNotFoundException.class)
                .hasMessage("Review with associate pokemon not found");
    }

    @Test
    public void getReviewById_ThrowsReviewNotFoundException_WhenReviewDoesNotBelongToPokemon() {
        int reviewId = 1;
        int pokemonId = 1;

        // Mock pokemon retrieval to return a valid pokemon
        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));

        // Create a different Pokemon object to simulate mismatch
        Pokemon otherPokemon = Pokemon.builder().id(2).name("charizard").type("fire").build();

        // Associate the review with a different Pokemon
        review.setPokemon(otherPokemon);

        // Mock review retrieval to return a valid review associated with the wrong Pokemon
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // Assert that the exception is thrown when service is called
        Assertions.assertThatThrownBy(() -> reviewService.getReviewById(reviewId, pokemonId))
                .isInstanceOf(ReviewNotFoundException.class)
                .hasMessage("This review does not belong to a pokemon");
    }





    @Test
    public void getReviewById_ReturnsReviewDto_Success(){
        int reviewId = 1;
        int pokemonId = 1;

        review.setPokemon(pokemon);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));

        ReviewDto reviewReturn = reviewService.getReviewById(reviewId,pokemonId);

        Assertions.assertThat(reviewReturn).isNotNull();
        Assertions.assertThat(reviewReturn).isNotNull();

    }

    @Test
    public void UpdatePokemon_ReturnReviewDto_Success(){
        int pokemonId =1;
        int reviewId =1;

        pokemon.setReviews(Arrays.asList(review));
        review.setPokemon(pokemon);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));

        when(reviewRepository.save(review)).thenReturn(review);

        ReviewDto reviewReturn = reviewService.updateReview(pokemonId,reviewId,reviewDto);

        Assertions.assertThat(reviewReturn).isNotNull();

    }


    @Test
    public void DeletePokemon_returnNull_Success(){

        int pokemonId =1;
        int reviewId =1;

        pokemon.setReviews(Arrays.asList(review));
        review.setPokemon(pokemon);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(pokemonRepository.findById(pokemonId)).thenReturn(Optional.of(pokemon));

        assertAll(() -> reviewService.deleteReview(pokemonId,reviewId));


    }


}
