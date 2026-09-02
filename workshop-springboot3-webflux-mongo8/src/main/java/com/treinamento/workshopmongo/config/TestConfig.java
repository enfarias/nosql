package com.treinamento.workshopmongo.config;

import java.time.Instant;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.treinamento.workshopmongo.models.embedded.Author;
import com.treinamento.workshopmongo.models.embedded.Comment;
import com.treinamento.workshopmongo.models.entities.Post;
import com.treinamento.workshopmongo.models.entities.User;
import com.treinamento.workshopmongo.repositories.PostRepository;
import com.treinamento.workshopmongo.repositories.UserRepository;

import reactor.core.publisher.Mono;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
	
    @Autowired
    private PostRepository postRepository;

    @Override
    public void run(String... args) throws Exception {

        // Instanciação dos objetos
        User maria = new User(null, "Maria Brown", "maria@gmail.com");
        User alex = new User(null, "Alex Green", "alex@gmail.com");
        User bob = new User(null, "Bob Grey", "bob@gmail.com");

        Post post1 = new Post(null, Instant.parse("2021-02-13T11:15:01Z"), "Partiu viagem", "Vou viajar para São Paulo. Abraços!", new Author(maria));
        Post post2 = new Post(null, Instant.parse("2021-02-14T10:05:49Z"), "Bom dia", "Acordei feliz hoje!", new Author(maria));

        Comment c1 = new Comment("Boa viagem mano!", Instant.parse("2021-02-13T14:30:01Z"), new Author(alex));
        Comment c2 = new Comment("Aproveite", Instant.parse("2021-02-13T15:38:05Z"), new Author(bob));
        Comment c3 = new Comment("Tenha um ótimo dia!", Instant.parse("2021-02-14T12:34:26Z"), new Author(alex));

        post1.getComments().addAll(Arrays.asList(c1, c2));
        post2.getComments().addAll(Arrays.asList(c3));

        // Encadeamento Reativo Encadeado (Garante a ordem exata de execução)
        userRepository.deleteAll()
            .then(postRepository.deleteAll())
            .thenMany(userRepository.saveAll(Arrays.asList(maria, alex, bob)))
            .thenMany(postRepository.saveAll(Arrays.asList(post1, post2)))
            .then(Mono.defer(() -> {
                maria.getPosts().addAll(Arrays.asList(post1, post2));
                return userRepository.save(maria);
            }))
            .subscribe(
                null, 
                error -> System.err.println("Erro na carga de dados: " + error.getMessage()),
                () -> System.out.println("Carga inicial Reativa no MongoDB concluída com sucesso!")
            );
    }
}