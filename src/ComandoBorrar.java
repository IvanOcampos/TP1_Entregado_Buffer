/*
Trabajo Práctico 1 - Algoritmos y Estructuras de Datos III (2026)
Grupo: g_ts13
Integrantes:
    - Ivan David Ocampos Acosta - 5796207 - TS
    - Edan Roberto Bizzozzero Ortega - 5291269 - TS
Declaración de Honor:
    Declaramos que el presente trabajo es de nuestra autoría, que fue realizado respetando el código de honor académico de la institución y que toda fuente externa consultada fue debidamente referenciada.
*/

// Comando encargado de borrar el carácter inmediatamente anterior
// a la posición actual del cursor.
//
// Para poder deshacer el borrado es necesario guardar el carácter que
// fue eliminado. A diferencia de ComandoInsertar, este carácter no se
// conoce antes de ejecutar la operación, por lo que se obtiene mediante
// buffer.borrar() y se guarda en caracterBorrado.
public class ComandoBorrar implements Comando {

    private final BufferGap<Character> buffer;

    // Guarda el carácter que fue eliminado por ejecutar().
    // Es necesario para poder volver a insertarlo cuando se deshaga
    // la operación.
    private Character caracterBorrado;

    // Recibe el buffer sobre el cual se realizará la operación.
    public ComandoBorrar(BufferGap<Character> buffer) {
        this.buffer = buffer;
        this.caracterBorrado = null;
    }

    // Ejecuta el borrado del carácter anterior al cursor.
    //
    // BufferGap.borrar() utiliza una excepción chequeada, pero los métodos
    // de la interfaz Comando no declaran excepciones. Por eso, si el borrado
    // no puede realizarse, se transforma la excepción en una
    // IllegalStateException.
    @Override
    public void ejecutar() {
        try {
            caracterBorrado = buffer.borrar();
        } catch (BufferVacioException e) {
            throw new IllegalStateException(
                    "No se puede ejecutar ComandoBorrar: el buffer está vacío en esa dirección.",
                    e
            );
        }
    }

    // Recupera el carácter que había sido eliminado.
    // Se vuelve a insertar en la posición actual del cursor para dejar
    // el buffer en el estado anterior al borrado.
    @Override
    public void deshacer() {
        buffer.insertar(caracterBorrado);
    }

    // Retorna una descripción de la operación realizada.
    // Es útil para identificar el comando durante las pruebas y las trazas.
    @Override
    public String descripcion() {
        return "Borrar() -> '" + caracterBorrado + "'";
    }
}