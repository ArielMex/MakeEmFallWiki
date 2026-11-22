# Make 'Em Fall — Wiki (Kotlin + Jetpack Compose)

App móvil tipo wiki del videojuego **MAKE 'EM FALL**: historia, personajes, enemigos, boons, mecánicas y controles.
Los datos llegan con un **GET** a una API (un archivo `wiki.json` publicado en GitHub). El diseño se basa en la pestaña
**Wiki** del Figma y en la página del juego en itch.io.

---

## Índice
1. [Cómo abrir y correr el proyecto](#1-cómo-abrir-y-correr-el-proyecto)
2. [Mapa del proyecto (dónde está cada cosa)](#2-mapa-del-proyecto-dónde-está-cada-cosa)
3. [Pantallas y rutas de navegación](#3-pantallas-y-rutas-de-navegación)
4. [La API y el método GET](#4-la-api-y-el-método-get)
5. [Textos y contenido de la wiki](#5-textos-y-contenido-de-la-wiki)
6. [Imágenes](#6-imágenes)
7. [Botones y componentes visuales](#7-botones-y-componentes-visuales)
8. [Colores y tipografías](#8-colores-y-tipografías)
9. [Favoritos (componente del dispositivo)](#9-favoritos-componente-del-dispositivo)
10. [Guía rápida: “quiero cambiar…”](#10-guía-rápida-quiero-cambiar)
11. [Qué cubre de la rúbrica](#11-qué-cubre-de-la-rúbrica)
12. [Origen de los datos y pendientes](#12-origen-de-los-datos-y-pendientes)

---

## 1. Cómo abrir y correr el proyecto
1. En Android Studio: **Open** → elige la carpeta que contiene `settings.gradle.kts`, `app` y `api`.
2. Si pregunta por la versión de Java (Gradle JVM), elige **JVM 21**.
3. Espera a que termine el *Gradle Sync*. **No** aceptes actualizar Kotlin ni el Android Gradle Plugin.
4. Elige un emulador (Device Manager) o un celular por USB y presiona **▶ Run**.

Para ver el GET en vivo: abre **Logcat** y busca `okhttp`.

---

## 2. Mapa del proyecto (dónde está cada cosa)

```
MakeEmFallWiki/
├── api/
│   └── wiki.json                  ← CONTENIDO de la wiki (se sube a GitHub, la app lo descarga)
├── app/
│   ├── build.gradle.kts           ← Dependencias y URL de la API (API_BASE_URL)
│   └── src/main/
│       ├── AndroidManifest.xml    ← Permiso de internet, ícono y nombre de la app
│       ├── java/com/makeemfall/wiki/
│       │   ├── MainActivity.kt    ← Punto de entrada: aplica el tema y abre la navegación
│       │   ├── data/              ← DATOS (no dibuja nada)
│       │   │   ├── model/
│       │   │   │   ├── WikiModels.kt   ← Clases que representan el JSON (Personaje, Boon, Enemigo…)
│       │   │   │   ├── WikiEntry.kt    ← Convierte cada tipo a una forma común para las pantallas
│       │   │   │   └── Seccion.kt      ← Las 6 secciones del menú (nombre, ícono, filtro)
│       │   │   ├── remote/
│       │   │   │   ├── WikiApi.kt      ← El método GET (@GET("wiki.json"))
│       │   │   │   └── RetrofitClient.kt ← Configuración de Retrofit/OkHttp
│       │   │   ├── repository/
│       │   │   │   └── WikiRepository.kt ← Llama al GET y atrapa errores
│       │   │   └── local/
│       │   │       └── FavoritosStore.kt ← Guarda favoritos en el celular (DataStore)
│       │   └── ui/                ← TODO LO VISUAL
│       │       ├── navigation/NavGraph.kt     ← Rutas entre pantallas
│       │       ├── viewmodel/WikiViewModel.kt ← Estado (Cargando / Listo / Error) y favoritos
│       │       ├── screens/                   ← Un archivo por pantalla
│       │       │   ├── SplashScreen.kt
│       │       │   ├── HomeScreen.kt
│       │       │   ├── ListaScreen.kt
│       │       │   ├── DetalleScreen.kt
│       │       │   └── FavoritosScreen.kt
│       │       ├── components/                ← Piezas reutilizables
│       │       │   ├── Componentes.kt   ← Botón del menú, encabezado de sección, paneles, fondo, imágenes, carga/error
│       │       │   ├── EntradaCard.kt   ← Tarjeta de cada elemento en las listas + bloque de Historia
│       │       │   ├── BarraWiki.kt     ← Barra superior (flecha volver + logo + "WIKI")
│       │       │   ├── Formas.kt        ← Formas inclinadas (botón del Figma, tarjetas, banners)
│       │       │   └── LogoAnimado.kt   ← Animación del logo del splash (traducida de Unity)
│       │       └── theme/
│       │           ├── Color.kt   ← Paleta oficial del juego
│       │           ├── Type.kt    ← Fuentes y tamaños de texto
│       │           └── Theme.kt   ← Tema oscuro de la app
│       └── res/
│           ├── drawable-nodpi/    ← TODAS las imágenes (logo, fondo, boons, personajes, piezas del splash)
│           ├── font/              ← Fuentes Tomorrow y Barlow Condensed (.ttf)
│           ├── mipmap-*/          ← Ícono de la app en el celular
│           └── values/            ← Nombre de la app (strings.xml) y color de la barra del sistema
├── build.gradle.kts / settings.gradle.kts / gradle/libs.versions.toml ← Configuración de Gradle y versiones
└── README.md
```

**Regla general:** si es **información** → `api/wiki.json`. Si es **cómo se ve** → `ui/`. Si es una **imagen** → `res/drawable-nodpi/`.

---

## 3. Pantallas y rutas de navegación

Las rutas están en `ui/navigation/NavGraph.kt` (objeto `Rutas`).

| Pantalla | Archivo | Ruta | Qué muestra |
|---|---|---|---|
| Splash | `SplashScreen.kt` | `splash` | Logo que se arma letra por letra + "WIKI". Dura ~4 s y pasa sola al inicio. |
| Inicio | `HomeScreen.kt` | `inicio` | Banner, texto de introducción, botones de las 6 secciones, Favoritos y pósters de Niko y Mateo. |
| Lista de sección | `ListaScreen.kt` | `lista/{seccion}` | Encabezado de la sección, buscador, filtros y tarjetas. En **Historia** muestra bloques de texto completos. |
| Detalle | `DetalleScreen.kt` | `detalle/{seccion}/{id}` | Imagen grande, descripción, tabla de datos, habilidades y botón de favorito. |
| Favoritos | `FavoritosScreen.kt` | `favoritos` | Lo que el usuario marcó con ★. |

Valores de `{seccion}`: `historia`, `personajes`, `enemigos`, `boons`, `mecanicas`, `controles` (definidos en `data/model/Seccion.kt`).
`{id}` es el campo `id` de cada elemento en `wiki.json` (por ejemplo `detalle/boons/extintor`).

Flujo: **Splash → Inicio → Lista → Detalle**, y **Inicio → Favoritos → Detalle**. La flecha amarilla de la barra superior regresa a la pantalla anterior.

---

## 4. La API y el método GET

| Qué | Dónde |
|---|---|
| URL base de la API | `app/build.gradle.kts` → `buildConfigField("String", "API_BASE_URL", ...)` (debe terminar en `/`) |
| Método GET | `data/remote/WikiApi.kt` → `@GET("wiki.json") suspend fun obtenerWiki(): WikiResponse` |
| Configuración de Retrofit | `data/remote/RetrofitClient.kt` (JSON con kotlinx.serialization, tiempo de espera, log de peticiones) |
| Manejo de errores | `data/repository/WikiRepository.kt` (si falla, la app muestra la pantalla **GAME!** en vez de cerrarse) |
| Cuándo se llama | `ui/viewmodel/WikiViewModel.kt` → `cargar()` al abrir la app y al presionar **Reintentar** |
| Archivo que se descarga | `api/wiki.json` publicado en el repo `makeemfall-api` de GitHub |

Recorrido: `WikiViewModel.cargar()` → `WikiRepository.obtenerWiki()` → `WikiApi.obtenerWiki()` → GET a GitHub → `WikiResponse`.

> **Importante:** si cambian `api/wiki.json` en este proyecto, **también deben subirlo** al repo de la API en GitHub; la app solo lee el de GitHub.
> GitHub puede tardar unos minutos en mostrar la versión nueva.

---

## 5. Textos y contenido de la wiki

### 5.1 Contenido (lo que cambia seguido) → `api/wiki.json`
Tiene 6 listas, una por sección:

| Lista en el JSON | Campos principales |
|---|---|
| `historia` | `id`, `titulo`, `texto`, `imagen` |
| `personajes` | `id`, `nombre`, `rol`, `estiloPelea`, `descripcion`, `apariencia`, `edad`, `peso`, `velocidad`, `vida`, `imagen`, `imagenPanel` |
| `enemigos` | `id`, `nombre`, `tipo` (Normal / Élite / Jefe), `piso`, `descripcion`, `habilidades`, `vida`, `imagen` |
| `boons` | `id`, `nombre`, `tipo`, `elemento`, `rareza`, `descripcion` (nivel 1), `nivelMaximo` (nivel 5), `imagen` |
| `mecanicas` | `id`, `nombre`, `categoria`, `descripcion`, `imagen` |
| `controles` | `id`, `accion`, `teclado`, `mando`, `categoria`, `descripcion` |

- El `id` debe ser único dentro de cada lista, en minúsculas y sin espacios (se usa en la ruta y en favoritos).
- Si agregan un campo nuevo al JSON, también deben agregarlo en `data/model/WikiModels.kt` y mostrarlo en `data/model/WikiEntry.kt`.
- Qué datos salen en la tabla del detalle (Rol, Peso, Rareza, Nivel 5…) se decide en `WikiEntry.kt`, en la parte `detalles = listOfNotNull(...)` de cada sección.

### 5.2 Textos fijos de la app (no vienen del JSON)

| Texto | Archivo |
|---|---|
| Nombre de la app en el celular | `res/values/strings.xml` (`app_name`) |
| Nombre, título del encabezado y descripción de cada sección del menú | `data/model/Seccion.kt` |
| Introducción del inicio ("Nuddle lo sabe todo de ti…") y "LA RESISTENCIA" | `ui/screens/HomeScreen.kt` |
| "WIKI" del splash | `ui/screens/SplashScreen.kt` |
| "WIKI" de la barra superior | `ui/components/BarraWiki.kt` |
| Mensaje de carga ("Hackeando los servidores de Nuddle…"), "GAME!" y "Reintentar" | `ui/components/Componentes.kt` (`PantallaCargando`, `PantallaError`) |
| Mensaje de error de conexión | `ui/viewmodel/WikiViewModel.kt` |
| Buscador, contador "Mostrando X de Y", "No hay resultados" | `ui/screens/ListaScreen.kt` |
| "HABILIDADES", "Agregar a favoritos / En favoritos" | `ui/screens/DetalleScreen.kt` |
| Mensaje cuando no hay favoritos | `ui/screens/FavoritosScreen.kt` |

---

## 6. Imágenes

Todas están en **`app/src/main/res/drawable-nodpi/`** (formato `.webp`).

| Nombre del archivo | Dónde se usa |
|---|---|
| `logo_make_em_fall` | Barra superior de todas las pantallas |
| `splash_logo_00` … `splash_logo_13` | Piezas del logo animado del splash (letras de MAKE'EM, FALL, pincelada, puntos y chispas) |
| `banner_wiki` | Banner grande del inicio |
| `fondo_wiki` | Fondo morado de todas las pantallas |
| `boon_<id>` (16 archivos) | Íconos de los boons (los mismos del juego) |
| `personaje_<nombre>` | Miniatura del personaje en la lista (retrato cuadrado) |
| `personaje_<nombre>_render` | Imagen grande en el detalle (cuerpo completo) |
| `personaje_niko_panel`, `personaje_mateo_panel` | Pósters del inicio y detalle de Niko y Mateo |
| `captura_combate` | Imagen de Historia y de la mecánica "Daño por porcentaje" |
| `mipmap-*/ic_launcher.png` | Ícono de la app en el celular |

**Cómo agregar una imagen nueva:**
1. Copia el archivo a `res/drawable-nodpi/` con nombre **en minúsculas, sin espacios, acentos ni guiones** (ej. `enemigo_guardia.webp`).
2. En `wiki.json` escribe ese nombre **sin extensión** en el campo `imagen` (ej. `"imagen": "enemigo_guardia"`).
3. Vuelve a correr la app (las imágenes van dentro de la app, no en GitHub).

También se puede usar `imagenUrl` con la URL de una imagen en internet. La lógica que decide qué imagen mostrar está en `ImagenWiki` (`ui/components/Componentes.kt`); si no encuentra ninguna, muestra el ícono de la sección.

Los boons y Craub se dibujan **sin suavizado** (pixel art). Eso se controla con `pixelado` en `data/model/WikiEntry.kt`.

---

## 7. Botones y componentes visuales

| Componente | Archivo | Para qué |
|---|---|---|
| `BotonMenu` | `ui/components/Componentes.kt` | Botón rojo del menú (forma del Figma). Al tocarlo se pone naranja con flecha y chispas. Se usa en el inicio, en "Reintentar" y en "Agregar a favoritos". |
| `FormaBotonMenu` | `ui/components/Formas.kt` | La forma exacta del botón copiada del vector "Fondo" del Figma. |
| `EncabezadoSeccion` | `ui/components/Componentes.kt` | Banda roja con semitono tipo "LA HISTORIA". |
| `PanelWiki` | `ui/components/Componentes.kt` | Panel azul oscuro con borde donde va el contenido. |
| `EntradaCard` | `ui/components/EntradaCard.kt` | Tarjeta de cada elemento en las listas (texto, imagen y ★). |
| `BloqueHistoria` | `ui/components/EntradaCard.kt` | Bloque de texto completo de la sección Historia. |
| `BarraWiki` | `ui/components/BarraWiki.kt` | Barra superior con flecha de volver, logo y "WIKI". |
| `TextoContorno` | `ui/components/Componentes.kt` | Texto con contorno oscuro (títulos estilo juego). |
| `FondoWiki` | `ui/components/Componentes.kt` | Fondo morado de todas las pantallas. |
| `LogoAnimado` | `ui/components/LogoAnimado.kt` | Animación del logo del splash. Tiempos e inclinaciones tomados de la escena `Title` de Unity. |
| Filtros (chips) | `ui/screens/ListaScreen.kt` (`Chip`) | Botones de filtro con la misma forma que los del menú. |

**Botones del menú del inicio:** salen de la lista de `Seccion` (`data/model/Seccion.kt`). Para agregar, quitar o reordenar secciones, se cambia ahí (y su contenido en `WikiEntry.kt` y `wiki.json`). El botón **Favoritos** se agrega aparte en `HomeScreen.kt`.

---

## 8. Colores y tipografías

- **Colores:** `ui/theme/Color.kt`. Paleta oficial (Rojo rebelión `#FF003C`, Naranja fuego `#FF6500`, Amarillo eléctrico `#FFE000`, Crema cálido `#FFEDC2`, Negro berenjena `#240B29`, Azul noche `#11132D`, etc.) y los colores exactos de los botones del Figma (`BotonRojo`, `BotonNaranja`, `BotonSombra`, `BotonTexto`, `ChispaAmarilla`).
- **Tipografías:** archivos en `res/font/` y configuración en `ui/theme/Type.kt`.
  - **Tomorrow ExtraBold Italic** → títulos, botones y encabezados.
  - **Barlow Condensed** → textos y descripciones.
- **Tema general (modo oscuro):** `ui/theme/Theme.kt`.
- **Color de fondo mientras abre la app:** `res/values/themes.xml` y `colors.xml`.

---

## 9. Favoritos (componente del dispositivo)

- Se guardan en el **almacenamiento del celular** con DataStore → `data/local/FavoritosStore.kt`.
- Se marcan con la ★ en las tarjetas (`EntradaCard.kt`) o en el detalle (`DetalleScreen.kt`).
- El `WikiViewModel` expone la lista y la función `alternarFavorito(...)`.
- Cada favorito se guarda como `"seccion:id"` (ej. `boons:extintor`). Siguen ahí aunque se cierre la app.

---

## 10. Guía rápida: “quiero cambiar…”

| Quiero cambiar… | Ve a… |
|---|---|
| La descripción de un boon, personaje, enemigo, etc. | `api/wiki.json` (y súbelo a GitHub) |
| Agregar un enemigo o una mecánica nueva | `api/wiki.json` → agrega un objeto a la lista correspondiente |
| La imagen de un elemento | `res/drawable-nodpi/` + campo `imagen` en `wiki.json` |
| El nombre de un botón del menú | `data/model/Seccion.kt` |
| Por qué se filtra cada sección (rareza, tipo…) | `data/model/Seccion.kt` (nombre del filtro) y `WikiEntry.kt` (`etiqueta`) |
| Los datos que salen en la tabla del detalle | `data/model/WikiEntry.kt` (`detalles`) |
| El texto de introducción del inicio | `ui/screens/HomeScreen.kt` |
| La duración del splash | `ui/screens/SplashScreen.kt` (`delay(...)`) |
| La animación del logo | `ui/components/LogoAnimado.kt` (lista `PIEZAS`: retraso e inclinación de cada pieza) |
| Un color | `ui/theme/Color.kt` |
| El tamaño de los textos | `ui/theme/Type.kt` |
| La URL de la API | `app/build.gradle.kts` (`API_BASE_URL`) |
| El nombre o ícono de la app | `res/values/strings.xml` y `res/mipmap-*/ic_launcher.png` |
| Agregar una pantalla nueva | Crear el archivo en `ui/screens/` y registrar la ruta en `ui/navigation/NavGraph.kt` |

---

## 11. Qué cubre de la rúbrica

| Criterio | Dónde |
|---|---|
| Portada / splash | `SplashScreen.kt` + `LogoAnimado.kt` |
| Inicio y navegación | `HomeScreen.kt` + `NavGraph.kt` |
| Consumo de API | `WikiApi.kt`, `RetrofitClient.kt`, `WikiRepository.kt` |
| CRUD (GET) | `WikiApi.obtenerWiki()` |
| Búsqueda y filtros | `ListaScreen.kt` (buscador, chips de filtro y contador) |
| Componente del dispositivo | Almacenamiento local de favoritos: `FavoritosStore.kt` |
| Diseño | `ui/theme/`, `ui/components/` e imágenes del Figma y del juego |

---

## 12. Origen de los datos y pendientes

- **Boons:** 16 boons en 4 rarezas, nivel máximo 5, con los valores del código del juego
  (repo RogueSmash → `Assets/_Game/Combat/*Boons.cs`).
- **Personajes:** peso y velocidad de `Assets/_Game/Characters/*/*.asset`; vida del jefe (450) de `Boss.asset`.
- **Imágenes de boons, personajes y piezas del logo:** `Assets/_Game/Resources/UI/` del juego.
- **Historia, enemigos, mecánicas y controles:** documento del proyecto integrador.

**Pendiente:** no hay imágenes de los enemigos ni de Mr. Big Data, por eso muestran un ícono.
