CREATE TABLE pacientes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    telefone VARCHAR(20),
    data_nascimento DATE,
    ativo BOOLEAN NOT NULL,
    CONSTRAINT pk_pacientes PRIMARY KEY (id)
);

CREATE TABLE medicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    crm VARCHAR(20) NOT NULL,
    especialidade VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    telefone VARCHAR(20),
    ativo BOOLEAN NOT NULL,
    CONSTRAINT pk_medicos PRIMARY KEY (id)
);

CREATE TABLE consultas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    paciente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,
    data_hora DATETIME NOT NULL,
    motivo_cancelamento VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    CONSTRAINT pk_consultas PRIMARY KEY (id),
    CONSTRAINT fk_consultas_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_consultas_medico FOREIGN KEY (medico_id) REFERENCES medicos(id)
);