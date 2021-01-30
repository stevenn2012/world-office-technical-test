# World Office Technical Test

## Requerimientos:

**Nombre de la Prueba:** API REST Carrito de Compras 

**Objetivo de la Prueba:** evaluar las habilidades técnicas que posee el candidato en el lenguaje Java al nivel requerido para el cargo.

**Lenguajes de Programación Evaluados:** Java.

**Descripción del Problema**
Realizar el cargue del catálogo de productos a la base de datos desde un archivo CSV para habilitar su posterior administración y venta a través de la página de la compañía.

**Primera Parte:** cargue del archivo CSV a la base de datos

**1.** Definir el modelo de datos para almacenar los productos.

**2.** Implementar un Job Batch que realice la lectura, limpieza y cargue del catálogo de productos a la base de datos. Solo debe cargar productos que tengan toda la información evitando registros duplicados.

    a. Reader: lee el producto desde el archivo csv.

    b. Processor: realiza las transformaciones a los datos leídos desde el archivo.

    c. Writer: escribe el producto en la base de datos.

**Segunda Parte:** API REST Carrito de Compras
    
**3.** Servicio que permita consultar de manera paginada los productos existentes en la base de datos. El servicio debe soportar la consulta de productos por los siguientes criterios:
        
     a. Por coincidencia del nombre completo o parte de él.
        
     b. Por rango de precios.
        
     c. Por marca.

**4.** Servicio para agregar un producto al carrito de compras. El servicio debe validar que haya existencias suficientes del producto antes de ser agregado al carrito.
  
**5.** Servicio para consultar los productos agregados en el carrito de compras.
    
**6.** Servicio para vaciar el carrito de compras.

**7.** Servicio que permita finalizar la compra de los productos existentes en el carrito de compras afectando de manera oficial las existencias de los productos en la base de datos.

**Artefactos a Entregar:**

1. Repositorio en GitHub público con el código fuente del Proyecto Java.

**Notas:**

1. Usar una base de datos en memoria (h2 por ejemplo).

2. Usar el Framework Spring dará un plus en la calificación.

3. Implementar pruebas unitarias dará un plus en la calificación.

4. Manejo de excepciones y Log4j darán un plus en la calificación.

5. Prácticas de código limpio - principios SOLID.
