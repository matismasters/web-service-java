package com.example.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementación artesanal de una Lista Enlazada (LinkedList).
 * Permite almacenar elementos de tipo genérico T.
 */
public class ListaEnlazada<T> implements Iterable<T> {
    
    /**
     * Nodo interno que representa un elemento de la lista.
     */
    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;
        
        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }
    
    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamaño;
    
    /**
     * Constructor que crea una lista enlazada vacía.
     */
    public ListaEnlazada() {
        this.cabeza = null;
        this.cola = null;
        this.tamaño = 0;
    }
    
    /**
     * Agrega un elemento al final de la lista.
     * @param elemento El elemento a agregar
     */
    public void agregar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        
        if (cabeza == null) {
            cabeza = nuevoNodo;
            cola = nuevoNodo;
        } else {
            cola.siguiente = nuevoNodo;
            cola = nuevoNodo;
        }
        tamaño++;
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
        
        if (indice == tamaño) {
            agregar(elemento);
            return;
        }
        
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        
        if (indice == 0) {
            nuevoNodo.siguiente = cabeza;
            cabeza = nuevoNodo;
        } else {
            Nodo<T> actual = obtenerNodo(indice - 1);
            nuevoNodo.siguiente = actual.siguiente;
            actual.siguiente = nuevoNodo;
        }
        
        if (nuevoNodo.siguiente == null) {
            cola = nuevoNodo;
        }
        
        tamaño++;
    }
    
    /**
     * Obtiene el elemento en la posición especificada.
     * @param indice La posición del elemento (0-based)
     * @return El elemento en esa posición
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamaño) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        return obtenerNodo(indice).dato;
    }
    
    /**
     * Obtiene el nodo en la posición especificada.
     */
    private Nodo<T> obtenerNodo(int indice) {
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        return actual;
    }
    
    /**
     * Elimina el elemento en la posición especificada.
     * @param indice La posición del elemento a eliminar
     * @return El elemento eliminado
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    public T eliminar(int indice) {
        if (indice < 0 || indice >= tamaño) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        
        T elementoEliminado;
        
        if (indice == 0) {
            elementoEliminado = cabeza.dato;
            cabeza = cabeza.siguiente;
            if (cabeza == null) {
                cola = null;
            }
        } else {
            Nodo<T> anterior = obtenerNodo(indice - 1);
            elementoEliminado = anterior.siguiente.dato;
            anterior.siguiente = anterior.siguiente.siguiente;
            
            if (anterior.siguiente == null) {
                cola = anterior;
            }
        }
        
        tamaño--;
        return elementoEliminado;
    }
    
    /**
     * Elimina la primera ocurrencia del elemento especificado.
     * @param elemento El elemento a eliminar
     * @return true si el elemento fue eliminado, false si no se encontró
     */
    public boolean eliminar(T elemento) {
        if (cabeza == null) {
            return false;
        }
        
        if (cabeza.dato != null && cabeza.dato.equals(elemento) || 
            cabeza.dato == null && elemento == null) {
            cabeza = cabeza.siguiente;
            if (cabeza == null) {
                cola = null;
            }
            tamaño--;
            return true;
        }
        
        Nodo<T> actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.dato != null && actual.siguiente.dato.equals(elemento) ||
                actual.siguiente.dato == null && elemento == null) {
                actual.siguiente = actual.siguiente.siguiente;
                if (actual.siguiente == null) {
                    cola = actual;
                }
                tamaño--;
                return true;
            }
            actual = actual.siguiente;
        }
        
        return false;
    }
    
    /**
     * Verifica si la lista contiene el elemento especificado.
     * @param elemento El elemento a buscar
     * @return true si el elemento está en la lista, false en caso contrario
     */
    public boolean contiene(T elemento) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.dato != null && actual.dato.equals(elemento) ||
                actual.dato == null && elemento == null) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }
    
    /**
     * Obtiene el índice de la primera ocurrencia del elemento.
     * @param elemento El elemento a buscar
     * @return El índice del elemento, o -1 si no se encuentra
     */
    public int indiceDe(T elemento) {
        Nodo<T> actual = cabeza;
        int indice = 0;
        while (actual != null) {
            if (actual.dato != null && actual.dato.equals(elemento) ||
                actual.dato == null && elemento == null) {
                return indice;
            }
            actual = actual.siguiente;
            indice++;
        }
        return -1;
    }
    
    /**
     * Reemplaza el elemento en la posición especificada.
     * @param indice La posición del elemento a reemplazar
     * @param elemento El nuevo elemento
     * @return El elemento anterior en esa posición
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    public T establecer(int indice, T elemento) {
        if (indice < 0 || indice >= tamaño) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        
        Nodo<T> nodo = obtenerNodo(indice);
        T anterior = nodo.dato;
        nodo.dato = elemento;
        return anterior;
    }
    
    /**
     * Obtiene el tamaño de la lista.
     * @return El número de elementos en la lista
     */
    public int tamaño() {
        return tamaño;
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
    public void limpiar() {
        cabeza = null;
        cola = null;
        tamaño = 0;
    }
    
    /**
     * Obtiene el primer elemento de la lista.
     * @return El primer elemento
     * @throws NoSuchElementException si la lista está vacía
     */
    public T obtenerPrimero() {
        if (cabeza == null) {
            throw new NoSuchElementException("La lista está vacía");
        }
        return cabeza.dato;
    }
    
    /**
     * Obtiene el último elemento de la lista.
     * @return El último elemento
     * @throws NoSuchElementException si la lista está vacía
     */
    public T obtenerUltimo() {
        if (cola == null) {
            throw new NoSuchElementException("La lista está vacía");
        }
        return cola.dato;
    }
    
    /**
     * Convierte la lista a un array.
     * @return Un array con todos los elementos de la lista
     */
    @SuppressWarnings("unchecked")
    public T[] aArray() {
        if (tamaño == 0) {
            return (T[]) new Object[0];
        }
        
        T[] array = (T[]) new Object[tamaño];
        Nodo<T> actual = cabeza;
        int indice = 0;
        while (actual != null) {
            array[indice++] = actual.dato;
            actual = actual.siguiente;
        }
        return array;
    }
    
    /**
     * Invierte el orden de los elementos en la lista.
     */
    public void invertir() {
        if (cabeza == null || cabeza.siguiente == null) {
            return;
        }
        
        Nodo<T> anterior = null;
        Nodo<T> actual = cabeza;
        Nodo<T> siguiente;
        
        cola = cabeza;
        
        while (actual != null) {
            siguiente = actual.siguiente;
            actual.siguiente = anterior;
            anterior = actual;
            actual = siguiente;
        }
        
        cabeza = anterior;
    }
    
    @Override
    public String toString() {
        if (cabeza == null) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        Nodo<T> actual = cabeza;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) {
                sb.append(", ");
            }
            actual = actual.siguiente;
        }
        sb.append("]");
        return sb.toString();
    }
    
    @Override
    public Iterator<T> iterator() {
        return new ListaEnlazadaIterator();
    }
    
    /**
     * Iterador interno para la lista enlazada.
     */
    private class ListaEnlazadaIterator implements Iterator<T> {
        private Nodo<T> actual = cabeza;
        
        @Override
        public boolean hasNext() {
            return actual != null;
        }
        
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T dato = actual.dato;
            actual = actual.siguiente;
            return dato;
        }
    }
}

