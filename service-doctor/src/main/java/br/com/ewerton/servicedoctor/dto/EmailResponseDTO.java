package br.com.ewerton.servicedoctor.dto;

public record EmailResponseDTO(String token) {
    public String getToken() {
        return token;
    }
}
