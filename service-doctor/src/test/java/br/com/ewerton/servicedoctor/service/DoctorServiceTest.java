package br.com.ewerton.servicedoctor.service;

import br.com.ewerton.servicedoctor.model.DoctorModel;
import br.com.ewerton.servicedoctor.repository.DoctorRepository;
import br.com.ewerton.servicedoctor.service.exceptions.DoctorNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    private DoctorModel doctor;
    private UUID doctorId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Preparando um médico para os testes
        doctorId = UUID.randomUUID();
        doctor = new DoctorModel();
        doctor.setId(doctorId);
        doctor.setEmail("joao@example.com");
        doctor.setPassword("password");
        doctor.setRole("USER");
    }

    /**
     * Testa o método allDoctor() quando há médicos cadastrados.
     */
    @Test
    public void testAllDoctorWhenDoctorsExist() {
        // Configurando o comportamento do repositório para retornar uma lista com 1 médico
        when(doctorRepository.count()).thenReturn(1L);
        when(doctorRepository.findAll()).thenReturn(Collections.singletonList(doctor));

        // Chamando o método
        List<DoctorModel> doctors = doctorService.allDoctor();

        // Verificando se a lista retornada não está vazia
        assertNotNull(doctors);
        assertFalse(doctors.isEmpty());
        assertEquals(1, doctors.size());
        assertEquals(doctor.getEmail(), doctors.get(0).getEmail());
    }

    /**
     * Testa o método allDoctor() quando não há médicos cadastrados.
     */
    @Test
    public void testAllDoctorWhenDoctorsNotExist() {
        // Configurando o comportamento do repositório para retornar 0 médicos
        when(doctorRepository.count()).thenReturn(0L);

        // Chamando o método e verificando se a exceção é lançada
        assertThrows(DoctorNotFound.class, () -> {
            doctorService.allDoctor();
        });
    }

    /**
     * Testa o método findDoctorById() quando o médico existe.
     */
    @Test
    public void testFindDoctorByIdWhenDoctorExists() {
        // Configurando o comportamento do repositório para retornar um médico com o ID específico
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        // Chamando o método
        Optional<DoctorModel> result = doctorService.findDoctorById(doctorId);

        // Verificando se o médico foi encontrado
        assertTrue(result.isPresent());
        assertEquals(doctorId, result.get().getId());
    }

    /**
     * Testa o método findDoctorById() quando o médico não existe.
     */
    @Test
    public void testFindDoctorByIdWhenDoctorNotFound() {
        // Configurando o comportamento do repositório para não encontrar o médico
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        // Chamando o método e verificando se a exceção é lançada
        assertThrows(DoctorNotFound.class, () -> {
            doctorService.findDoctorById(doctorId);
        });
    }
}
