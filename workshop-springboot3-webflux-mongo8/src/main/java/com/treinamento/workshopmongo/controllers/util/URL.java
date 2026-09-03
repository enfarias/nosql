package com.treinamento.workshopmongo.controllers.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

public class URL {

	public static Instant convertDate(String textDate, Instant defaultValue) {
		if (textDate == null || textDate.isBlank()) {
			return defaultValue;
		}
		try {
			// Tenta interpretar como Instant ISO-8601 completo (ex: 2026-08-01T00:00:00Z)
			return Instant.parse(textDate);
		} catch (DateTimeParseException e) {
			try {
				// Tenta interpretar como data simples (ex: 2026-08-01) e converte para o início do dia em UTC
				return LocalDate.parse(textDate).atStartOfDay(ZoneOffset.UTC).toInstant();
			} catch (DateTimeParseException e2) {
				return defaultValue;
			}
		}
	}
}