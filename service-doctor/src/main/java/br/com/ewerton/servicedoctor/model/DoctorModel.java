package br.com.ewerton.servicedoctor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Representa um modelo de médico no sistema.
 * Esta classe implementa a interface {@link UserDetails} do Spring Security para ser usada na autenticação.
 * <p>
 * Contém informações como nome, especialidade, CRM, clínica, e-mail, senha e o papel (role) do médico.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
@Entity
@Table(name = "tb_doctor")
public class DoctorModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String specialty;

    private String crm;

    private String clinic;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 8)
    @Column(nullable = false)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoctorRole role;

    /**
     * Construtor padrão.
     */
    public DoctorModel() {
    }

    /**
     * Construtor para criação de um novo médico com todos os dados.
     *
     * @param id        O ID do médico.
     * @param name      O nome do médico.
     * @param specialty A especialidade do médico.
     * @param crm       O CRM do médico.
     * @param clinic    A clínica do médico.
     * @param email     O e-mail do médico.
     * @param password  A senha do médico.
     * @param role      O papel (role) do médico.
     */
    public DoctorModel(String id, String name, String specialty, String crm, String clinic, String email, String password, DoctorRole role) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.crm = crm;
        this.clinic = clinic;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Construtor para criação de um novo médico com e-mail, senha e papel.
     *
     * @param email    O e-mail do médico.
     * @param password A senha do médico.
     * @param role     O papel (role) do médico.
     */
    public DoctorModel(String email, String password, DoctorRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters e Setters

    public String getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DoctorRole getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Método toString que representa o médico como uma string.
     *
     * @return A string representando o objeto médico.
     */
    @Override
    public String toString() {
        return "DoctorModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", specialty='" + specialty + '\'' +
                ", crm='" + crm + '\'' +
                ", clinic='" + clinic + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    /**
     * Método equals que verifica a igualdade entre dois objetos médicos.
     *
     * @param o O objeto a ser comparado com o médico.
     * @return true se os objetos forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorModel that = (DoctorModel) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(specialty, that.specialty) && Objects.equals(crm, that.crm) && Objects.equals(clinic, that.clinic) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && role == that.role;
    }

    /**
     * Método hashCode que gera um valor hash único para o médico.
     *
     * @return O valor hash do médico.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, specialty, crm, clinic, email, password, role);
    }

    /**
     * Método que retorna as autoridades (roles) do médico.
     *
     * @return As autoridades do médico baseadas no seu papel (role).
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == DoctorRole.ADMIN)
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * Método que retorna a senha do médico.
     *
     * @return A senha do médico.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Método que retorna o nome de usuário (e-mail) do médico.
     *
     * @return O e-mail do médico.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Método que verifica se a conta do médico não está expirada.
     *
     * @return true se a conta não está expirada, false caso contrário.
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Método que verifica se a conta do médico não está bloqueada.
     *
     * @return true se a conta não está bloqueada, false caso contrário.
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * Método que verifica se as credenciais do médico não estão expiradas.
     *
     * @return true se as credenciais não estão expiradas, false caso contrário.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * Método que verifica se o médico está habilitado.
     *
     * @return true se o médico está habilitado, false caso contrário.
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
