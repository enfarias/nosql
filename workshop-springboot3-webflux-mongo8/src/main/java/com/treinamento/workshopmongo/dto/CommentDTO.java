package com.treinamento.workshopmongo.dto;

import java.time.Instant;

import com.treinamento.workshopmongo.models.embedded.Comment;

public class CommentDTO {
	
	private String text;
	private Instant moment;
	private AuthorDTO author;

	public CommentDTO() {
	}

	public CommentDTO(String text, Instant moment, AuthorDTO author) {
		this.text = text;
		this.moment = moment;
		this.author = author;
	}

	public CommentDTO(Comment entity) {
		this.text = entity.getText();
		this.moment = entity.getMoment(); 
		if (entity.getAuthor() != null) {
			this.author = new AuthorDTO(entity.getAuthor());
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Instant getMoment() {
		return moment;
	}

	public void setMoment(Instant moment) {
		this.moment = moment;
	}

	public AuthorDTO getAuthor() {
		return author;
	}

	public void setAuthor(AuthorDTO author) {
		this.author = author;
	}
}