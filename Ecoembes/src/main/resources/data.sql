-- ============================================================
-- DATA.SQL PARA ECOEMBES (CENTRAL SERVER)
-- ============================================================

-- 1. PLANTAS DE RECICLAJE (El Directorio)
-- IMPORTANTE: Los IDs deben coincidir con lo que esperan tus Factories
-- ID 1 -> Sockets (ContSocket)
-- ID 2 -> REST (PlasSB)
INSERT INTO recycling_plant (id, name, location) VALUES (1, 'ContSocket Ltd.', 'Madrid - Sockets');
INSERT INTO recycling_plant (id, name, location) VALUES (2, 'PlasSB Ltd.', 'Bilbao - REST API');

-- 2. EMPLEADOS (ADAPTADO A TU CONSTRUCTOR)
-- Columnas: ID, NAME, EMAIL, PASSWORD
-- Nota: Aunque el constructor no tenga ID, la tabla en BBDD sí lo necesita.
INSERT INTO employee (id, name, email, password) VALUES (1, 'Admin General', 'admin@ecoembes.com', 'admin123');
INSERT INTO employee (id, name, email, password) VALUES (2, 'Pepe Recicla', 'pepe@ecoembes.com', 'pepe123');

-- 3. CONTENEDORES (DUMPSTERS)
-- CORRECCIÓN: Usamos 'max_capacity' en vez de 'capacity'.
-- CORRECCIÓN: Quitamos 'fill_level' porque tu clase Dumpster NO tiene ese campo persistente (lo tienes en fill_level_record).
INSERT INTO dumpster (id, location, max_capacity) VALUES (101, 'Calle Gran Vía 1', 1000.0);
INSERT INTO dumpster (id, location, max_capacity) VALUES (102, 'Plaza Mayor', 800.0);
INSERT INTO dumpster (id, location, max_capacity) VALUES (103, 'Paseo Castellana 50', 1200.0);
INSERT INTO dumpster (id, location, max_capacity) VALUES (104, 'Barrio Salamanca', 1000.0);
INSERT INTO dumpster (id, location, max_capacity) VALUES (105, 'Puerta del Sol', 1500.0);

-- 4. REGISTROS DE LLENADO (OPCIONAL - Para que no salgan vacíos)
-- Si quieres que tengan datos, insertamos en la tabla histórica que Hibernate creó.
-- Asumo columnas: id, fill_level, date, dumpster_id
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (101, 0.85, '2026-01-20');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (102, 0.95, '2026-01-20');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (103, 0.20, '2026-01-20');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (104, 0.45, '2026-01-20');
INSERT INTO fill_level_record (dumpster_id, fill_level, date) VALUES (105, 0.99, '2026-01-20');