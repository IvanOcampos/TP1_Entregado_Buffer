/*
Trabajo Práctico 1 - Algoritmos y Estructuras de Datos III (2026)
Grupo: g_ts13
Integrantes:
    - Ivan David Ocampos Acosta
Declaración de Honor:
    Declaramos que el presente trabajo es de nuestra autoría, que fue realizado respetando el código de honor académico de la institución y que toda fuente externa consultada fue debidamente referenciada.
*/

// Comando encargado de insertar un carácter en la posición actual
// del cursor.
//
// Para poder deshacer la operación solo necesitamos guardar el carácter.
// No hace falta guardar la posición porque, después de insertar,
// el carácter queda inmediatamente antes del cursor. Por lo tanto,
// buffer.borrar() permite eliminar exactamente el carácter insertado.
public class ComandoInsertar implements Comando {

    private final BufferGap<Character> buffer;
    private final char caracter;

    // Recibe el buffer donde se realizará la operación y el carácter
    // que se debe insertar.
    public ComandoInsertar(BufferGap<Character> buffer, char caracter) {
        this.buffer = buffer;
        this.caracter = caracter;
    }

    // Ejecuta la inserción del carácter en la posición actual del cursor.
    @Override
    public void ejecutar() {
        buffer.insertar(caracter);
    }

    // Deshace la inserción eliminando el carácter que acaba de ser
    // insertado. Si este comando fue ejecutado correctamente, debe existir
    // un elemento antes del cursor para poder realizar el borrado.
    @Override
    public void deshacer() {
        try {
            buffer.borrar();
        } catch (BufferVacioException e) {
            // Esta situación no debería ocurrir si el estado del buffer
            // es consistente, porque ejecutar() insertó previamente
            // el carácter que ahora se intenta borrar.
            throw new IllegalStateException(
                    "Estado inconsistente al deshacer ComandoInsertar",
                    e
            );
        }
    }

    // Retorna una descripción breve del comando para facilitar
    // las pruebas y el seguimiento del historial.
    @Override
    public String descripcion() {
        return "Insertar('" + caracter + "')";
    }
}