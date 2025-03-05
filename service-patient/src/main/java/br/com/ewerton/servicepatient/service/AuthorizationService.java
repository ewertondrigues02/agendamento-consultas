package br.com.ewerton.servicepatient.service;

import br.com.ewerton.servicepatient.model.PatientModel;
import br.com.ewerton.servicepatient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela autorização de usuários, implementando o
 * {@link UserDetailsService} do Spring Security para carregar os detalhes
 * de um paciente a partir do seu e-mail.
 * <p>
 * Este serviço é utilizado para autenticar e autorizar pacientes no sistema.
 */
@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private PatientRepository patientRepository;

    /**
     * Carrega os detalhes do paciente a partir do e-mail fornecido.
     * Este método é utilizado pelo Spring Security durante o processo de autenticação.
     *
     * @param username O e-mail do paciente que será usado para buscar no banco de dados.
     * @return Os detalhes do paciente, implementados pela classe {@link PatientModel}.
     * @throws UsernameNotFoundException Se o paciente com o e-mail fornecido não for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o paciente no repositório pelo e-mail
        PatientModel patient = (PatientModel) patientRepository.findByEmail(username);

        // Se o paciente não for encontrado, lança uma exceção
        if (patient == null) {
            throw new UsernameNotFoundException("Paciente não encontrado com o e-mail: " + username);
        }

        // Retorna os detalhes do paciente
        return patient;
    }
}
