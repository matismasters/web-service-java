package com.example.estructuras;

import java.util.Iterator;

/**
 * Clase de ejemplo que demuestra el uso de las estructuras de datos implementadas.
 * Esta clase puede ser eliminada si no se necesita, es solo para referencia.
 */
public class EjemplosUso {
    
    public static void ejemploListaEnlazada() {
        System.out.println("=== Ejemplo ListaEnlazada ===");
        
        ListaEnlazada<String> lista = new ListaEnlazada<>();
        
        // Agregar elementos
        lista.agregar("A");
        lista.agregar("B");
        lista.agregar("C");
        System.out.println("Lista después de agregar A, B, C: " + lista);
        
        // Agregar en posición específica
        lista.agregar(1, "X");
        System.out.println("Lista después de agregar X en posición 1: " + lista);
        
        // Obtener elemento
        System.out.println("Elemento en posición 2: " + lista.obtener(2));
        
        // Verificar si contiene
        System.out.println("¿Contiene 'B'? " + lista.contiene("B"));
        
        // Obtener índice
        System.out.println("Índice de 'C': " + lista.indiceDe("C"));
        
        // Iterar
        System.out.print("Iteración: ");
        for (String elemento : lista) {
            System.out.print(elemento + " ");
        }
        System.out.println();
        
        // Invertir
        lista.invertir();
        System.out.println("Lista invertida: " + lista);
        
        // Eliminar
        lista.eliminar("X");
        System.out.println("Lista después de eliminar 'X': " + lista);
        
        System.out.println("Tamaño: " + lista.tamaño());
        System.out.println();
    }
    
    public static void ejemploListaNormal() {
        System.out.println("=== Ejemplo ListaNormal ===");
        
        ListaNormal<Integer> lista = new ListaNormal<>();
        
        // Agregar elementos
        for (int i = 1; i <= 5; i++) {
            lista.agregar(i);
        }
        System.out.println("Lista: " + lista);
        System.out.println("Tamaño: " + lista.tamaño() + ", Capacidad: " + lista.capacidad());
        
        // Agregar en posición específica
        lista.agregar(2, 99);
        System.out.println("Lista después de agregar 99 en posición 2: " + lista);
        
        // Obtener elementos
        System.out.println("Primer elemento: " + lista.obtenerPrimero());
        System.out.println("Último elemento: " + lista.obtenerUltimo());
        System.out.println("Elemento en posición 3: " + lista.obtener(3));
        
        // Establecer elemento
        lista.establecer(0, 100);
        System.out.println("Lista después de establecer 100 en posición 0: " + lista);
        
        // Eliminar por índice
        lista.eliminar(2);
        System.out.println("Lista después de eliminar elemento en posición 2: " + lista);
        
        // Eliminar por valor
        lista.eliminar(Integer.valueOf(4));
        System.out.println("Lista después de eliminar valor 4: " + lista);
        
        // Convertir a array
        Integer[] array = lista.aArray();
        System.out.print("Array: [");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        
        System.out.println();
    }
    
    public static void ejemploGrafoDirigido() {
        System.out.println("=== Ejemplo GrafoDirigido ===");
        
        GrafoDirigido<String> grafo = new GrafoDirigido<>();
        
        // Agregar vértices
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarVertice("D");
        grafo.agregarVertice("E");
        
        // Agregar aristas
        grafo.agregarArista("A", "B", 5.0);
        grafo.agregarArista("A", "C", 3.0);
        grafo.agregarArista("B", "D", 2.0);
        grafo.agregarArista("C", "D", 1.0);
        grafo.agregarArista("D", "E", 4.0);
        grafo.agregarArista("B", "E", 6.0);
        
        System.out.println(grafo);
        
        // Información del grafo
        System.out.println("Número de vértices: " + grafo.numeroVertices());
        System.out.println("Número de aristas: " + grafo.numeroAristas());
        
        // Grado de entrada y salida
        System.out.println("Grado de salida de A: " + grafo.gradoSalida("A"));
        System.out.println("Grado de entrada de D: " + grafo.gradoEntrada("D"));
        
        // Adyacentes
        System.out.println("Vértices adyacentes desde A: " + grafo.obtenerAdyacentes("A"));
        System.out.println("Predecesores de D: " + grafo.obtenerPredecesores("D"));
        
        // Verificar arista
        System.out.println("¿Existe arista A->B? " + grafo.existeArista("A", "B"));
        System.out.println("Peso de arista A->B: " + grafo.obtenerPeso("A", "B"));
        
        // Recorridos
        System.out.println("DFS desde A: " + grafo.recorridoProfundidad("A"));
        System.out.println("BFS desde A: " + grafo.recorridoAnchura("A"));
        
        // Eliminar arista
        grafo.eliminarArista("A", "C");
        System.out.println("\nGrafo después de eliminar arista A->C:");
        System.out.println(grafo);
        
        System.out.println();
    }
    
    public static void main(String[] args) {
        ejemploListaEnlazada();
        ejemploListaNormal();
        ejemploGrafoDirigido();
    }
}

