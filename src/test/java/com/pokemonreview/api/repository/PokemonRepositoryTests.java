package com.pokemonreview.api.repository;


import com.pokemonreview.api.models.Pokemon;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PokemonRepositoryTests {

    @Autowired
    private PokemonRepository pokemonRepository;

    @Test
    public void PokemonRepository_SaveAll_Returned_SavedPokemon(){

        //Arrange
        Pokemon pokemon = Pokemon.builder().
                name("pikachu").
                type("electric").
                build();

        //Act
        Pokemon savedPokemon = pokemonRepository.save(pokemon);

        //Assert
        Assertions.assertThat(savedPokemon).isNotNull();
        Assertions.assertThat(savedPokemon.getId()).isGreaterThan(0);

    }

    @Test
    public void PokemonRepository_findAll_Success(){


        Pokemon pokemon = Pokemon.builder().
                name("pikachu").
                type("electric").
                build();
        Pokemon pokemon2 = Pokemon.builder().
                name("pikachu2").
                type("electric").
                build();

        pokemonRepository.save(pokemon);
        pokemonRepository.save(pokemon2);

        List<Pokemon> pokemonList = pokemonRepository.findAll();
        Assertions.assertThat(pokemonList).isNotNull();
        Assertions.assertThat(pokemonList.size()).isEqualTo(2);


    }

    @Test
    public void PokemonRepository_findById_Success(){

        Pokemon pokemon = Pokemon.builder().
                name("pikachu").
                type("electric").
                build();

        pokemonRepository.save(pokemon);

        Pokemon pokemonReturn = pokemonRepository.findById(pokemon.getId()).get();

        Assertions.assertThat(pokemonReturn).isNotNull();
        Assertions.assertThat(pokemonReturn.getId()).isEqualTo(pokemon.getId());

    }


    @Test
    public void PokemonRepository_findByType_Success(){

        Pokemon pokemon = Pokemon.builder().
                name("pikachu").
                type("electric").
                build();

        pokemonRepository.save(pokemon);

        Pokemon pokemonReturn = pokemonRepository.findByType(pokemon.getType()).get();

        Assertions.assertThat(pokemonReturn).isNotNull();
        Assertions.assertThat(pokemonReturn.getType()).isEqualTo(pokemon.getType());

    }


    @Test
    public void PokemonRepository_UpdatePokemon_Success(){

        Pokemon pokemon = Pokemon.builder().
                name("pikachu").
                type("electric").
                build();

        pokemonRepository.save(pokemon);

        Pokemon pokemonReturn = pokemonRepository.findById(pokemon.getId()).get();
        pokemonReturn.setType("Electric");
        pokemonReturn.setName("Nive");

        Pokemon updatedPokemon = pokemonRepository.save(pokemonReturn);

        Assertions.assertThat(updatedPokemon.getName()).isNotNull();
        Assertions.assertThat(updatedPokemon.getType()).isNotNull();

    }


    @Test
    public void PokemonRepository_DeleteById_SuccessIsEmpty(){

        Pokemon pokemon = Pokemon.builder().
                name("pikachu").
                type("electric").
                build();

        pokemonRepository.save(pokemon);

        pokemonRepository.deleteById(pokemon.getId());

        Optional<Pokemon> pokemonReturn = pokemonRepository.findById(pokemon.getId());

        Assertions.assertThat(pokemonReturn).isEmpty();

    }


}
