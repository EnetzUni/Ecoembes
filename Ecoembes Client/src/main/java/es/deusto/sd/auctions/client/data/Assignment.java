package es.deusto.sd.ecoembes.dto;

import java.util.Date;
import java.util.List;

public record Assignment(long id, Date date, long employeeId, long recyclingPlantId, List<Dumpster> dumpsters) {}
