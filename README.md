# 🕹️ Proyecto Final – Buscaminas en Java

Proyecto desarrollado como entrega final del curso **Programación I** en la Universidad Técnica Nacional (UTN).  
Implementa el clásico juego de Buscaminas con interfaz gráfica usando **Java Swing** y conexión a base de datos SQL Server para registrar estadísticas de los jugadores.

---

## 📋 Características principales

- **Interfaz gráfica** en Java Swing (NetBeans).
- **Registro de jugadores** en SQL Server.
- **Validación de nombre**: detecta si un jugador ya existe y pregunta si usar o crear uno nuevo.
- **Estadísticas guardadas**: partidas ganadas, perdidas y abandonadas.
- **Marcado de minas con clic derecho** (banderas).
- **Límites configurados**:
  - Tamaño del tablero: mínimo 4x4 y máximo 25x25.
  - Número de minas: mínimo 4 y máximo 89 (o configurable).
- **Confirmación al cambiar ajustes**: si hay partida en curso y cambias el tamaño o número de minas, cuenta como derrota.
- **Opción de reinicio** tras ganar o perder, o volver al menú principal.
- **Base de datos**: manejo mediante JDBC y patrón DAO.

---



---

## ⚙️ Requisitos

- **Java JDK** 8 o superior.
- **NetBeans IDE** (recomendado) o cualquier IDE compatible con proyectos Ant.
- **SQL Server** 2019 o superior.
- Conector JDBC para SQL Server (`mssql-jdbc`).

---

## 🗄️ Configuración de la Base de Datos

Ejecutar en SQL Server:

```sql
CREATE DATABASE BuscaminasDB;
GO

USE BuscaminasDB;
GO

CREATE TABLE jugador (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100) NOT NULL UNIQUE,
    partidas_ganadas INT DEFAULT 0,
    partidas_perdidas INT DEFAULT 0
);
Configurar las credenciales en ConexionBD.java:

private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=BuscaminasDB;encrypt=true;trustServerCertificate=true";
private static final String USUARIO = "tu_usuario_sql";
private static final String CONTRASENA = "tu_contraseña_sql";
```
```
▶️ Ejecución del proyecto
Clonar el repositorio:


git clone https://github.com/usuario/repositorio.git
Abrir el proyecto en NetBeans.

Configurar la conexión a la base de datos en ConexionBD.java.

Compilar y ejecutar desde MenuPrincipal.java o Main.java.
```
```

🏗️ Estructura del proyecto

src/
 └── buscaminas/
      ├── Main/              # Clase principal para iniciar el programa
      ├── modelo/            # Lógica del juego (Tablero, Casilla)
      ├── util/              # Conexión y acceso a la base de datos (ConexionBD, JugadorDAO)
      └── vista/             # Interfaces gráficas (MenuPrincipal, JuegoGUI)
👨‍💻 Autor
-Pamela Espinoza Potoy
-Benjamin Bolaños Alpizar

📜 Licencia
Este proyecto es de uso académico. Puede ser modificado o adaptado para fines educativos.

