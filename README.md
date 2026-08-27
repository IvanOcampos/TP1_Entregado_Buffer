# TP1 – BufferGap y Historial de Edición

**Algoritmos y Estructuras de Datos III (2026) — Grupo g_ts13**

Integrantes:
- Ocampos Acosta, Ivan David — 5796207 — TS
- Bizzozzero Ortega, Edan Roberto — 5291269 — TS

## Decisiones de diseño

**Ubicación del hueco al duplicar la capacidad.** Al crecer, copiamos la parte
anterior al cursor al comienzo del arreglo nuevo y la parte posterior al final,
dejando el hueco exactamente en el medio, en la misma posición lógica donde
estaba el cursor. Evaluamos la alternativa de dejar todo el espacio nuevo al
final del arreglo (como un `ArrayList`), pero eso rompería el invariante
"antes del hueco = antes del cursor, después del hueco = después del cursor"
y obligaría a un `moverCursor` interno adicional inmediatamente después de
duplicar. Como copiar todos los elementos es inevitable en ambos casos (ya
estamos recorriendo el arreglo entero para copiarlo), no hay costo extra en
elegir la opción que además preserva el invariante sin trabajo adicional.

**`ComandoMoverCursor` guarda `delta`, no la posición anterior.** Deshacer un
movimiento de cursor solo requiere invertir el mismo desplazamiento
(`moverCursor(-delta)`); no hace falta memorizar ni reconstruir una posición
absoluta, y el comando queda más simple e independiente del resto del
historial.

**Excepciones.** `BufferVacioException` es chequeada porque representa una
situación esperable durante el uso normal del editor (intentar borrar con el
cursor al principio del texto), algo que el código cliente puede querer
manejar explícitamente. `PosicionInvalidaException` (en `BufferGap`) y
`PilaVaciaException` (en `PilaES`) son no chequeadas porque señalan un error
de programación del que llama (índice fuera de rango, operar una pila vacía
sin chequear antes con `estavacia()`).

**`Nodo` como clase anidada estática en `PilaES`.** No necesita ninguna
referencia a la pila que la contiene, así que se declaró `static` para no
cargar una referencia implícita innecesaria; sigue siendo una clase privada
anidada de `PilaES`.

## Tabla de desplazamientos (Ejercicio 1, punto 3)

Cursor ubicado en `n/2`, contador reiniciado, 10.000 inserciones en esa
posición:

| N | Desplazamientos BufferGap | Desplazamientos arreglo simple |
|---:|---:|---:|
| 100.000 | 50.000 | 549.995.000 |
| 200.000 | 100.000 | 1.049.995.000 |
| 1.000.000 | 500.000 | 5.049.995.000 |

**Análisis.** El `BufferGap` paga exactamente `n/2` desplazamientos —el costo
de mover el cursor una sola vez hasta el centro del documento— y después las
10.000 inserciones no desplazan nada más, porque cada una escribe
directamente en una celda del hueco. Por eso el total crece linealmente con
`n/2` y es independiente de la cantidad de inserciones realizadas en esa
zona.

El arreglo simple, en cambio, tiene que correr elementos en *cada* inserción:
la primera desplaza los `n/2` elementos posteriores, y como el arreglo va
creciendo con cada inserción previa, la siguiente desplaza uno más, y así
sucesivamente. El total resulta `5000·n + 49.995.000`, que además del término
lineal en `n` incluye un costo que crece con el cuadrado de la cantidad de
inserciones locales — por eso la diferencia con `BufferGap` se dispara: para
`n = 1.000.000` el arreglo simple hace más de diez mil veces más movimientos
que el `BufferGap` para lograr exactamente el mismo resultado final.

Esto confirma la intuición central del TP: un editor de texto real pasa la
mayor parte del tiempo escribiendo en una misma zona del documento, así que
concentrar el costo en el salto inicial (mover el cursor) y dejar gratis las
ediciones posteriores es lo que hace que el `BufferGap` responda de forma
instantánea.
