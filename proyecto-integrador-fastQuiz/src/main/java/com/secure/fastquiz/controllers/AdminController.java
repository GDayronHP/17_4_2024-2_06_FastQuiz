package com.secure.fastquiz.controllers;

import com.secure.fastquiz.dtos.UserDTO;
import com.secure.fastquiz.models.Role;
import com.secure.fastquiz.models.User;
import com.secure.fastquiz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    // Inyección del servicio UserService que contiene la lógica de negocio para usuarios
    @Autowired
    UserService userService;

    /**
     * Endpoint para obtener la lista de todos los usuarios.
     * Método GET: /api/admin/getusers
     *
     * @return ResponseEntity con la lista de usuarios y un estado HTTP 200 (OK).
     */
    @GetMapping("/getusers")
    public ResponseEntity<List<User>> getAllUsers() {
        // Llama al servicio para obtener todos los usuarios.
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    /**
     * Endpoint para actualizar el rol de un usuario.
     * Método PUT: /api/admin/update-role
     *
     * @param userId   ID del usuario cuyo rol será actualizado.
     * @param roleName Nombre del nuevo rol a asignar.
     * @return ResponseEntity con un mensaje de confirmación.
     */
    @PutMapping("/update-role")
    public ResponseEntity<String> updateUserRole(@RequestParam Long userId, @RequestParam String roleName) {
        // Llama al servicio para actualizar el rol del usuario.
        userService.updateUserRole(userId, roleName);
        return ResponseEntity.ok("User role updated");
    }

    /**
     * Endpoint para obtener la información de un usuario por su ID.
     * Método GET: /api/admin/user/{id}
     *
     * @param id ID del usuario que se desea obtener.
     * @return ResponseEntity con los datos del usuario y un estado HTTP 200 (OK).
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        // Llama al servicio para obtener los datos del usuario por ID.
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    /**
     * Endpoint para actualizar el estado de bloqueo de una cuenta de usuario.
     * Método PUT: /api/admin/update-lock-status
     *
     * @param userId ID del usuario.
     * @param lock   `true` para bloquear la cuenta, `false` para desbloquearla.
     * @return ResponseEntity con un mensaje de confirmación.
     */
    @PutMapping("/update-lock-status")
    public ResponseEntity<String> updateAccountLockStatus(@RequestParam Long userId, @RequestParam boolean lock) {
        // Llama al servicio para actualizar el estado de bloqueo de la cuenta.
        userService.updateAccountLockStatus(userId, lock);
        return ResponseEntity.ok("Account lock status updated");
    }

    /**
     * Endpoint para obtener la lista de todos los roles disponibles.
     * Método GET: /api/admin/roles
     *
     * @return Lista de objetos Role.
     */
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        // Llama al servicio para obtener todos los roles disponibles.
        return userService.getAllRoles();
    }

    /**
     * Endpoint para actualizar el estado de expiración de una cuenta de usuario.
     * Método PUT: /api/admin/update-expiry-status
     *
     * @param userId ID del usuario.
     * @param expire `true` para marcar la cuenta como expirada, `false` para no expirada.
     * @return ResponseEntity con un mensaje de confirmación.
     */
    @PutMapping("/update-expiry-status")
    public ResponseEntity<String> updateAccountExpiryStatus(@RequestParam Long userId, @RequestParam boolean expire) {
        // Llama al servicio para actualizar el estado de expiración de la cuenta.
        userService.updateAccountExpiryStatus(userId, expire);
        return ResponseEntity.ok("Account expiry status updated");
    }

    /**
     * Endpoint para habilitar o deshabilitar una cuenta de usuario.
     * Método PUT: /api/admin/update-enabled-status
     *
     * @param userId  ID del usuario.
     * @param enabled `true` para habilitar la cuenta, `false` para deshabilitarla.
     * @return ResponseEntity con un mensaje de confirmación.
     */
    @PutMapping("/update-enabled-status")
    public ResponseEntity<String> updateAccountEnabledStatus(@RequestParam Long userId, @RequestParam boolean enabled) {
        // Llama al servicio para actualizar el estado de habilitación de la cuenta.
        userService.updateAccountEnabledStatus(userId, enabled);
        return ResponseEntity.ok("Account enabled status updated");
    }

    /**
     * Endpoint para actualizar el estado de expiración de las credenciales de un usuario.
     * Método PUT: /api/admin/update-credentials-expiry-status
     *
     * @param userId ID del usuario.
     * @param expire `true` para marcar las credenciales como expiradas, `false` para no expiradas.
     * @return ResponseEntity con un mensaje de confirmación.
     */
    @PutMapping("/update-credentials-expiry-status")
    public ResponseEntity<String> updateCredentialsExpiryStatus(@RequestParam Long userId, @RequestParam boolean expire) {
        // Llama al servicio para actualizar el estado de expiración de las credenciales.
        userService.updateCredentialsExpiryStatus(userId, expire);
        return ResponseEntity.ok("Credentials expiry status updated");
    }

    /**
     * Endpoint para actualizar la contraseña de un usuario.
     * Método PUT: /api/admin/update-password
     *
     * @param userId   ID del usuario.
     * @param password Nueva contraseña a asignar.
     * @return ResponseEntity con un mensaje de confirmación o error.
     */
    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestParam Long userId, @RequestParam String password) {
        try {
            // Llama al servicio para actualizar la contraseña del usuario.
            userService.updatePassword(userId, password);
            return ResponseEntity.ok("Password updated");
        } catch (RuntimeException e) {
            // Si ocurre un error, retorna un mensaje con estado HTTP 400 (BAD REQUEST).
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
