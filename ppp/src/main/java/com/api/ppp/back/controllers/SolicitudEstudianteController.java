package com.api.ppp.back.controllers;

import com.api.ppp.back.models.Convocatoria;
import com.api.ppp.back.models.SolicitudEmpresa;
import com.api.ppp.back.models.SolicitudEstudiante;
import com.api.ppp.back.services.ConvocaroriaService;
import com.api.ppp.back.services.SolicitudEstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/solicitudEstudiante")
@CrossOrigin(origins="*")
public class SolicitudEstudianteController {

    @Autowired
    private SolicitudEstudianteService service;

    @Autowired
    private ConvocaroriaService convocaroriaService;

    // To list all records
    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok().body(service.findAll());
    }

    // To find one record, specifically by a unique identifier (PK or ID)
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarID(@PathVariable("id") Integer id) {
        Optional<SolicitudEstudiante> current = service.findById(id);
        if(current.isPresent()) {
            return ResponseEntity.ok().body(current.get());
        }
        return ResponseEntity.notFound().build();
    }

    // To create a record
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody SolicitudEstudiante entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    // To find one record and update it, specifically by a unique identifier (PK or ID)
    @PostMapping("/editar/{id}")
    public ResponseEntity<?> editar(@PathVariable("id") Integer id, @RequestBody SolicitudEstudiante entity) {
        Optional<SolicitudEstudiante> optional = service.findById(id);
        if(optional.isPresent()) {
            SolicitudEstudiante current = optional.get();
            current.setEstudiante(entity.getEstudiante());
            current.setConvocatoria(entity.getConvocatoria());
            current.setEstado(entity.getEstado());
            current.setFechaEnvio(entity.getFechaEnvio());
            current.setUrl(entity.getUrl());
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

    //To find by convocatoria by MAC
    @GetMapping("/listarxconvocatoria")
    public List<SolicitudEstudiante> listarxConvocatoria(@RequestBody Convocatoria entity) {
        return service.solicitudesxConvocatoria(entity);
    }

    //Endopiont a peticion de brayan
    @GetMapping("/listarxconvocatoria2")
    public List<SolicitudEstudiante> listarxConvocatoria2(@RequestParam Integer id) {
        Convocatoria entity = new Convocatoria();
        entity.setId(id);
        return service.solicitudesxConvocatoria(entity);
    }

    //by myrian
    @PutMapping ("/editarEstado/{id}")
    public ResponseEntity<?> editar(@PathVariable("id") Integer id, @RequestParam("estado") Integer estado) {
        Optional<SolicitudEstudiante> optional = service.findById(id);
        if (optional.isPresent()) {
            SolicitudEstudiante current = optional.get();
            current.setEstado(estado);
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(current));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/listaraprobadasxconvocatoria")
    public ResponseEntity<?> listar(@RequestParam("id") Integer id) {
        return ResponseEntity.ok().body(service.solicitudesAprovadasxConvocatoria(convocaroriaService.findById(id).orElse(null)));
    }

    @GetMapping("/listarpendientesxconvocatoria")
    public ResponseEntity<?> listarsolicitadas(@RequestParam("id") Integer id) {
        //AQUI LA SUBIDA
        return ResponseEntity.ok().body(service.solicitudesPendientesxConvocatoria(convocaroriaService.findById(id).orElse(null)));
    }

    @PostMapping("/guardarpdf")
    public ResponseEntity<String> guardarDocumento(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id")Integer id) {
        try {
            Optional<SolicitudEstudiante> optional = service.findById(id);
            if (optional.isPresent()) {
                SolicitudEstudiante current = optional.get();
                current.setUrl(archivo.getBytes());
                service.save(current);
                return ResponseEntity.ok("El documento se ha guardado correctamente. "+current.getUrl().length);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el documento.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el documento.");
    }

    @GetMapping("/mostrarpdf/{id}")
    public ResponseEntity<byte[]> obtenerDocumento(@PathVariable("id") Integer id) {
        Optional<SolicitudEstudiante> optional = service.findById(id);
        if (optional.isPresent()) {
            SolicitudEstudiante current = optional.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment().filename(current.getEstudiante().getUsuario().getCedula()+".pdf").build());
            return new ResponseEntity<>(current.getUrl(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
