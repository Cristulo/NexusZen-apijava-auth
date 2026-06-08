-- Roles base para NexusZen (Idempotente)
MERGE INTO roles (id, name, description) KEY (id) VALUES 
('11111111-1111-1111-1111-111111111111', 'ROLE_USER', 'Usuario estándar de la plataforma'),
('22222222-2222-2222-2222-222222222222', 'ROLE_ADMIN', 'Administrador de sistema'),
('33333333-3333-3333-3333-333333333333', 'ROLE_SUPERADMIN', 'Super Administrador global'),
('44444444-4444-4444-4444-444444444444', 'ROLE_DOCENTE', 'Rol para profesores y personal académico'),
('55555555-5555-5555-5555-555555555555', 'ROLE_ALUMNO', 'Rol para estudiantes registrados');

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
('44444444-4444-4444-4444-444444444444', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
('55555555-5555-5555-5555-555555555555', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa');

-- Parametros de Preferencia base
MERGE INTO parametros_preferencia (id, nombre, descripcion) KEY (id) VALUES
('10000000-0000-0000-0000-000000000001', 'tema_oscuro', 'Preferencia de tema oscuro para la interfaz (Booleano)'),
('10000000-0000-0000-0000-000000000002', 'notificaciones_email', 'Recibir notificaciones por correo electrónico (Booleano)'),
('10000000-0000-0000-0000-000000000003', 'idioma_preferido', 'Idioma preferido de la interfaz (Cadena)'),
('10000000-0000-0000-0000-000000000004', 'acceso_directo', 'Acceso directo de navegación en la navbar (Cadena)');
