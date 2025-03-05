package br.com.ewerton.servicepatient.service;

import br.com.ewerton.servicepatient.model.PatientModel;
import br.com.ewerton.servicepatient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço responsável pela gestão dos pacientes.
 * <p>
 * Esta classe contém métodos para operações CRUD (Create, Read, Update, Delete)
 * para os pacientes no sistema. Ela interage com o repositório {@link PatientRepository}
 * para realizar as operações no banco de dados.
 */
@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    /**
     * Retorna todos os pacientes cadastrados no sistema.
     *
     * @return Uma lista de objetos {@link PatientModel} representando todos os pacientes.
     */
    public List<PatientModel> allPatient() {
        return patientRepository.findAll();
    }

    /**
     * Encontra um paciente pelo seu ID.
     *
     * @param id O ID do paciente a ser encontrado.
     * @return Um {@link Optional} contendo o paciente, se encontrado, ou vazio se não.
     */
    public Optional<PatientModel> findPatientById(UUID id) {
        return patientRepository.findById(id);
    }

    /**
     * Exclui um paciente do sistema baseado no seu ID.
     *
     * @param id O ID do paciente a ser excluído.
     */
    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }

    /**
     * Salva um paciente no banco de dados.
     *
     * @param obj O objeto {@link PatientModel} que representa o paciente a ser salvo.
     * @return O paciente salvo, com os dados persistidos no banco de dados.
     */
    public PatientModel savePatient(PatientModel obj) {
        return patientRepository.save(obj);
    }
}
