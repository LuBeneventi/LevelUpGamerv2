# Proyecto Final: LevelUp Gamer

## Introducción

**LevelUp Gamer** es una aplicación móvil para Android desarrollada como proyecto final, que simula una tienda de comercio electrónico especializada en productos y artículos para gamers. La aplicación permite a los usuarios explorar un catálogo, gestionar un carrito de compras, ganar puntos de recompensa y canjearlos por productos exclusivos.

Este proyecto fue construido utilizando tecnologías modernas del ecosistema de Android, con un enfoque en la arquitectura limpia, la escalabilidad y una experiencia de usuario fluida gracias a Jetpack Compose.

---

## Proceso de Desarrollo: Un Vistazo a la Estrategia

El desarrollo de **LevelUp Gamer** siguió un enfoque metodológico y estructurado, partiendo desde la capa de datos hasta la interfaz de usuario para asegurar una base sólida y escalable.

1.  **Fundamentos: Modelado de Datos y Persistencia Local**
    El primer paso fue definir el corazón de la aplicación: nuestros datos. Creamos las clases de modelo (Entities) como `User`, `Product`, `Order`, `Reward`, etc., para representar cada concepto clave. Inmediatamente después, diseñamos las interfaces de acceso a datos **(DAOs)** con Room, definiendo las operaciones CRUD (Crear, Leer, Actualizar, Borrar) para cada entidad. Finalmente, integramos todos los DAOs en una única clase `AppDatabase` que actúa como la base de datos central y local de la aplicación.

2.  **Capa de Lógica: Repositorios y ViewModels**
    Con la persistencia de datos resuelta, construimos la capa de lógica de negocio. Implementamos el patrón **Repositorio**, creando clases como `ProductRepository` o `UserRepository`. Estos repositorios actúan como intermediarios, abstrayendo el origen de los datos (en este caso, la base de datos Room) de las demás partes de la aplicación.

    Posteriormente, desarrollamos los **ViewModels**. Cada pantalla o feature con estado tiene su propio ViewModel (ej. `CartViewModel`, `LoginViewModel`), que se encarga de obtener los datos del repositorio correspondiente, manejar la lógica de negocio y exponer el estado a la interfaz de usuario a través de flujos de datos (StateFlow). Para manejar la inyección de dependencias de manera simple, creamos una `ViewModelFactory` personalizada.

3.  **Construcción de la Interfaz: Jetpack Compose**
    La última etapa fue la construcción de la interfaz de usuario de forma declarativa con **Jetpack Compose**. Desarrollamos cada pantalla (`LoginScreen`, `CatalogScreen`, `RewardsShopScreen`, etc.) como un conjunto de Composables que reaccionan al estado expuesto por sus respectivos ViewModels. La navegación entre pantallas se gestionó con `NavHost` de la librería de Navegación de Compose, creando una experiencia de usuario cohesiva.

Este enfoque por capas nos permitió trabajar de manera ordenada, asegurar que la lógica de negocio estuviera desacoplada de la UI y facilitar las pruebas y el mantenimiento a futuro.

---

## Funcionalidades Implementadas

*   **Autenticación de Usuarios:** Sistema completo de registro e inicio de sesión.
*   **Gestión de Perfil:** Los usuarios pueden ver y editar la información básica de su perfil.
*   **Catálogo de Productos:** Visualización de productos con detalles, incluyendo nombre, descripción, precio e imagen.
*   **Carrito de Compras:** Funcionalidad para agregar, visualizar y eliminar productos del carrito.
*   **Sistema de Recompensas (Rewards):**
    *   Los usuarios acumulan puntos (ej. participando en eventos).
    *   Existe una "Tienda de Canje" donde los usuarios pueden usar sus puntos para obtener recompensas.
*   **Gestión de Direcciones:** Los usuarios pueden agregar y administrar sus direcciones de envío.
*   **Reseñas de Productos:** Los usuarios pueden dejar comentarios y valoraciones en los productos.

## Funcionalidades No Implementadas o Futuras Mejoras

*   **Conectividad con Backend:** La aplicación opera de manera 100% local. No hay conexión con un servidor o API REST real. Todos los datos son simulados o almacenados en la base de datos local del dispositivo.
*   **Pasarela de Pagos:** El proceso de "Checkout" es una simulación y no integra una pasarela de pagos real.
*   **Notificaciones Push:** No se implementó un sistema de notificaciones.
*   **Pruebas Unitarias y de Integración:** Aunque la arquitectura facilita las pruebas, no se desarrollaron casos de prueba exhaustivos.

---

## Arquitectura y Tecnologías Utilizadas

*   **Lenguaje:** Kotlin
*   **Interfaz de Usuario:** Jetpack Compose
*   **Arquitectura:** MVVM (Model-View-ViewModel)
*   **Base de Datos:** Room para persistencia de datos local.
*   **Asincronía:** Corrutinas de Kotlin y Flows para manejar operaciones en segundo plano.
*   **Navegación:** Jetpack Navigation para Compose.
*   **Inyección de Dependencias:** Manual a través de una `ViewModelFactory`.

---

## Cómo Ejecutar el Proyecto

1.  Clonar o descargar el repositorio.
2.  Abrir el proyecto con una versión reciente de Android Studio.
3.  Esperar a que Gradle sincronice todas las dependencias.
4.  Ejecutar la aplicación en un emulador o en un dispositivo físico con Android.
