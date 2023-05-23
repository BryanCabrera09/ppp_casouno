package com.api.ppp.back.services;

import com.api.ppp.back.models.Anexos;
import com.api.ppp.back.models.Practica;

import java.util.List;

public interface AnexoService extends BaseService<Anexos, Integer> {

    List<Anexos> anexoTipo(Practica practica,Integer tipo);

}
