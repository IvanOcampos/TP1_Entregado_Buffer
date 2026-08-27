/*
Trabajo Práctico 1 - Algoritmos y Estructuras de Datos III (2026)
Grupo: g_ts13
Integrantes:
    - Ivan David Ocampos Acosta - 5796207 - TS
    - Edan Roberto Bizzozzero Ortega - 5291269 - TS
Declaración de Honor:
    Declaramos que el presente trabajo es de nuestra autoría, que fue realizado respetando el código de honor académico de la institución y que toda fuente externa consultada fue debidamente referenciada.
*/

import java.util.Iterator;

public class BufferGap<E> implements Iterable<E> {

    // Capacidad inicial solicitada en el enunciado.
    private static final int TAM_INICIAL = 16;

    // Arreglo donde se almacenan físicamente los elementos.
    private E[] datos;

    // El inicio del hueco coincide con la posición lógica del cursor.
    private int inicioHueco;

    // Primer índice después del hueco.
    private int finHueco;

    // Cuenta cuántas veces un elemento fue trasladado de una celda
    // física a otra, ya sea al mover el cursor o al aumentar la capacidad.
    private long desplazamientos;

    // Crea un buffer vacío. Al principio todo el arreglo es el hueco,
    // por lo que el cursor comienza en la posición 0.
    @SuppressWarnings("unchecked")
    public BufferGap() {
        datos = (E[]) new Object[TAM_INICIAL];
        inicioHueco = 0;
        finHueco = TAM_INICIAL;
        desplazamientos = 0;
    }

    // Inserta el elemento en la posición actual del cursor.
    // No es necesario desplazar otros elementos: simplemente se utiliza
    // la primera posición disponible del hueco.
    public void insertar(E obj) {
        if (inicioHueco == finHueco) {
            duplicarCapacidad();
        }

        datos[inicioHueco] = obj;
        inicioHueco++;
    }

    // Aumenta al doble la capacidad del arreglo cuando el hueco se agota.
    //
    // Los elementos que están antes del cursor se mantienen al comienzo
    // del nuevo arreglo. Los elementos posteriores se trasladan al final.
    // De esta forma se conserva el contenido lógico y se obtiene un hueco
    // más grande entre ambos grupos.
    @SuppressWarnings("unchecked")
    private void duplicarCapacidad() {
        int capacidadVieja = datos.length;
        int capacidadNueva = capacidadVieja * 2;

        E[] datosNuevos = (E[]) new Object[capacidadNueva];

        // Copiamos los elementos que están antes del cursor.
        for (int i = 0; i < inicioHueco; i++) {
            datosNuevos[i] = datos[i];
            desplazamientos++;
        }

        // Copiamos los elementos que están después del cursor
        // y los colocamos al final del nuevo arreglo.
        int cantidadFinal = capacidadVieja - finHueco;
        int nuevoFinHueco = capacidadNueva - cantidadFinal;

        for (int i = 0; i < cantidadFinal; i++) {
            datosNuevos[nuevoFinHueco + i] = datos[finHueco + i];
            desplazamientos++;
        }

        finHueco = nuevoFinHueco;
        datos = datosNuevos;
    }

    // Elimina el elemento inmediatamente anterior al cursor.
    // Para borrar no es necesario desplazar elementos: el hueco
    // simplemente aumenta una posición hacia la izquierda.
    public E borrar() throws BufferVacioException {
        if (inicioHueco == 0) {
            throw new BufferVacioException(
                    "No hay elementos antes del cursor para borrar."
            );
        }

        inicioHueco--;

        E borrado = datos[inicioHueco];
        datos[inicioHueco] = null;

        return borrado;
    }

    // Mueve el cursor la cantidad de posiciones indicada por delta.
    // Si delta es positivo, el cursor se mueve hacia la derecha.
    // Si delta es negativo, se mueve hacia la izquierda.
    //
    // Al mover el cursor sí es necesario trasladar elementos de un lado
    // del hueco al otro. Cada elemento trasladado aumenta el contador.
    public void moverCursor(int delta) {
        int destino = inicioHueco + delta;

        if (destino < 0 || destino > size()) {
            throw new PosicionInvalidaException(
                    "Posición de cursor inválida: " + destino +
                            ". Rango válido: [0, " + size() + "]."
            );
        }

        if (delta > 0) {

            // Al movernos hacia la derecha, el siguiente elemento que está
            // después del hueco pasa a ocupar la primera posición del hueco.
            for (int i = 0; i < delta; i++) {
                datos[inicioHueco] = datos[finHueco];
                datos[finHueco] = null;

                inicioHueco++;
                finHueco++;

                desplazamientos++;
            }

        } else if (delta < 0) {

            // Al movernos hacia la izquierda, el elemento anterior al hueco
            // pasa a ocupar la última posición del hueco.
            int pasos = -delta;

            for (int i = 0; i < pasos; i++) {
                inicioHueco--;
                finHueco--;

                datos[finHueco] = datos[inicioHueco];
                datos[inicioHueco] = null;

                desplazamientos++;
            }
        }

        // Si delta es 0, el cursor permanece en la misma posición.
    }

    // Retorna la posición lógica actual del cursor.
    // El cursor coincide con inicioHueco.
    public int posicionCursor() {
        return inicioHueco;
    }

    // La cantidad de elementos se obtiene restando el tamaño del hueco
    // a la capacidad total del arreglo.
    public int size() {
        return datos.length - (finHueco - inicioHueco);
    }

    // Retorna la capacidad actual del arreglo interno.
    public int capacidad() {
        return datos.length;
    }

    // Retorna la cantidad acumulada de desplazamientos.
    public long desplazamientos() {
        return desplazamientos;
    }

    // Reinicia el contador para poder medir una nueva secuencia
    // de operaciones de forma independiente.
    public void reiniciarDesplazamientos() {
        desplazamientos = 0;
    }

    // Convierte una posición lógica en una posición física del arreglo.
    //
    // Si el índice está antes del hueco, coincide con el índice físico.
    // Si está después del hueco, hay que sumar el tamaño del hueco
    // para encontrar su posición física.
    private int indiceFisico(int index) {
        return index < inicioHueco
                ? index
                : index + (finHueco - inicioHueco);
    }

    // Obtiene un elemento mediante su posición lógica.
    // Primero se verifica que el índice esté dentro del rango permitido.
    public E get(int index) {
        if (index < 0 || index >= size()) {
            throw new PosicionInvalidaException(
                    "Índice fuera de rango: " + index +
                            ". Rango válido: [0, " + size() + ")."
            );
        }

        return datos[indiceFisico(index)];
    }

    // Reemplaza el elemento ubicado en la posición lógica indicada
    // y retorna el elemento que estaba anteriormente en esa posición.
    public E set(E obj, int index) {
        if (index < 0 || index >= size()) {
            throw new PosicionInvalidaException(
                    "Índice fuera de rango: " + index +
                            ". Rango válido: [0, " + size() + ")."
            );
        }

        int posicion = indiceFisico(index);
        E anterior = datos[posicion];

        datos[posicion] = obj;

        return anterior;
    }

    // Permite recorrer el buffer con un for-each.
    // El recorrido utiliza las posiciones lógicas y por eso no devuelve
    // las posiciones que forman parte del hueco.
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int actual = 0;

            @Override
            public boolean hasNext() {
                return actual < size();
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new PosicionInvalidaException(
                            "No hay más elementos para iterar."
                    );
                }

                E elemento = get(actual);
                actual++;

                return elemento;
            }
        };
    }

    // Muestra los elementos en su orden lógico y coloca una barra |
    // en la posición actual del cursor.
    //
    // Por ejemplo, si el contenido es HoXla y el cursor está entre X y l,
    // el resultado será HoX|la.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int n = size();

        for (int i = 0; i < n; i++) {

            if (i == inicioHueco) {
                sb.append('|');
            }

            sb.append(get(i));
        }

        // Si el cursor está al final del contenido, agregamos la barra
        // después del último elemento.
        if (inicioHueco == n) {
            sb.append('|');
        }

        return sb.toString();
    }
}