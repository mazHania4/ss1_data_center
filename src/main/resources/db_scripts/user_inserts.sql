-- La contraseña para todos los usuarios: pass1234

--Usuarios para pruebas de desarrollo
INSERT INTO app_user(username, role, email, phone_number, name, lastname, status, password, mfa_activated, created_at) VALUES
    ('admin_yenni', 'ADMIN', 'yenniferdeleon202231084@cunoc.edu.gt', '25143684', 'Yennifer', 'de León', 'ACTIVE', '$2b$12$byDpe5HAWxL3bXJMGtpnxuTl.NCIyv3OWtCLylSkgmPy7c8cUPDDO', false, NOW()),
    ('journalist_yenni', 'JOURNALIST', 'yenniferdeleon202231084@cunoc.edu.gt', '14257896', 'Yennifer', 'de León', 'ACTIVE', '$2b$12$byDpe5HAWxL3bXJMGtpnxuTl.NCIyv3OWtCLylSkgmPy7c8cUPDDO', false, NOW()),
    ('client_yenni', 'CLIENT', 'yenniferdeleon202231084@cunoc.edu.gt', '46231578', 'Yennifer', 'de León', 'ACTIVE', '$2b$12$byDpe5HAWxL3bXJMGtpnxuTl.NCIyv3OWtCLylSkgmPy7c8cUPDDO', false, NOW()),

    ('admin_hania', 'ADMIN', 'haniamazariegos202031953@cunoc.edu.gt', '43685792', 'Hania', 'Mazariegos', 'ACTIVE', '$2b$12$byDpe5HAWxL3bXJMGtpnxuTl.NCIyv3OWtCLylSkgmPy7c8cUPDDO', false, NOW()),
    ('journalist_hania', 'JOURNALIST', 'haniamazariegos202031953@cunoc.edu.gt', '34258671', 'Hania', 'Mazariegos', 'ACTIVE', '$2b$12$byDpe5HAWxL3bXJMGtpnxuTl.NCIyv3OWtCLylSkgmPy7c8cUPDDO', false, NOW()),
    ('client_hania', 'CLIENT', 'haniamazariegos202031953@cunoc.edu.gt', '27586914', 'Hania', 'Mazariegos', 'ACTIVE', '$2b$12$byDpe5HAWxL3bXJMGtpnxuTl.NCIyv3OWtCLylSkgmPy7c8cUPDDO', false, NOW());


-- USUARIOS ADMINISTRADORES (ADMIN)
INSERT INTO app_user (username, role, email, phone_number, name, status, password, mfa_activated, created_at) VALUES
    ('admin_main', 'ADMIN', 'admin.main@system.gt', '+50210001000', 'Ricardo', 'Morales', 'ACTIVE', '$2a$12$ERLcu0Jy9I77YuDEc3I.qeZT7RXuAwzoHpIYNeLynFQOQAyrSEzWi', TRUE, NOW()),
    ('super.admin', 'ADMIN', 'sper.user@system.gt', '+50210001001', 'Elena', 'Castillo', 'ACTIVE', '$2a$12$ERLcu0Jy9I77YuDEc3I.qeZT7RXuAwzoHpIYNeLynFQOQAyrSEzWi', TRUE, NOW()),
    ('tech_admin', 'ADMIN', 'tech.adm@system.gt', '+50210001002', 'Javier', 'Soto', 'ACTIVE', '$2a$12$ERLcu0Jy9I77YuDEc3I.qeZT7RXuAwzoHpIYNeLynFQOQAyrSEzWi', FALSE, NOW()),

-- USUARIOS CLIENTES (CLIENT)
    ('maria_g', 'CLIENT', 'maria.garcia@mail.com', '+50220002000', 'María', 'García', 'ACTIVE', '$2a$12$ERLcu0Jy9I77YuDEc3I.qeZT7RXuAwzoHpIYNeLynFQOQAyrSEzWi', FALSE, NOW()),
    ('jperez', 'CLIENT', 'juan.perez@mail.com', '+50220002001', 'Juan', 'Pérez', 'ACTIVE', '$2a$12$ERLcu0Jy9I77YuDEc3I.qeZT7RXuAwzoHpIYNeLynFQOQAyrSEzWi', TRUE, NOW()),
    ('acastro', 'CLIENT', 'ana.castro@mail.com', '+50220002002', 'Ana', 'Castro', 'ACTIVE', '$2a$12$ERLcu0Jy9I77YuDEc3I.qeZT7RXuAwzoHpIYNeLynFQOQAyrSEzWi', FALSE, NOW()),

-- USUARIOS Periodistas (JOURNALIST)
    ('journalist_a', 'JOURNALIST', 'journal_a@news.gt', '+50230003000', 'News', 'Guate', 'ACTIVE', '$2a$12$ERLcu0Jy9I77YuDEc3I.qeZT7RXuAwzoHpIYNeLynFQOQAyrSEzWi', TRUE, NOW()),
    ('journalist_b', 'JOURNALIST', 'periodista_b@uh.gt', '+50230003001', 'Periodista', 'Guate', 'ACTIVE', '$2a$12$ERLcu0Jy9I77YuDEc3I.qeZT7RXuAwzoHpIYNeLynFQOQAyrSEzWi', FALSE, NOW()),
    ('journalist_c', 'JOURNALIST', 'c_jrl@journal.gt', '+50230003002', 'Journal', 'Guate', 'ACTIVE', '$2a$12$ERLcu0Jy9I77YuDEc3I.qeZT7RXuAwzoHpIYNeLynFQOQAyrSEzWi', TRUE, NOW());

