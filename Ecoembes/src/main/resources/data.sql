-- Insert employees
INSERT INTO employee (name, email, password) VALUES ('Alice', 'alice@example.com', 'pass123');
INSERT INTO employee (name, email, password) VALUES ('Bob', 'bob@example.com', 'pass456');

-- Insert dumpsters
INSERT INTO dumpster (location, max_capacity) VALUES ('Calle Mayor, Madrid', 500.0);
INSERT INTO dumpster (location, max_capacity) VALUES ('Passeig de Gràcia, Barcelona', 400.0);

-- Insert fill level records for Dumpster 1 (id=1)
INSERT INTO fill_level_record (date, fill_level, dumpster_id) VALUES ('2025-11-01', 120.0, 1);
INSERT INTO fill_level_record (date, fill_level, dumpster_id) VALUES ('2025-11-02', 200.0, 1);
INSERT INTO fill_level_record (date, fill_level, dumpster_id) VALUES ('2025-11-03', 300.0, 1);

-- Insert fill level records for Dumpster 2 (id=2)
INSERT INTO fill_level_record (date, fill_level, dumpster_id) VALUES ('2025-11-01', 100.0, 2);
INSERT INTO fill_level_record (date, fill_level, dumpster_id) VALUES ('2025-11-02', 150.0, 2);
INSERT INTO fill_level_record (date, fill_level, dumpster_id) VALUES ('2025-11-03', 250.0, 2);

-- Insert assignments
INSERT INTO assignment (date, employee_id) VALUES ('2025-11-01 09:00:00', 1);
INSERT INTO assignment (date, employee_id) VALUES ('2025-11-02 10:00:00', 2);

-- Link dumpsters to assignments (many-to-many)
INSERT INTO assignment_dumpster (assignment_id, dumpster_id) VALUES (1, 1);
INSERT INTO assignment_dumpster (assignment_id, dumpster_id) VALUES (2, 2);
