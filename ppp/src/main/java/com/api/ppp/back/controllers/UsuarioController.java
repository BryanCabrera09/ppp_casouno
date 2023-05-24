package com.api.ppp.back.controllers;

import com.api.ppp.back.daos.AuthorityRepository;
import com.api.ppp.back.models.Authority;
import com.api.ppp.back.models.Usuario;
import com.api.ppp.back.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins="*")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private AuthorityRepository authorityRepository;

    // To list all records
    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok().body(service.findAll());
    }

    // To find one record, specifically by a unique identifier (PK or ID)
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarID(@PathVariable("id") Integer id) {
        Optional<Usuario> current = service.findById(id);
        if(current.isPresent()) {
            return ResponseEntity.ok().body(current.get());
        }
        return ResponseEntity.notFound().build();
    }

    // To create a record
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Usuario entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    // To list users by their roles
    @GetMapping("/listar/rol")
    public ResponseEntity<?> listarRol(String rol) {
        return ResponseEntity.ok().body(authorityRepository.findByName(rol));
    }

    // to list all records in table authorities
    @GetMapping("/authority/listar")
    public ResponseEntity<?> buscarAuth() {
        List<Authority> authorities = authorityRepository.findAll();
        return ResponseEntity.ok().body(authorities);
    }

    // To update a user's role name just by its name
    @PostMapping("/authority/editar/{id}")
    public ResponseEntity<?> editarAuthority(@PathVariable("id") Integer id, @RequestParam String name) {
        Optional<Authority> current = authorityRepository.findById(id);
        if (current.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Authority authority = current.get();
        authority.setName(name);
        return ResponseEntity.ok().body(authorityRepository.save(authority));
    }

}
