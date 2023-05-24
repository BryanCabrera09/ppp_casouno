package com.api.ppp.back.controllers;

import com.api.ppp.back.models.Accion;
import com.api.ppp.back.models.SemanaActividad;
import com.api.ppp.back.services.SemanaActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/semanaActividad")
@CrossOrigin(origins="*")
public class SemanaActividadController {

    @Autowired
    private SemanaActividadService service;

    // To list all records
    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok().body(service.findAll());
    }

    // To find one record, specifically by a unique identifier (PK or ID)
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarID(@PathVariable("id") Integer id) {
        Optional<SemanaActividad> current = service.findById(id);
        if(current.isPresent()) {
            return ResponseEntity.ok().body(current.get());
        }
        return ResponseEntity.notFound().build();
    }

    // To create a record
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody SemanaActividad entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    // To find one record and update it, specifically by a unique identifier (PK or ID)
    @PostMapping("/editar/{id}")
    public ResponseEntity<?> editar(@PathVariable("id") Integer id, @RequestBody SemanaActividad entity) {
        Optional<SemanaActividad> optional = service.findById(id);
        if(optional.isPresent()) {
            SemanaActividad current = optional.get();
            current.setPractica(entity.getPractica());
            current.setDia(entity.getDia());
            current.setHoraInicio(entity.getHoraInicio());
            current.setHoraFin(entity.getHoraFin());
            current.setTotalHoras(entity.getTotalHoras());
            current.setActividad(entity.getActividad());
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

    // to find a list of SemanaActividad by an Estudiante ID
    @GetMapping("/buscar/estudiante/{id}")
    public ResponseEntity<?> buscarSActividadEstudianteID(@PathVariable("id") Integer id) {
        List<SemanaActividad> listaActividades = service.listarSActividadEstudianteId(id);
        Collections.sort(listaActividades, Comparator.comparing(SemanaActividad::getDia));
        return ResponseEntity.ok().body(listaActividades);
    }

}
