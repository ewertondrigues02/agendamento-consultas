package br.com.ewerton.servicedoctor.configuration;

import br.com.ewerton.servicedoctor.dto.PatientDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Classe responsável por ouvir mensagens da fila de agendamentos de pacientes.
 * Esta classe é configurada para ser um listener de eventos de criação de agendamentos de pacientes,
 * utilizando o RabbitMQ como sistema de mensagens.
 * Quando um novo agendamento de paciente é criado, a mensagem é recebida e processada por este listener.
 * <p>
 * A anotação {@link RabbitListener} define a fila que este listener deve monitorar e processar as mensagens recebidas.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
@Component
public class PatientSchedulesCreatedListener {

    /**
     * Método que é chamado quando uma mensagem é recebida da fila de agendamentos de pacientes.
     * Este método processa a mensagem, que contém os dados de um paciente agendado.
     * <p>
     * A mensagem recebida é um objeto do tipo {@link PatientDTO}, que contém as informações do paciente
     * que foi agendado.
     *
     * @param patientDTO O objeto {@link PatientDTO} contendo os dados do paciente agendado.
     */
    @RabbitListener(queues = "schedules.v1.patients-schedules-created-queue-doctor")
    public void onPatientSchedulesCreated(PatientDTO patientDTO) {
        // Imprime os dados do paciente agendado no console.
        System.out.println("Patient Scheduled: " + patientDTO);
    }
}
