CREATE DATABASE "Granja"

/*SEGURIDAD*/
CREATE TABLE seg_usuario (
  idusuario SERIAL PRIMARY KEY,
  idempresa INTEGER NOT NULL,
  idperfil INTEGER NOT NULL,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  estado CHAR(1) NOT NULL DEFAULT 'A',
  fchcrea TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  usrcrea VARCHAR(50) NOT NULL,
  canalcrea VARCHAR(20),
  fchmod TIMESTAMP,
  usrmod VARCHAR(50),
  canalmod VARCHAR(20),
  flgeli CHAR(1) NOT NULL DEFAULT '0'
);

CREATE TABLE seg_perfil (
  idperfil SERIAL PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  abreviatura VARCHAR(20),
  estado CHAR(1) NOT NULL DEFAULT 'A',
  fchcrea TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  usrcrea VARCHAR(50) NOT NULL
);

CREATE TABLE seg_opciones (
  idopciones SERIAL PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  icono VARCHAR(50),
  url VARCHAR(200),
  parent INTEGER,
  nivel INTEGER NOT NULL,
  orden INTEGER NOT NULL,
  estado CHAR(1) NOT NULL DEFAULT 'A'
);

CREATE TABLE seg_perfilopciones (
  id_perfilopciones SERIAL PRIMARY KEY,
  idperfil INTEGER NOT NULL,
  idopciones INTEGER NOT NULL,
  acceso CHAR(1) NOT NULL DEFAULT '1',
  UNIQUE(idperfil, idopciones)
);

CREATE TABLE seg_bitacora (
  idbitacora BIGSERIAL PRIMARY KEY,
  idusuario INTEGER NOT NULL,
  evento VARCHAR(255) NOT NULL,
  tabla VARCHAR(100),
  registro_id INTEGER,
  ip_address VARCHAR(45),
  user_agent VARCHAR(255),
  fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

 /*PARÁMETROS*/
CREATE TABLE adm_empresa (
  idempresa SERIAL PRIMARY KEY,
  ruc VARCHAR(11) NOT NULL UNIQUE,
  razonsocial VARCHAR(200) NOT NULL,
  direccion VARCHAR(255),
  telefono VARCHAR(15),
  celular VARCHAR(15),
  email VARCHAR(100),
  representante VARCHAR(100),
  logo VARCHAR(255),
  estado CHAR(1) NOT NULL DEFAULT 'A',
  fchcrea TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  usrcrea VARCHAR(50) NOT NULL,
  canalcrea VARCHAR(20),
  fchmod TIMESTAMP,
  usrmod VARCHAR(50),
  canalmod VARCHAR(20),
  flgeli CHAR(1) NOT NULL DEFAULT '0'
);

CREATE TABLE param_tabla (
  codtabla VARCHAR(10) PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  descripcion VARCHAR(255),
  estado CHAR(1) NOT NULL DEFAULT 'A'
);

CREATE TABLE param_tabladetalle (
  codtabladet VARCHAR(10) NOT NULL,
  codtabla VARCHAR(10) NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  adicional1 VARCHAR(100),
  adicional2 VARCHAR(100),
  adicional3 VARCHAR(100),
  adicional4 VARCHAR(100),
  orden INTEGER,
  estado CHAR(1) NOT NULL DEFAULT 'A',
  PRIMARY KEY (codtabladet, codtabla)
);

 /*INFRAESTRUCTURA*/
CREATE TABLE adm_granjas (
  idgranja SERIAL PRIMARY KEY,
  idempresa INTEGER NOT NULL,
  codigo VARCHAR(20) NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  direccion VARCHAR(255),
  capacidad INTEGER,
  responsable VARCHAR(100),
  telefono VARCHAR(15),
  estado CHAR(1) NOT NULL DEFAULT 'A',
  fchcrea TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  usrcrea VARCHAR(50) NOT NULL,
  fchmod TIMESTAMP,
  usrmod VARCHAR(50),
  flgeli CHAR(1) NOT NULL DEFAULT '0',
  UNIQUE(codigo, idempresa)
);

CREATE TABLE adm_cliente (
  idcliente SERIAL PRIMARY KEY,
  idempresa INTEGER NOT NULL,
  codigo VARCHAR(20) NOT NULL,
  nombre VARCHAR(200) NOT NULL,
  abreviatura VARCHAR(50),
  tipo_documento VARCHAR(10),
  documento VARCHAR(15),
  direccion VARCHAR(255),
  telefono VARCHAR(15),
  email VARCHAR(100),
  es_cliente CHAR(1) NOT NULL DEFAULT '1',
  contacto VARCHAR(100),
  credito_dias INTEGER,
  limite_credito DECIMAL(12,2),
  estado CHAR(1) NOT NULL DEFAULT 'A',
  fchcrea TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  usrcrea VARCHAR(50) NOT NULL,
  fchmod TIMESTAMP,
  usrmod VARCHAR(50),
  flgeli CHAR(1) NOT NULL DEFAULT '0',
  UNIQUE(codigo, idempresa)
);

CREATE TABLE adm_producto (
  idproducto SERIAL PRIMARY KEY,
  idempresa INTEGER NOT NULL,
  codigo VARCHAR(20) NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  abreviatura VARCHAR(20),
  descripcion_comercial VARCHAR(150),
  descripcion_corta VARCHAR(50),
  unidad_medida VARCHAR(10) NOT NULL DEFAULT 'KG',
  peso_minimo DECIMAL(8,3),
  peso_maximo DECIMAL(8,3),
  casilleros INTEGER NOT NULL,
  tara DECIMAL(8,3) NOT NULL,
  decimales INTEGER NOT NULL DEFAULT 3,
  unidades_paquete INTEGER,
  ruta_etiqueta VARCHAR(255),
  precio_sugerido DECIMAL(10,2),
  estado CHAR(1) NOT NULL DEFAULT 'A',
  fchcrea TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  usrcrea VARCHAR(50) NOT NULL,
  fchmod TIMESTAMP,
  usrmod VARCHAR(50),
  flgeli CHAR(1) NOT NULL DEFAULT '0',
  UNIQUE(codigo, idempresa)
);

 /*TRANSACCIONES*/
CREATE TABLE trx_pedido (
  idpedido SERIAL PRIMARY KEY,
  idempresa INTEGER NOT NULL,
  numero_pedido VARCHAR(20) NOT NULL,
  fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_pedido DATE NOT NULL,
  idgranja INTEGER NOT NULL,
  idproducto INTEGER NOT NULL,
  idcliente INTEGER NOT NULL,
  precio_huevo DECIMAL(10,2),
  imprimir_etiqueta CHAR(1) NOT NULL DEFAULT '1',
  total_paquetes INTEGER NOT NULL,
  total_kilos DECIMAL(12,3) NOT NULL,
  peso_promedio DECIMAL(8,3),
  total_casilleros INTEGER,
  observaciones TEXT,
  estado VARCHAR(20) NOT NULL DEFAULT 'REGISTRADO',
  fchcrea TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  usrcrea VARCHAR(50) NOT NULL,
  fchmod TIMESTAMP,
  usrmod VARCHAR(50),
  flgeli CHAR(1) NOT NULL DEFAULT '0',
  UNIQUE(numero_pedido, idempresa)
);

CREATE TABLE trx_pedidodetalle (
  idpedidodetalle SERIAL PRIMARY KEY,
  idpedido INTEGER NOT NULL,
  idproducto INTEGER NOT NULL,
  item INTEGER NOT NULL,
  peso_bruto DECIMAL(8,3) NOT NULL,
  peso_tara DECIMAL(8,3) NOT NULL,
  peso_neto DECIMAL(8,3) NOT NULL,
  casilleros INTEGER NOT NULL,
  codigo_etiqueta VARCHAR(50),
  disponible CHAR(1) NOT NULL DEFAULT '1',
  observaciones VARCHAR(255),
  fchcrea TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  usrcrea VARCHAR(50) NOT NULL,
  UNIQUE(idpedido, item)
);

CREATE TABLE trx_despacho (
  iddespacho SERIAL PRIMARY KEY,
  idempresa INTEGER NOT NULL,
  numero_packing VARCHAR(20) NOT NULL,
  fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_despacho DATE NOT NULL,
  idcliente INTEGER NOT NULL,
  tipo_salida VARCHAR(10) NOT NULL DEFAULT 'VENTA',
  total_paquetes INTEGER NOT NULL,
  total_kilos DECIMAL(12,3) NOT NULL,
  peso_promedio DECIMAL(8,3),
  precio_venta DECIMAL(10,2),
  total_importe DECIMAL(12,2),
  total_casilleros INTEGER,
  chofer VARCHAR(100),
  placa_vehiculo VARCHAR(15),
  guia_remision VARCHAR(20),
  observaciones TEXT,
  estado VARCHAR(20) NOT NULL DEFAULT 'REGISTRADO',
  fchcrea TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  usrcrea VARCHAR(50) NOT NULL,
  fchmod TIMESTAMP,
  usrmod VARCHAR(50),
  flgeli CHAR(1) NOT NULL DEFAULT '0',
  UNIQUE(numero_packing, idempresa)
);

CREATE TABLE trx_despachopedido (
  iddespachopedido SERIAL PRIMARY KEY,
  iddespacho INTEGER NOT NULL,
  idpedido INTEGER NOT NULL,
  total_paquetes INTEGER NOT NULL,
  total_kilos DECIMAL(12,3) NOT NULL,
  fchcrea TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(iddespacho, idpedido)
);

CREATE TABLE trx_despachodetalle (
  iddespachodetalle SERIAL PRIMARY KEY,
  iddespachopedido INTEGER NOT NULL,
  idpedidodetalle INTEGER NOT NULL,
  idproducto INTEGER NOT NULL,
  item_despacho INTEGER NOT NULL,
  peso_neto DECIMAL(8,3) NOT NULL,
  casilleros INTEGER NOT NULL,
  precio_unitario DECIMAL(10,2),
  importe DECIMAL(10,2),
  fchcrea TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO seg_usuario (idempresa, idperfil, username, password, nombre, estado, usrcrea, fchcrea)
VALUES (1, 1, 'admin', '$2a$10$slYQmyNdGzin7olVN3p5Be6SAi9XlMJXlqfGm9lN7g5vDj3R3H1Je', 'Administrador Sistema', 'A', 'admin', NOW());
SELECT * FROM seg_usuario WHERE username = 'admin';