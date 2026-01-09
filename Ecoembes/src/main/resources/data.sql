-- 1. Insertar EMPLEADOS (Usuarios)
INSERT INTO employee (name, email, password) VALUES ('Alice', 'alice@example.com', 'pass123');
INSERT INTO employee (name, email, password) VALUES ('Bob', 'bob@example.com', 'pass456');

-- 2. Insertar CONTENEDORES (Dumpsters)
INSERT INTO dumpster (location, max_capacity) VALUES ('Calle Mayor, Madrid', 500.0);
INSERT INTO dumpster (location, max_capacity) VALUES ('Passeig de Gràcia, Barcelona', 400.0);
INSERT INTO dumpster (location, max_capacity) VALUES ('Gran Vía, Bilbao', 600.0);

-- 3. Insertar REGISTROS DE LLENADO (FillLevelRecord)
-- Nota: Las fechas van como texto 'YYYY-MM-DD'
INSERT INTO fill_level_record (date, fill_level, dumpster_id) VALUES ('2025-11-01', 120.0, 1);
INSERT INTO fill_level_record (date, fill_level, dumpster_id) VALUES ('2025-11-02', 200.0, 1);
INSERT INTO fill_level_record (date, fill_level, dumpster_id) VALUES ('2025-11-03', 300.0, 1);

INSERT INTO fill_level_record (date, fill_level, dumpster_id) VALUES ('2025-11-01', 100.0, 2);
INSERT INTO fill_level_record (date, fill_level, dumpster_id) VALUES ('2025-11-02', 150.0, 2);

-- 4. NUEVO: Insertar PLANTA DE RECICLAJE (RecyclingPlant)
-- Esto faltaba en tu archivo original y es necesario para las asignaciones
INSERT INTO recycling_plant (name, location) VALUES ('EcoPlanta Norte', 'Polígono Industrial, Bilbao');
INSERT INTO recycling_plant (name, location) VALUES ('Planta Sur', 'Polígono Ventorro, Madrid');

-- 5. NUEVO: Insertar ASIGNACIONES (Assignment) - ESTRUCTURA 1:N
-- Ahora vinculamos directamente un empleado, un contenedor y una planta
-- employee_id=1 (Alice), dumpster_id=1 (Madrid), recycling_plant_id=2 (Madrid)
INSERT INTO assignment (date, employee_id, dumpster_id, recycling_plant_id) 
VALUES ('2025-11-05', 1, 1, 2);

-- employee_id=2 (Bob), dumpster_id=3 (Bilbao), recycling_plant_id=1 (Bilbao)
INSERT INTO assignment (date, employee_id, dumpster_id, recycling_plant_id) 
VALUES ('2025-11-06', 2, 3, 1);