package br.com.ewerton.servicedoctor.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
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
 * Configuração do RabbitMQ para integração com o serviço de agendamento de pacientes.
 * Esta classe configura as filas, exchanges, e a conversão de mensagens para uso do RabbitMQ
 * no sistema de agendamento.
 * <p>
 * O RabbitMQ é utilizado para envio e recebimento de mensagens entre os componentes do sistema,
 * especialmente para a comunicação relacionada ao agendamento de pacientes.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Define a fila onde os agendamentos de pacientes são processados.
     *
     * @return A fila "schedules.v1.patients-schedules-created-queue-doctor".
     */
    @Bean
    public Queue queueDoctor() {
        return new Queue("schedules.v1.patients-schedules-created-queue-doctor");
    }

    /**
     * Cria o vínculo (binding) entre a fila e a exchange.
     * O vínculo associa a fila "schedules.v1.patients-schedules-created-queue-doctor" à exchange
     * do tipo fanout "schedules.v1.patients-schedules-created", permitindo que as mensagens enviadas
     * para a exchange sejam encaminhadas para a fila.
     *
     * @return O objeto de binding que conecta a fila à exchange.
     */
    @Bean
    public Binding binding() {
        Queue queue = new Queue("schedules.v1.patients-schedules-created-queue-doctor");
        FanoutExchange exchange = new FanoutExchange("schedules.v1.patients-schedules-created");
        return BindingBuilder.bind(queue).to(exchange);
    }

    /**
     * Cria o objeto RabbitAdmin, que é responsável pela administração do RabbitMQ,
     * incluindo a criação de filas, exchanges e bindings.
     *
     * @param connectionFactory A fábrica de conexão com o RabbitMQ.
     * @return O objeto RabbitAdmin configurado.
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * Configura um ouvinte para o evento ApplicationReadyEvent, que é disparado
     * quando a aplicação está completamente inicializada.
     * Este ouvinte é usado para inicializar o RabbitAdmin após a aplicação estar pronta.
     *
     * @param rabbitAdmin O objeto RabbitAdmin.
     * @return O ApplicationListener para o evento ApplicationReadyEvent.
     */
    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(RabbitAdmin rabbitAdmin) {
        return patientDTO -> rabbitAdmin.initialize();
    }

    /**
     * Configura o conversor de mensagens para o RabbitMQ, utilizando a biblioteca Jackson
     * para converter objetos em JSON e vice-versa.
     *
     * @return O conversor de mensagens Jackson2JsonMessageConverter.
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Cria e configura o objeto RabbitTemplate, que é utilizado para enviar e receber
     * mensagens do RabbitMQ. O RabbitTemplate usa o conversor de mensagens para serializar
     * e desserializar os dados.
     *
     * @param connectionFactory A fábrica de conexão com o RabbitMQ.
     * @param messageConverter  O conversor de mensagens.
     * @return O RabbitTemplate configurado.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
