package com.api.ppp.back.controllers;

import com.api.ppp.back.daos.AuthorityRepository;
import com.api.ppp.back.models.Authority;
import com.api.ppp.back.models.Estudiante;
import com.api.ppp.back.models.Usuario;
import com.api.ppp.back.services.EstudianteService;
import com.api.ppp.back.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityRepository authorityRepository;

    @PostMapping("/register")
    public ResponseEntity<?> crear(@RequestBody Estudiante entity) {
        Estudiante estudiante = null;
        ResponseEntity response = null;
        try {
            String hashPwd = passwordEncoder.encode(entity.getUsuario().getPassword());
            entity.getUsuario().setPassword(hashPwd);
            estudiante = estudianteService.save(entity);
            if (estudiante.getId() > 0) {
                Authority role = new Authority();
                role.setName("ROLE_ESTUD");
                role.setUsuario(estudiante.getUsuario());
                authorityRepository.save(role);
                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("Given user details are successfully registered");
            }
        } catch (Exception ex) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occured due to " + ex.getMessage());
        }
        return response;
    }

    @RequestMapping("/ingresar")
    public Usuario getUserDetailsAfterLogin(Authentication authentication) {
        Optional<Usuario> usuario = usuarioService.findByCorreo(authentication.getName());
        return usuario.orElse(null);
    }

}
