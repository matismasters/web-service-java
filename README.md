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

