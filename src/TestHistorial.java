/*
Trabajo Práctico 1 - Algoritmos y Estructuras de Datos III (2026)
Grupo: g_ts13
Integrantes:
    - Ocampos Acosta, Ivan David - 5796207 - TS
    - Bizzozzero Ortega, Edan Roberto - 5291269 - TS
Declaración de Honor:
    Declaramos que el presente trabajo es de nuestra autoría, que fue realizado respetando el código de honor académico de la institución y que toda fuente externa consultada fue debidamente referenciada.
*/

// Reproduce EXACTAMENTE la traza obligatoria del Ejercicio 2 (tabla de
// 12 pasos del enunciado), partiendo del buffer en el estado "HoX|la"
// (cursor en la posición 3).
//
// Ese estado inicial se construye desde cero en este archivo (no se
// reutiliza el objeto BufferGap de TestBufferGap, que para ese punto ya
// fue mutado por el borrado de demostración, el forzado de la excepción
// y el forzado del redimensionamiento).
//
// En cada paso se imprime: número de paso, operación realizada, valor
// de retorno cuando corresponde (deshacer()/rehacer() devuelven boolean),
// contenido del buffer y tamaño de ambas pilas. La salida debe coincidir,
// fila por fila, con la tabla del enunciado.
public class TestHistorial {

    public static void main(String[] args) {

        // --- Construcción del estado inicial "HoX|la" (cursor = 3) ---
        BufferGap<Character> buffer = new BufferGap<>();

        buffer.insertar('H');
        buffer.insertar('o');
        buffer.insertar('l');
        buffer.insertar('a');
        buffer.moverCursor(-2);
        buffer.insertar('X');

        System.out.println("Estado inicial esperado: \"HoX|la\"");
        System.out.println("Estado inicial obtenido: \"" + buffer + "\"");
        System.out.println();

        HistorialEdicion historial = new HistorialEdicion();

        imprimirEstado(0, "(estado inicial)", null, buffer, historial);

        // --- Paso 1: Insertar('!') -> "HoX!|la", desh=1, reh=0 ---
        historial.ejecutar(new ComandoInsertar(buffer, '!'));
        imprimirEstado(1, "Insertar('!')", null, buffer, historial);

        // --- Paso 2: Insertar('?') -> "HoX!?|la", desh=2, reh=0 ---
        historial.ejecutar(new ComandoInsertar(buffer, '?'));
        imprimirEstado(2, "Insertar('?')", null, buffer, historial);

        // --- Paso 3: deshacer() -> true : deshace '?' -> "HoX!|la", desh=1, reh=1 ---
        boolean r3 = historial.deshacer();
        imprimirEstado(3, "deshacer()", r3, buffer, historial);

        // --- Paso 4: deshacer() -> true : deshace '!' -> "HoX|la", desh=0, reh=2 ---
        boolean r4 = historial.deshacer();
        imprimirEstado(4, "deshacer()", r4, buffer, historial);

        // --- Paso 5: rehacer() -> true : rehace '!' -> "HoX!|la", desh=1, reh=1 ---
        boolean r5 = historial.rehacer();
        imprimirEstado(5, "rehacer()", r5, buffer, historial);

        // --- Paso 6: MoverCursor(-4) -> "|HoX!la", desh=2, reh=0 (descarta rehacer) ---
        historial.ejecutar(new ComandoMoverCursor(buffer, -4));
        imprimirEstado(6, "MoverCursor(-4)", null, buffer, historial);

        // --- Paso 7: rehacer() -> false : no hay nada que rehacer ---
        boolean r7 = historial.rehacer();
        imprimirEstado(7, "rehacer()", r7, buffer, historial);

        // --- Paso 8: deshacer() -> true : deshace moverCursor -> "HoX!|la", desh=1, reh=1 ---
        boolean r8 = historial.deshacer();
        imprimirEstado(8, "deshacer()", r8, buffer, historial);

        // --- Paso 9: Borrar() -> "HoX|la", desh=2, reh=0 (borra '!', descarta rehacer) ---
        historial.ejecutar(new ComandoBorrar(buffer));
        imprimirEstado(9, "Borrar()", null, buffer, historial);

        // --- Paso 10: deshacer() -> true : reaparece '!' -> "HoX!|la", desh=1, reh=1 ---
        boolean r10 = historial.deshacer();
        imprimirEstado(10, "deshacer()", r10, buffer, historial);

        // --- Paso 11: deshacer() -> true -> "HoX|la", desh=0, reh=2 ---
        boolean r11 = historial.deshacer();
        imprimirEstado(11, "deshacer()", r11, buffer, historial);

        // --- Paso 12: deshacer() -> false : pila vacía, no hace nada ---
        boolean r12 = historial.deshacer();
        imprimirEstado(12, "deshacer()", r12, buffer, historial);

        System.out.println();
        System.out.println(
                "Comparar la salida de arriba, fila por fila, con la tabla "
                        + "\"Traza obligatoria a reproducir\" del enunciado."
        );
    }

    // Imprime el número de paso, la operación, el valor de retorno
    // (cuando aplica), el contenido del buffer y el tamaño de ambas
    // pilas del historial, en un formato fácil de comparar con la tabla.
    private static void imprimirEstado(
            int numeroPaso,
            String operacion,
            Boolean retorno,
            BufferGap<Character> buffer,
            HistorialEdicion historial
    ) {

        String retornoTexto = (retorno == null) ? "" : (" -> " + retorno);

        System.out.printf(
                "#%2d [%-24s] contenido=\"%-8s\" pilaDeshacer=%d pilaRehacer=%d%n",
                numeroPaso,
                operacion + retornoTexto,
                buffer.toString(),
                historial.sizeDeshacer(),
                historial.sizeRehacer()
        );
    }
}