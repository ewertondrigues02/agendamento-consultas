package br.com.ewerton.servicedoctor.controller;

import br.com.ewerton.servicedoctor.configuration.security.TokenService;
import br.com.ewerton.servicedoctor.dto.AuthenticationDTO;
import br.com.ewerton.servicedoctor.dto.EmailResponseDTO;
import br.com.ewerton.servicedoctor.dto.RegisterDTO;
import br.com.ewerton.servicedoctor.model.DoctorModel;
import br.com.ewerton.servicedoctor.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe {@link AuthenticationController}.
 * Testa os endpoints de login e registro de médicos no sistema.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private DoctorModel doctor;
    private AuthenticationDTO authenticationDTO;
    private RegisterDTO registerDTO;

    @BeforeEach
    public void setUp() {
        // Inicializando mocks
        MockitoAnnotations.openMocks(this);

        // Criando dados de exemplo
        doctor = new DoctorModel();
        doctor.setEmail("joao@example.com");
        doctor.setPassword("password");
        doctor.setRole("USER");

        authenticationDTO = new AuthenticationDTO("joao@example.com", "password");
        registerDTO = new RegisterDTO("maria@example.com", "password", "USER");
    }

    /**
     * Testa o endpoint login() quando as credenciais são válidas.
     */
    @Test
    public void testLoginSuccessful() {
        // Simula o comportamento do AuthenticationManager
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Simula o comportamento do TokenService
        when(tokenService.generateToken(any(DoctorModel.class))).thenReturn("mockToken");

        // Chama o endpoint
        ResponseEntity<EmailResponseDTO> response = authenticationController.login(authenticationDTO);

        // Verifica o status da resposta e o conteúdo
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("mockToken", response.getBody().getToken());
    }

    /**
     * Testa o endpoint login() quando as credenciais são inválidas.
     */
    @Test
    public void testLoginFailed() {
        // Simula o comportamento do AuthenticationManager lançando uma exceção
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Chama o endpoint
        ResponseEntity<EmailResponseDTO> response = authenticationController.login(authenticationDTO);

        // Verifica o status da resposta
        assertEquals(500, response.getStatusCodeValue());
    }

    /**
     * Testa o endpoint register() quando o email não está cadastrado.
     */
    @Test
    public void testRegisterSuccessful() {
        // Simula que o email não está cadastrado
        when(doctorRepository.findByEmail(registerDTO.email())).thenReturn(null);

        // Simula o comportamento do BCryptPasswordEncoder
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());

        // Chama o endpoint
        ResponseEntity<DoctorModel> response = authenticationController.register(registerDTO);

        // Verifica o status da resposta
        assertEquals(200, response.getStatusCodeValue());
        verify(doctorRepository, times(1)).save(any(DoctorModel.class));
    }

    /**
     * Testa o endpoint register() quando o email já está cadastrado.
     */
    @Test
    public void testRegisterEmailAlreadyExists() {
        // Simula que o email já está cadastrado
        when(doctorRepository.findByEmail(registerDTO.email())).thenReturn(doctor);

        // Chama o endpoint
        ResponseEntity<DoctorModel> response = authenticationController.register(registerDTO);

        // Verifica o status da resposta
        assertEquals(400, response.getStatusCodeValue());
        verify(doctorRepository, times(0)).save(any(DoctorModel.class));
    }
}
