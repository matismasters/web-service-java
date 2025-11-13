package com.example.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementación artesanal de una Lista Dinámica usando arrays (similar a ArrayList).
 * Permite almacenar elementos de tipo genérico T.
 */
public class ListaNormal<T> implements Iterable<T> {
    
    private static final int CAPACIDAD_INICIAL = 10;
    private static final int FACTOR_CRECIMIENTO = 2;
    
    private T[] elementos;
    private int tamaño;
    private int capacidad;
    
    /**
     * Constructor que crea una lista vacía con capacidad inicial por defecto.
     */
    @SuppressWarnings("unchecked")
    public ListaNormal() {
        this.capacidad = CAPACIDAD_INICIAL;
        this.elementos = (T[]) new Object[capacidad];
        this.tamaño = 0;
    }
    
    /**
     * Constructor que crea una lista vacía con capacidad inicial especificada.
     * @param capacidadInicial La capacidad inicial del array
     * @throws IllegalArgumentException si la capacidad es negativa
     */
    @SuppressWarnings("unchecked")
    public ListaNormal(int capacidadInicial) {
        if (capacidadInicial < 0) {
            throw new IllegalArgumentException("La capacidad no puede ser negativa: " + capacidadInicial);
        }
        this.capacidad = capacidadInicial == 0 ? CAPACIDAD_INICIAL : capacidadInicial;
        this.elementos = (T[]) new Object[this.capacidad];
        this.tamaño = 0;
    }
    
    /**
     * Agrega un elemento al final de la lista.
     * @param elemento El elemento a agregar
     */
    public void agregar(T elemento) {
        asegurarCapacidad(tamaño + 1);
        elementos[tamaño++] = elemento;
    }
    
    /**
     * Agrega un elemento en una posición específica.
     * @param indice La posición donde insertar (0-based)
     * @param elemento El elemento a agregar
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    public void agregar(int indice, T elemento) {
        if (indice < 0 || indice > tamaño) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        
        asegurarCapacidad(tamaño + 1);
        
        // Desplazar elementos hacia la derecha
        for (int i = tamaño; i > indice; i--) {
            elementos[i] = elementos[i - 1];
        }
        
        elementos[indice] = elemento;
        tamaño++;
    }
    
    /**
     * Asegura que el array tenga suficiente capacidad.
     * Si no la tiene, lo redimensiona.
     */
    @SuppressWarnings("unchecked")
    private void asegurarCapacidad(int capacidadMinima) {
        if (capacidadMinima > capacidad) {
            int nuevaCapacidad = capacidad * FACTOR_CRECIMIENTO;
            if (nuevaCapacidad < capacidadMinima) {
                nuevaCapacidad = capacidadMinima;
            }
            
            T[] nuevoArray = (T[]) new Object[nuevaCapacidad];
            for (int i = 0; i < tamaño; i++) {
                nuevoArray[i] = elementos[i];
            }
            elementos = nuevoArray;
            capacidad = nuevaCapacidad;
        }
    }
    
    /**
     * Obtiene el elemento en la posición especificada.
     * @param indice La posición del elemento (0-based)
     * @return El elemento en esa posición
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    public T obtener(int indice) {
        validarIndice(indice);
        return elementos[indice];
    }
    
    /**
     * Valida que el índice esté dentro del rango válido.
     */
    private void validarIndice(int indice) {
        if (indice < 0 || indice >= tamaño) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice + ", tamaño: " + tamaño);
        }
    }
    
    /**
     * Reemplaza el elemento en la posición especificada.
     * @param indice La posición del elemento a reemplazar
     * @param elemento El nuevo elemento
     * @return El elemento anterior en esa posición
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    public T establecer(int indice, T elemento) {
        validarIndice(indice);
        T anterior = elementos[indice];
        elementos[indice] = elemento;
        return anterior;
    }
    
    /**
     * Elimina el elemento en la posición especificada.
     * @param indice La posición del elemento a eliminar
     * @return El elemento eliminado
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    public T eliminar(int indice) {
        validarIndice(indice);
        
        T elementoEliminado = elementos[indice];
        
        // Desplazar elementos hacia la izquierda
        for (int i = indice; i < tamaño - 1; i++) {
            elementos[i] = elementos[i + 1];
        }
        
        elementos[--tamaño] = null; // Ayudar al garbage collector
        
        // Reducir capacidad si es necesario (opcional, para ahorrar memoria)
        reducirCapacidadSiEsNecesario();
        
        return elementoEliminado;
    }
    
    /**
     * Elimina la primera ocurrencia del elemento especificado.
     * @param elemento El elemento a eliminar
     * @return true si el elemento fue eliminado, false si no se encontró
     */
    public boolean eliminar(T elemento) {
        int indice = indiceDe(elemento);
        if (indice >= 0) {
            eliminar(indice);
            return true;
        }
        return false;
    }
    
    /**
     * Verifica si la lista contiene el elemento especificado.
     * @param elemento El elemento a buscar
     * @return true si el elemento está en la lista, false en caso contrario
     */
    public boolean contiene(T elemento) {
        return indiceDe(elemento) >= 0;
    }
    
    /**
     * Obtiene el índice de la primera ocurrencia del elemento.
     * @param elemento El elemento a buscar
     * @return El índice del elemento, o -1 si no se encuentra
     */
    public int indiceDe(T elemento) {
        for (int i = 0; i < tamaño; i++) {
            if (elementos[i] != null && elementos[i].equals(elemento) ||
                elementos[i] == null && elemento == null) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Obtiene el índice de la última ocurrencia del elemento.
     * @param elemento El elemento a buscar
     * @return El índice del elemento, o -1 si no se encuentra
     */
    public int ultimoIndiceDe(T elemento) {
        for (int i = tamaño - 1; i >= 0; i--) {
            if (elementos[i] != null && elementos[i].equals(elemento) ||
                elementos[i] == null && elemento == null) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Obtiene el tamaño de la lista.
     * @return El número de elementos en la lista
     */
    public int tamaño() {
        return tamaño;
    }
    
    /**
     * Obtiene la capacidad actual del array interno.
     * @return La capacidad actual
     */
    public int capacidad() {
        return capacidad;
    }
    
    /**
     * Verifica si la lista está vacía.
     * @return true si la lista está vacía, false en caso contrario
     */
    public boolean estaVacia() {
        return tamaño == 0;
    }
    
    /**
     * Elimina todos los elementos de la lista.
     */
    @SuppressWarnings("unchecked")
    public void limpiar() {
        for (int i = 0; i < tamaño; i++) {
            elementos[i] = null;
        }
        tamaño = 0;
        // Opcional: reducir capacidad a la inicial
        capacidad = CAPACIDAD_INICIAL;
        elementos = (T[]) new Object[capacidad];
    }
    
    /**
     * Obtiene el primer elemento de la lista.
     * @return El primer elemento
     * @throws NoSuchElementException si la lista está vacía
     */
    public T obtenerPrimero() {
        if (tamaño == 0) {
            throw new NoSuchElementException("La lista está vacía");
        }
        return elementos[0];
    }
    
    /**
     * Obtiene el último elemento de la lista.
     * @return El último elemento
     * @throws NoSuchElementException si la lista está vacía
     */
    public T obtenerUltimo() {
        if (tamaño == 0) {
            throw new NoSuchElementException("La lista está vacía");
        }
        return elementos[tamaño - 1];
    }
    
    /**
     * Convierte la lista a un array.
     * @return Un array con todos los elementos de la lista
     */
    @SuppressWarnings("unchecked")
    public T[] aArray() {
        T[] array = (T[]) new Object[tamaño];
        for (int i = 0; i < tamaño; i++) {
            array[i] = elementos[i];
        }
        return array;
    }
    
    /**
     * Reduce la capacidad del array si es necesario para ahorrar memoria.
     */
    @SuppressWarnings("unchecked")
    private void reducirCapacidadSiEsNecesario() {
        // Reducir si el tamaño es menor que 1/4 de la capacidad
        if (tamaño > 0 && tamaño < capacidad / 4 && capacidad > CAPACIDAD_INICIAL) {
            int nuevaCapacidad = capacidad / FACTOR_CRECIMIENTO;
            if (nuevaCapacidad < CAPACIDAD_INICIAL) {
                nuevaCapacidad = CAPACIDAD_INICIAL;
            }
            
            T[] nuevoArray = (T[]) new Object[nuevaCapacidad];
            for (int i = 0; i < tamaño; i++) {
                nuevoArray[i] = elementos[i];
            }
            elementos = nuevoArray;
            capacidad = nuevaCapacidad;
        }
    }
    
    /**
     * Trunca la capacidad del array al tamaño actual para ahorrar memoria.
     */
    @SuppressWarnings("unchecked")
    public void ajustarCapacidad() {
        if (tamaño < capacidad) {
            T[] nuevoArray = (T[]) new Object[tamaño];
            for (int i = 0; i < tamaño; i++) {
                nuevoArray[i] = elementos[i];
            }
            elementos = nuevoArray;
            capacidad = tamaño;
        }
    }
    
    @Override
    public String toString() {
        if (tamaño == 0) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < tamaño; i++) {
            sb.append(elementos[i]);
            if (i < tamaño - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    @Override
    public Iterator<T> iterator() {
        return new ListaNormalIterator();
    }
    
    /**
     * Iterador interno para la lista normal.
     */
    private class ListaNormalIterator implements Iterator<T> {
        private int indiceActual = 0;
        
        @Override
        public boolean hasNext() {
            return indiceActual < tamaño;
        }
        
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return elementos[indiceActual++];
        }
    }
}

