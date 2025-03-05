package br.com.ewerton.servicepatient.controller;

import br.com.ewerton.servicepatient.dto.PatientDTO;
import br.com.ewerton.servicepatient.model.PatientModel;
import br.com.ewerton.servicepatient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador responsável pelo gerenciamento de agendamentos de pacientes.
 * <p>
 * Este controlador oferece um endpoint para registrar um paciente e enviar uma mensagem para
 * uma fila do RabbitMQ, indicando que um novo paciente foi criado.
 */
@Tag(name = "Schedules endpoint")
@RestController
@RequestMapping("/patient-service")
public class PatientsSchedulesController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Endpoint para registrar um paciente e enviar uma mensagem para a fila de agendamentos.
     * <p>
     * Este endpoint converte um objeto do tipo DTO para o tipo Model, salva o paciente no banco
     * de dados, e em seguida envia uma mensagem para uma fila no RabbitMQ informando que um
     * paciente foi registrado. O paciente registrado é retornado no formato DTO.
     *
     * @param patientDTO O DTO que contém os dados do paciente a ser registrado.
     * @return O DTO do paciente registrado.
     */
    @Operation(summary = "converts an object of type model to type dto then saves it in the database and sends a message to the queue")
    @PostMapping("/schedules")
    public ResponseEntity<PatientDTO> patientsSchedules(@RequestBody PatientDTO patientDTO) {
        // Converte o DTO para o modelo de paciente
        PatientModel patientModel = new PatientModel(patientDTO.name(), patientDTO.address(), patientDTO.phone(), patientDTO.email());

        // Salva o paciente no banco de dados
        patientService.savePatient(patientModel);

        // Converte o paciente salvo de volta para o DTO
        PatientDTO savedPatientDTO = new PatientDTO(patientModel.getName(), patientModel.getPhone(), patientModel.getAddress(), patientModel.getEmail());

        // Envia a mensagem para a fila do RabbitMQ
        rabbitTemplate.convertAndSend("schedules.v1.patients-schedules-created", "", savedPatientDTO);

        // Retorna o paciente registrado como resposta
        return ResponseEntity.ok(savedPatientDTO);
    }
}
