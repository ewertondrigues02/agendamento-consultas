package br.com.ewerton.servicedoctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(@Email @NotBlank String email , @NotBlank String password, String role) {
}
