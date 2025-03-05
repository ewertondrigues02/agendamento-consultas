package br.com.ewerton.servicepatient.configuration;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do RabbitMQ para o gerenciamento de filas e exchanges.
 * <p>
 * Esta classe define a configuração do RabbitMQ para o envio e recebimento de mensagens relacionadas aos agendamentos de pacientes.
 * A configuração inclui a criação de uma exchange do tipo fanout, uma fila específica para os agendamentos e um template para o envio de mensagens.
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Define a exchange do tipo Fanout, que envia mensagens para todas as filas ligadas a ela.
     *
     * @return A exchange fanout para agendamentos de pacientes.
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("schedules.v1.patients-schedules-created");
    }

    /**
     * Define a fila para o recebimento de mensagens sobre os agendamentos de pacientes para médicos.
     *
     * @return A fila do RabbitMQ que recebe mensagens sobre agendamentos de pacientes.
     */
    @Bean
    public Queue queueDoctor() {
        return new Queue("schedules.v1.patients-schedules-created-queue-doctor");
    }

    /**
     * Define o RabbitAdmin, que é responsável pela inicialização e gerenciamento dos recursos do RabbitMQ.
     *
     * @param connectionFactory A fábrica de conexões com o RabbitMQ.
     * @return O RabbitAdmin configurado para inicializar e administrar as filas e exchanges.
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * Define um listener que será executado assim que a aplicação estiver pronta, inicializando o RabbitAdmin.
     *
     * @param rabbitAdmin O RabbitAdmin que será inicializado.
     * @return O ApplicationListener que gerencia o evento de aplicação pronta.
     */
    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(RabbitAdmin rabbitAdmin) {
        return patientDTO -> rabbitAdmin.initialize();
    }

    /**
     * Define o conversor de mensagens para converter objetos em formato JSON ao serem enviados ou recebidos pelo RabbitMQ.
     *
     * @return O conversor de mensagens Jackson2JsonMessageConverter.
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Define o RabbitTemplate, que é utilizado para enviar mensagens para o RabbitMQ.
     *
     * @param connectionFactory A fábrica de conexões com o RabbitMQ.
     * @param messageConverter  O conversor de mensagens para enviar mensagens em formato JSON.
     * @return O RabbitTemplate configurado com o conversor de mensagens.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

}
