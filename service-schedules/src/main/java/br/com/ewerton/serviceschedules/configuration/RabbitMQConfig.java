package br.com.ewerton.serviceschedules.configuration;

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
 * Configuração do RabbitMQ para o serviço de agendamento de pacientes.
 * <p>
 * Esta classe configura as filas, exchanges e o RabbitTemplate para envio e recebimento
 * de mensagens entre os microsserviços utilizando o RabbitMQ. Ela também define o
 * conversor de mensagens para JSON e inicializa a fila quando a aplicação está pronta.
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Cria a fila para os agendamentos de pacientes.
     *
     * @return A fila configurada para o agendamento de pacientes.
     */
    @Bean
    public Queue queueSchedules() {
        return new Queue("schedules.v1.patients-schedules-created-queue-schedules");
    }

    /**
     * Cria e configura o Binding entre a fila de agendamentos e a Exchange Fanout.
     * <p>
     * A fila é conectada a uma exchange do tipo Fanout, onde todas as mensagens publicadas
     * na exchange são enviadas para a fila vinculada.
     *
     * @return O Binding configurado entre a fila e a exchange.
     */
    @Bean
    public Binding binding() {
        Queue queue = new Queue("schedules.v1.patients-schedules-created-queue-schedules");
        FanoutExchange exchange = new FanoutExchange("schedules.v1.patients-schedules-created");
        return BindingBuilder.bind(queue).to(exchange);
    }

    /**
     * Configura o RabbitAdmin para gerenciar a configuração do RabbitMQ.
     * <p>
     * RabbitAdmin inicializa as filas e exchanges, garantindo que a configuração esteja
     * pronta para o uso quando a aplicação for iniciada.
     *
     * @param connectionFactory A fábrica de conexões para o RabbitMQ.
     * @return O RabbitAdmin configurado.
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * Configura o listener para ser executado quando a aplicação estiver pronta.
     * <p>
     * Inicializa o RabbitAdmin quando a aplicação é carregada, garantindo que as filas
     * e exchanges sejam criadas.
     *
     * @param rabbitAdmin O RabbitAdmin para inicialização.
     * @return O listener para o evento de "ApplicationReadyEvent".
     */
    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    /**
     * Configura o conversor de mensagens para converter objetos Java para JSON e vice-versa.
     * <p>
     * O conversor é usado pelo {@link RabbitTemplate} para enviar e receber mensagens no formato JSON.
     *
     * @return O conversor de mensagens Jackson configurado.
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configura o RabbitTemplate para interagir com o RabbitMQ.
     * <p>
     * O RabbitTemplate é usado para enviar e receber mensagens do RabbitMQ. Ele é configurado
     * com uma conexão e um conversor de mensagens para garantir que as mensagens sejam manipuladas
     * corretamente no formato JSON.
     *
     * @param connectionFactory A fábrica de conexões para o RabbitMQ.
     * @param messageConverter  O conversor de mensagens para JSON.
     * @return O RabbitTemplate configurado.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

}
