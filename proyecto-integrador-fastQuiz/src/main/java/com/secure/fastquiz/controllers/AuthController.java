package com.secure.fastquiz.controllers;

import com.secure.fastquiz.models.AppRole;
import com.secure.fastquiz.models.Role;
import com.secure.fastquiz.models.User;
import com.secure.fastquiz.repositories.RoleRepository;
import com.secure.fastquiz.repositories.UserRepository;
import com.secure.fastquiz.security.jwt.JwtUtils;
import com.secure.fastquiz.security.request.LoginRequest;
import com.secure.fastquiz.security.request.SignupRequest;
import com.secure.fastquiz.security.response.LoginResponse;
import com.secure.fastquiz.security.response.MessageResponse;
import com.secure.fastquiz.security.response.UserInfoResponse;
import com.secure.fastquiz.security.services.UserDetailsImpl;
import com.secure.fastquiz.services.TotpService;
import com.secure.fastquiz.services.UserService;
import com.secure.fastquiz.util.AuthUtil;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    TotpService totpService;

    /**
     * Endpoint para iniciar sesión.
     * Recibe las credenciales del usuario (nombre de usuario y contraseña),
     * autentica al usuario y genera un token JWT.
     */
    @PostMapping("/public/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Credenciales incorrectas");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     * Recibe los datos del usuario, valida si el nombre de usuario o correo electrónico ya existen,
     * asigna un rol y guarda el usuario en la base de datos.
     */
    @PostMapping("/public/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: ¡El nombre de usuario ya está en uso!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: ¡El correo ya está en uso!"));
        }

        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
        Role role = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));

        user.setRole(role);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("¡Usuario registrado exitosamente!"));
    }

    /**
     * Endpoint para obtener los detalles del usuario autenticado.
     * Devuelve información sobre el usuario, incluyendo roles y estado de la cuenta.
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(
                user.getUserId(), user.getUserName(), user.getEmail(),
                user.isAccountNonLocked(), user.isAccountNonExpired(),
                user.isCredentialsNonExpired(), user.isEnabled(),
                user.getCredentialsExpiryDate(), user.getAccountExpiryDate(),
                user.isTwoFactorEnabled(), roles
        );

        return ResponseEntity.ok().body(response);
    }

    /**
     * Endpoint para restablecer la contraseña de un usuario.
     * Genera un token de restablecimiento y envía un correo con las instrucciones.
     */
    @PostMapping("/public/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            userService.generatePasswordResetToken(email);
            return ResponseEntity.ok(new MessageResponse("¡Correo de restablecimiento de contraseña enviado!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error al enviar el correo de restablecimiento"));
        }
    }

    /**
     * Endpoint para cambiar la contraseña utilizando un token de restablecimiento.
     * Recibe el token y la nueva contraseña, y actualiza los datos del usuario.
     */
    @PostMapping("/public/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok(new MessageResponse("¡Contraseña restablecida con éxito!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Endpoint para habilitar la autenticación de dos factores (2FA).
     * Genera un código secreto y una URL de código QR para configurar la autenticación 2FA.
     */
    @PostMapping("/enable-2fa")
    public ResponseEntity<String> enable2FA() {
        Long userId = authUtil.loggedInUserId();
        GoogleAuthenticatorKey secret = userService.generate2FASecret(userId);
        String qrCodeUrl = totpService.getQrCodeUrl(secret, userService.getUserById(userId).getUserName());
        return ResponseEntity.ok(qrCodeUrl);
    }

    /**
     * Endpoint para deshabilitar la autenticación de dos factores (2FA).
     * Marca el 2FA como deshabilitado para el usuario autenticado.
     */
    @PostMapping("/disable-2fa")
    public ResponseEntity<String> disable2FA() {
        Long userId = authUtil.loggedInUserId();
        userService.disable2FA(userId);
        return ResponseEntity.ok("2FA deshabilitado");
    }

    /**
     * Endpoint para verificar el código de autenticación de dos factores (2FA).
     * Valida el código proporcionado por el usuario.
     */
    @PostMapping("/verify-2fa")
    public ResponseEntity<String> verify2FA(@RequestParam int code) {
        Long userId = authUtil.loggedInUserId();
        boolean isValid = userService.validate2FACode(userId, code);
        if (isValid) {
            userService.enable2FA(userId);
            return ResponseEntity.ok("2FA verificado");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Código 2FA inválido");
        }
    }
}
