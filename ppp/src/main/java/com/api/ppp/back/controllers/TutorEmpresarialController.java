package com.api.ppp.back.controllers;

import com.api.ppp.back.models.Accion;
import com.api.ppp.back.models.TutorEmpresarial;
import com.api.ppp.back.services.TutorEmpresarialService;
import com.api.ppp.back.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/tutorEmpresa")
@CrossOrigin(origins="*")
public class TutorEmpresarialController {

    @Autowired
    private TutorEmpresarialService service;

    @Autowired
    private UsuarioService usuarioService;

    // To list all records
    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok().body(service.findAll());
    }

    // To find one record, specifically by a unique identifier (PK or ID)
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarID(@PathVariable("id") Integer id) {
        Optional<TutorEmpresarial> current = service.findById(id);
        if(current.isPresent()) {
            return ResponseEntity.ok().body(current.get());
        }
        return ResponseEntity.notFound().build();
    }

    // To create a record
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody TutorEmpresarial entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    // To find one record and update it, specifically by a unique identifier (PK or ID)
    @PostMapping("/editar/{id}")
    public ResponseEntity<?> editar(@PathVariable("id") Integer id, @RequestBody TutorEmpresarial entity) {
        Optional<TutorEmpresarial> optional = service.findById(id);
        if(optional.isPresent()) {
            TutorEmpresarial current = optional.get();
            current.setEmpresa(entity.getEmpresa());
            current.setUsuario(entity.getUsuario());
            current.setCargo(entity.getCargo());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(current));
        }
        return ResponseEntity.notFound().build();
    }

    // To find one record and delete it, specifically by a unique identifier (PK or ID)
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarID(@PathVariable("id") Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscarxusuario/{id}")
    public ResponseEntity<?> buscarxUsuario(@PathVariable("id") Integer id) {
        Optional<TutorEmpresarial> current = Optional.ofNullable(service.tutorxUsuario(usuarioService.findById(id).orElse(null)));
        if(current.isPresent()) {
            return ResponseEntity.ok().body(current.get());
        }
        return ResponseEntity.notFound().build();
    }



}
