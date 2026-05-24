-- Roles base para NexusZen (Idempotente)
MERGE INTO roles (id, name, description) KEY (id) VALUES 
('11111111-1111-1111-1111-111111111111', 'ROLE_USER', 'Usuario estándar de la plataforma'),
('22222222-2222-2222-2222-222222222222', 'ROLE_ADMIN', 'Administrador global del sistema'),
('33333333-3333-3333-3333-333333333333', 'ROLE_DOCENTE', 'Rol para profesores y personal académico'),
('44444444-4444-4444-4444-444444444444', 'ROLE_ALUMNO', 'Rol para estudiantes registrados');

-- Permisos base para NexusZen (Idempotente)
MERGE INTO permisos (id, name, description) KEY (id) VALUES 
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'READ_PRIVILEGES', 'Permiso para leer recursos del sistema'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'WRITE_PRIVILEGES', 'Permiso para crear y modificar recursos'),
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'DELETE_PRIVILEGES', 'Permiso para eliminar recursos');

-- Relaciones de Rol y Permisos (Idempotente)
-- Admin tiene todos los permisos
MERGE INTO rol_permisos (rol_id, permiso_id) KEY (rol_id, permiso_id) VALUES 
('22222222-2222-2222-2222-222222222222', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
('22222222-2222-2222-2222-222222222222', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
('22222222-2222-2222-2222-222222222222', 'cccccccc-cccc-cccc-cccc-cccccccccccc'),
-- Docente y Alumno tienen permiso de lectura
('33333333-3333-3333-3333-333333333333', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
('44444444-4444-4444-4444-444444444444', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa');
