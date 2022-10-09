# Información del Proyecto Desafio TENPO
___________________

API correspondiente al desafío TENPO el cual consiste en registro de usuario, login de usuario, operación de suma de 2 números, historial de consumo de servicios y 
consumo de servicio Mock Externo, el cual retornara valor correspondiente al porcentaje. 

Esta API corre sobre Docker, mediante Docker Compose.

## Entorno:

* **Java 11**
* **SpringBoot 2.7.4**
* **Maven 3.8.6** o superior
* **BD PostgreSQL 14** o superior
* **Docker 20.10.7** o superior
* **Postman 10.0.20** o superior
* **DBeaver 22.2.1** o superior
* **IntelliJ 2022.2.3 CE** o superior

### Endpoint externo de apoyo

API que retorna valor (porcentaje)

URI: https://run.mocky.io/v3/66c206ab-f14a-4fb9-82c3-3e598bc3d030

TIPO: GET

JSON Response Body: ``{"porcentaje":70"}``

HTTP Status Esperado: ``Status 200 OK``


## Archivos de configuración

* Dockerfile
* docker-compose.yml
* application.properties

## Desplegar API

**NOTA: Docker debe estar en ejecución**

**Abrir una terminal / consola y dirigirse al directorio raiz del proyecto.**

### Compilar y generar package

``$ mvn clean package ``

### Ejecutar con Docker Compose

Iniciar por primera vez API mediante Docker Compose:

``$ docker-compose up``

Ejecutar desde segunda vez en adelante:

``$ docker-compose start``

**NOTA: En caso de que hayan borrado los contenedores / imagenes, volver a iniciar utilizando ``docker-compose up``**

Detener contenedores:

``$ docker-compose stop``

Eliminar contenedores:

``$ docker-compose down``


## Comprobar Funcionamiento API

La API se encontrará en ejecución en **http://localhost:8080/api** . Para verificar funcionamiento, se recomienda utilizar Postman.

Dentro de Postman, se debe importar el Collection adjunto, ubicado en el directorio raíz del proyecto.

Nombre Archivo (Collection Postman): **Tenpo.postman_collection.json**

**NOTA: Dentro de cada servicio del Collection, se encuentran disponible diferentes ejemplos.**

### API:

1. #### Registro de Usuario

    URI: http://localhost:8080/api/login/signup

    Tipo: POST

    Seguridad: Sin autenticacion

    JSON Request Body:

   ``{
   "nombre": "Nicolas",
   "apellido": "Aranda",
   "email": "nicolas.aranda@gmail.com",
   "clave": "welcome1"
   }``

    HTTP Status esperado: 

    ``Status 201 Created``

1. #### Login de Usuario

    URI: http://localhost:8080/api/login/

    Tipo: POST

    Seguridad: Sin autenticacion

    JSON Request Body:

    ``{
   "email": "nicolas.aranda@gmail.com",
   "clave": "welcome1"
   }``

    JSON Response Body

    ``{
   "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuaWNvbGFzLmFyYW5kYUBnbWFpbC5jb20iLCJleHAiOjE2NjUyNjAwNzQsImlhdCI6MTY2NTI0MjA3NH0.R90uHGm7-u7VWcw6-zwSpGoXpSO8YzHE1nQ20BqLfVi6QrE7AVTqnmA9up-QH7O-WyKPTSPNCAtmHXqNQCaH7w"
   }``

   HTTP Status esperado:

    ``Status 200 OK``

1. #### Operacion Suma

    URI: http://localhost:8080/api/operacion/suma

    Tipo: POST

    Seguridad: Header Authorization , Bearer Token JWT. Token obtenido desde respuesta de servicio Login

    JSON Request Body:

   ``{
   "numero1":10,
   "numero2":30
   }``

   JSON Response Body:

   ``{
   "resultado": 68
   }``

   HTTP Status esperado

   ``Status 200 OK``

1. #### Historico

   URI: http://localhost:8080/api/historial

   Tipo GET

   Seguridad: Header Authorization, Bearer Token JWT. Token obtenido desde respuesta de servicio Login

   JSON Response Body

   ``
   {
   "totalPagina": 1,
   "pagina": 0,
   "totalElementos": 4,
   "historial": [
   {
   "id": 4,
   "fechaRegistro": "08-10-2022 18:49:11",
   "usuario": "nicolas.aranda@gmail.com",
   "method": "POST",
   "status": 200,
   "url": "/api/operacion/suma",
   "resultado": "{\"resultado\":68.0}"
   },
   {
   "id": 3,
   "fechaRegistro": "08-10-2022 18:49:11",
   "usuario": "nicolas.aranda@gmail.com",
   "method": "CACHE",
   "status": 200,
   "url": "https://run.mocky.io/v3/66c206ab-f14a-4fb9-82c3-3e598bc3d030",
   "resultado": "{\n  \"porcentaje\" : 70\n}"
   },
   {
   "id": 2,
   "fechaRegistro": "08-10-2022 18:46:34",
   "usuario": "anonimo",
   "method": "POST",
   "status": 200,
   "url": "/api/login/",
   "resultado": "{\"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuaWNvbGFzLmFyYW5kYUBnbWFpbC5jb20iLCJleHAiOjE2NjUyODM1OTMsImlhdCI6MTY2NTI2NTU5M30.zGjdaDokA7iOgDT1KnJSOAGpw7EvDymsUGsDov_xYu7Pj8mtob2yb9y-cntvhjZZVCSK3aAvyWIfoUmOIcjywQ\"}"
   },
   {
   "id": 1,
   "fechaRegistro": "08-10-2022 18:46:28",
   "usuario": "anonimo",
   "method": "POST",
   "status": 201,
   "url": "/api/login/signup",
   "resultado": ""
   }
   ]
   }``

   HTTP Status esperado

   ``Status 200 OK``

## Credenciales BD

Para la verificación de BD, se recomienda utilizar cliente DBeaver

   
   HOST: localhost

   PUERTO: 5432

   USUARIO: postgres

   CLAVE: welcome1

   NOMBRE BD: postgres
