/*
Trabajo Práctico 1 - Algoritmos y Estructuras de Datos III (2026)
Grupo: g_ts13
Integrantes:
    - Ivan David Ocampos Acosta - 5796207 - TS
    - Edan Roberto Bizzozzero Ortega - 5291269 - TS
Declaración de Honor:
    Declaramos que el presente trabajo es de nuestra autoría, que fue realizado respetando el código de honor académico de la institución y que toda fuente externa consultada fue debidamente referenciada.
*/

// Excepción chequeada que se utiliza cuando se intenta borrar un elemento
// y no existen elementos antes de la posición actual del cursor.
//
// Es una excepción chequeada porque representa una situación que puede
// ocurrir normalmente durante el uso del buffer y que el programa puede
// manejar. Por ejemplo, si el cursor está en la posición 0 y se intenta
// borrar, el código cliente puede decidir ignorar la operación o informar
// al usuario.
//
// Al extender Exception, Java obliga al código que utiliza borrar() a
// manejar la excepción mediante try/catch o declararla con throws.
// Esto evita que el caso de intentar borrar sin elementos sea ignorado.
public class BufferVacioException extends Exception {

    // Crea la excepción utilizando el mensaje recibido para describir
    // el motivo por el cual no se pudo realizar el borrado.
    public BufferVacioException(String mensaje) {

        super(mensaje);
    }
}

