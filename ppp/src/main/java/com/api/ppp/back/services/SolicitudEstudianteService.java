package com.api.ppp.back.services;

import com.api.ppp.back.models.Convocatoria;
import com.api.ppp.back.models.SolicitudEstudiante;

import java.util.List;

public interface SolicitudEstudianteService extends BaseService<SolicitudEstudiante, Integer> {

    public List<SolicitudEstudiante> solicitudesxConvocatoria(Convocatoria convocatoria);

    public List<SolicitudEstudiante> solicitudesAprovadasxConvocatoria(Convocatoria convocatoria);

    public List<SolicitudEstudiante> solicitudesPendientesxConvocatoria(Convocatoria convocatoria);

}
