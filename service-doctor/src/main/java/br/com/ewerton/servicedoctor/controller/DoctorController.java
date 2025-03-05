package br.com.ewerton.servicedoctor.controller;

import br.com.ewerton.servicedoctor.model.DoctorModel;
import br.com.ewerton.servicedoctor.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controlador responsável pela gestão de médicos no sistema.
 * Ele fornece endpoints para consultar a lista de médicos registrados
 * e buscar informações de médicos específicos por ID.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
@Tag(name = "Doctor endpoints")
@RestController
@RequestMapping(name = "/doctor-service")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * Endpoint para consultar a lista de todos os médicos registrados no sistema.
     *
     * @return ResponseEntity contendo a lista de médicos registrados.
     */
    @Operation(summary = "Find a List of registered patients")
    @GetMapping
    public ResponseEntity<List<DoctorModel>> doctorAll() {
        List<DoctorModel> doctors = doctorService.allDoctor();
        return ResponseEntity.ok().body(doctors);
    }

    /**
     * Endpoint para buscar um médico específico pelo seu ID.
     *
     * @param id O ID do médico a ser consultado.
     * @return ResponseEntity contendo o médico encontrado ou resposta de erro caso não exista.
     */
    @Operation(summary = "Find a specific patient by your ID")
    @GetMapping(value = "/doctor-service/{id}")
    public ResponseEntity<DoctorModel> doctorById(@RequestParam UUID id) {
        Optional<DoctorModel> obj = doctorService.findDoctorById(id);
        return obj.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
