-- ============================================================
-- DATA.SQL PARA ECOEMBES (CENTRAL SERVER)
-- ============================================================

-- 1. PLANTAS DE RECICLAJE (El Directorio)
-- IMPORTANTE: Los IDs deben coincidir con lo que esperan tus Factories
-- ID 1 -> Sockets (ContSocket)
-- ID 2 -> REST (PlasSB)
INSERT INTO recycling_plant (id, name, location, capacity) VALUES (1, 'ContSocket Ltd.', 'Madrid - Sockets', 1200.0);
INSERT INTO recycling_plant (id, name, location, capacity) VALUES (2, 'PlasSB Ltd.', 'Bilbao - REST API', 1500.0);

-- 2. EMPLEADOS (ADAPTADO A TU CONSTRUCTOR)
-- Columnas: ID, NAME, EMAIL, PASSWORD
-- Nota: Aunque el constructor no tenga ID, la tabla en BBDD sí lo necesita.
INSERT INTO employee (id, name, email, password) VALUES (1, 'Admin General', 'admin@ecoembes.com', 'admin123');
INSERT INTO employee (id, name, email, password) VALUES (2, 'Pepe Recicla', 'pepe@ecoembes.com', 'pepe123');

--- ID: 1 -> Bilbao Centro (Normal)
INSERT INTO dumpster (location, max_capacity, container_count, fill_level) 
VALUES ('Gran Via 25, Bilbao', 1000.0, 1, 0.45);

-- ID: 2 -> Bilbao Moyua (Casi lleno - ¡PELIGRO!)
INSERT INTO dumpster (location, max_capacity, container_count, fill_level) 
VALUES ('Plaza Moyua, Bilbao', 1200.0, 2, 0.95);

-- ID: 3 -> Madrid Sol (Recién vaciado)
INSERT INTO dumpster (location, max_capacity, container_count, fill_level) 
VALUES ('Puerta del Sol, Madrid', 2000.0, 4, 0.05);

-- ID: 4 -> Madrid Retiro (Medio lleno)
INSERT INTO dumpster (location, max_capacity, container_count, fill_level) 
VALUES ('Parque del Retiro, Madrid', 1500.0, 3, 0.60);

-- ID: 5 -> Vitoria (Pequeño y lleno)
INSERT INTO dumpster (location, max_capacity, container_count, fill_level) 
VALUES ('Calle Dato, Vitoria', 800.0, 1, 0.85);

-- ID: 6 -> San Mamés (Estadio - Varía mucho)
INSERT INTO dumpster (location, max_capacity, container_count, fill_level) 
VALUES ('Estadio San Mames, Bilbao', 3000.0, 5, 0.30);


-- =================================================================================
-- 5. INSERTAR HISTORIAL DE LLENADO (Usamos IDs 1 al 6 generados arriba)
-- =================================================================================

-- --- HISTORIAL CONTENEDOR 1 (Gran Vía) ---
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (1, 0.05, '2026-01-01');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (1, 0.10, '2026-01-03');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (1, 0.15, '2026-01-05');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (1, 0.22, '2026-01-08');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (1, 0.30, '2026-01-12');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (1, 0.35, '2026-01-15');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (1, 0.40, '2026-01-18');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (1, 0.45, '2026-01-20');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (1, 0.50, '2026-01-22');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (1, 0.55, '2026-01-25');

--- ------------------------------------------------------------
-- HISTORIAL DUMPSTER 2 (Moyua): Dientes de sierra (Lleno -> Vaciado -> Lleno)
-- ------------------------------------------------------------
-- Ciclo 1
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (2, 0.20, '2026-01-01');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (2, 0.50, '2026-01-02');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (2, 0.90, '2026-01-03');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (2, 0.00, '2026-01-04'); -- ¡VACIADO! ♻️

-- Ciclo 2
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (2, 0.30, '2026-01-05');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (2, 0.70, '2026-01-06');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (2, 0.95, '2026-01-07');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (2, 0.10, '2026-01-08'); -- ¡VACIADO! ♻️

-- Ciclo 3 (Lento)
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (2, 0.30, '2026-01-12');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (2, 0.50, '2026-01-15');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (2, 0.85, '2026-01-18');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (2, 0.95, '2026-01-20');
-- HISTORIAL DUMPSTER 3 (Sol): Caos total y sobrecapacidad
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (3, 0.50, '2026-01-05');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (3, 0.80, '2026-01-06');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (3, 1.10, '2026-01-07'); -- ¡DESBORDADO! ⚠️
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (3, 0.00, '2026-01-08'); -- Recogida urgente
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (3, 0.20, '2026-01-10');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (3, 0.60, '2026-01-15');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (3, 0.90, '2026-01-19');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (3, 0.05, '2026-01-20'); -- Vaciado hoy

-- --- HISTORIAL CONTENEDOR 4 (Retiro) ---
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (4, 0.20, '2026-01-18');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (4, 0.40, '2026-01-19');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (4, 0.60, '2026-01-20');

-- --- HISTORIAL CONTENEDOR 5 (Vitoria) ---
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (5, 0.30, '2026-01-01');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (5, 0.50, '2026-01-10');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (5, 0.85, '2026-01-20');

-- Semana 1 (Partido domingo)
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (6, 0.05, '2026-01-01');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (6, 0.10, '2026-01-03'); -- Sábado
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (6, 0.90, '2026-01-04'); -- DOMINGO PARTIDO ⚽
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (6, 0.00, '2026-01-05'); -- Lunes limpieza

-- Semana 2 (Partido miércoles Copa)
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (6, 0.10, '2026-01-06');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (6, 0.85, '2026-01-07'); -- MIERCOLES COPA ⚽
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (6, 0.05, '2026-01-08'); -- Jueves limpieza

-- Semana 3 (Tranquila)
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (6, 0.10, '2026-01-15');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (6, 0.20, '2026-01-18');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (6, 0.30, '2026-01-20');