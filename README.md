# DiosMC Client — Fabric 1.16.5

Cliente PvP para **DiosMC** usando **Fabric Loader 0.11.6** y **Fabric API 0.36.1**.

---

## 📦 Módulos

### ⚔️ PvP
| Módulo | Descripción | Tecla |
|--------|-------------|-------|
| **KillAura** | Ataca al jugador más cercano automáticamente | `R` |
| **Sprint** | Sprint continuo + omni-sprint opcional | `V` |
| **Reach** | Extiende el alcance vía Mixin | — |

### 🖥️ HUD
| Módulo | Descripción |
|--------|-------------|
| **HUDOverlay** | Watermark + lista de módulos activos (arriba derecha) |
| **Coordinates** | XYZ, dirección cardinal y dimensión actual |
| **FPSCounter** | FPS en verde ≥60 / amarillo ≥30 / rojo <30 |

### 👁️ Visual
| Módulo | Descripción | Tecla |
|--------|-------------|-------|
| **ESP** | Cajas alrededor de jugadores a través de paredes | — |
| **Fullbright** | Gamma máxima, visibilidad total en cuevas | — |
| **Zoom** | Zoom suave interpolado vía Mixin en GameRenderer | `C` |

---

## 🚀 Compilar

### Requisitos
- **JDK 8** (o 11+)
- Gradle (incluido con el wrapper)
- IntelliJ IDEA recomendado

### Pasos
```bash
# Configurar workspace
./gradlew genSources

# Para IntelliJ
./gradlew ideaSources

# Compilar
./gradlew build

# Output en: build/libs/DiosMCClient-1.0.0.jar
```

### Ejecutar en desarrollo
```bash
./gradlew runClient
```

---

## 📁 Estructura

```
DiosMCFabric/
├── build.gradle
├── settings.gradle
└── src/main/
    ├── java/com/diosmc/client/
    │   ├── DiosMCClient.java          ← ClientModInitializer
    │   ├── events/EventBus.java
    │   ├── modules/
    │   │   ├── Module.java
    │   │   ├── ModuleManager.java
    │   │   ├── pvp/  KillAura · Sprint · Reach
    │   │   ├── hud/  HUDOverlay · Coordinates · FPSCounter
    │   │   └── visual/ ESP · Fullbright · Zoom
    │   └── mixin/
    │       ├── ReachMixin.java        ← modifica getReachDistance()
    │       └── ZoomMixin.java         ← modifica getFov()
    └── resources/
        ├── fabric.mod.json
        └── diosmc_client.mixins.json
```

---

## ⚙️ Personalizar módulos en runtime

```java
// KillAura — cambiar rango y velocidad de ataque
KillAura ka = (KillAura) DiosMCClient.moduleManager.getModuleByName("KillAura");
ka.setRange(4.0);
ka.setAttackDelay(8);

// ESP — cambiar color (R, G, B de 0.0 a 1.0)
ESP esp = (ESP) DiosMCClient.moduleManager.getModuleByName("ESP");
esp.setColor(0.0f, 1.0f, 0.4f); // Verde

// Zoom — ajustar FOV objetivo
Zoom zoom = (Zoom) DiosMCClient.moduleManager.getModuleByName("Zoom");
zoom.setZoomFOV(5.0f);

// Reach — ajustar alcance
Reach reach = (Reach) DiosMCClient.moduleManager.getModuleByName("Reach");
reach.setReachDistance(4.0);
```

---

## ⚠️ Notas

- **Solo cliente** (`"environment": "client"` en fabric.mod.json)
- Requiere **Fabric API** instalada junto al mod
- Los Mixins (`ReachMixin`, `ZoomMixin`) se aplican al iniciar el juego
- Testear siempre en un servidor privado antes de usar en DiosMC

---

*DiosMC Client — Fabric Edition 2024*
