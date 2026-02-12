
CREATE DATABASE challenge;



CREATE TABLE clientes (
    cliente_id      VARCHAR(50) PRIMARY KEY,
    nombre          VARCHAR(255) NOT NULL,
    genero          VARCHAR(50) NOT NULL,
    edad            INTEGER NOT NULL CHECK (edad >= 0),
    identificacion  VARCHAR(255) NOT NULL,
    direccion       VARCHAR(255) NOT NULL,
    telefono        VARCHAR(50) NOT NULL,
    contrasena      VARCHAR(255) NOT NULL,
    estado          BOOLEAN NOT NULL
);



CREATE TABLE cuentas (
    numero_cuenta   VARCHAR(50) PRIMARY KEY,
    tipo_cuenta     VARCHAR(50) NOT NULL,
    saldo_inicial   NUMERIC(18,2) NOT NULL,
    estado          BOOLEAN NOT NULL,
    cliente_id      VARCHAR(50) NOT NULL,
    CONSTRAINT fk_cuenta_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes(cliente_id)
        ON DELETE CASCADE
);



CREATE TABLE movimientos (
    id              BIGSERIAL PRIMARY KEY,
    fecha           DATE NOT NULL,
    tipo_movimiento VARCHAR(50) NOT NULL,
    valor           NUMERIC(18,2) NOT NULL,
    saldo           NUMERIC(18,2) NOT NULL,
    numero_cuenta   VARCHAR(50) NOT NULL,
    CONSTRAINT fk_movimiento_cuenta
        FOREIGN KEY (numero_cuenta)
        REFERENCES cuentas(numero_cuenta)
        ON DELETE CASCADE
);




CREATE INDEX idx_movimientos_cuenta ON movimientos(numero_cuenta);
CREATE INDEX idx_movimientos_fecha ON movimientos(fecha);
