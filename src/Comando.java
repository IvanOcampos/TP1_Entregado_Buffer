/*
Trabajo Práctico 1 - Algoritmos y Estructuras de Datos III (2026)
Grupo: g_ts13
Integrantes:
    - Ivan David Ocampos Acosta
Declaración de Honor:
    Declaramos que el presente trabajo es de nuestra autoría, que fue realizado respetando el código de honor académico de la institución y que toda fuente externa consultada fue debidamente referenciada.
*/

// Interfaz que representa un comando dentro del historial de edición.
// Cada comando representa una operación que puede ejecutarse y luego
// deshacerse. Esto permite implementar las funciones Ctrl+Z y Ctrl+Y.
//
// Los comandos concretos deben guardar el estado mínimo necesario para
// poder revertir la operación que realizaron sobre el BufferGap.
public interface Comando {

    // Ejecuta la operación representada por el comando.
    void ejecutar();

    // Deshace la operación realizada por ejecutar(), dejando el buffer
    // en el estado que tenía antes de ejecutar el comando.
    void deshacer();

    // Retorna una descripción breve de la operación.
    // Se puede utilizar para mostrar las operaciones durante las trazas
    // y facilitar la comprobación del funcionamiento del historial.
    String descripcion();
}
