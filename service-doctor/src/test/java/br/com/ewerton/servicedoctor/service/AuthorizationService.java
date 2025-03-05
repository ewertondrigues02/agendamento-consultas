package br.com.ewerton.servicedoctor.service;

import br.com.ewerton.servicedoctor.model.DoctorModel;
import br.com.ewerton.servicedoctor.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para a classe {@link AuthorizationService}.
 * Testa o comportamento do método loadUserByUsername para autenticação de médicos.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
class AuthorizationServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AuthorizationService authorizationService;

    private DoctorModel doctor;

    @BeforeEach
    public void setUp() {
        // Inicializando mocks
        MockitoAnnotations.openMocks(this);

        // Criando dados de exemplo
        doctor = new DoctorModel();
        doctor.setEmail("joao@example.com");
        doctor.setPassword("password");
        doctor.setRole("USER");
    }

    /**
     * Testa o método loadUserByUsername() quando o e-mail é encontrado.
     */
    @Test
    public void testLoadUserByUsernameSuccessful() {
        // Simula o comportamento do repositório retornando um médico encontrado
        when(doctorRepository.findByEmail("joao@example.com")).thenReturn(doctor);

        // Chama o método
        UserDetails userDetails = authorizationService.loadUserByUsername("joao@example.com");

        // Verifica se o retorno é o esperado
        assertNotNull(userDetails);
        assertEquals("joao@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
    }

    /**
     * Testa o método loadUserByUsername() quando o e-mail não é encontrado.
     */
    @Test
    public void testLoadUserByUsernameNotFound() {
        // Simula o comportamento do repositório retornando null quando o e-mail não é encontrado
        when(doctorRepository.findByEmail("joao@example.com")).thenReturn(null);

        // Chama o método e verifica se a exceção é lançada
        assertThrows(UsernameNotFoundException.class, () -> {
            authorizationService.loadUserByUsername("joao@example.com");
        });
    }
}
