package com.treinamento.workshopmongo.controllers.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.format.DateTimeParseException;

public class URL {

	public static String decodeParam(String text) {
		if (text == null) {
			return "";
		}
		return URLDecoder.decode(text, StandardCharsets.UTF_8);
	}

	public static Instant convertDate(String textDate, Instant defaultValue) {
		if (textDate == null || textDate.isBlank()) {
			return defaultValue;
		}
		try {
			return Instant.parse(textDate);
		} catch (DateTimeParseException e) {
			return defaultValue;
		}
	}
}