package br.com.ewerton.servicepatient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Modelo que representa um paciente no sistema.
 * Esta classe implementa a interface {@link UserDetails} para integração com o Spring Security
 * e gerenciar a autenticação e autorização de usuários.
 */
@Entity
@Table(name = "tb_patient")
public class PatientModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String address;

    private String phone;

    @Email
    private String email;

    private String password;

    @Enumerated
    private PatientRole role;

    /**
     * Construtor padrão.
     */
    public PatientModel() {
    }

    /**
     * Construtor para criar um paciente com email, senha e papel.
     *
     * @param email    O email do paciente.
     * @param password A senha do paciente.
     * @param role     O papel (role) do paciente.
     */
    public PatientModel(String email, String password, PatientRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Construtor para criar um paciente com todos os campos.
     *
     * @param id       O ID do paciente.
     * @param name     O nome do paciente.
     * @param address  O endereço do paciente.
     * @param phone    O telefone do paciente.
     * @param email    O email do paciente.
     * @param password A senha do paciente.
     * @param role     O papel (role) do paciente.
     */
    public PatientModel(UUID id, String name, String address, String phone, String email, String password, PatientRole role) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Construtor para criar um paciente com nome, endereço, telefone e email.
     *
     * @param name    O nome do paciente.
     * @param address O endereço do paciente.
     * @param phone   O telefone do paciente.
     * @param email   O email do paciente.
     */
    public PatientModel(String name, String address, String phone, String email) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    // Getters e Setters

    public UUID getId() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    // Métodos do UserDetails

    /**
     * Retorna as autoridades (permissões) associadas a este paciente.
     *
     * @return Uma coleção de {@link GrantedAuthority} representando as permissões do paciente.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == PatientRole.ADMIN)
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * Retorna a senha do paciente.
     *
     * @return A senha do paciente.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Retorna o nome de usuário do paciente (o email).
     *
     * @return O email do paciente.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Verifica se a conta do paciente não está expirada.
     *
     * @return True se a conta não estiver expirada.
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Verifica se a conta do paciente não está bloqueada.
     *
     * @return True se a conta não estiver bloqueada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * Verifica se as credenciais do paciente não estão expiradas.
     *
     * @return True se as credenciais não estiverem expiradas.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * Verifica se o paciente está habilitado.
     *
     * @return True se o paciente estiver habilitado.
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    /**
     * Define a senha do paciente.
     *
     * @param password A senha do paciente.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retorna o papel (role) do paciente.
     *
     * @return O papel do paciente.
     */
    public PatientRole getRole() {
        return role;
    }

    /**
     * Define o papel (role) do paciente.
     *
     * @param role O papel do paciente.
     */
    public void setRole(PatientRole role) {
        this.role = role;
    }
}
