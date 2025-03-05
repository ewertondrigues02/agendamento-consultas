package br.com.ewerton.servicedoctor.service;

import br.com.ewerton.servicedoctor.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela autenticação de usuários médicos.
 * Implementa a interface {@link UserDetailsService} para fornecer detalhes do usuário durante o processo de login.
 * Utiliza o repositório {@link DoctorRepository} para buscar os médicos no banco de dados.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
@Service
class AuthorizationService implements UserDetailsService {

    /**
     * Repositório utilizado para acessar os dados dos médicos no banco de dados.
     */
    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * Carrega os detalhes do médico a partir do seu e-mail, utilizado durante o processo de autenticação.
     *
     * @param username O e-mail do médico, utilizado como nome de usuário para autenticação.
     * @return O objeto {@link UserDetails} representando o médico, que contém informações necessárias para autenticação.
     * @throws UsernameNotFoundException Caso o e-mail não seja encontrado no banco de dados, uma exceção será lançada.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return doctorRepository.findByEmail(username);
    }
}
