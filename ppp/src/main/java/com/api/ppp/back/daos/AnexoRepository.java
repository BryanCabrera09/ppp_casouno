package com.api.ppp.back.daos;

import com.api.ppp.back.models.Anexos;
import com.api.ppp.back.models.Practica;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnexoRepository extends BaseRepository<Anexos, Integer> {

    Optional<List<Anexos>> findAllByPractica(Practica practica);
    Optional<List<Anexos>> findAllByPracticaAndTipo(Practica practica,Integer tipo);


}
