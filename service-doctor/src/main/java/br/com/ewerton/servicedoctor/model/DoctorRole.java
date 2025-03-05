package br.com.ewerton.servicedoctor.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Enum que define os papéis (roles) de um médico no sistema.
 * Utilizado para controlar o acesso e permissões no sistema.
 * <p>
 * Cada papel representa uma função específica dentro do sistema, como ADMIN para administradores e USER para médicos comuns.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
public enum DoctorRole {

    /**
     * Papel de administrador, com permissões elevadas no sistema.
     */
    ADMIN("admin"),

    /**
     * Papel de usuário comum, com permissões limitadas.
     */
    USER("user");

    @NotBlank
    private final String role;

    /**
     * Construtor que define o valor do papel.
     *
     * @param role O nome do papel (role).
     */
    DoctorRole(String role) {
        this.role = role;
    }

    /**
     * Obtém o nome do papel (role).
     *
     * @return O nome do papel.
     */
    public String getRole() {
        return role;
    }
}
