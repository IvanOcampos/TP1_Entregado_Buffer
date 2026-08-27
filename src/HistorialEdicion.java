/*
Trabajo Práctico 1 - Algoritmos y Estructuras de Datos III (2026)
Grupo: g_ts13
Integrantes:
    - Ocampos Acosta, Ivan David - 5796207 - TS
    - Bizzozzero Ortega, Edan Roberto - 5291269 - TS
Declaración de Honor:
    Declaramos que el presente trabajo es de nuestra autoría, que fue realizado respetando el código de honor académico de la institución y que toda fuente externa consultada fue debidamente referenciada.
*/

// Administra el historial de edición utilizando dos pilas:
// una para los comandos que se pueden deshacer y otra para los comandos
// que se pueden rehacer.
//
// La pila de deshacer contiene los comandos que ya fueron ejecutados.
// Cuando se deshace un comando, este pasa a la pila de rehacer.
// Si después se ejecuta un comando nuevo, se elimina todo el historial
// de rehacer porque esas operaciones ya no corresponden al nuevo estado
// del documento.
public class HistorialEdicion {

    private final PilaES<Comando> pilaDeshacer;
    private final PilaES<Comando> pilaRehacer;

    // Crea las dos pilas vacías para comenzar un nuevo historial.
    public HistorialEdicion() {
        pilaDeshacer = new PilaES<>();
        pilaRehacer = new PilaES<>();
    }

    // Ejecuta un nuevo comando y lo guarda en la pila de deshacer.
    //
    // Al realizar una nueva edición después de haber deshecho operaciones,
    // la pila de rehacer debe quedar vacía. Por ejemplo:
    // A -> B -> deshacer B -> ejecutar C
    // En este caso, B ya no puede rehacerse porque C creó una nueva
    // continuación del historial.
    public void ejecutar(Comando c) {
        c.ejecutar();
        pilaDeshacer.apilar(c);
        vaciarPilaRehacer();
    }

    // Deshace el último comando ejecutado.
    //
    // El comando se retira de la pila de deshacer, se ejecuta su método
    // deshacer() y luego se coloca en la pila de rehacer para poder
    // recuperarlo posteriormente con rehacer().
    //
    // Retorna false si no hay ningún comando disponible para deshacer.
    public boolean deshacer() {
        if (pilaDeshacer.estavacia()) {
            return false;
        }

        Comando c = pilaDeshacer.desapilar();
        c.deshacer();
        pilaRehacer.apilar(c);

        return true;
    }

    // Rehace el último comando que había sido deshecho.
    //
    // El comando se retira de la pila de rehacer, se ejecuta nuevamente
    // y vuelve a la pila de deshacer.
    //
    // Retorna false si no hay ningún comando disponible para rehacer.
    public boolean rehacer() {
        if (pilaRehacer.estavacia()) {
            return false;
        }

        Comando c = pilaRehacer.desapilar();
        c.ejecutar();
        pilaDeshacer.apilar(c);

        return true;
    }

    // Retorna la cantidad de comandos disponibles para deshacer.
    public int sizeDeshacer() {
        return pilaDeshacer.size();
    }

    // Retorna la cantidad de comandos disponibles para rehacer.
    public int sizeRehacer() {
        return pilaRehacer.size();
    }

    // Vacía la pila de rehacer desapilando todos sus comandos.
    // Se utiliza cuando se ejecuta una nueva operación después de haber
    // realizado uno o más deshacer.
    private void vaciarPilaRehacer() {
        while (!pilaRehacer.estavacia()) {
            pilaRehacer.desapilar();
        }
    }
}