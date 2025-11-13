# Web Service Java - JAX-RS

Proyecto base para crear servicios REST utilizando las librerías estándar JAX-RS (Jakarta RESTful Web Services).

## Tecnologías Utilizadas

- **Java 11**
- **JAX-RS 3.1** (Jakarta RESTful Web Services)
- **Jersey 3.1.3** (Implementación de referencia de JAX-RS)
- **Maven** (Gestión de dependencias y construcción)
- **Jackson** (Serialización/Deserialización JSON)

## Estructura del Proyecto

```
web-service-java/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── rest/
│       │               ├── JaxRsApplication.java    # Clase de aplicación JAX-RS
│       │               └── HelloResource.java       # Ejemplo de recurso REST
│       ├── resources/                               # Recursos de configuración
│       └── webapp/
│           └── WEB-INF/
│               └── web.xml                          # Configuración del servlet
├── pom.xml                                          # Configuración Maven
└── README.md                                        # Este archivo
```

## Configuración

### Requisitos Previos

- Java 11 o superior
- Maven 3.6 o superior

### Instalación de Maven

#### Linux

##### Arch Linux / Manjaro
```bash
sudo pacman -S maven
```

##### Ubuntu / Debian
```bash
sudo apt update
sudo apt install maven
```

##### Fedora / RHEL / CentOS
```bash
sudo dnf install maven
# O para versiones más antiguas:
sudo yum install maven
```

##### Verificación de instalación
Después de instalar, verifica que Maven esté correctamente instalado:
```bash
mvn --version
```

Deberías ver algo como:
```
Apache Maven 3.x.x
Maven home: /usr/share/maven
Java version: 11.x.x, vendor: ...
```

#### Windows

##### Opción 1: Usando Chocolatey (Recomendado)
Si tienes Chocolatey instalado:
```powershell
choco install maven
```

##### Opción 2: Instalación Manual

1. **Descargar Maven:**
   - Visita: https://maven.apache.org/download.cgi
   - Descarga el archivo `apache-maven-x.x.x-bin.zip` (versión más reciente)

2. **Extraer el archivo:**
   - Extrae el contenido en una ubicación como `C:\Program Files\Apache\maven`

3. **Configurar Variables de Entorno:**
   - Abre "Variables de entorno" desde el Panel de Control
   - Crea una nueva variable de sistema llamada `MAVEN_HOME` con el valor: `C:\Program Files\Apache\maven`
   - Edita la variable `Path` y agrega: `%MAVEN_HOME%\bin`

4. **Verificar instalación:**
   Abre una nueva terminal (PowerShell o CMD) y ejecuta:
   ```cmd
   mvn --version
   ```

##### Opción 3: Usando Scoop
Si tienes Scoop instalado:
```powershell
scoop install maven
```

### Instalación del Proyecto

1. Clonar o descargar el proyecto
2. Compilar el proyecto:
   ```bash
   mvn clean compile
   ```

3. Empaquetar como WAR:
   ```bash
   mvn clean package
   ```

## Ejecutar el Servicio

### Opción 1: Usando Jetty Embebido (Recomendado para Desarrollo)

El proyecto incluye el plugin de Jetty Maven que permite ejecutar el servicio directamente sin necesidad de un servidor externo.

**Ejecutar el servicio:**
```bash
mvn jetty:run
```

El servicio estará disponible en: `http://localhost:8081/api/hello`

**Detener el servicio:**
Presiona `Ctrl+C` en la terminal donde está ejecutándose.

**Notas:**
- El plugin está configurado para recargar automáticamente los cambios cada 10 segundos (hot reload).
- El puerto por defecto es **8081**. Si necesitas cambiarlo, edita el archivo `pom.xml` en la sección del plugin `jetty-maven-plugin` y modifica el valor de `<port>`.
- Si el puerto está en uso, verás un error. Puedes cambiar el puerto o detener el proceso que lo está usando con: `kill <PID>` (donde PID es el número del proceso).

### Opción 2: Desplegar en un Servidor de Aplicaciones

#### Tomcat

1. Descargar e instalar Apache Tomcat
2. Compilar y empaquetar el proyecto:
   ```bash
   mvn clean package
   ```
3. Copiar el archivo `target/web-service-java-1.0-SNAPSHOT.war` al directorio `webapps` de Tomcat
4. Renombrar el archivo a `web-service-java.war` (opcional, para cambiar el contexto)
5. Iniciar Tomcat
6. Acceder a los endpoints en: `http://localhost:8080/web-service-java/api/hello` (o el puerto configurado en tu servidor)

#### Otros Servidores

El archivo WAR generado puede desplegarse en cualquier servidor de aplicaciones compatible con Jakarta EE 9+:
- WildFly
- Payara
- GlassFish
- Jetty (standalone)

### Probar los Endpoints

Una vez que el servicio esté ejecutándose, puedes probar los endpoints usando:

**cURL:**
```bash
# GET simple
curl http://localhost:8081/api/hello

# GET con parámetro
curl http://localhost:8081/api/hello/Juan

# POST
curl -X POST http://localhost:8081/api/hello \
  -H "Content-Type: application/json" \
  -d '{"message": "Mi mensaje"}'

# PUT
curl -X PUT http://localhost:8081/api/hello/123 \
  -H "Content-Type: application/json" \
  -d '{"message": "Mensaje actualizado"}'

# DELETE
curl -X DELETE http://localhost:8081/api/hello/123
```

**Navegador:**
Simplemente abre: `http://localhost:8081/api/hello` en tu navegador para probar el endpoint GET.

**Postman / Insomnia:**
Importa los endpoints y prueba las diferentes operaciones HTTP.

## Endpoints de Ejemplo

El proyecto incluye un recurso de ejemplo (`HelloResource`) con los siguientes endpoints:

### GET /api/hello
Retorna un mensaje de saludo simple.

**Respuesta:**
```json
{
  "message": "Hola desde JAX-RS!"
}
```

### GET /api/hello/{name}
Retorna un saludo personalizado.

**Ejemplo:** `GET /api/hello/Juan`

**Respuesta:**
```json
{
  "message": "Hola, Juan!"
}
```

### POST /api/hello
Crea un nuevo mensaje.

**Body:**
```json
{
  "message": "Mi mensaje"
}
```

**Respuesta:**
```json
{
  "message": "Mensaje recibido: Mi mensaje"
}
```

### PUT /api/hello/{id}
Actualiza un mensaje por ID.

**Ejemplo:** `PUT /api/hello/123`

**Body:**
```json
{
  "message": "Mensaje actualizado"
}
```

### DELETE /api/hello/{id}
Elimina un mensaje por ID.

**Ejemplo:** `DELETE /api/hello/123`

## Recurso Usuario

El proyecto incluye un recurso completo de gestión de usuarios (`UsuarioResource`) con todas las operaciones CRUD y almacenamiento en memoria.

### Estructura del Usuario

Un usuario tiene los siguientes campos:
- `id` (Long): Identificador único (generado automáticamente)
- `nombre` (String): Nombre del usuario (obligatorio)
- `apellido` (String): Apellido del usuario
- `email` (String): Email del usuario (obligatorio, único)
- `edad` (Integer): Edad del usuario
- `telefono` (String): Número de teléfono
- `direccion` (String): Dirección del usuario

### Endpoints Disponibles

#### GET /api/usuarios
Obtiene todos los usuarios. Soporta filtrado por nombre mediante query parameter.

**Query Parameters:**
- `nombre` (opcional): Filtra usuarios por nombre (búsqueda parcial, case-insensitive)

**Ejemplos:**
```bash
# Obtener todos los usuarios
curl http://localhost:8081/api/usuarios

# Buscar usuarios por nombre
curl http://localhost:8081/api/usuarios?nombre=Juan
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan.perez@example.com",
    "edad": 30,
    "telefono": "+1234567890",
    "direccion": "Calle Principal 123"
  },
  {
    "id": 2,
    "nombre": "María",
    "apellido": "González",
    "email": "maria.gonzalez@example.com",
    "edad": 25,
    "telefono": "+0987654321",
    "direccion": "Avenida Central 456"
  }
]
```

#### GET /api/usuarios/{id}
Obtiene un usuario específico por su ID.

**Ejemplo:** `GET /api/usuarios/1`

**Respuesta:**
```json
{
  "id": 1,
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez@example.com",
  "edad": 30,
  "telefono": "+1234567890",
  "direccion": "Calle Principal 123"
}
```

**Error (404):**
```json
{
  "error": "Usuario no encontrado con ID: 999"
}
```

#### POST /api/usuarios
Crea un nuevo usuario.

**Body:**
```json
{
  "nombre": "Pedro",
  "apellido": "Martínez",
  "email": "pedro.martinez@example.com",
  "edad": 28,
  "telefono": "+5566778899",
  "direccion": "Calle Secundaria 789"
}
```

**Respuesta (201 Created):**
```json
{
  "id": 4,
  "nombre": "Pedro",
  "apellido": "Martínez",
  "email": "pedro.martinez@example.com",
  "edad": 28,
  "telefono": "+5566778899",
  "direccion": "Calle Secundaria 789"
}
```

**Error (400):**
```json
{
  "error": "El nombre es obligatorio"
}
```

o

```json
{
  "error": "Ya existe un usuario con el email: pedro.martinez@example.com"
}
```

#### PUT /api/usuarios/{id}
Actualiza un usuario existente.

**Ejemplo:** `PUT /api/usuarios/1`

**Body:**
```json
{
  "nombre": "Juan Carlos",
  "apellido": "Pérez",
  "email": "juan.perez@example.com",
  "edad": 31,
  "telefono": "+1234567890",
  "direccion": "Calle Principal 123, Piso 2"
}
```

**Respuesta:**
```json
{
  "id": 1,
  "nombre": "Juan Carlos",
  "apellido": "Pérez",
  "email": "juan.perez@example.com",
  "edad": 31,
  "telefono": "+1234567890",
  "direccion": "Calle Principal 123, Piso 2"
}
```

#### DELETE /api/usuarios/{id}
Elimina un usuario por ID.

**Ejemplo:** `DELETE /api/usuarios/1`

**Respuesta:**
```json
{
  "message": "Usuario eliminado correctamente"
}
```

**Error (404):**
```json
{
  "error": "Usuario no encontrado con ID: 999"
}
```

#### GET /api/usuarios/contar
Obtiene el número total de usuarios almacenados.

**Ejemplo:** `GET /api/usuarios/contar`

**Respuesta:**
```json
{
  "total": 3
}
```

### Características del Recurso Usuario

- **Almacenamiento en memoria**: Los datos se guardan en memoria usando `ConcurrentHashMap` para thread-safety
- **Datos de ejemplo**: El repositorio se inicializa automáticamente con 3 usuarios de ejemplo
- **Validaciones**: 
  - Email único (no se permiten duplicados)
  - Campos obligatorios: nombre y email
- **Búsqueda**: Soporta búsqueda por nombre (parcial, case-insensitive)
- **Thread-safe**: Utiliza estructuras de datos concurrentes para soportar múltiples peticiones simultáneas

### Ejemplos de Uso Completo

```bash
# 1. Obtener todos los usuarios
curl http://localhost:8081/api/usuarios

# 2. Buscar usuarios por nombre
curl http://localhost:8081/api/usuarios?nombre=Juan

# 3. Obtener un usuario específico
curl http://localhost:8081/api/usuarios/1

# 4. Crear un nuevo usuario
curl -X POST http://localhost:8081/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Ana",
    "apellido": "López",
    "email": "ana.lopez@example.com",
    "edad": 27,
    "telefono": "+9988776655",
    "direccion": "Avenida Norte 321"
  }'

# 5. Actualizar un usuario
curl -X PUT http://localhost:8081/api/usuarios/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Carlos",
    "apellido": "Pérez",
    "email": "juan.perez@example.com",
    "edad": 31,
    "telefono": "+1234567890",
    "direccion": "Calle Principal 123"
  }'

# 6. Eliminar un usuario
curl -X DELETE http://localhost:8081/api/usuarios/1

# 7. Contar usuarios
curl http://localhost:8081/api/usuarios/contar
```

## Recurso Amistades

El proyecto incluye un sistema de gestión de amistades entre usuarios, permitiendo que los usuarios se agreguen como amigos.

### Endpoints Disponibles

#### POST /api/amistades
Crea una nueva relación de amistad entre dos usuarios.

**Body:**
```json
{
  "usuarioId1": 1,
  "usuarioId2": 2
}
```

**Respuesta (201 Created):**
```json
{
  "id": 1,
  "usuarioId1": 1,
  "usuarioId2": 2,
  "fechaAmistad": "2025-11-13 11:15:30"
}
```

**Errores posibles:**
- `400`: Un usuario no puede ser amigo de sí mismo
- `400`: Los usuarios ya son amigos
- `400`: Usuario no encontrado

#### GET /api/amistades/usuario/{usuarioId}
Obtiene todos los amigos de un usuario (objetos Usuario completos).

**Ejemplo:** `GET /api/amistades/usuario/1`

**Respuesta:**
```json
[
  {
    "id": 2,
    "nombre": "María",
    "apellido": "González",
    "email": "maria.gonzalez@example.com",
    "edad": 25,
    "telefono": "+0987654321",
    "direccion": "Avenida Central 456"
  }
]
```

#### GET /api/amistades/verificar/{usuarioId1}/{usuarioId2}
Verifica si dos usuarios son amigos.

**Ejemplo:** `GET /api/amistades/verificar/1/2`

**Respuesta:**
```json
{
  "sonAmigos": true
}
```

#### DELETE /api/amistades/{usuarioId1}/{usuarioId2}
Elimina una relación de amistad entre dos usuarios.

**Ejemplo:** `DELETE /api/amistades/1/2`

**Respuesta:**
```json
{
  "message": "Amistad eliminada correctamente"
}
```

#### GET /api/amistades
Obtiene todas las relaciones de amistad en el sistema.

## Recurso Publicaciones

El proyecto incluye un sistema completo de publicaciones donde los usuarios pueden crear, actualizar y eliminar publicaciones, así como dar likes.

### Estructura de la Publicación

- `id` (Long): Identificador único (generado automáticamente)
- `usuarioId` (Long): ID del usuario que creó la publicación (obligatorio)
- `contenido` (String): Contenido de la publicación (obligatorio)
- `fechaCreacion` (String): Fecha y hora de creación (generada automáticamente)
- `likes` (Integer): Número de likes (inicia en 0)

### Endpoints Disponibles

#### GET /api/publicaciones
Obtiene todas las publicaciones del sistema.

**Respuesta:**
```json
[
  {
    "id": 1,
    "usuarioId": 1,
    "contenido": "¡Hola mundo! Esta es mi primera publicación.",
    "fechaCreacion": "2025-11-13 11:20:00",
    "likes": 5
  }
]
```

#### GET /api/publicaciones/{id}
Obtiene una publicación específica por ID.

**Ejemplo:** `GET /api/publicaciones/1`

#### GET /api/publicaciones/usuario/{usuarioId}
Obtiene todas las publicaciones de un usuario específico (ordenadas por fecha, más recientes primero).

**Ejemplo:** `GET /api/publicaciones/usuario/1`

#### GET /api/publicaciones/amigos/{usuarioId}
Obtiene las publicaciones de los amigos de un usuario (útil para un feed de noticias).

**Ejemplo:** `GET /api/publicaciones/amigos/1`

#### POST /api/publicaciones
Crea una nueva publicación.

**Body:**
```json
{
  "usuarioId": 1,
  "contenido": "¡Hola mundo! Esta es mi primera publicación."
}
```

**Respuesta (201 Created):**
```json
{
  "id": 1,
  "usuarioId": 1,
  "contenido": "¡Hola mundo! Esta es mi primera publicación.",
  "fechaCreacion": "2025-11-13 11:20:00",
  "likes": 0
}
```

#### PUT /api/publicaciones/{id}
Actualiza una publicación existente (solo el contenido puede ser modificado).

**Ejemplo:** `PUT /api/publicaciones/1`

**Body:**
```json
{
  "contenido": "Contenido actualizado de la publicación"
}
```

#### DELETE /api/publicaciones/{id}
Elimina una publicación y todos sus comentarios asociados.

**Ejemplo:** `DELETE /api/publicaciones/1`

**Respuesta:**
```json
{
  "message": "Publicación eliminada correctamente"
}
```

#### POST /api/publicaciones/{id}/like
Incrementa los likes de una publicación.

**Ejemplo:** `POST /api/publicaciones/1/like`

**Respuesta:**
```json
{
  "id": 1,
  "usuarioId": 1,
  "contenido": "¡Hola mundo!",
  "fechaCreacion": "2025-11-13 11:20:00",
  "likes": 6
}
```

#### DELETE /api/publicaciones/{id}/like
Decrementa los likes de una publicación.

**Ejemplo:** `DELETE /api/publicaciones/1/like`

## Recurso Comentarios

El proyecto incluye un sistema de comentarios para las publicaciones, permitiendo que los usuarios comenten en las publicaciones.

### Estructura del Comentario

- `id` (Long): Identificador único (generado automáticamente)
- `publicacionId` (Long): ID de la publicación (obligatorio)
- `usuarioId` (Long): ID del usuario que hizo el comentario (obligatorio)
- `contenido` (String): Contenido del comentario (obligatorio)
- `fechaCreacion` (String): Fecha y hora de creación (generada automáticamente)

### Endpoints Disponibles

#### GET /api/comentarios/publicacion/{publicacionId}
Obtiene todos los comentarios de una publicación (ordenados por fecha, más antiguos primero).

**Ejemplo:** `GET /api/comentarios/publicacion/1`

**Respuesta:**
```json
[
  {
    "id": 1,
    "publicacionId": 1,
    "usuarioId": 2,
    "contenido": "¡Excelente publicación!",
    "fechaCreacion": "2025-11-13 11:25:00"
  },
  {
    "id": 2,
    "publicacionId": 1,
    "usuarioId": 3,
    "contenido": "Me encanta esto.",
    "fechaCreacion": "2025-11-13 11:30:00"
  }
]
```

#### GET /api/comentarios/{id}
Obtiene un comentario específico por ID.

**Ejemplo:** `GET /api/comentarios/1`

#### GET /api/comentarios/usuario/{usuarioId}
Obtiene todos los comentarios realizados por un usuario.

**Ejemplo:** `GET /api/comentarios/usuario/1`

#### POST /api/comentarios
Crea un nuevo comentario en una publicación.

**Body:**
```json
{
  "publicacionId": 1,
  "usuarioId": 2,
  "contenido": "¡Excelente publicación!"
}
```

**Respuesta (201 Created):**
```json
{
  "id": 1,
  "publicacionId": 1,
  "usuarioId": 2,
  "contenido": "¡Excelente publicación!",
  "fechaCreacion": "2025-11-13 11:25:00"
}
```

#### PUT /api/comentarios/{id}
Actualiza un comentario existente (solo el contenido puede ser modificado).

**Ejemplo:** `PUT /api/comentarios/1`

**Body:**
```json
{
  "contenido": "Comentario actualizado"
}
```

#### DELETE /api/comentarios/{id}
Elimina un comentario.

**Ejemplo:** `DELETE /api/comentarios/1`

**Respuesta:**
```json
{
  "message": "Comentario eliminado correctamente"
}
```

### Ejemplos de Uso Completo - Red Social

```bash
# 1. Crear usuarios
curl -X POST http://localhost:8081/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Juan", "apellido": "Pérez", "email": "juan@example.com"}'

curl -X POST http://localhost:8081/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre": "María", "apellido": "González", "email": "maria@example.com"}'

# 2. Hacer que sean amigos
curl -X POST http://localhost:8081/api/amistades \
  -H "Content-Type: application/json" \
  -d '{"usuarioId1": 1, "usuarioId2": 2}'

# 3. Verificar si son amigos
curl http://localhost:8081/api/amistades/verificar/1/2

# 4. Obtener amigos de un usuario
curl http://localhost:8081/api/amistades/usuario/1

# 5. Crear una publicación
curl -X POST http://localhost:8081/api/publicaciones \
  -H "Content-Type: application/json" \
  -d '{"usuarioId": 1, "contenido": "¡Hola mundo! Esta es mi primera publicación."}'

# 6. Obtener publicaciones de un usuario
curl http://localhost:8081/api/publicaciones/usuario/1

# 7. Obtener publicaciones de amigos (feed)
curl http://localhost:8081/api/publicaciones/amigos/2

# 8. Dar like a una publicación
curl -X POST http://localhost:8081/api/publicaciones/1/like

# 9. Comentar en una publicación
curl -X POST http://localhost:8081/api/comentarios \
  -H "Content-Type: application/json" \
  -d '{"publicacionId": 1, "usuarioId": 2, "contenido": "¡Excelente publicación!"}'

# 10. Obtener comentarios de una publicación
curl http://localhost:8081/api/comentarios/publicacion/1

# 11. Actualizar un comentario
curl -X PUT http://localhost:8081/api/comentarios/1 \
  -H "Content-Type: application/json" \
  -d '{"contenido": "Comentario actualizado"}'

# 12. Eliminar un comentario
curl -X DELETE http://localhost:8081/api/comentarios/1

# 13. Eliminar una publicación (también elimina sus comentarios)
curl -X DELETE http://localhost:8081/api/publicaciones/1

# 14. Eliminar amistad
curl -X DELETE http://localhost:8081/api/amistades/1/2
```

### Características del Sistema de Red Social

- **Relaciones bidireccionales**: Las amistades funcionan en ambas direcciones
- **Validaciones**: No se pueden crear amistades duplicadas ni con uno mismo
- **Feed de amigos**: Endpoint especial para ver publicaciones de amigos
- **Likes**: Sistema de likes para publicaciones
- **Cascada**: Al eliminar una publicación, se eliminan todos sus comentarios
- **Ordenamiento**: Publicaciones ordenadas por fecha (más recientes primero), comentarios por fecha (más antiguos primero)
- **Thread-safe**: Todos los repositorios usan estructuras de datos concurrentes

## Despliegue

Para información detallada sobre cómo ejecutar y desplegar el servicio, consulta la sección [Ejecutar el Servicio](#ejecutar-el-servicio) más arriba.

El plugin de Jetty ya está configurado en el `pom.xml`, por lo que puedes ejecutar el servicio directamente con `mvn jetty:run`.

## Crear Nuevos Endpoints

Para crear un nuevo endpoint REST:

1. Crear una nueva clase en el paquete `com.example.rest`
2. Anotarla con `@Path("/tu-path")`
3. Agregar métodos anotados con `@GET`, `@POST`, `@PUT`, `@DELETE`, etc.
4. Registrar la clase en `JaxRsApplication.getClasses()`

**Ejemplo:**

```java
package com.example.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @GET
    public Response getUsers() {
        // Tu lógica aquí
        return Response.ok().build();
    }
}
```

Luego registrar en `JaxRsApplication`:

```java
classes.add(UserResource.class);
```

## Notas

- Todos los endpoints están bajo el path base `/api` definido en `JaxRsApplication`
- Los recursos están configurados para usar JSON como formato de intercambio
- El proyecto usa Jakarta EE 9+ (anteriormente Java EE)

## Licencia

Este proyecto es de código abierto y está disponible para uso libre.

