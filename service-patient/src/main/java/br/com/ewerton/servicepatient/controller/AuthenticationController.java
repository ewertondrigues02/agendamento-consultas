package br.com.ewerton.servicepatient.controller;

import br.com.ewerton.servicepatient.controller.security.TokenService;
import br.com.ewerton.servicepatient.dto.AuthenticationDTO;
import br.com.ewerton.servicepatient.dto.EmailResponseDTO;
import br.com.ewerton.servicepatient.dto.RegisterDTO;
import br.com.ewerton.servicepatient.model.PatientModel;
import br.com.ewerton.servicepatient.repository.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador responsável pela autenticação e registro de pacientes.
 * <p>
 * Este controlador fornece dois endpoints principais: um para login (autenticação) e outro para registro de novos pacientes.
 * O login gera um token JWT após a autenticação bem-sucedida, enquanto o registro cria um novo paciente na base de dados.
 */
@RestController
@RequestMapping("patient-service/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TokenService tokenService;

    /**
     * Endpoint para autenticação de um paciente.
     * <p>
     * Recebe os dados de login (email e senha), autentica o paciente e, se a autenticação for bem-sucedida, gera um token JWT
     * que será retornado na resposta.
     *
     * @param data O objeto DTO contendo o email e senha do paciente para login.
     * @return A resposta contendo o token JWT gerado.
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var authentication = authenticationManager.authenticate(usernamePassword);  // Autentica o usuário com o AuthenticationManager
        var token = tokenService.generateToken((PatientModel) authentication.getPrincipal());  // Gera o token JWT
        return ResponseEntity.ok(new EmailResponseDTO(token));  // Retorna o token no corpo da resposta
    }

    /**
     * Endpoint para registrar um novo paciente.
     * <p>
     * Recebe os dados necessários para registrar um paciente (email, senha, role). Se o email já estiver em uso, retorna um erro 400.
     * Caso contrário, cria o paciente, criptografa sua senha e o salva no banco de dados.
     *
     * @param data O objeto DTO contendo as informações para o registro do paciente.
     * @return A resposta indicando o sucesso ou falha no registro.
     */
    @PostMapping("/register")
    public ResponseEntity<List<PatientModel>> registerPatient(@RequestBody @Valid RegisterDTO data) {
        // Verifica se já existe um paciente com o mesmo email
        if (this.patientRepository.findByEmail(data.email()) != null)
            return ResponseEntity.badRequest().build();  // Retorna erro se o email já estiver em uso

        // Criptografa a senha do paciente
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        // Cria o novo paciente
        PatientModel newPatient = new PatientModel(data.email(), encryptedPassword, data.role());

        // Salva o paciente no banco de dados
        this.patientRepository.save(newPatient);

        // Retorna resposta com status 200 (sucesso)
        return ResponseEntity.ok().build();
    }
}
