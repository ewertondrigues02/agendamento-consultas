package br.com.ewerton.servicedoctor.service;

import br.com.ewerton.servicedoctor.model.DoctorModel;
import br.com.ewerton.servicedoctor.repository.DoctorRepository;
import br.com.ewerton.servicedoctor.service.exceptions.DoctorNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço responsável pela gestão dos dados dos médicos.
 * Contém métodos para consultar médicos e realizar operações no banco de dados relacionados à entidade {@link DoctorModel}.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
@Service
public class DoctorService {

    /**
     * Repositório utilizado para acessar e manipular os dados dos médicos no banco de dados.
     */
    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * Retorna todos os médicos registrados no banco de dados.
     * Caso não haja médicos cadastrados, uma exceção {@link DoctorNotFound} será lançada.
     *
     * @return Uma lista de médicos registrados.
     * @throws DoctorNotFound Se não houver médicos registrados no banco de dados.
     */
    public List<DoctorModel> allDoctor() {
        if (doctorRepository.count() != 0) {
            return doctorRepository.findAll();
        } else {
            throw new DoctorNotFound("Doctor not found");
        }
    }

    /**
     * Encontra um médico pelo seu identificador único (UUID).
     * Caso o médico não seja encontrado, uma exceção {@link DoctorNotFound} será lançada.
     *
     * @param id O identificador único (UUID) do médico.
     * @return Um {@link Optional} contendo o médico encontrado ou vazio se não encontrado.
     * @throws DoctorNotFound Se o médico não for encontrado.
     */
    public Optional<DoctorModel> findDoctorById(UUID id) {
        if (doctorRepository.count() != 0) {
            Optional<DoctorModel> doctor = doctorRepository.findById(id);
            if (doctor.isPresent()) {
                return doctor;
            }
        }
        throw new DoctorNotFound("Doctor not found");
    }

}
