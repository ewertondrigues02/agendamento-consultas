package br.com.ewerton.servicepatient.controller.security;

import br.com.ewerton.servicepatient.model.PatientModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Serviço responsável pela geração e validação de tokens JWT.
 * <p>
 * Este serviço utiliza a biblioteca `auth0` para criar e verificar tokens JWT. O token é utilizado para autenticar os usuários
 * e garantir que as requisições realizadas à aplicação sejam feitas por usuários autenticados.
 */
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera um token JWT para o paciente fornecido.
     * <p>
     * O token contém o email do paciente como sujeito e tem uma validade de 2 horas. O algoritmo HMAC256 é utilizado para
     * assinar o token com a chave secreta configurada na aplicação.
     *
     * @param patientModel O modelo do paciente, que contém as informações necessárias para gerar o token.
     * @return O token JWT gerado.
     * @throws RuntimeException Se ocorrer um erro ao gerar o token.
     */
    public String generateToken(PatientModel patientModel) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);  // Define o algoritmo de assinatura com a chave secreta
            // Cria o token com o email do paciente e tempo de expiração
            String token = JWT.create()
                    .withIssuer("auth-api")  // Define o emissor do token
                    .withSubject(patientModel.getEmail())  // Define o email do paciente como o sujeito
                    .withExpiresAt(generateExpirationTime())  // Define o tempo de expiração
                    .sign(algorithm);  // Assina o token com o algoritmo
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generation token", exception);  // Lança exceção caso ocorra erro na criação do token
        }
    }

    /**
     * Valida o token JWT fornecido.
     * <p>
     * O token é verificado utilizando o algoritmo HMAC256 e a chave secreta configurada na aplicação.
     * Caso o token seja válido, o método retorna o email do paciente associado ao token. Caso contrário, retorna uma string vazia.
     *
     * @param token O token JWT a ser validado.
     * @return O email do paciente se o token for válido, ou uma string vazia se for inválido.
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);  // Define o algoritmo de assinatura com a chave secreta
            // Valida o token e retorna o email (sujeito) do paciente
            return JWT.require(algorithm)
                    .withIssuer("auth-api")  // Verifica o emissor do token
                    .build()
                    .verify(token)  // Verifica a validade do token
                    .getSubject();  // Retorna o sujeito (email) do token
        } catch (JWTVerificationException exception) {
            return "";  // Retorna string vazia se o token for inválido
        }
    }

    /**
     * Gera o tempo de expiração do token, que é 2 horas a partir do momento atual.
     *
     * @return O tempo de expiração do token em formato `Instant`.
     */
    private Instant generateExpirationTime() {
        // Define o tempo de expiração para 2 horas à frente
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
