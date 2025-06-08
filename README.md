# 🌐 Proyecto Biblioteca Don Bosco - Fase II (Versión Web)

Este repositorio contiene la **Fase II** del sistema de gestión de biblioteca del Colegio Amigos de Don Bosco, donde se migra y amplía la funcionalidad desarrollada en la [Fase I (Escritorio)](https://github.com/Delgadoes/proyecto-fase-1-POO901-.git) hacia una **plataforma web dinámica basada en Java**.

---

## 📋 Descripción General

Este sistema web mejora el control y acceso a los recursos bibliográficos de la institución. Los usuarios ahora pueden consultar el catálogo en línea, realizar préstamos (según privilegios) y administrar ejemplares, todo desde un entorno accesible vía navegador.

---

## 🧩 Módulos Implementados

### 🔐 Módulo de Encargados (Migrado del entorno de escritorio)

- Registro y administración de usuarios con roles (Administrador, Profesor, Alumno)
- Restablecimiento de contraseñas
- Registro de nuevos ejemplares (libros, revistas, tesis, CD, etc.)
- Consultas generales del inventario
- Configuración de límites de préstamos según tipo de usuario
- Gestión de préstamos y devoluciones
- Cálculo automático de mora según días de retraso

---

### 🔍 Módulo de Consultas (Catálogo en Línea)

Este módulo es **de acceso público**, no requiere inicio de sesión.

- Búsqueda por título, autor, tipo de documento o ubicación
- Vista detallada del ejemplar (disponibilidad, ubicación física, cantidad total y prestada)

---

### 📚 Módulo de Préstamos

Disponible solo para usuarios registrados. Las condiciones varían según el tipo de usuario:

- **Validación automática de mora antes de permitir préstamos**
- **Docentes**:
  - Hasta 6 libros simultáneos
  - Plazo de devolución extendido
- **Alumnos**:
  - Hasta 3 libros simultáneos
  - Plazo regular de devolución
- Las configuraciones pueden ser modificadas por los encargados

---

## ⚙️ Tecnologías Utilizadas

- **Lenguaje Backend**: Java EE (Servlets y JSP)
- **Servidor de Aplicaciones**: Apache Tomcat
- **Frontend**: HTML5, CSS3, Bootstrap
- **Base de Datos**: MySQL (modelada y gestionada con MySQL Workbench)
- **IDE recomendado**: Eclipse o NetBeans
- **Control de versiones**: Git + GitHub

---

## 🚀 Instrucciones para Ejecutar el Proyecto

### Requisitos

- Apache Tomcat instalado
- MySQL Server y Workbench
- IDE como Eclipse o NetBeans

### Pasos

1. Clona el repositorio:
2. Importa el proyecto como un proyecto web dinámico en tu IDE.
3. Configura la conexión a la base de datos en los archivos Java/JSP correspondientes.
4. Crea la base de datos ejecutando el archivo biblioteca_db.sql en MySQL Workbench.
5. Inicia Apache Tomcat y despliega la aplicación.
6. Accede desde tu navegador:http://localhost:8080
7. despliega el proyecto 

