/*
Trabajo Práctico 1 - Algoritmos y Estructuras de Datos III (2026)
Grupo: g_ts13
Integrantes:
    - Ocampos Acosta, Ivan David
Declaración de Honor:
    Declaramos que el presente trabajo es de nuestra autoría, que fue realizado respetando el código de honor académico de la institución y que toda fuente externa consultada fue debidamente referenciada.
*/

import java.util.Random;

// Batería de pruebas para verificar el funcionamiento de BufferGap.
//
// Se prueban las operaciones básicas mediante una traza paso a paso,
// una prueba masiva con 100.000 caracteres y una comparación empírica
// de desplazamientos contra un arreglo simple.
//
// La prueba también permite observar la posición del hueco, la capacidad
// del buffer y la cantidad acumulada de desplazamientos.
public class TestBufferGap {

    public static void main(String[] args) throws BufferVacioException {

        System.out.println("========================================");
        System.out.println(" FASE A y FASE B: traza paso a paso");
        System.out.println("========================================");

        BufferGap<Character> buffer = new BufferGap<>();

        faseA(buffer);
        faseB(buffer);

        System.out.println();
        System.out.println("========================================");
        System.out.println(" PRUEBA MASIVA: 100.000 caracteres aleatorios");
        System.out.println("========================================");

        pruebaMasiva();

        System.out.println();
        System.out.println("========================================");
        System.out.println(" PRUEBA EMPIRICA DE DESPLAZAMIENTOS");
        System.out.println("========================================");

        pruebaEmpiricaDesplazamientos();
    }

    // FASE A: construye el contenido "Hola" mediante inserciones.
    //
    // Como cada carácter se inserta en la posición actual del cursor
    // y el cursor queda después del carácter insertado, el resultado
    // final debe ser "Hola|".
    private static void faseA(BufferGap<Character> buffer) {

        System.out.println("--- Fase A: inserciones iniciales ---");

        char[] letras = {'H', 'o', 'l', 'a'};

        for (char c : letras) {
            buffer.insertar(c);
            imprimirEstado(buffer, "insertar('" + c + "')");
        }
    }

    // FASE B: mueve el cursor hacia el medio del contenido, inserta
    // un carácter, demuestra la traducción de índice lógico a físico
    // con get(4) y finalmente demuestra borrar().
    //
    // Reproduce exactamente el orden de la tabla "Fase B" del enunciado:
    // moverCursor(-2) -> insertar('X') -> get(4) -> borrar().
    //
    // IMPORTANTE: según la nota del enunciado, el estado que continúa
    // en el Ejercicio 2 es "HoX|la" con inicioHueco=3, es decir, el que
    // queda inmediatamente después de get(4). Todo lo que sigue después
    // (la demostración de borrar(), forzar la excepción y forzar el
    // redimensionamiento) es exploratorio y no debe arrastrarse: por eso
    // TestHistorial construye su propio BufferGap desde cero.
    private static void faseB(BufferGap<Character> buffer) {

        System.out.println("--- Fase B: mover cursor e insertar en el medio ---");

        buffer.moverCursor(-2);
        imprimirEstado(buffer, "moverCursor(-2)");

        buffer.insertar('X');
        imprimirEstado(buffer, "insertar('X')");

        System.out.println("Estado esperado por enunciado: \"HoX|la\"");
        System.out.println("Estado obtenido:                \"" + buffer + "\"");
        System.out.println();

        System.out.println("--- Fase B (cont.): traducción de índice lógico a físico con get(4) ---");

        // Antes de leer, guardamos el contador de desplazamientos para
        // demostrar que get() no desplaza absolutamente nada: es una
        // traducción directa (comparación + suma), no un recorrido.
        long desplazamientosAntesDeGet = buffer.desplazamientos();

        Character elementoEnCuatro = buffer.get(4);

        long desplazamientosDespuesDeGet = buffer.desplazamientos();

        System.out.println("get(4) -> '" + elementoEnCuatro + "'");
        System.out.println("Valor esperado por enunciado: 'a'");
        imprimirEstado(buffer, "get(4) -> '" + elementoEnCuatro + "'");

        System.out.println(
                "¿get(4) modificó el contador de desplazamientos? " +
                        (desplazamientosAntesDeGet != desplazamientosDespuesDeGet)
                        + " (debe ser false: get() traduce el índice, no recorre nada)"
        );

        System.out.println();
        System.out.println(
                "*** Estado que se lleva al Ejercicio 2: \"" + buffer
                        + "\" con inicioHueco=" + buffer.posicionCursor()
                        + " (inmediatamente después de get(4)). "
                        + "Lo que sigue a continuación es solo exploratorio. ***"
        );

        System.out.println();
        System.out.println("--- Fase B (cont.): borrado y manejo de excepción ---");

        try {
            Character borrado = buffer.borrar();
            imprimirEstado(buffer, "borrar() -> '" + borrado + "'");
        } catch (BufferVacioException e) {
            System.out.println("No se pudo borrar: " + e.getMessage());
        }

        // Llevamos el cursor hasta la posición 0 para provocar
        // intencionalmente el caso en el que no existe ningún elemento
        // anterior al cursor.
        while (buffer.posicionCursor() > 0) {
            buffer.moverCursor(-1);
        }

        // Se espera que borrar() lance BufferVacioException porque
        // el cursor se encuentra en la posición 0.
        try {
            buffer.borrar();
        } catch (BufferVacioException e) {
            System.out.println(
                    "Excepción esperada al borrar con cursor en 0: "
                            + e.getMessage()
            );
        }

        System.out.println();
        System.out.println("--- Fase B (cont.): forzando redimensión ---");

        int capacidadPrevia = buffer.capacidad();

        // Movemos el cursor hasta el final para que las nuevas inserciones
        // se realicen consecutivamente en esa posición.
        buffer.moverCursor(
                buffer.size() - buffer.posicionCursor()
        );

        // Insertamos elementos hasta superar la capacidad disponible.
        // Esto obliga al BufferGap a duplicar la capacidad del arreglo.
        int i = 0;

        while (buffer.capacidad() == capacidadPrevia) {
            buffer.insertar((char) ('a' + (i % 26)));
            i++;
        }

        imprimirEstado(
                buffer,
                "inserciones sucesivas hasta duplicar capacidad"
        );

        System.out.println(
                "Capacidad antes: " + capacidadPrevia
                        + " -> Capacidad después: " + buffer.capacidad()
        );
    }

    // Muestra el estado interno relevante del BufferGap después
    // de cada operación.
    //
    // inicioHueco coincide con la posición lógica del cursor.
    // El tamaño del hueco se obtiene como capacidad - size().
    private static void imprimirEstado(
            BufferGap<Character> buffer,
            String operacion
    ) {

        int inicioHueco = buffer.posicionCursor();

        // El final del hueco se obtiene sumando a inicioHueco
        // el tamaño actual del hueco.
        int finHueco =
                inicioHueco + (buffer.capacidad() - buffer.size());

        System.out.printf(
                "[%-40s] contenido=\"%s\" inicioHueco=%d "
                        + "finHueco=%d capacidad=%d desplazamientos=%d%n",
                operacion,
                buffer,
                inicioHueco,
                finHueco,
                buffer.capacidad(),
                buffer.desplazamientos()
        );
    }

    // PRUEBA MASIVA:
    // Inserta 100.000 caracteres aleatorios y verifica que el buffer
    // conserve correctamente la cantidad y el orden de los elementos.
    private static void pruebaMasiva() {

        final int N = 100_000;

        // Se utiliza una semilla fija para que la prueba sea reproducible.
        Random random = new Random(42);

        BufferGap<Character> buffer = new BufferGap<>();

        // Arreglo auxiliar propio utilizado para comparar el contenido
        // esperado con el contenido obtenido del BufferGap.
        // No se utiliza ArrayList ni otra colección de Java.
        char[] esperado = new char[N];

        for (int i = 0; i < N; i++) {

            char c = (char) ('a' + random.nextInt(26));

            buffer.insertar(c);
            esperado[i] = c;
        }

        System.out.println("Elementos insertados: " + N);
        System.out.println("size() del buffer:    " + buffer.size());

        boolean cantidadOk = buffer.size() == N;

        boolean ordenOk = true;
        int idx = 0;

        // El for-each utiliza el iterator() implementado por BufferGap.
        // Se compara cada elemento obtenido con el arreglo esperado.
        for (char c : buffer) {

            if (idx >= N || c != esperado[idx]) {
                ordenOk = false;
                break;
            }

            idx++;
        }

        ordenOk = ordenOk && (idx == N);

        System.out.println("¿Cantidad correcta?   " + cantidadOk);
        System.out.println("¿Orden correcto?       " + ordenOk);

        if (cantidadOk && ordenOk) {
            System.out.println("PRUEBA MASIVA: OK");
        } else {
            System.out.println("PRUEBA MASIVA: FALLO");
        }
    }

    // Estructura simple utilizada como referencia para comparar
    // la cantidad de desplazamientos con BufferGap.
    //
    // En cada inserción en una posición intermedia se deben desplazar
    // manualmente los elementos que se encuentran a la derecha.
    // Se utiliza solamente para realizar la comparación empírica.
    private static class ArregloSimple<E> {

        private E[] datos;
        private int cantidad;
        private long desplazamientos;

        @SuppressWarnings("unchecked")
        ArregloSimple(int capacidadInicial) {
            datos = (E[]) new Object[capacidadInicial];
            cantidad = 0;
            desplazamientos = 0;
        }

        // Inserta un elemento en una posición determinada.
        //
        // Para abrir espacio, se desplazan los elementos hacia la derecha
        // comenzando desde el último elemento y avanzando hacia la posición
        // de inserción.
        void insertarEn(int posicion, E valor) {

            for (int i = cantidad; i > posicion; i--) {
                datos[i] = datos[i - 1];
                desplazamientos++;
            }

            datos[posicion] = valor;
            cantidad++;
        }

        // Retorna la cantidad acumulada de desplazamientos.
        long desplazamientos() {
            return desplazamientos;
        }

        // Reinicia el contador para realizar una nueva medición.
        void reiniciarDesplazamientos() {
            desplazamientos = 0;
        }

        // Retorna la cantidad de elementos almacenados.
        int size() {
            return cantidad;
        }
    }

    // Compara empíricamente la cantidad de desplazamientos producidos
    // por BufferGap y por un arreglo simple al realizar muchas
    // inserciones en el centro.
    private static void pruebaEmpiricaDesplazamientos() {

        int[] tamanios = {100_000, 200_000, 1_000_000};
        int insercionesEnMedio = 10_000;

        System.out.printf(
                "%-15s %-25s %-25s%n",
                "n",
                "BufferGap (desplaz.)",
                "ArregloSimple (desplaz.)"
        );

        System.out.println(
                "---------------------------------------------------------------------"
        );

        for (int n : tamanios) {

            // Preparamos ambas estructuras con n elementos iniciales.

            BufferGap<Character> bg = new BufferGap<>();

            for (int i = 0; i < n; i++) {
                bg.insertar('a');
            }

            ArregloSimple<Character> arr =
                    new ArregloSimple<>(n + insercionesEnMedio + 16);

            for (int i = 0; i < n; i++) {
                arr.insertarEn(arr.size(), 'a');
            }

            // BUFFER GAP:
            //
            // Movemos el cursor una sola vez hasta el centro.
            // Después realizamos todas las inserciones en esa posición.
            //
            // Una vez ubicado el hueco, insertar() puede utilizar
            // directamente las posiciones libres sin desplazar nuevamente
            // los elementos que están a la derecha.
            bg.reiniciarDesplazamientos();

            bg.moverCursor(
                    n / 2 - bg.posicionCursor()
            );

            for (int i = 0; i < insercionesEnMedio; i++) {
                bg.insertar('X');
            }

            long desplazamientosBg = bg.desplazamientos();

            // ARREGLO SIMPLE:
            //
            // Cada inserción en n/2 necesita desplazar nuevamente todos
            // los elementos posteriores a la posición de inserción.
            arr.reiniciarDesplazamientos();

            for (int i = 0; i < insercionesEnMedio; i++) {
                arr.insertarEn(n / 2, 'X');
            }

            long desplazamientosArr = arr.desplazamientos();

            System.out.printf(
                    "%-15d %-25d %-25d%n",
                    n,
                    desplazamientosBg,
                    desplazamientosArr
            );
        }

        System.out.println();

        // El BufferGap concentra el costo de desplazamiento en el momento
        // en que se mueve el cursor hasta la zona de edición.
        //
        // Una vez ubicado el hueco, las inserciones consecutivas en esa
        // posición no necesitan desplazar los elementos restantes.
        //
        // En el arreglo simple, cada inserción en el centro requiere
        // desplazar nuevamente los elementos ubicados a la derecha.
        //
        // Por eso, cuando se realizan muchas inserciones en una zona
        // localizada, BufferGap puede reducir considerablemente la cantidad
        // de movimientos realizados en comparación con un arreglo simple.
        System.out.println(
                "Análisis: el BufferGap concentra los desplazamientos en el "
                        + "movimiento del cursor hacia la zona de edición y en las "
                        + "redimensiones necesarias. Una vez ubicado el hueco, las "
                        + "inserciones consecutivas pueden utilizar directamente las "
                        + "celdas disponibles sin mover nuevamente el resto de los "
                        + "elementos. En cambio, ArregloSimple debe desplazar los "
                        + "elementos posteriores en cada inserción realizada en el "
                        + "centro. Por esta razón, la cantidad de desplazamientos del "
                        + "arreglo simple crece mucho más rápido cuando aumenta la "
                        + "cantidad de inserciones."
        );
    }
}