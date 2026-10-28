# Sistema de gestión de citas médicas

Aplicación web de gestión de citas médicas con integración con **Google Calendar**. Desarrollada como Trabajo de Fin de Grado del ciclo DAM (Desarrollo de Aplicaciones Multiplataforma).

## Funcionalidades

**Doctores**
- Gestionar su agenda: aceptar o rechazar solicitudes, reprogramar, cancelar y completar citas.
- Consultar y mantener el historial médico de sus pacientes y registrar notas de cada sesión.
- Comunicarse con otros doctores mediante mensajes y recibir notificaciones.
- Sincronizar las citas con su Google Calendar.

**Pacientes**
- Solicitar citas a un doctor y gestionar las propias (consulta, cancelación, calendario).
- Consultar sus notificaciones y editar su perfil.

## Tecnologías

Java 21 · Spring Boot 3.5 (Web, Security, Data JPA, Validation) · Thymeleaf · MySQL · JWT (JJWT) · Google Calendar API · Maven

## Arquitectura

Aplicación monolítica por capas (`controller` → `service` → `repository` → `model`). Expone una API REST bajo `/api/**` y una interfaz web con plantillas Thymeleaf. El acceso se controla por roles (`DOCTOR` y `PATIENT`) con Spring Security, sesión de formulario y autenticación JWT para la API.

## Requisitos

- JDK 21
- MySQL con una base de datos llamada `proyectodam`
- Un cliente OAuth 2.0 en [Google Cloud Console](https://console.cloud.google.com/) con la API de Google Calendar habilitada y `http://localhost:8080/api/auth/google/callback` como URI de redirección autorizada

## Configuración

Los secretos no se incluyen en el repositorio; se leen de variables de entorno:

| Variable | Obligatoria | Descripción |
|---|---|---|
| `JWT_SECRET` | Sí | Clave en Base64 para firmar los JWT (`openssl rand -base64 32`) |
| `GOOGLE_CLIENT_ID` | Sí | ID de cliente OAuth de Google |
| `GOOGLE_CLIENT_SECRET` | Sí | Secreto del cliente OAuth de Google |
| `DB_PASSWORD` | Según tu MySQL | Contraseña del usuario de la base de datos |
| `DB_USERNAME` | No | Usuario de MySQL (por defecto `root`) |
| `DB_URL` | No | URL JDBC (por defecto `localhost/proyectodam`) |
| `GOOGLE_REDIRECT_URI` | No | URI de redirección OAuth (por defecto la de `localhost:8080`) |
| `SEED_DOCTOR_EMAIL`, `SEED_DOCTOR_PASSWORD` | Recomendada | Crean un doctor verificado en el primer arranque (ver [Primer doctor verificado](#primer-doctor-verificado)) |

## Ejecución

```bash
# Linux / macOS
export JWT_SECRET=... GOOGLE_CLIENT_ID=... GOOGLE_CLIENT_SECRET=... DB_PASSWORD=...
./mvnw spring-boot:run
```

```powershell
# Windows (PowerShell)
$env:JWT_SECRET="..."; $env:GOOGLE_CLIENT_ID="..."; $env:GOOGLE_CLIENT_SECRET="..."; $env:DB_PASSWORD="..."
./mvnw spring-boot:run
```

La aplicación queda disponible en <http://localhost:8080>. Las tablas se crean automáticamente (`ddl-auto=update`).

## Primer doctor verificado

Los doctores que se registran quedan pendientes de verificación (`verificado = false`) y no aparecen para los pacientes hasta que un doctor ya verificado los verifica. Solo un doctor verificado puede usar `GET /api/doctor/pending` y `PUT /api/doctor/{id}/verify`.

Para disponer del primer doctor verificado, define `SEED_DOCTOR_EMAIL` y `SEED_DOCTOR_PASSWORD` antes del primer arranque: la aplicación creará esa cuenta ya verificada (puedes retirar las variables después). Para verificar a otros doctores, obtén un token con `POST /api/auth/login` y úsalo como `Authorization: Bearer <token>` en las dos llamadas anteriores.

## Estructura

```
src/main/java/com/mikeldi/demo/
├── config/       Seguridad, JWT y cliente de Google Calendar
├── controller/   API REST y controladores de vistas
├── service/      Lógica de negocio
├── repository/   Acceso a datos (Spring Data JPA)
├── model/        Entidades
└── dto/          Objetos de transferencia
src/main/resources/templates/   Vistas Thymeleaf (auth, doctor, paciente)
```

## Estado y limitaciones conocidas

Proyecto académico. No está pensado para su uso en producción con datos médicos reales sin una revisión adicional de seguridad y cumplimiento normativo (RGPD).

- **Roles en las rutas de paciente:** `SecurityConfig` protege las rutas `/api/patient/**`, `/api/appointments/patient/**` y `/api/requests/patient/**` con `hasRole("PATIENT")`, pero el rol almacenado es `PACIENTE` (`ROLE_PACIENTE`). Esas rutas de la API REST pueden devolver 403 a los pacientes. La corrección prevista es sustituir `"PATIENT"` por `"PACIENTE"` en `SecurityConfig`.
- **Verificación de doctores:** no existe interfaz web; se realiza mediante la API (ver [Primer doctor verificado](#primer-doctor-verificado)).
