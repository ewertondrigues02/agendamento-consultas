package br.com.ewerton.servicedoctor.repository;

import br.com.ewerton.servicedoctor.model.DoctorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repositório para operações de persistência relacionadas aos médicos.
 * Extende a interface {@link JpaRepository} para fornecer funcionalidades CRUD básicas.
 * Inclui um método para buscar médicos pelo e-mail, utilizado para autenticação.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
@Repository
public interface DoctorRepository extends JpaRepository<DoctorModel, UUID> {

    /**
     * Busca um médico no banco de dados pelo seu e-mail.
     *
     * @param email O e-mail do médico.
     * @return O médico encontrado, representado por um objeto {@link UserDetails}.
     */
    UserDetails findByEmail(String email);
}
