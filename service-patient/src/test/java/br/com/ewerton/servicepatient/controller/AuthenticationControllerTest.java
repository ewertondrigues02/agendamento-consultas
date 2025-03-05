package br.com.ewerton.servicepatient.controller;

import br.com.ewerton.servicepatient.controller.security.TokenService;
import br.com.ewerton.servicepatient.dto.AuthenticationDTO;
import br.com.ewerton.servicepatient.dto.RegisterDTO;
import br.com.ewerton.servicepatient.model.PatientModel;
import br.com.ewerton.servicepatient.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    private AuthenticationDTO authenticationDTO;
    private RegisterDTO registerDTO;
    private PatientModel patientModel;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();

        // Preparando objetos para os testes
        authenticationDTO = new AuthenticationDTO("johndoe@example.com", "password123");
        registerDTO = new RegisterDTO("johndoe@example.com", "password123", "ROLE_USER");

        patientModel = new PatientModel();
    }

    @Test
    void testLogin_Success() throws Exception {
        // Configura o comportamento de autenticação
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationDTO.email(), authenticationDTO.password());
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Mockando o token gerado
        String generatedToken = "fake-jwt-token";
        when(tokenService.generateToken(any(PatientModel.class))).thenReturn(generatedToken);

        // Realiza a requisição POST para login
        mockMvc.perform(MockMvcRequestBuilders.post("/patient-service/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(authenticationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(generatedToken));

        // Verifica se o método de autenticação foi chamado
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, times(1)).generateToken(any(PatientModel.class));
    }

    @Test
    void testLogin_Failure() throws Exception {
        // Configura o comportamento para falha na autenticação
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Realiza a requisição POST para login
        mockMvc.perform(MockMvcRequestBuilders.post("/patient-service/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(authenticationDTO)))
                .andExpect(status().isUnauthorized());

        // Verifica se o método de autenticação foi chamado
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, times(0)).generateToken(any(PatientModel.class));
    }

    @Test
    void testRegister_Success() throws Exception {
        // Configura o comportamento do repositório
        when(patientRepository.findByEmail(registerDTO.email())).thenReturn(null);
        when(patientRepository.save(any(PatientModel.class))).thenReturn(patientModel);

        // Realiza a requisição POST para registro de paciente
        mockMvc.perform(MockMvcRequestBuilders.post("/patient-service/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registerDTO)))
                .andExpect(status().isOk());

        // Verifica se o paciente foi salvo
        verify(patientRepository, times(1)).findByEmail(registerDTO.email());
        verify(patientRepository, times(1)).save(any(PatientModel.class));
    }

    @Test
    void testRegister_EmailAlreadyExists() throws Exception {
        // Configura o comportamento do repositório para retornar um paciente existente
        when(patientRepository.findByEmail(registerDTO.email())).thenReturn(patientModel);

        // Realiza a requisição POST para registro de paciente
        mockMvc.perform(MockMvcRequestBuilders.post("/patient-service/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registerDTO)))
                .andExpect(status().isBadRequest());

        // Verifica se o método de busca do email foi chamado
        verify(patientRepository, times(1)).findByEmail(registerDTO.email());
        verify(patientRepository, times(0)).save(any(PatientModel.class));
    }

    // Método auxiliar para converter objetos em JSON
    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

