# 🌐 MARCOS DE DESARROLLO WEB

> **Unidad 3:** Spring con base de datos
>
> **Tema:** Integración de JPA con Hibernate en Spring Web para operaciones CRUD en una base de datos.
>
> **Institución:** Universidad Tecnológica del Perú (UTP).

---

## 📑 Índice

1. [Logro de aprendizaje](#1--logro-de-aprendizaje)
2. [Agenda](#2--agenda)
3. [Recordando](#3--recordando)
4. [Saberes previos](#4--saberes-previos)
5. [Introducción a Hibernate y JPA en Spring](#5--introducción-a-hibernate-y-jpa-en-spring)
6. [Configuración de Spring Web con JPA y Hibernate](#6--configuración-de-spring-web-con-jpa-y-hibernate)
7. [Creación de la entidad JPA](#7--creación-de-la-entidad-jpa)
8. [Repositorio JPA](#8--repositorio-jpa)
9. [Servicio de negocio](#9--servicio-de-negocio)
10. [Controlador web](#10--controlador-web)
11. [Ejecución del proyecto](#11--ejecución-del-proyecto)
12. [Parte práctica](#12--parte-práctica)
13. [Preguntas](#13--preguntas)
14. [Resumiendo](#14--resumiendo)

---

## 1. 🎯 Logro de aprendizaje

> Al finalizar la sesión, el estudiante integra JPA con Hibernate en proyectos Spring Web para operaciones CRUD en una base de datos.

---

## 2. 📋 Agenda

### Integración

- JPA con Hibernate en Spring Web para operaciones CRUD en una base de datos.

---

## 3. 🔁 Recordando

> **¿Por qué es importante la validación de los datos almacenados en la base de datos?**

---

## 4. 🧠 Saberes previos

- ¿En qué archivo se configura el proyecto Spring Boot con JPA y Hibernate?
- ¿En qué archivo se configura la conexión a la base de datos?

---

## 5. 📚 Introducción a Hibernate y JPA en Spring

### 5.1. JPA

- **JPA:** Es una especificación de Java para el mapeo de datos entre objetos Java y bases de datos relacionales.
- No es una implementación, sino una serie de interfaces que definen cómo las aplicaciones pueden interactuar con la base de datos.

### 5.2. Hibernate

- **Hibernate:** Es una implementación popular de la especificación JPA.
- Proporciona las herramientas necesarias para interactuar con la base de datos utilizando los estándares definidos por JPA.

---

## 6. ⚙️ Configuración de Spring Web con JPA y Hibernate

### 6.1. Dependencias en `pom.xml`

Para configurar un proyecto Spring Boot con JPA y Hibernate, se debe agregar las dependencias al archivo `pom.xml`.

```xml
<dependencies>
    <!-- Dependencias de Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webmvc</artifactId>
    </dependency>

    <!-- Driver de la base de datos (por ejemplo, MySQL) -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Otras dependencias necesarias -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
</dependencies>
```

### 6.2. Configuración del archivo `application.properties`

Configura la conexión a la base de datos y otras propiedades relacionadas con JPA en:

`src/main/resources/application.properties`

```properties
# Configuración de la base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/mi_base_de_datos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración de JPA y Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

---

## 7. 🗃️ Creación de la entidad JPA

- Define una entidad JPA para mapear una tabla en la base de datos.
- Ejemplo, un modelo `User`:

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // Getters y Setters
}
```

### Anotaciones utilizadas

| Anotación | Descripción |
|---|---|
| `@Entity` | Marca la clase como una entidad JPA. |
| `@Id` | Indica que el campo es la clave primaria. |
| `@GeneratedValue` | Especifica la estrategia para la generación de valores de la clave primaria. |
| `@Column` | Define las propiedades de la columna en la tabla de la base de datos. |

---

## 8. 🗂️ Repositorio JPA

- Crea un repositorio para realizar operaciones CRUD en la base de datos.
- El repositorio es una interfaz que extiende `JpaRepository`:

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```

### Métodos del repositorio

- **JpaRepository:** Proporciona métodos CRUD básicos como `save`, `findById`, `findAll`, `deleteById`, etc.
- **Métodos personalizados:** Puedes definir métodos de consulta personalizados, como `findByEmail`.

---

## 9. 🧩 Servicio de negocio

El servicio maneja la lógica de negocio y utiliza el repositorio para interactuar con la base de datos.

```java
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
```

---

## 10. 🌐 Controlador web

El controlador expone las operaciones CRUD a través de endpoints HTTP.

### 10.1. Creación y listado de usuarios

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
```

### 10.2. Consulta, actualización y eliminación de usuarios

> **Nota:** El siguiente bloque continúa la clase `UserController` del apartado anterior.

```java
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
```

### Anotaciones del controlador

| Anotación | Descripción |
|---|---|
| `@RestController` | Marca la clase como un controlador REST. |
| `@RequestMapping` | Define la ruta base para todos los endpoints. |
| `@PostMapping` | Define el método HTTP para operaciones de creación. |
| `@GetMapping` | Define el método HTTP para operaciones de consulta. |
| `@PutMapping` | Define el método HTTP para operaciones de actualización. |
| `@DeleteMapping` | Define el método HTTP para operaciones de eliminación. |

---

## 11. ▶️ Ejecución del proyecto

- Una vez configurado todo, se puede ejecutar la aplicación.
- Spring Boot realizará las siguientes acciones:
  - Configurará automáticamente Hibernate como el proveedor JPA.
  - Gestionará la conexión a la base de datos.
  - Ejecutará las operaciones CRUD a través de los endpoints definidos en el controlador.

---

## 12. 💻 Parte práctica

> Realice los ejercicios planteados por el docente.

---

## 13. ❓ Preguntas

Espacio de preguntas de la sesión.

---

## 14. 📝 Resumiendo

### Responder/completar

- ¿En qué archivo se configura el proyecto Spring Boot con JPA y Hibernate?
- ¿En qué archivo se configura la conexión a la base de datos?

---
