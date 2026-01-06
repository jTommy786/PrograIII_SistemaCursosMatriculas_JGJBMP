DROP DATABASE IF EXISTS cursos_db;
CREATE DATABASE cursos_db;
USE cursos_db;

-- =====================================
-- TABLA DOCENTE
-- =====================================
CREATE TABLE docente (
    id_docente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cedula VARCHAR(20) NOT NULL UNIQUE,
    especialidad VARCHAR(100) NOT NULL,
    CONSTRAINT uk_docente_cedula UNIQUE (cedula),
    INDEX idx_docente_cedula (cedula)
);

-- =====================================
-- TABLA ESTUDIANTE
-- =====================================
CREATE TABLE estudiante (
    id_estudiante INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cedula VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    CONSTRAINT uk_estudiante_cedula UNIQUE (cedula),
    CONSTRAINT uk_estudiante_email UNIQUE (email),
    INDEX idx_estudiante_cedula (cedula),
    INDEX idx_estudiante_email (email)
);

-- =====================================
-- TABLA CURSO
-- =====================================
CREATE TABLE curso (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    cupo_maximo INT NOT NULL CHECK (cupo_maximo > 0),
    cupos_disponibles INT NOT NULL CHECK (cupos_disponibles >= 0),
    id_docente INT,
    FOREIGN KEY (id_docente) REFERENCES docente(id_docente) ON DELETE SET NULL,
    INDEX idx_curso_docente (id_docente),
    CONSTRAINT ck_cupos_validos CHECK (cupos_disponibles <= cupo_maximo)
);

-- =====================================
-- TABLA MATRICULA
-- =====================================
CREATE TABLE matricula (
    id_matricula INT AUTO_INCREMENT PRIMARY KEY,
    id_estudiante INT NOT NULL,
    id_curso INT NOT NULL,
    fecha DATE NOT NULL DEFAULT (CURRENT_DATE),
    FOREIGN KEY (id_estudiante) REFERENCES estudiante(id_estudiante) ON DELETE CASCADE,
    FOREIGN KEY (id_curso) REFERENCES curso(id_curso) ON DELETE CASCADE,
    CONSTRAINT uk_matricula_unica UNIQUE (id_estudiante, id_curso),
    INDEX idx_matricula_estudiante (id_estudiante),
    INDEX idx_matricula_curso (id_curso),
    INDEX idx_matricula_fecha (fecha)
);
