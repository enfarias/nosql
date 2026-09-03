package com.treinamento.workshopmongo.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.treinamento.workshopmongo.models.entities.Post;

public class PostDTO {

	private String id;
	private Instant moment;
	private String title;
	private String body;

	private AuthorDTO author;

	private List<CommentDTO> comments = new ArrayList<>();

	public PostDTO() {
	}

	public PostDTO(String id, Instant moment, String title, String body, AuthorDTO author) {
		this.id = id;
		this.moment = moment;
		this.title = title;
		this.body = body;
		this.author = author;
	}
	
	public PostDTO(Post post) {
	    this.id = post.getId();
	    this.moment = post.getMoment();
	    this.title = post.getTitle();
	    this.body = post.getBody();
	    
	    if (post.getAuthor() != null) {
	        this.author = new AuthorDTO(post.getAuthor());
	    }	    
	    
	    if (post.getComments() != null) {
	        this.comments.addAll(
	            post.getComments()
	                .stream()
	                .map(CommentDTO::new)
	                .toList()
	        );
	    }
	}	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Instant getMoment() {
		return moment;
	}

	public void setMoment(Instant moment) {
		this.moment = moment;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public AuthorDTO getAuthor() {
		return author;
	}

	public void setAuthor(AuthorDTO author) {
		this.author = author;
	}

	public List<CommentDTO> getComments() {
		return comments;
	}
}