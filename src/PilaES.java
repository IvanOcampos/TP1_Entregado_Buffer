/*
Trabajo Práctico 1 - Algoritmos y Estructuras de Datos III (2026)
Grupo: g_ts13
Integrantes:
    - Ocampos Acosta, Ivan David - 5796207 - TS
    - Bizzozzero Ortega, Edan Roberto - 5291269 - TS
Declaración de Honor:
    Declaramos que el presente trabajo es de nuestra autoría, que fue realizado respetando el código de honor académico de la institución y que toda fuente externa consultada fue debidamente referenciada.
*/

// Pila genérica implementada mediante una lista enlazada propia.
// Sigue el principio LIFO: el último elemento que se apila es el primero
// que se desapila.
//
// No se utilizan las estructuras de la API de colecciones de Java,
// ya que el TP requiere una implementación propia de la pila.
public class PilaES<E> {

    // Nodo interno utilizado para construir la lista enlazada.
    // Cada nodo almacena un elemento y una referencia al nodo que está
    // inmediatamente debajo en la pila.
    //
    // Se declara "static" a propósito: Nodo no necesita ninguna referencia
    // al PilaES que lo contiene (no llama a métodos ni accede a campos de
    // la pila externa), así que hacerlo estático evita que cada nodo cargue
    // una referencia implícita innecesaria al objeto PilaES. Sigue siendo
    // una clase anidada privada de PilaES, tal como pide el enunciado; la
    // única diferencia es que no arrastra el vínculo con la instancia
    // externa porque no lo necesita.
    private static class Nodo<E> {
        private final E valor;
        private Nodo<E> siguiente;

        private Nodo(E valor, Nodo<E> siguiente) {
            this.valor = valor;
            this.siguiente = siguiente;
        }
    }

    // Referencia al elemento que se encuentra actualmente en el tope.
    // Si la pila está vacía, tope es null.
    private Nodo<E> tope;

    // Cantidad de elementos que contiene actualmente la pila.
    private int cantidad;

    // Crea una pila vacía.
    public PilaES() {
        tope = null;
        cantidad = 0;
    }

    // Agrega un elemento al tope de la pila.
    //
    // El nuevo nodo apunta al antiguo tope y luego pasa a ser
    // el nuevo tope.
    public void apilar(E elemento) {
        tope = new Nodo<>(elemento, tope);
        cantidad++;
    }

    // Elimina y retorna el elemento que está en el tope.
    //
    // Si la pila está vacía no existe ningún elemento que pueda
    // desapilarse, por lo que se lanza PilaVaciaException.
    public E desapilar() {
        if (estavacia()) {
            throw new PilaVaciaException(
                    "No se puede desapilar: la pila está vacía."
            );
        }

        E valor = tope.valor;
        tope = tope.siguiente;
        cantidad--;

        return valor;
    }

    // Retorna el elemento que está en el tope sin eliminarlo.
    //
    // Si la pila está vacía, se lanza PilaVaciaException porque no
    // existe un elemento que pueda ser consultado.
    public E tope() {
        if (estavacia()) {
            throw new PilaVaciaException(
                    "No se puede consultar el tope: la pila está vacía."
            );
        }

        return tope.valor;
    }

    // Indica si la pila no contiene ningún elemento.
    public boolean estavacia() {
        return cantidad == 0;
    }

    // Retorna la cantidad de elementos almacenados en la pila.
    public int size() {
        return cantidad;
    }

    // Excepción no chequeada que se utiliza cuando se intenta operar
    // sobre una pila vacía.
    //
    // Es no chequeada porque el intento de desapilar o consultar el tope
    // de una pila vacía representa un error de uso de la estructura.
    // El código cliente debería verificar previamente si la pila contiene
    // elementos mediante estavacia() antes de realizar la operación.
    //
    // Al extender RuntimeException, Java no obliga a utilizar try/catch
    // ni a declarar throws en cada método que pueda producirla.
    public static class PilaVaciaException extends RuntimeException {

        public PilaVaciaException(String mensaje) {
            super(mensaje);
        }
    }
}