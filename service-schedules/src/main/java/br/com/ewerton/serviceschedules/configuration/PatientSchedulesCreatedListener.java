package br.com.ewerton.serviceschedules.configuration;

import br.com.ewerton.serviceschedules.dto.PatientDTO;
import br.com.ewerton.serviceschedules.model.SchedulesModel;
import br.com.ewerton.serviceschedules.repository.SchedulesRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Listener para o evento de criação de agendamento de paciente.
 * <p>
 * Esta classe escuta a fila do RabbitMQ "schedules.v1.patients-schedules-created-queue-schedules"
 * e processa os dados do paciente recebidos na mensagem, salvando os detalhes do agendamento
 * no banco de dados e imprimindo um log.
 */
@Component
public class PatientSchedulesCreatedListener {

    @Autowired
    private SchedulesRepository schedulesRepository;

    /**
     * Método que é chamado quando uma mensagem é recebida da fila RabbitMQ.
     * Ele converte os dados do paciente em um objeto {@link SchedulesModel} e o salva
     * no banco de dados. Após o salvamento, imprime os detalhes do agendamento no console.
     *
     * @param patientDTO O DTO contendo os dados do paciente a ser agendado.
     */
    @RabbitListener(queues = "schedules.v1.patients-schedules-created-queue-schedules")
    public void onPatientSchedulesCreated(PatientDTO patientDTO) {
        SchedulesModel schedulesModel = new SchedulesModel();
        schedulesModel.setName(patientDTO.name());
        schedulesModel.setPhone(patientDTO.phone());
        schedulesModel.setAddress(patientDTO.address());
        schedulesModel.setEmail(patientDTO.email());

        // Salva o agendamento no banco de dados
        schedulesRepository.save(schedulesModel);

        // Imprime no console o agendamento realizado
        System.out.println("Patient Scheduled: " + patientDTO);
    }
}
