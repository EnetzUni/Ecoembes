-- ============================================================
-- 1. INSERTAR PLANTAS (SOLO TECNOLOGÍA PLASSB/REST)
-- ============================================================

-- ID 2: La planta principal que Ecoembes tiene mapeada como "PlasSB"
INSERT INTO recycling_plant (id, name, location) VALUES (2, 'Plant PlasSB - Bilbao (Main)', 'Bilbao');

-- ID 3: Una planta extra para demostrar escalabilidad (Ecoembes podría añadirla a futuro)
INSERT INTO recycling_plant (id, name, location) VALUES (3, 'Plant PlasSB - Vitoria (Expansion)', 'Vitoria');


-- ============================================================
-- 2. DATOS DE CAPACIDAD - PLANTA 2 (BILBAO) - Rango: ~80 tons
-- ============================================================

-- --- NOVIEMBRE 2025 ---
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2025-11-01', 80.5, 2);
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2025-11-05', 83.2, 2);
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2025-11-10', 79.8, 2);
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2025-11-15', 82.4, 2);
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2025-11-20', 81.0, 2);
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2025-11-25', 84.1, 2);
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2025-11-30', 80.0, 2);

-- --- DICIEMBRE 2025 ---
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2025-12-01', 75.5, 2);
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2025-12-05', 78.0, 2);
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2025-12-10', 85.2, 2); -- Pico por Navidad
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2025-12-15', 88.5, 2);
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2025-12-20', 90.0, 2);
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2025-12-24', 60.0, 2); -- Nochebuena baja capacidad
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2025-12-25', 40.0, 2); -- Navidad
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2025-12-30', 82.0, 2);

-- --- ENERO 2026 (IMPORTANTE: Para demos actuales) ---
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2026-01-01', 45.0, 2); -- Año nuevo
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2026-01-05', 80.0, 2);
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2026-01-10', 82.5, 2);
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2026-01-15', 81.3, 2);
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2026-01-20', 83.7, 2);
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2026-01-25', 79.9, 2);
INSERT INTO daily_plant_capacity (date, capacity, plant_id) VALUES ('2026-01-30', 81.1, 2);

-- --- FEBRERO 2026 (Futuro inmediato