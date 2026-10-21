\# OstraVerde - Pruebas de Software



\## Descripción del proyecto



Proyecto académico desarrollado para la asignatura ISW-622 - Pruebas de Software de la Universidad Técnica Nacional.



El proyecto consiste en la implementación y verificación de funcionalidades para un sistema de gestión de producción de hongos ostra de la empresa Ostra Verde S.A.



\## Integrantes



\- Danna Umaña Villegas

\- Santiago Salas Rodríguez



\*\*Profesor:\*\* Víctor Iván Zúñiga



\## Tecnologías utilizadas



\- Java 21

\- Maven

\- JUnit 5

\- Eclipse IDE

\- Git y GitHub



\## Funcionalidades desarrolladas



\- Gestión de secciones de producción.

\- Registro y administración de trabajadores.

\- Registro de siembras.

\- Gestión de productos agrícolas.

\- Registro de aplicaciones.

\- Registro de cosechas.

\- Generación de reportes de producción.



\## Pruebas de software



Se desarrollaron pruebas unitarias utilizando JUnit 5 y el patrón AAA (Arrange, Act, Assert).



Además, se documentó la aplicación del enfoque TDD (Test Driven Development) mediante las etapas RED, GREEN y REFACTOR.



El proyecto cuenta con 55 pruebas unitarias, las cuales finalizaron satisfactoriamente en la ejecución general documentada, sin errores ni fallos.



También se diseñaron 38 casos de prueba en el catálogo del proyecto, correspondientes a 30 casos funcionales y ocho no funcionales.



De los ocho casos no funcionales, cuatro fueron aprobados dentro del alcance evaluado y cuatro quedaron pendientes de ejecución por requerir componentes todavía no implementados.



\## Requisitos para ejecutar el proyecto



\- JDK 21.

\- Apache Maven.

\- Eclipse IDE (opcional).



\## Ejecución de las pruebas



Desde una terminal ubicada en la carpeta principal del proyecto, ejecutar:



```bash

mvn test

```



También pueden ejecutarse las clases de prueba desde Eclipse mediante JUnit.



\## Estructura del proyecto



\- `src/main/java/com/ostraverde/model`: clases del modelo.

\- `src/main/java/com/ostraverde/service`: servicios y lógica del sistema.

\- `src/test/java/com/ostraverde/service`: pruebas unitarias desarrolladas con JUnit 5.

\- `pom.xml`: configuración de Maven y dependencias.



\## Alcance



La implementación actual se enfoca en los componentes Java y sus pruebas unitarias. No constituye una aplicación completa con interfaz gráfica, autenticación, base de datos persistente o despliegue permanente.



\## Documentación



El informe académico incluye el análisis de requerimientos, catálogo de casos de prueba, matrices de trazabilidad, evidencias de ejecución, aplicación de AAA y TDD, conclusiones y recomendaciones.

