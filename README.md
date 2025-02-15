# Agendamento de Consultas

O sistema de agendamento de consultas médicas é dividido em diversos microserviços: `Doctors`, `Patients` e `Scheduling`. O fluxo do sistema é o seguinte:

1. **Cadastro do Paciente**:
   - Pacientes novos podem se cadastrar através do endpoint `register` na API de `Patients`.
   - Se o paciente ainda não estiver cadastrado, ele deve se registrar para poder agendar consultas.

2. **Autenticação**:
   - Após o cadastro, o paciente pode realizar login.
   - A autenticação do paciente é feita utilizando JWT (JSON Web Token) para garantir a segurança.
   - Os microserviços de `Doctors` e `Patients` implementam Spring Security e JWT para validação e autorização.

3. **Consulta de Disponibilidade**:
   - Uma vez autenticado, o paciente pode fazer uma requisição para verificar a disponibilidade dos médicos.
   - O sistema verifica a disponibilidade nos microserviços de `Doctors`.

4. **Agendamento de Consultas**:
   - Quando uma consulta é agendada, os microserviços `Doctors` e `Patients` enviam mensagens ao microserviço `Scheduling` via RabbitMQ.
   - Uma exchange específica para agendamentos distribui as mensagens para as filas de `Doctors` e `Patients`.

5. **Armazenamento e Mensageria**:
   - Cada microserviço possui seu próprio banco de dados PostgreSQL.
   - Tanto PostgreSQL quanto RabbitMQ são executados em contêineres Docker para garantir escalabilidade e facilidade de implantação.

### Tecnologias Utilizadas

- **Java** e **Spring Boot** para o desenvolvimento dos microserviços.
- **Spring Security** e **JWT** para autenticação e autorização.
- **RabbitMQ** para comunicação assíncrona entre os microserviços.
- **Docker** para contêinerização dos serviços.
- **PostgreSQL** como banco de dados para cada microserviço.
- **Swagger** para documentação das APIs.

---

Esta descrição detalha as funcionalidades e tecnologias do sistema, destacando como cada componente interage para fornecer um serviço robusto e seguro de agendamento de consultas médicas.

![Esquema-pronto](https://github.com/ewertondrigues02/appointment-scheduling/assets/106437473/157eedcf-3f08-43c2-9d63-710502553cd3)
