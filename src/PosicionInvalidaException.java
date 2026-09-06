/*
Trabajo Práctico 1 - Algoritmos y Estructuras de Datos III (2026)
Grupo: g_ts13
Integrantes:
    - Ivan David Ocampos Acosta
Declaración de Honor:
    Declaramos que el presente trabajo es de nuestra autoría, que fue realizado respetando el código de honor académico de la institución y que toda fuente externa consultada fue debidamente referenciada.
*/

// Excepción no chequeada utilizada cuando se intenta acceder,
// modificar o mover el cursor a una posición que está fuera del
// rango permitido.
//
// Es no chequeada porque representa un error de programación del
// código que utiliza la estructura. El cliente debe enviar índices
// válidos y puede comprobar previamente el tamaño del buffer cuando
// sea necesario.
//
// Al extender RuntimeException, Java no obliga a capturar esta excepción
// mediante try/catch ni a declararla con throws. De esta manera,
// operaciones como get() y set() pueden utilizarse sin agregar
// manejo obligatorio de excepciones en cada llamada.
public class PosicionInvalidaException extends RuntimeException {

    // Crea la excepción con un mensaje que permite identificar
    // la posición que produjo el error y el rango esperado.
    public PosicionInvalidaException(String mensaje) {
        super(mensaje);
    }
}