package com.api.ppp.back.services;

import com.api.ppp.back.daos.BaseRepository;
import com.api.ppp.back.daos.SolicitudEstudianteRepository;
import com.api.ppp.back.models.Convocatoria;
import com.api.ppp.back.models.SolicitudEstudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudEstudianteServiceImpl extends BaseServiceImpl<SolicitudEstudiante, Integer> implements SolicitudEstudianteService {

    @Autowired
    private SolicitudEstudianteRepository repository;

    public SolicitudEstudianteServiceImpl(BaseRepository<SolicitudEstudiante, Integer> baseRepository) {
        super(baseRepository);
    }

    @Override
    public List<SolicitudEstudiante> solicitudesxConvocatoria(Convocatoria convocatoria) {
        return (List<SolicitudEstudiante>) repository.findAllByConvocatoria(convocatoria).orElse(null);
    }

    @Override
    public List<SolicitudEstudiante> solicitudesAprovadasxConvocatoria(Convocatoria convocatoria) {
        return (List<SolicitudEstudiante>) repository.findByConvocatoriaAndEstado(convocatoria,2).orElse(null);
    }

    @Override
    public List<SolicitudEstudiante> solicitudesPendientesxConvocatoria(Convocatoria convocatoria) {
        return (List<SolicitudEstudiante>) repository.findByConvocatoriaAndEstado(convocatoria,0).orElse(null);
    }
}
