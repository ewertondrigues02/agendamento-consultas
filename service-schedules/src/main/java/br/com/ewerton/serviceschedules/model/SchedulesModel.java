package br.com.ewerton.serviceschedules.model;

import jakarta.persistence.*;

import java.util.UUID;

/**
 * Modelo de dados para o agendamento de um paciente.
 * <p>
 * Esta classe representa a entidade "tb_schedules" no banco de dados e contém informações
 * relacionadas ao agendamento de um paciente, como nome, telefone, endereço e e-mail.
 * A classe é mapeada como uma entidade JPA e será utilizada para persistir e recuperar
 * dados de agendamentos.
 */
@Entity
@Table(name = "tb_schedules")
public class SchedulesModel {

    /**
     * ID único do agendamento.
     * Utiliza a estratégia UUID para geração do identificador.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Nome do paciente agendado.
     */
    private String name;

    /**
     * Telefone do paciente agendado.
     */
    private String phone;

    /**
     * Endereço do paciente agendado.
     */
    private String address;

    /**
     * E-mail do paciente agendado.
     */
    private String email;

    /**
     * Construtor vazio necessário para o JPA.
     */
    public SchedulesModel() {
    }

    /**
     * Construtor para inicialização completa de um agendamento com todos os campos.
     *
     * @param id      O ID do agendamento.
     * @param name    O nome do paciente.
     * @param phone   O telefone do paciente.
     * @param address O endereço do paciente.
     * @param email   O e-mail do paciente.
     */
    public SchedulesModel(UUID id, String name, String phone, String address, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    /**
     * Obtém o ID do agendamento.
     *
     * @return O ID do agendamento.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Define o ID do agendamento.
     *
     * @param id O ID do agendamento.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Obtém o nome do paciente.
     *
     * @return O nome do paciente.
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome do paciente.
     *
     * @param name O nome do paciente.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtém o telefone do paciente.
     *
     * @return O telefone do paciente.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Define o telefone do paciente.
     *
     * @param phone O telefone do paciente.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Obtém o endereço do paciente.
     *
     * @return O endereço do paciente.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Define o endereço do paciente.
     *
     * @param address O endereço do paciente.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Obtém o e-mail do paciente.
     *
     * @return O e-mail do paciente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o e-mail do paciente.
     *
     * @param email O e-mail do paciente.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
