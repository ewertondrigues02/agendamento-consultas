package br.com.ewerton.servicepatient.model;

/**
 * Enum que representa os papéis (roles) possíveis para um paciente no sistema.
 * <p>
 * Cada paciente pode ter um papel de {@link PatientRole#ADMIN} ou {@link PatientRole#USER},
 * definindo o nível de acesso e permissões no sistema.
 */
public enum PatientRole {

    /**
     * Papel de administrador, com permissões elevadas no sistema.
     */
    ADMIN("admin"),

    /**
     * Papel de usuário, com permissões limitadas no sistema.
     */
    USER("user");

    private String role;

    /**
     * Construtor para inicializar o papel (role) do paciente.
     *
     * @param role O nome do papel.
     */
    PatientRole(String role) {
        this.role = role;
    }

    /**
     * Retorna o nome do papel do paciente.
     *
     * @return O nome do papel (role).
     */
    public String getRole() {
        return role;
    }
}
