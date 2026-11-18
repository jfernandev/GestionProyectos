# GestionProyectos

## Sistema de gestión de proyectos, proveedores, piezas y suministros

## Instalación y puesta en marcha

### Requisitos

- JDK 21.
- Git.
- Docker Desktop con Docker Compose.
- IntelliJ IDEA, recomendado para ejecutar la aplicación.

### 1. Clonar y abrir el proyecto

Clona el repositorio y entra en la carpeta interior del proyecto. Esa es la carpeta que contiene `pom.xml`, `compose.yaml` y `src`:

```bash
git clone https://github.com/jfernandev/GestionProyectos.git
cd GestionProyectos/GestionProyectos
```

### 2. Configurar y arrancar MariaDB

En esa carpeta, junto a `compose.yaml`, crea `.env` si todavía no existe; si ya existe, conserva sus valores y comprueba que estén definidas ambas variables. Sustituye los valores de ejemplo por contraseñas propias:

```dotenv
DB_ROOT_PASSWORD=CAMBIAR_POR_UNA_CLAVE_SEGURA
DB_PASSWORD=CAMBIAR_POR_UNA_CLAVE_SEGURA
```

No subas `.env` al repositorio ni compartas sus contraseñas. El archivo está excluido por `.gitignore`.

Desde esa misma carpeta, inicia la base de datos:

```bash
docker compose up -d
```

La configuración del proyecto publica MariaDB en `localhost:33060` y crea la base de datos `gestion_proyectos` con el usuario `gestion_user`. En IntelliJ, abre esta carpeta interior e importa el proyecto desde su `pom.xml`; espera a que Maven descargue las dependencias.

MariaDB utiliza un volumen persistente: las contraseñas del `.env` inicializan la base de datos al crearla por primera vez. Si el volumen ya existe, utiliza las credenciales que ya están configuradas en esa base de datos; cambiar `.env` no cambia por sí solo las contraseñas existentes.

### 3. Ejecutar las pruebas (opcional)

En IntelliJ, abre la ventana **Maven**, despliega **Lifecycle** y ejecuta `test`. También puedes ejecutar `mvn test` desde una terminal si Maven está instalado y disponible.

### 4. Configurar y ejecutar la aplicación

El `.env` configura Docker Compose, pero Java no lo carga automáticamente. En IntelliJ, abre **Run → Edit Configurations**, selecciona o crea la configuración para `App` y añade `DB_PASSWORD` en **Environment variables**, usando el mismo valor del `.env`.

Ejecuta `src/main/java/App.java`. Cuando aparezca el menú de acceso, crea el administrador inicial si la base de datos todavía no tiene usuarios e inicia sesión. No existe una contraseña predeterminada.

La conexión configurada es `jdbc:mariadb://localhost:33060/gestion_proyectos`. Para detener MariaDB sin borrar los datos, ejecuta `docker compose down` desde la carpeta que contiene `compose.yaml`.

## 1. Introducción

**GestionProyectos** es una aplicación de consola desarrollada en Java que permite gestionar la información relacionada con proyectos, proveedores, piezas y las operaciones de suministro realizadas entre ellos.

La aplicación está diseñada para facilitar el control de los materiales suministrados a diferentes proyectos, permitiendo registrar operaciones, consultar información, realizar búsquedas avanzadas, obtener estadísticas y exportar los resultados a archivos CSV.

El sistema utiliza Hibernate como herramienta de persistencia para trabajar con una base de datos MariaDB. Su arquitectura está organizada en diferentes capas para separar las responsabilidades y facilitar el mantenimiento y la ampliación del código.

Además, incorpora un sistema de autenticación con distintos niveles de permisos, almacenamiento seguro de contraseñas, registro de eventos y pruebas unitarias automatizadas.

### 1.1. Objetivos del proyecto

Los principales objetivos de GestionProyectos son:

- Gestionar proveedores que suministran materiales.
- Mantener un catálogo de piezas y sus precios.
- Administrar los proyectos registrados en el sistema.
- Registrar las cantidades de piezas suministradas a cada proyecto.
- Consultar y filtrar las operaciones de suministro.
- Calcular estadísticas relacionadas con los suministros y sus costes.
- Exportar información para su posterior análisis.
- Proteger las operaciones mediante un sistema de autenticación y permisos.
- Garantizar la calidad del software mediante pruebas automatizadas.

## 2. Tecnologías utilizadas

El proyecto utiliza las siguientes tecnologías y herramientas:

| Tecnología | Utilización |
|---|---|
| Java 21 | Lenguaje de programación |
| Maven | Gestión de dependencias y construcción del proyecto |
| Hibernate ORM 6.6.36.Final | Persistencia y mapeo objeto-relacional |
| Jakarta Persistence 3.1.0 | Anotaciones y especificaciones JPA |
| MariaDB | Sistema gestor de bases de datos |
| MariaDB JDBC Driver 3.5.6 | Conexión entre Java y MariaDB |
| JUnit Jupiter 5.12.2 | Pruebas unitarias |
| Mockito 5.15.2 | Simulación de dependencias en las pruebas |
| jBCrypt 0.4 | Hash seguro de contraseñas |
| SLF4J | API de registro de eventos |
| Logback 1.5.18 | Implementación del sistema de logging |
| Docker Desktop | Ejecución del servidor MariaDB |
| IntelliJ IDEA | Entorno de desarrollo |

## 3. Arquitectura de la aplicación

La aplicación utiliza una arquitectura organizada por capas. Cada paquete tiene una responsabilidad concreta y se comunica con los demás mediante interfaces y clases de servicio.

Esta separación permite mantener el código organizado, facilita las pruebas y evita concentrar toda la lógica de la aplicación en una única clase.

### 3.1. Estructura de paquetes

La estructura principal del proyecto es la siguiente:

```text
GestionProyectos/
│
├── pom.xml
├── compose.yaml
├── README.md
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── dao/
│   │   │   ├── menu/
│   │   │   ├── modelo/
│   │   │   ├── servicios/
│   │   │   ├── utilidades/
│   │   │   └── App.java
│   │   │
│   │   └── resources/
│   │       ├── hibernate.cfg.xml
│   │       └── logback.xml
│   │
│   └── test/
│       └── java/
│           └── servicios/
│
├── exportaciones/
│
└── .gitignore
```

Este árbol muestra la carpeta interior `GestionProyectos` del repositorio clonado, que es la raíz del proyecto Maven.

El archivo `.env` se crea localmente para las contraseñas y no se incluye en el repositorio.

### 3.2. Paquete modelo

Contiene las entidades que representan los objetos principales del sistema.

Las entidades utilizan anotaciones de Jakarta Persistence para definir su correspondencia con las tablas de la base de datos.

Las principales clases son:

- **Proveedor:** representa a una empresa o persona que suministra piezas.
- **Pieza:** representa un material o componente que puede utilizarse en los proyectos.
- **Proyecto:** representa un proyecto al que se suministran materiales.
- **Gestion:** representa una operación de suministro de una determinada cantidad de piezas a un proyecto, realizada por un proveedor en una fecha concreta.
- **Usuario:** representa a las personas que pueden acceder a la aplicación.

Las entidades incluyen sus identificadores, atributos y relaciones necesarias para mantener la integridad de los datos.

### 3.3. Paquete dao

Contiene las interfaces DAO y sus implementaciones.

El patrón DAO (Data Access Object) permite separar el acceso a la base de datos de la lógica de negocio.

Entre sus responsabilidades se encuentran:

- Guardar registros.
- Consultar entidades por identificador o código.
- Obtener listados.
- Actualizar información.
- Eliminar registros.
- Ejecutar consultas específicas y agregaciones.

Las operaciones de persistencia se realizan mediante Hibernate.

### 3.4. Paquete servicios

Contiene la lógica de negocio de la aplicación.

Los servicios reciben las peticiones de los menús, realizan las validaciones necesarias y utilizan los DAO para acceder a los datos.

Entre las principales clases se encuentran:

- **ProveedorServicio:** gestiona las operaciones relacionadas con proveedores.
- **PiezaServicio:** gestiona las piezas.
- **ProyectoServicio:** gestiona los proyectos.
- **GestionServicio:** controla las operaciones de suministro y las búsquedas relacionadas.
- **UsuarioServicio:** gestiona el registro y la autenticación de usuarios.
- **ExportacionCSVServicio:** coordina las exportaciones de información.

Esta capa también comprueba los permisos necesarios para realizar operaciones de modificación.

### 3.5. Paquete menu

Contiene la interfaz de usuario basada en consola.

Los menús permiten interactuar con la aplicación mediante opciones numéricas y entradas de texto.

Entre sus principales clases se encuentran:

- **MenuAcceso:** permite iniciar sesión, crear el administrador inicial y salir.
- **MenuPrincipal:** muestra las opciones disponibles según el rol del usuario.
- **MenuProveedores:** gestiona las operaciones con proveedores.
- **MenuPiezas:** gestiona las piezas.
- **MenuProyectos:** gestiona los proyectos.
- **MenuGestiones:** permite registrar y consultar suministros.
- **MenuEstadisticas:** presenta las estadísticas y permite exportar información.

La interfaz se mantiene separada de la lógica de negocio para evitar que las operaciones principales dependan directamente de la entrada y salida por consola.

### 3.6. Paquete utilidades

Contiene clases auxiliares utilizadas en diferentes partes de la aplicación.

Entre ellas destacan:

- **HibernateUtil:** centraliza la configuración y gestión de la `SessionFactory` de Hibernate.
- **Entrada:** facilita la lectura y validación de datos introducidos por teclado.
- **SesionUsuario:** mantiene la información del usuario autenticado y permite comprobar sus permisos.
- **ExportadorCSV:** proporciona las operaciones necesarias para generar archivos CSV.

## 4. Modelo de datos

La base de datos utilizada se denomina `gestion_proyectos`.

Las principales tablas corresponden a las entidades del sistema:

- `proveedores`
- `piezas`
- `proyectos`
- `gestiones`
- `usuarios`

### 4.1. Relaciones entre entidades

Una gestión relaciona tres elementos principales:

- Un proveedor.
- Una pieza.
- Un proyecto.

Además, almacena información propia de la operación, como la fecha y la cantidad suministrada.

De esta forma, una misma pieza puede suministrarse a distintos proyectos, un proveedor puede participar en diferentes operaciones y un proyecto puede recibir materiales de distintos proveedores.

La tabla `gestiones` contiene las referencias a las entidades relacionadas mediante sus respectivos códigos.

### 4.2. Persistencia con Hibernate

Hibernate se encarga de convertir los objetos Java en registros de la base de datos y viceversa.

La configuración se centraliza en `hibernate.cfg.xml`.

Se utiliza la opción de actualización automática del esquema (`hbm2ddl.auto=update`) para adaptar la estructura de las tablas a los mapeos de las entidades cuando corresponde.

La clase `HibernateUtil` proporciona el acceso centralizado a la configuración de Hibernate.

## 5. Funcionalidades de la aplicación

### 5.1. Autenticación y acceso

La aplicación incorpora un sistema de autenticación para controlar quién puede acceder a sus funcionalidades.

Al iniciar la aplicación se muestra el menú de acceso.

Las opciones principales son:

- Iniciar sesión.
- Crear el administrador inicial, cuando todavía no existen usuarios registrados.
- Salir de la aplicación.

Para iniciar sesión se solicita un nombre de usuario y una contraseña.

El sistema comprueba las credenciales y verifica que la cuenta esté activa.

Las contraseñas no se almacenan directamente en la base de datos, sino mediante hashes generados con BCrypt.

### 5.2. Gestión de proveedores

Permite administrar los proveedores que participan en el suministro de materiales.

Las operaciones disponibles incluyen:

- Registrar un proveedor.
- Consultar un proveedor por su código.
- Buscar proveedores por nombre.
- Listar todos los proveedores.
- Actualizar sus datos.
- Eliminar proveedores cuando las condiciones de integridad lo permiten.

El sistema comprueba los datos introducidos y evita registrar códigos duplicados.

### 5.3. Gestión de piezas

Permite mantener un catálogo de materiales y componentes.

Las operaciones incluyen:

- Registrar piezas.
- Consultar piezas por código.
- Listar todas las piezas.
- Actualizar información.
- Eliminar piezas cuando no existen restricciones por relaciones con otras operaciones.

Cada pieza contiene información identificativa, una descripción y un precio.

Se aplican validaciones para evitar precios no válidos y códigos duplicados.

### 5.4. Gestión de proyectos

Permite administrar los proyectos registrados en el sistema.

Las operaciones incluyen:

- Registrar proyectos.
- Buscar proyectos por código.
- Buscar proyectos por nombre.
- Buscar proyectos por ciudad.
- Listar todos los proyectos.
- Actualizar información.
- Eliminar proyectos cuando no están sujetos a restricciones de integridad.

Se validan los campos obligatorios y se evita registrar códigos duplicados.

### 5.5. Gestión de suministros

Es una de las funcionalidades centrales de la aplicación.

Permite registrar las operaciones mediante las cuales un proveedor suministra una determinada cantidad de una pieza a un proyecto.

Cada gestión contiene:

- Fecha de la operación.
- Cantidad suministrada.
- Proveedor.
- Pieza.
- Proyecto.

Las operaciones disponibles incluyen:

- Registrar una gestión.
- Listar todas las gestiones.
- Buscar una gestión por identificador.
- Consultar gestiones por proveedor.
- Consultar gestiones por pieza.
- Consultar gestiones por proyecto.
- Realizar búsquedas avanzadas.
- Actualizar gestiones.
- Eliminar gestiones.

Antes de registrar una operación se comprueba que las entidades relacionadas existen y que los datos introducidos son válidos.

### 5.6. Búsqueda avanzada

La aplicación incorpora un sistema de búsqueda avanzada que permite combinar distintos filtros para localizar operaciones de suministro.

Los filtros disponibles son:

- Código del proveedor.
- Código de la pieza.
- Código del proyecto.
- Fecha inicial.
- Fecha final.
- Cantidad mínima.
- Cantidad máxima.

Los filtros son opcionales, por lo que se pueden combinar libremente.

Por ejemplo, es posible consultar todas las gestiones de un proveedor concreto realizadas durante un periodo determinado y que superen una cantidad mínima.

La consulta se construye dinámicamente mediante HQL y utiliza parámetros para evitar concatenar directamente los valores introducidos por el usuario.

Los resultados se ordenan por fecha e identificador de forma descendente.

### 5.7. Estadísticas

La aplicación dispone de un apartado específico para consultar información agregada.

Entre las estadísticas disponibles se encuentran:

**Cantidad total suministrada**

Calcula la suma de todas las cantidades registradas en las gestiones.

**Proveedores más activos**

Obtiene los proveedores que acumulan mayores cantidades suministradas.

**Piezas más utilizadas**

Identifica las piezas que presentan mayores cantidades suministradas en las operaciones registradas.

**Costes por proyecto**

Calcula el coste de los materiales suministrados a cada proyecto utilizando la cantidad registrada y el precio de la pieza.

El cálculo se realiza mediante la suma de los productos de cantidad por precio, agrupando los resultados por proyecto.

Estas consultas permiten obtener una visión general de la actividad registrada en la aplicación.

### 5.8. Exportación de información a CSV

El sistema permite exportar información a archivos CSV para facilitar su consulta y tratamiento en herramientas externas, como Microsoft Excel o LibreOffice Calc.

La exportación genera los siguientes archivos:

| Archivo | Contenido |
|---|---|
| `gestiones.csv` | Información de las operaciones de suministro |
| `cantidad_total.csv` | Cantidad total suministrada |
| `proveedores_mas_activos.csv` | Estadísticas de actividad de proveedores |
| `piezas_mas_utilizadas.csv` | Estadísticas de utilización de piezas |
| `costes_por_proyecto.csv` | Costes calculados para cada proyecto |

Los archivos se generan en la carpeta `exportaciones`, situada en el directorio de trabajo desde el que se ejecuta la aplicación.

El exportador utiliza:

- Codificación UTF-8 con BOM para mejorar la compatibilidad con Excel.
- Punto y coma (`;`) como separador de columnas.
- Tratamiento de comillas y campos que contienen caracteres especiales.

Los archivos generados pueden abrirse con programas de hojas de cálculo o utilizarse en otros procesos de análisis.

## 6. Seguridad y control de permisos

GestionProyectos incorpora un sistema de roles para limitar las operaciones disponibles según el tipo de usuario.

### 6.1. Roles

**ADMIN**

El administrador dispone de permisos para:

- Registrar proveedores.
- Actualizar proveedores.
- Eliminar proveedores.
- Registrar piezas.
- Actualizar piezas.
- Eliminar piezas.
- Registrar proyectos.
- Actualizar proyectos.
- Eliminar proyectos.
- Registrar gestiones.
- Actualizar gestiones.
- Eliminar gestiones.
- Consultar estadísticas y realizar exportaciones.

**USER**

El menú del usuario normal permite consultar estadísticas y exportar sus resultados; no ofrece operaciones para modificar los datos principales.

### 6.2. Protección de contraseñas

Las contraseñas se almacenan mediante BCrypt.

El sistema utiliza un factor de coste configurado en 12 para la generación de los hashes.

Durante el inicio de sesión se compara la contraseña introducida con el hash almacenado.

En ningún caso es necesario almacenar la contraseña original en la base de datos.

### 6.3. Administrador inicial

El sistema permite crear un administrador inicial cuando todavía no existen usuarios registrados.

Una vez creado el primer usuario, esta opción deja de estar disponible.

`UsuarioServicio` contiene un método para registrar usuarios con el rol `USER`, pero actualmente no hay una opción en los menús que lo invoque.

### 6.4. Protección de operaciones

Las operaciones de escritura de los servicios comprueban que existe una sesión válida y que el usuario tiene permisos de administrador.

De esta forma, las restricciones no dependen exclusivamente de las opciones que aparecen en los menús.

## 7. Sistema de logging

El proyecto incorpora un sistema de registro de eventos mediante SLF4J y Logback.

El logging permite obtener información sobre el funcionamiento interno de la aplicación y facilita la detección de posibles errores.

Se registran eventos relacionados con:

- Operaciones realizadas por los servicios.
- Procesos de autenticación.
- Errores durante las operaciones.
- Funcionamiento general de la aplicación.

La configuración se encuentra en `logback.xml`.

Se utilizan diferentes niveles de registro para distinguir entre información general y situaciones que requieren atención.

Los mensajes de registro no deben contener contraseñas ni información sensible.

## 8. Pruebas unitarias

El proyecto incorpora pruebas automatizadas mediante JUnit Jupiter y Mockito.

Las pruebas se centran en comprobar el comportamiento de la lógica de negocio de los servicios.

Mockito permite simular los DAO para comprobar las operaciones sin necesidad de acceder a una base de datos real.

### 8.1. Clases de prueba

Se han desarrollado cuatro clases principales:

| Clase de prueba | Número de pruebas |
|---|---:|
| `ProveedorServicioTest` | 11 |
| `PiezaServicioTest` | 11 |
| `ProyectoServicioTest` | 14 |
| `GestionServicioTest` | 17 |
| **Total** | **53** |

### 8.2. Casos comprobados

Las pruebas incluyen distintos escenarios.

**Validación de datos**

- Objetos nulos.
- Códigos vacíos.
- Campos obligatorios sin completar.
- Precios no válidos.
- Cantidades iguales o inferiores a cero.
- Descripciones demasiado largas.

**Control de duplicados**

- Intentos de registrar proveedores con códigos existentes.
- Intentos de registrar piezas duplicadas.
- Intentos de registrar proyectos con códigos ya registrados.

**Operaciones correctas**

- Registro de entidades válidas.
- Consultas por identificador o código.
- Listados.
- Actualizaciones.
- Eliminaciones.

**Relaciones entre entidades**

- Comprobación de la existencia de proveedores.
- Comprobación de la existencia de piezas.
- Comprobación de la existencia de proyectos.
- Registro de gestiones con asociaciones válidas.

**Búsquedas avanzadas**

- Consultas con filtros.
- Validación de rangos de fechas.
- Validación de cantidades mínimas y máximas.
- Rechazo de parámetros no válidos.

### 8.3. Preparación de las pruebas

Las pruebas utilizan mocks para simular las dependencias de acceso a datos.

Los servicios que requieren permisos de administrador se ejecutan dentro de una sesión de prueba con un usuario ADMIN.

Al finalizar cada prueba se cierra la sesión para evitar que el estado de autenticación afecte a otras pruebas.

### 8.4. Ejecución

Las pruebas se ejecutan mediante Maven utilizando el objetivo `test`.

Si Maven está disponible en el PATH del sistema, se puede utilizar:

```bash
mvn test
```

También pueden ejecutarse desde IntelliJ IDEA:

1. Abrir el panel Maven.
2. Desplegar el proyecto.
3. Desplegar Lifecycle.
4. Ejecutar el objetivo `test`.

La configuración de Maven puede utilizar la instalación incluida en IntelliJ mediante la opción `Bundled`.

Las clases actuales contienen 53 pruebas unitarias. Estas no sustituyen a las pruebas funcionales de la aplicación conectada a MariaDB.

## 9. Instalación y configuración

Esta sección explica los requisitos necesarios para preparar el entorno y ejecutar la aplicación.

### 9.1. Requisitos previos

Para instalar y ejecutar GestionProyectos en un equipo nuevo se necesitan:

- **JDK 21:** para compilar y ejecutar la aplicación.
- **Git:** si se va a clonar el repositorio.
- **IntelliJ IDEA:** recomendado para abrir, configurar y ejecutar el proyecto.
- **Docker Desktop:** para ejecutar MariaDB en un contenedor.
- **MariaDB:** puede ejecutarse mediante Docker o instalarse directamente.
- **Conexión a Internet:** necesaria la primera vez para descargar las dependencias Maven.

No es obligatorio instalar Maven por separado si se utiliza la instalación `Bundled` incluida en IntelliJ IDEA.

### 9.2. Descargar el proyecto

Se puede obtener el proyecto clonando el repositorio de GitHub:

```bash
git clone https://github.com/jfernandev/GestionProyectos.git
```

También se puede descargar el ZIP desde la página del repositorio. En el repositorio clonado o descomprimido, abre en IntelliJ IDEA la carpeta interior `GestionProyectos`, que contiene `pom.xml` y `compose.yaml`.

### 9.3. Preparar MariaDB con Docker

La configuración utilizada durante el desarrollo fue:

| Parámetro | Valor |
|---|---|
| Nombre del contenedor | `mariadb_gestion` |
| Nombre de la base de datos | `gestion_proyectos` |
| Usuario de aplicación | `gestion_user` |
| Puerto del equipo anfitrión | `33060` |
| Puerto habitual de MariaDB dentro del contenedor | `3306` |

La configuración de MariaDB está en `compose.yaml`. Crea `.env` si no existe (o comprueba sus valores si ya existe) en la misma carpeta, con las variables `DB_ROOT_PASSWORD` y `DB_PASSWORD`, y arranca el servicio desde allí:

```bash
docker compose up -d
```

El puerto publicado es `33060` en el equipo anfitrión y `3306` dentro del contenedor. Si el puerto `33060` ya está ocupado, cambia el puerto anfitrión en `compose.yaml` y actualiza la URL JDBC en `src/main/resources/hibernate.cfg.xml`.

### 9.4. Comprobar la base de datos y el usuario

Al crear el contenedor con las variables anteriores, la imagen oficial de MariaDB crea la base de datos y el usuario indicados.

Si se utiliza una instalación de MariaDB ya existente, hay que crear una base de datos y un usuario con permisos sobre ella. Por ejemplo, desde una sesión administrativa de MariaDB:

```sql
CREATE DATABASE gestion_proyectos;

CREATE USER 'gestion_user'@'%' IDENTIFIED BY 'CAMBIAR_PASSWORD_BD';

GRANT ALL PRIVILEGES ON gestion_proyectos.* TO 'gestion_user'@'%';

FLUSH PRIVILEGES;
```

No ejecutes estas instrucciones si la base de datos y el usuario ya existen; en ese caso, comprueba sus permisos y credenciales.

### 9.5. Configurar la conexión de Hibernate

La configuración se encuentra en:

```text
src/main/resources/hibernate.cfg.xml
```

Comprueba especialmente:

- El controlador JDBC de MariaDB.
- La URL de conexión.
- El nombre de usuario.
- La forma en la que se obtiene la contraseña.
- Los mapeos de las entidades.
- La propiedad `hibernate.hbm2ddl.auto`.

La URL JDBC configurada, coherente con `compose.yaml`, es:

```text
jdbc:mariadb://localhost:33060/gestion_proyectos
```

El proyecto obtiene la contraseña de la variable de entorno `DB_PASSWORD`. Docker Compose obtiene las contraseñas de `.env`, pero IntelliJ no pasa automáticamente esas variables a la aplicación Java. Configura `DB_PASSWORD` en la configuración de ejecución de IntelliJ y no escribas contraseñas reales en el código ni en el repositorio.

### 9.6. Configurar `DB_PASSWORD` en IntelliJ IDEA

El archivo `.env` no se carga automáticamente en una aplicación Java ejecutada desde IntelliJ. Que Docker Compose pueda leer un `.env` no significa que IntelliJ transmita esas variables al proceso Java.

Para configurar la contraseña en IntelliJ:

1. Abre **Run → Edit Configurations**.
2. Selecciona la configuración que ejecuta `App`.
3. Busca el apartado **Environment variables**.
4. Añade la variable:

```text
DB_PASSWORD=tu_contraseña_de_mariadb
```

5. Guarda los cambios.

Utiliza la contraseña del usuario `gestion_user` que tenga permisos sobre `gestion_proyectos`. No compartas capturas donde aparezca la contraseña.

Si ejecutas la aplicación desde una terminal, configura `DB_PASSWORD` en el entorno de esa terminal antes de iniciar Java.

### 9.7. Importar las dependencias Maven

Abre el proyecto en IntelliJ IDEA y espera a que detecte el archivo `pom.xml`.

Si las dependencias no se descargan automáticamente:

1. Abre la ventana **Maven**.
2. Pulsa **Reload All Maven Projects**.
3. Espera a que termine la descarga de dependencias.

En **File → Settings → Build, Execution, Deployment → Build Tools → Maven**, se puede seleccionar `Bundled` en **Maven home path** para utilizar la versión incluida en IntelliJ IDEA.

No es necesario que el comando `mvn` funcione en PowerShell si se ejecutan las tareas desde la ventana Maven de IntelliJ.

### 9.8. Ejecutar las pruebas unitarias

Antes de iniciar la aplicación, se pueden ejecutar las pruebas:

1. Abre la ventana Maven.
2. Despliega el proyecto.
3. Abre **Lifecycle**.
4. Ejecuta `test`.

También se pueden ejecutar desde una terminal que tenga Maven configurado:

```bash
mvn test
```

Las pruebas unitarias utilizan JUnit y Mockito. Simulan los DAO y no necesitan conectarse a MariaDB.

### 9.9. Primer inicio y creación del administrador

Una vez iniciada MariaDB y configurada la variable `DB_PASSWORD`:

1. Abre `src/main/java/App.java` en IntelliJ IDEA.
2. Ejecuta el método `main`.
3. Espera a que Hibernate inicialice la conexión y aparezca el menú de acceso.
4. Si la base de datos todavía no tiene usuarios, selecciona la opción para crear el administrador inicial.
5. Introduce el nombre de usuario y la contraseña que quieras utilizar.
6. Cuando termine el registro, vuelve al menú de acceso e inicia sesión con esas credenciales.

**No existe una contraseña de administrador predeterminada.** El administrador inicial lo crea la persona que instala y ejecuta la aplicación por primera vez. Guarda las credenciales en un lugar seguro.

Una vez creado el primer usuario, la opción de creación del administrador inicial deja de estar disponible. Aunque `UsuarioServicio` tiene un método para registrar usuarios `USER`, la aplicación no ofrece actualmente una opción de menú para hacerlo.

### 9.10. Acceso y utilización de la aplicación

Después de iniciar sesión, se muestra el menú principal correspondiente al rol del usuario.

**Acceso como ADMIN**

El administrador puede gestionar proveedores, piezas, proyectos y gestiones, además de consultar estadísticas y generar exportaciones CSV.

**Acceso como USER**

El usuario normal puede acceder a las opciones de consulta y estadísticas que ofrece su menú, pero no puede realizar operaciones de modificación.

Para empezar a utilizar la aplicación se recomienda este orden:

1. Registrar uno o varios proveedores.
2. Registrar las piezas y sus precios.
3. Registrar los proyectos.
4. Crear las gestiones que relacionan proveedor, pieza y proyecto.
5. Consultar los listados y probar las búsquedas.
6. Acceder a las estadísticas.
7. Generar las exportaciones CSV desde el menú de estadísticas.

Las opciones concretas se muestran en los menús de la aplicación. Para salir, utiliza la opción correspondiente del menú.

### 9.11. Ubicación de las exportaciones

Al generar los CSV, la aplicación crea la carpeta `exportaciones` en el directorio de trabajo desde el que se ejecuta el programa, si todavía no existe.

Por tanto, la carpeta no tiene por qué aparecer dentro de `src`. En IntelliJ, comprueba el **Working directory** de la configuración de ejecución de `App` para localizarla.

## 10. Estructura de los archivos exportados

Los archivos CSV se generan en la carpeta:

```text
exportaciones/
```

La carpeta se crea en el directorio de trabajo de ejecución si no existe.

Los archivos disponibles son:

```text
exportaciones/
│
├── gestiones.csv
├── cantidad_total.csv
├── proveedores_mas_activos.csv
├── piezas_mas_utilizadas.csv
└── costes_por_proyecto.csv
```

Estos archivos pueden utilizarse para:

- Revisar las operaciones registradas.
- Analizar cantidades suministradas.
- Comparar la actividad de los proveedores.
- Identificar las piezas más utilizadas.
- Consultar los costes acumulados por proyecto.

## 11. Gestión de errores y consideraciones

La aplicación incorpora validaciones para evitar operaciones con información incorrecta.

Entre otras situaciones, se controlan:

- Campos obligatorios vacíos.
- Códigos duplicados.
- Cantidades no válidas.
- Precios incorrectos.
- Asociaciones con entidades inexistentes.
- Fechas incompatibles en las búsquedas.
- Operaciones realizadas por usuarios sin permisos suficientes.

Las operaciones de eliminación deben respetar las relaciones existentes en la base de datos.

Por ejemplo, no debe eliminarse una entidad que siga siendo necesaria para mantener la integridad de una gestión registrada.

## 12. Posibles problemas durante la instalación

### 12.1. Error de conexión con MariaDB

Si la aplicación no consigue conectarse:

- Comprobar que Docker Desktop está iniciado.
- Comprobar que el contenedor MariaDB está en ejecución.
- Revisar el puerto `33060`.
- Verificar el nombre de la base de datos.
- Comprobar el usuario y la contraseña.
- Verificar que `DB_PASSWORD` está disponible para la aplicación.

### 12.2. Maven no se reconoce como comando

Si al ejecutar `mvn test` aparece un error indicando que Maven no se reconoce, puede utilizarse Maven desde IntelliJ IDEA.

Pasos:

1. Abrir el panel Maven.
2. Desplegar Lifecycle.
3. Ejecutar `test`.

También se puede revisar la opción Maven Home Path en:

```text
File → Settings → Build, Execution, Deployment
→ Build Tools → Maven
```

La opción `Bundled` permite utilizar la instalación de Maven incluida en IntelliJ.

### 12.3. Error al cargar la configuración XML

Si Hibernate no puede cargar `hibernate.cfg.xml`, hay que comprobar:

- Que el archivo está dentro de `src/main/resources`.
- Que el XML está bien formado.
- Que la declaración XML aparece al principio del documento.
- Que los nombres de las propiedades son correctos.

### 12.4. No aparecen los archivos CSV

Si la aplicación indica que ha exportado los archivos, pero no se encuentran:

- Buscar la carpeta `exportaciones` en el directorio de trabajo de ejecución.
- Comprobar desde qué directorio se ha iniciado `App.java`.
- Verificar que el proceso tiene permisos de escritura.

La ruta es relativa al directorio de trabajo, por lo que puede cambiar según la configuración de ejecución de IntelliJ.

## 13. Mantenimiento y ampliaciones

La arquitectura del proyecto permite incorporar nuevas funcionalidades sin necesidad de modificar toda la aplicación.

Entre las posibles ampliaciones se encuentran:

- Incorporar una interfaz gráfica.
- Añadir nuevos filtros de búsqueda.
- Ampliar las estadísticas.
- Incorporar nuevos formatos de exportación.
- Mejorar el sistema de auditoría.
- Añadir nuevas pruebas automatizadas.
- Incorporar nuevas entidades y relaciones.

La separación por capas facilita que estas mejoras puedan desarrollarse de forma independiente.

## 14. Conclusiones

GestionProyectos permite centralizar la gestión de proveedores, piezas, proyectos y suministros en una única aplicación.

El uso de Java, Hibernate y MariaDB proporciona una solución basada en persistencia relacional, mientras que la separación en capas mejora la organización y el mantenimiento del código.

La aplicación incorpora funcionalidades de consulta, búsqueda avanzada, estadísticas y exportación de datos, además de un sistema de autenticación con roles y almacenamiento seguro de contraseñas.

Por último, la incorporación de pruebas unitarias con JUnit y Mockito permite comprobar el comportamiento de los servicios y detectar errores durante el desarrollo.

El resultado es una aplicación modular y ampliable que cubre las necesidades principales de gestión planteadas para el proyecto.

---

## 15. Información del proyecto

| Elemento | Información |
|---|---|
| Nombre | GestionProyectos |
| Lenguaje | Java |
| Versión de Java | 21 |
| Persistencia | Hibernate ORM |
| Base de datos | MariaDB |
| Construcción | Maven |
| Pruebas | JUnit 5 y Mockito |
| Interfaz | Consola |
| Exportaciones | CSV |
| Autenticación | BCrypt |
| Logging | SLF4J y Logback |

**Nota de mantenimiento:** los datos de conexión, las versiones de las dependencias y las instrucciones de instalación deben mantenerse sincronizados con los archivos reales de configuración del repositorio.
