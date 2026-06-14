# 📦 Sistema de Inventario Didáctico — ProEA

> Herramienta de software educativa para el análisis de inventarios y abastecimiento, desarrollada como proyecto de aula en la Universidad Simón Bolívar de Barranquilla.

---

## 🎯 ¿Qué es esto?

ProEA es un sistema de gestión de inventarios orientado al aprendizaje práctico de estudiantes de **Administración de Empresas**. Su propósito no es solo registrar productos y movimientos — sino aplicar matemáticas reales (límites, derivadas y tasas de cambio) para analizar el comportamiento del stock y apoyar la toma de decisiones.

El sistema traduce conceptos de Cálculo Diferencial en herramientas útiles:

| Concepto matemático | Lo que ve el usuario |
|---|---|
| Derivada numérica I'(t) | Velocidad de cambio del stock |
| Límite de tendencia | Proyección estimada del inventario |
| Tasa de cambio promedio | Comportamiento general del stock |
| Puntos extremos | Stock más alto y más bajo registrado |

---

## ✨ Funcionalidades

- 🔐 **Autenticación** — Login y registro de usuarios con persistencia en archivo plano. Cada usuario tiene su propio inventario aislado.
- 📦 **Gestión de productos** — Registro, consulta y eliminación de productos con tres niveles de estado: Normal, Bajo y Crítico.
- 🔄 **Movimientos** — Registro de entradas y salidas con historial completo y filtrado por producto.
- 📈 **Comportamiento del inventario** — Gráfica I(t) con puntos máximo y mínimo identificados visualmente.
- 📉 **Tasa de cambio** — Gráfica de barras de I'(t) por movimiento con tabla detallada e interpretación automática.
- 🔮 **Proyección** — Línea de tendencia estimada para los próximos 5 movimientos usando regresión lineal, con tabla de valores proyectados.
- 🚨 **Alertas de reposición** — Detección automática de productos en riesgo con sugerencia basada en tendencia matemática.
- 📊 **Dashboard** — Resumen general con métricas clave, estado de productos y alertas activas.

---

## 🧮 Motor matemático

El análisis matemático se realiza sobre el historial discreto de stock I(t), donde cada punto t representa el estado del inventario tras un movimiento:

**Derivada numérica** (diferencia central):
```
I'(t) = (I(t+1) - I(t-1)) / 2
```

**Límite de tendencia** (regresión lineal sobre los últimos k puntos):
```
proyección(t + n) = pendiente * t + intercepto
```

**Detección de riesgo**:
```
Si I(t_actual) ≤ stockMínimo → alerta inmediata
Si proyección(t+3) ≤ stockMínimo → alerta preventiva
```

---

## 🛠️ Stack tecnológico

| Tecnología | Uso |
|---|---|
| Java 17 | Lenguaje principal |
| Swing + AbsoluteLayout | Interfaz gráfica |
| NetBeans | IDE de desarrollo |
| Maven | Gestión de dependencias |
| CSV (archivo plano) | Persistencia de datos |

---

## 🗂️ Arquitectura del proyecto

```
src/
├── Main.java
├── model/
│   ├── Producto.java
│   ├── Movimiento.java
│   ├── Inventario.java
│   └── Usuario.java
├── math/
│   ├── FuncionInventario.java
│   └── AnalizadorMatematico.java
├── controller/
│   ├── InventarioController.java
│   └── LoginController.java
├── service/
│   ├── UsuarioService.java
│   └── PersistenciaService.java
└── ui/
    ├── ProEA.java
    ├── MainFrame.java
    ├── PanelDashboard.java
    ├── PanelProductos.java
    ├── PanelMovimientos.java
    ├── PanelComportamiento.java
    ├── PanelTasaCambio.java
    ├── PanelProyeccion.java
    ├── PanelAlertas.java
    ├── GraficaInventario.java
    ├── GraficaDerivadas.java
    ├── GraficaProyeccion.java
    └── components/
        ├── RoundedPanel.java
        ├── MetricCard.java
        ├── StyledTable.java
        └── SidebarButton.java
```

---

## 🚀 ¿Cómo ejecutarlo?

1. Clona el repositorio:
```bash
git clone git@github.com:AnthonyTCv5/Sistema-de-Inventario-Didactico.git
```

2. Ábrelo en NetBeans como proyecto Maven.

3. Ejecuta `Main.java`.

4. Regístrate con un usuario nuevo y empieza a registrar productos y movimientos.

---

## 🔭 Roadmap — Lo que viene

- [ ] Migración a base de datos (SQLite o PostgreSQL)
- [ ] Automatización de alertas por correo electrónico
- [ ] Exportación de reportes en PDF
- [ ] Módulo multiusuario con roles (admin, operador)
- [ ] API REST para integración con otros sistemas
- [ ] Versión web del sistema

---

## 👨‍💻 Autor

** Anthony De Jesús Peñaloza Díaz*

---

> *"Un inventario bien analizado no solo dice cuánto hay — dice hacia dónde va."*
