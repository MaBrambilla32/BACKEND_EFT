# Banco XYZ - Microservicios

## Descripción
Proyecto desarrollado con Java 17 y Spring Boot para implementar una arquitectura de microservicios orientada a la gestión bancaria.

## Servicios

| Servicio | Puerto | Función |
|---|---:|---|
| Config Server | 8888 | Centraliza la configuración |
| Eureka Server | 8761 | Registro y descubrimiento de servicios |
| Banco Service | 8080 | Gestiona operaciones bancarias |
| Cuenta Service | 8082 | Gestiona cuentas y recibe mensajes JMS |
| Transacción Service | 8083 | Gestiona transacciones |

## Tecnologías utilizadas
- Java 17 y Spring Boot
- Spring Cloud Config y Eureka
- Spring Security y JWT
- Spring Batch
- Resilience4j
- ActiveMQ y JMS
- Base de datos H2
- Docker y Docker Compose

## Seguridad
Spring Security protege los endpoints mediante autenticación con tokens JWT. Las solicitudes a los endpoints protegidos requieren un token válido.

## Procesamiento por lotes
Spring Batch permite importar movimientos financieros, intereses y estados anuales desde archivos CSV. Los procesos registran la cantidad de elementos leídos, escritos y omitidos.

## Mensajería y resiliencia
Banco Service utiliza JMS para enviar eventos a ActiveMQ mediante la cola `cola.transacciones.banco`. Resilience4j incorpora un Circuit Breaker y un método de respaldo para manejar fallos en el envío de mensajes.

## Ejecución local
Cada microservicio cuenta con Maven Wrapper. Para iniciar un servicio, ingresar a su carpeta desde PowerShell y ejecutar:

```powershell
.\mvnw.cmd spring-boot:run