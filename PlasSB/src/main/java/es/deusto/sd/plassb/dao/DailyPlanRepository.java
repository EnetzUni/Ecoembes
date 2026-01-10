package es.deusto.sd.plassb.dao;

import es.deusto.sd.plassb.entity.DailyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {
    // ¡No hace falta escribir nada más! Spring hace la magia.
}