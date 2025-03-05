package br.com.ewerton.servicepatient.controller;

import br.com.ewerton.servicepatient.dto.PatientDTO;
import br.com.ewerton.servicepatient.model.PatientModel;
import br.com.ewerton.servicepatient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controlador responsável pelas operações relacionadas aos pacientes.
 * <p>
 * Este controlador fornece endpoints para consultar, deletar e listar pacientes. A interação com os pacientes
 * ocorre por meio de operações CRUD simples, e os dados dos pacientes são retornados no formato DTO (Data Transfer Object).
 */
@Tag(name = "Patients endpoints")
@RestController
@RequestMapping("/patient-service")
public class PatientController {

    @Autowired
    private PatientService patientService;

    /**
     * Endpoint para obter a lista de todos os pacientes registrados.
     * <p>
     * Este endpoint retorna uma lista de pacientes, onde cada paciente é representado por um DTO contendo
     * os campos necessários (nome, telefone, endereço e email).
     *
     * @return Uma lista de DTOs de pacientes registrados.
     */
    @Operation(summary = "Find a List of registered patients")
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientModel> patients = patientService.allPatient();
        List<PatientDTO> patientDTOS = patients.stream()
                .map(patient -> new PatientDTO(patient.getName(), patient.getPhone(), patient.getAddress(), patient.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(patientDTOS);
    }

    /**
     * Endpoint para buscar um paciente específico pelo ID.
     * <p>
     * Este endpoint retorna as informações de um paciente com base no ID fornecido. Caso o paciente
     * não seja encontrado, é retornado um código de status HTTP 404 (Not Found).
     *
     * @param id O ID do paciente a ser buscado.
     * @return O DTO do paciente se encontrado, ou código 404 caso o paciente não exista.
     */
    @Operation(summary = "Find a specific patient by your ID")
    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> findPatientById(@PathVariable UUID id) {
        Optional<PatientModel> patient = patientService.findPatientById(id);
        if (patient.isPresent()) {
            PatientDTO patientDTO = new PatientDTO(patient.get().getName(), patient.get().getPhone(), patient.get().getAddress(), patient.get().getEmail());
            return ResponseEntity.ok().body(patientDTO);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Endpoint para deletar um paciente específico pelo ID.
     * <p>
     * Este endpoint permite a exclusão de um paciente. Caso o paciente seja encontrado, ele é deletado
     * e retorna um código de status HTTP 204 (No Content). Se o paciente não for encontrado, um código
     * de status HTTP 404 (Not Found) é retornado.
     *
     * @param id O ID do paciente a ser deletado.
     * @return Código de status HTTP 204 caso o paciente seja deletado com sucesso, ou HTTP 404 se o paciente não existir.
     */
    @Operation(summary = "Delete a  specific patient by your ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        Optional<PatientModel> patient = patientService.findPatientById(id);
        if (patient.isPresent()) {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
