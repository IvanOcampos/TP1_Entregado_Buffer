/*
Trabajo Práctico 1 - Algoritmos y Estructuras de Datos III (2026)
Grupo: g_ts13
Integrantes:
    - Ivan David Ocampos Acosta
Declaración de Honor:
    Declaramos que el presente trabajo es de nuestra autoría, que fue realizado respetando el código de honor académico de la institución y que toda fuente externa consultada fue debidamente referenciada.
*/

// Comando encargado de mover el cursor una determinada cantidad
// de posiciones.
//
// Para deshacer el movimiento solo es necesario guardar delta,
// ya que aplicar el valor contrario (-delta) lleva el cursor nuevamente
// a la posición que tenía antes de ejecutar el comando.
public class ComandoMoverCursor implements Comando {

    private final BufferGap<Character> buffer;
    private final int delta;

    // Recibe el buffer sobre el cual se moverá el cursor y la cantidad
    // de posiciones que debe desplazarse.
    // Un valor positivo mueve el cursor hacia la derecha y uno negativo
    // lo mueve hacia la izquierda.
    public ComandoMoverCursor(BufferGap<Character> buffer, int delta) {
        this.buffer = buffer;
        this.delta = delta;
    }

    // Ejecuta el movimiento del cursor utilizando el desplazamiento indicado.
    @Override
    public void ejecutar() {
        buffer.moverCursor(delta);
    }

    // Deshace el movimiento aplicando el desplazamiento contrario.
    // Si originalmente se movió delta posiciones, mover -delta
    // devuelve el cursor a su posición anterior.
    @Override
    public void deshacer() {
        buffer.moverCursor(-delta);
    }

    // Retorna una descripción breve del movimiento realizado.
    @Override
    public String descripcion() {
        return "MoverCursor(" + delta + ")";
    }
}