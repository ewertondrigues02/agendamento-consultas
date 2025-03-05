package br.com.ewerton.servicedoctor.controller;

import br.com.ewerton.servicedoctor.model.DoctorModel;
import br.com.ewerton.servicedoctor.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para a classe {@link DoctorController}.
 * Testa os endpoints da API para garantir que a lógica de controle está funcionando corretamente.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
public class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    private DoctorModel doctor;
    private UUID doctorId;

    @BeforeEach
    public void setUp() {
        // Inicializando mocks
        MockitoAnnotations.openMocks(this);

        // Criando dados de exemplo
        doctorId = UUID.randomUUID();
        doctor = new DoctorModel();
        doctor.setId(doctorId.toString());
        doctor.setName("Dr. João");
        doctor.setSpecialty("Cardiology");
        doctor.setEmail("joao@example.com");
        doctor.setPassword("password");
    }

    /**
     * Testa o endpoint doctorAll() que retorna a lista de médicos.
     */
    @Test
    public void testDoctorAll() {
        // Simula o comportamento do serviço
        when(doctorService.allDoctor()).thenReturn(List.of(doctor));

        // Chama o endpoint
        ResponseEntity<List<DoctorModel>> response = doctorController.doctorAll();

        // Verifica o status da resposta e o conteúdo
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
    }

    /**
     * Testa o endpoint doctorById() quando o médico é encontrado.
     */
    @Test
    public void testDoctorByIdFound() {
        // Simula o comportamento do serviço
        when(doctorService.findDoctorById(doctorId)).thenReturn(Optional.of(doctor));

        // Chama o endpoint
        ResponseEntity<DoctorModel> response = doctorController.doctorById(doctorId);

        // Verifica o status da resposta e o conteúdo
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(doctorId.toString(), response.getBody().getId());
    }

    /**
     * Testa o endpoint doctorById() quando o médico não é encontrado.
     */
    @Test
    public void testDoctorByIdNotFound() {
        // Simula o comportamento do serviço
        when(doctorService.findDoctorById(doctorId)).thenReturn(Optional.empty());

        // Chama o endpoint
        ResponseEntity<DoctorModel> response = doctorController.doctorById(doctorId);

        // Verifica o status da resposta
        assertEquals(404, response.getStatusCodeValue());
    }
}
