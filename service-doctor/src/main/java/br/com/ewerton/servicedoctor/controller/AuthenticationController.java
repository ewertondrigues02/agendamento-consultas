package br.com.ewerton.servicedoctor.controller;

import br.com.ewerton.servicedoctor.configuration.security.TokenService;
import br.com.ewerton.servicedoctor.dto.AuthenticationDTO;
import br.com.ewerton.servicedoctor.dto.EmailResponseDTO;
import br.com.ewerton.servicedoctor.dto.RegisterDTO;
import br.com.ewerton.servicedoctor.model.DoctorModel;
import br.com.ewerton.servicedoctor.repository.DoctorRepository;
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

/**
 * Controlador responsável pela autenticação de médicos no sistema.
 * Ele fornece endpoints para login e registro de novos médicos,
 * além de gerenciar a geração e validação de tokens de autenticação.
 *
 * @author Ewerton Rodrigues
 * @version 1.0
 */
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TokenService tokenService;

    /**
     * Endpoint para realizar o login de um médico no sistema.
     * Realiza a autenticação do médico e retorna um token JWT para autenticação em futuras requisições.
     *
     * @param data Dados de autenticação, incluindo email e senha.
     * @return ResponseEntity contendo o token gerado ou erro de autenticação.
     */
    @PostMapping("/login")
    public ResponseEntity<EmailResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var authentication = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((DoctorModel) authentication.getPrincipal());
        return ResponseEntity.ok(new EmailResponseDTO(token));
    }

    /**
     * Endpoint para registrar um novo médico no sistema.
     * Verifica se o email fornecido já está cadastrado e, se não estiver,
     * cria um novo registro de médico com a senha criptografada.
     *
     * @param data Dados do novo médico, incluindo email, senha e cargo.
     * @return ResponseEntity com o status da operação de registro.
     */
    @PostMapping("/register")
    public ResponseEntity<DoctorModel> register(@RequestBody @Valid RegisterDTO data) {
        if (this.doctorRepository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().build();
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        DoctorModel newDoctor = new DoctorModel(data.email(), encryptedPassword, data.role());
        this.doctorRepository.save(newDoctor);
        return ResponseEntity.ok().build();
    }
}
