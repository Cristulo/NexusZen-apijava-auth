-- Roles base para NexusZen (Idempotente)
MERGE INTO roles (id, name, description) KEY (id) VALUES 
('11111111-1111-1111-1111-111111111111', 'ROLE_USER', 'Usuario estándar de la plataforma'),
('22222222-2222-2222-2222-222222222222', 'ROLE_ADMIN', 'Administrador de sistema'),
('33333333-3333-3333-3333-333333333333', 'ROLE_SUPERADMIN', 'Super Administrador global');

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
-- Superadmin tiene permiso de lectura
('33333333-3333-3333-3333-333333333333', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa');

-- Parametros de Preferencia base
MERGE INTO parametros_preferencia (id, nombre, descripcion, categoria) KEY (id) VALUES
('10000000-0000-0000-0000-000000000001', 'tema_oscuro', 'Preferencia de tema oscuro para la interfaz (Booleano)', 'FRONTEND'),
('10000000-0000-0000-0000-000000000002', 'notificaciones_email', 'Recibir notificaciones por correo electrónico (Booleano)', 'NOTIFICACIONES'),
('10000000-0000-0000-0000-000000000003', 'idioma_preferido', 'Idioma preferido de la interfaz (Cadena)', 'FRONTEND'),
('10000000-0000-0000-0000-000000000004', 'acceso_directo', 'Acceso directo de navegación en la navbar (Cadena)', 'FRONTEND'),
('10000000-0000-0000-0000-000000000005', 'rol_activo', 'Rol activo del usuario en la plataforma (Cadena)', 'SISTEMA');

-- Crear usuario cristulo con todos los roles (Contraseña local: cristulo)
MERGE INTO usuarios (id, usuario, username, password_hash, estado) KEY (id) VALUES
('99999999-9999-9999-9999-999999999999', 'cristulo', 'Cristian', '$2a$10$vD2.h9S6P1D8h9u9sFq1.OGt57B4O9Gpeq76pQy4UoW8uWJmpxcKq', 'ACTIVO');

-- Vincular roles autorizados a cristulo (USER, ADMIN, SUPERADMIN)
MERGE INTO usuario_roles (usuario_id, rol_id) KEY (usuario_id, rol_id) VALUES
('99999999-9999-9999-9999-999999999999', '11111111-1111-1111-1111-111111111111'),
('99999999-9999-9999-9999-999999999999', '22222222-2222-2222-2222-222222222222'),
('99999999-9999-9999-9999-999999999999', '33333333-3333-3333-3333-333333333333');

-- Vincular email a cristulo
MERGE INTO usuario_emails (id, usuario_id, email, tipo, categoria, verified) KEY (id) VALUES
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '99999999-9999-9999-9999-999999999999', 'cristulox@gmail.com', 'PRIMARY', 'PERSONAL', true);
