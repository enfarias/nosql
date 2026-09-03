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

import reactor.core.publisher.Flux;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Override
	public void run(String... args) throws Exception {

		// Limpa o banco de dados
		userRepository.deleteAll()        // Step 1: Cria o publisher de deleção de usuários
		.then(postRepository.deleteAll()) // Step 2: Encadeia a deleção de posts
		.thenMany(Flux.defer(() -> {      // Step 3: Encadeia a criação/salvamento de usuários

			// Cria os objetos de usuário
			User maria = new User(null, "Maria Brown", "maria@gmail.com");
			User alex = new User(null, "Alex Green", "alex@gmail.com");
			User bob = new User(null, "Bob Grey", "bob@gmail.com");

			// Persiste os usuários primeiro para obter os IDs gerados pelo MongoDB
			return userRepository.saveAll(Arrays.asList(maria, alex, bob));
		}))
		.collectList()
		.flatMapMany(users -> {           // Step 4: Encadeia o salvamento de posts
			// Recupera as instâncias salvas com IDs preenchidos
			User mariaSaved = users.get(0);
			User alexSaved = users.get(1);
			User bobSaved = users.get(2);

			// Istancia os posts utilizando o Author com o ID salvo da Maria
			Post post1 = new Post(null, Instant.parse("2021-02-13T11:15:01Z"), 
					"Partiu viagem", "Vou viajar para São Paulo. Abraços!", new Author(mariaSaved));
			Post post2 = new Post(null, Instant.parse("2021-02-14T10:05:49Z"), 
					"Bom dia", "Acordei feliz hoje!", new Author(mariaSaved));

			// Cria os comentários usando os autores salvos (Alex e Bob)
			Comment c1 = new Comment("Boa viagem mano!", Instant.parse("2021-02-13T14:30:01Z"), new Author(alexSaved));
			Comment c2 = new Comment("Aproveite", Instant.parse("2021-02-13T15:38:05Z"), new Author(bobSaved));
			Comment c3 = new Comment("Tenha um ótimo dia!", Instant.parse("2021-02-14T12:34:26Z"), new Author(alexSaved));

			post1.getComments().addAll(Arrays.asList(c1, c2));
			post2.getComments().addAll(Arrays.asList(c3));

			// Salva os posts com os comentários incorporados
			return postRepository.saveAll(Arrays.asList(post1, post2));
		})
		.blockLast();                      // Step 5: SUBSCREVE e BLOQUEIA até a conclusão das inserções
		
	}

}