# BodegaPro — Spring MVC, JPA/Hibernate y Spring Validation

Aplicación web de administración para una bodega. Mantiene la interfaz Thymeleaf y los estilos del proyecto original e integra persistencia relacional, operaciones CRUD y validación de formularios.

## Tecnologías

- Java 25 y Spring Boot 4.1.2.
- Spring MVC y Thymeleaf para la interfaz web.
- Spring Data JPA con Hibernate como proveedor ORM.
- PostgreSQL para almacenamiento persistente.
- Jakarta Bean Validation mediante `spring-boot-starter-validation`.
- H2 en pruebas automatizadas, sin necesidad de PostgreSQL para ejecutar los tests.

## Arquitectura

```text
Thymeleaf / HTML
        ↓ formulario + DTO validado
Controller (@Valid + BindingResult)
        ↓
Service (reglas de negocio y transacciones)
        ↓
Repository (JpaRepository)
        ↓
Hibernate / JPA
        ↓
PostgreSQL
```

- `controller/`: rutas MVC, formularios, mensajes y resultados de validación.
- `dto/`: objetos de entrada con anotaciones como `@NotBlank`, `@Pattern`, `@NotNull`, `@Positive` y `@DecimalMin`.
- `model/`: entidades JPA y restricciones de persistencia.
- `repository/`: repositorios Spring Data JPA y consultas de búsqueda.
- `service/`: operaciones CRUD, reglas de integridad y gestión de stock.
- `templates/`: pantallas Thymeleaf; los errores de validación se muestran junto a los controles.
- `static/css/`: hojas de estilo existentes.

## Relaciones de la base de datos

- Una **categoría** tiene varios productos; cada producto pertenece a una categoría (`Producto` → `Categoria`, `ManyToOne`).
- Un **proveedor** puede abastecer varios productos; la asociación del producto con un proveedor es opcional (`Producto` → `Proveedor`, `ManyToOne`).
- Un **cliente** puede registrar varias ventas; toda venta pertenece a un cliente (`Venta` → `Cliente`, `ManyToOne`).
- Una **venta** tiene uno o más detalles; cada detalle vincula una venta y un producto (`Venta` → `DetalleVenta` → `Producto`).
- La configuración de la bodega se persiste en la tabla `configuracion`.

Hibernate actualiza las tablas al iniciar con `spring.jpa.hibernate.ddl-auto=update`. Las claves foráneas evitan borrar clientes, categorías, proveedores o productos que aún estén relacionados. Al eliminar una venta se devuelve al inventario el stock vendido. La edición de venta ajusta el inventario y se ejecuta dentro de una transacción.

## Preparar PostgreSQL

1. Instala e inicia PostgreSQL.
2. Crea una base de datos vacía llamada `bodegapro`.
3. Revisa `src/main/resources/application.properties`. Los valores predeterminados son usuario `postgres` y contraseña vacía.
4. Si tu instalación usa otras credenciales, define las variables de entorno:

```bash
export DB_URL='jdbc:postgresql://localhost:5432/bodegapro'
export DB_USERNAME='postgres'
export DB_PASSWORD='tu_clave'
```

En PowerShell:

```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/bodegapro'
$env:DB_USERNAME = 'postgres'
$env:DB_PASSWORD = 'tu_clave'
```

> `ddl-auto=update` crea las tablas que faltan y ajusta el esquema, pero antes de usar una base existente de otra versión conviene realizar una copia de seguridad. Para una entrega nueva, se recomienda crear una base vacía.

## Ejecutar

En la carpeta `BodegaPro`:

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Después abre `http://localhost:8080`. Para conservar la compatibilidad con la interfaz inicial se mantienen las credenciales de demostración `admin / admin123`; cámbialas antes de usar el sistema en un entorno real.

## Pruebas

```bash
./mvnw test
```

Las pruebas usan el perfil `test` y una base de datos H2 en memoria; no requieren un servidor PostgreSQL.

## Funcionalidades CRUD y validación

| Módulo | Funcionalidad |
|---|---|
| Categorías | Crear, listar, buscar, editar y eliminar; no permite borrar categorías con productos asociados. |
| Clientes | Crear, listar, buscar, editar y eliminar; valida el documento y evita borrar clientes con ventas. |
| Proveedores | Crear, listar, buscar, editar y eliminar; valida RUC y protege referencias desde productos. |
| Productos | Crear, listar, buscar, editar y eliminar; valida precios, stock, categoría, proveedor y URL de imagen. |
| Ventas | Registrar, listar, buscar, editar y eliminar; valida cliente, producto y cantidad, controla stock y revierte existencias al anular. |
| Configuración | Guarda nombre de bodega, moneda y tema en la base de datos con validación. |

## Cuentas y seguridad

El login original es una demostración local y usa credenciales estáticas; no reemplaza autenticación segura. No despliegues la aplicación públicamente con esas credenciales.
