package com.example.estructuras;

import java.util.*;

/**
 * Implementación artesanal de un Grafo Dirigido usando listas de adyacencia.
 * Permite almacenar vértices de tipo genérico V y aristas con peso opcional.
 */
public class GrafoDirigido<V> {
    
    /**
     * Clase interna que representa una arista dirigida.
     */
    public static class Arista<V> {
        private V destino;
        private double peso;
        
        public Arista(V destino, double peso) {
            this.destino = destino;
            this.peso = peso;
        }
        
        public Arista(V destino) {
            this(destino, 1.0);
        }
        
        public V getDestino() {
            return destino;
        }
        
        public double getPeso() {
            return peso;
        }
        
        public void setPeso(double peso) {
            this.peso = peso;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Arista<?> arista = (Arista<?>) o;
            return Double.compare(arista.peso, peso) == 0 &&
                   Objects.equals(destino, arista.destino);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(destino, peso);
        }
        
        @Override
        public String toString() {
            return "->" + destino + "(" + peso + ")";
        }
    }
    
    // Mapa que almacena las listas de adyacencia: vértice -> lista de aristas
    private Map<V, List<Arista<V>>> adyacencias;
    private int numeroAristas;
    
    /**
     * Constructor que crea un grafo dirigido vacío.
     */
    public GrafoDirigido() {
        this.adyacencias = new HashMap<>();
        this.numeroAristas = 0;
    }
    
    /**
     * Agrega un vértice al grafo si no existe.
     * @param vertice El vértice a agregar
     * @return true si el vértice fue agregado, false si ya existía
     */
    public boolean agregarVertice(V vertice) {
        if (vertice == null) {
            throw new IllegalArgumentException("El vértice no puede ser nulo");
        }
        
        if (!adyacencias.containsKey(vertice)) {
            adyacencias.put(vertice, new ArrayList<>());
            return true;
        }
        return false;
    }
    
    /**
     * Agrega una arista dirigida desde origen hacia destino con peso por defecto (1.0).
     * @param origen El vértice origen
     * @param destino El vértice destino
     * @return true si la arista fue agregada, false si ya existía
     */
    public boolean agregarArista(V origen, V destino) {
        return agregarArista(origen, destino, 1.0);
    }
    
    /**
     * Agrega una arista dirigida desde origen hacia destino con peso especificado.
     * @param origen El vértice origen
     * @param destino El vértice destino
     * @param peso El peso de la arista
     * @return true si la arista fue agregada, false si ya existía
     */
    public boolean agregarArista(V origen, V destino, double peso) {
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Los vértices no pueden ser nulos");
        }
        
        // Agregar vértices si no existen
        agregarVertice(origen);
        agregarVertice(destino);
        
        List<Arista<V>> aristas = adyacencias.get(origen);
        
        // Verificar si la arista ya existe
        for (Arista<V> arista : aristas) {
            if (arista.getDestino().equals(destino)) {
                return false; // La arista ya existe
            }
        }
        
        // Agregar la nueva arista
        aristas.add(new Arista<>(destino, peso));
        numeroAristas++;
        return true;
    }
    
    /**
     * Elimina una arista dirigida desde origen hacia destino.
     * @param origen El vértice origen
     * @param destino El vértice destino
     * @return true si la arista fue eliminada, false si no existía
     */
    public boolean eliminarArista(V origen, V destino) {
        if (origen == null || destino == null) {
            return false;
        }
        
        List<Arista<V>> aristas = adyacencias.get(origen);
        if (aristas == null) {
            return false;
        }
        
        Iterator<Arista<V>> iterador = aristas.iterator();
        while (iterador.hasNext()) {
            Arista<V> arista = iterador.next();
            if (arista.getDestino().equals(destino)) {
                iterador.remove();
                numeroAristas--;
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Elimina un vértice y todas sus aristas asociadas.
     * @param vertice El vértice a eliminar
     * @return true si el vértice fue eliminado, false si no existía
     */
    public boolean eliminarVertice(V vertice) {
        if (vertice == null) {
            return false;
        }
        
        if (!adyacencias.containsKey(vertice)) {
            return false;
        }
        
        // Contar aristas salientes del vértice
        int aristasSalientes = adyacencias.get(vertice).size();
        
        // Eliminar todas las aristas entrantes al vértice
        for (List<Arista<V>> listaAristas : adyacencias.values()) {
            Iterator<Arista<V>> iterador = listaAristas.iterator();
            while (iterador.hasNext()) {
                if (iterador.next().getDestino().equals(vertice)) {
                    iterador.remove();
                    numeroAristas--;
                }
            }
        }
        
        // Eliminar el vértice y sus aristas salientes
        adyacencias.remove(vertice);
        numeroAristas -= aristasSalientes;
        
        return true;
    }
    
    /**
     * Verifica si existe una arista desde origen hacia destino.
     * @param origen El vértice origen
     * @param destino El vértice destino
     * @return true si existe la arista, false en caso contrario
     */
    public boolean existeArista(V origen, V destino) {
        if (origen == null || destino == null) {
            return false;
        }
        
        List<Arista<V>> aristas = adyacencias.get(origen);
        if (aristas == null) {
            return false;
        }
        
        for (Arista<V> arista : aristas) {
            if (arista.getDestino().equals(destino)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Obtiene el peso de una arista.
     * @param origen El vértice origen
     * @param destino El vértice destino
     * @return El peso de la arista, o Double.POSITIVE_INFINITY si no existe
     */
    public double obtenerPeso(V origen, V destino) {
        if (origen == null || destino == null) {
            return Double.POSITIVE_INFINITY;
        }
        
        List<Arista<V>> aristas = adyacencias.get(origen);
        if (aristas == null) {
            return Double.POSITIVE_INFINITY;
        }
        
        for (Arista<V> arista : aristas) {
            if (arista.getDestino().equals(destino)) {
                return arista.getPeso();
            }
        }
        
        return Double.POSITIVE_INFINITY;
    }
    
    /**
     * Obtiene todos los vértices adyacentes (destinos) desde un vértice origen.
     * @param vertice El vértice origen
     * @return Lista de vértices adyacentes
     */
    public List<V> obtenerAdyacentes(V vertice) {
        if (vertice == null) {
            return new ArrayList<>();
        }
        
        List<Arista<V>> aristas = adyacencias.get(vertice);
        if (aristas == null) {
            return new ArrayList<>();
        }
        
        List<V> adyacentes = new ArrayList<>();
        for (Arista<V> arista : aristas) {
            adyacentes.add(arista.getDestino());
        }
        return adyacentes;
    }
    
    /**
     * Obtiene todas las aristas salientes de un vértice.
     * @param vertice El vértice
     * @return Lista de aristas salientes
     */
    public List<Arista<V>> obtenerAristasSalientes(V vertice) {
        if (vertice == null) {
            return new ArrayList<>();
        }
        
        List<Arista<V>> aristas = adyacencias.get(vertice);
        return aristas == null ? new ArrayList<>() : new ArrayList<>(aristas);
    }
    
    /**
     * Obtiene todos los vértices que tienen aristas hacia el vértice especificado.
     * @param vertice El vértice destino
     * @return Lista de vértices predecesores
     */
    public List<V> obtenerPredecesores(V vertice) {
        if (vertice == null) {
            return new ArrayList<>();
        }
        
        List<V> predecesores = new ArrayList<>();
        for (Map.Entry<V, List<Arista<V>>> entrada : adyacencias.entrySet()) {
            for (Arista<V> arista : entrada.getValue()) {
                if (arista.getDestino().equals(vertice)) {
                    predecesores.add(entrada.getKey());
                    break;
                }
            }
        }
        return predecesores;
    }
    
    /**
     * Obtiene el grado de salida de un vértice (número de aristas salientes).
     * @param vertice El vértice
     * @return El grado de salida
     */
    public int gradoSalida(V vertice) {
        List<Arista<V>> aristas = adyacencias.get(vertice);
        return aristas == null ? 0 : aristas.size();
    }
    
    /**
     * Obtiene el grado de entrada de un vértice (número de aristas entrantes).
     * @param vertice El vértice
     * @return El grado de entrada
     */
    public int gradoEntrada(V vertice) {
        if (vertice == null) {
            return 0;
        }
        
        int grado = 0;
        for (List<Arista<V>> aristas : adyacencias.values()) {
            for (Arista<V> arista : aristas) {
                if (arista.getDestino().equals(vertice)) {
                    grado++;
                }
            }
        }
        return grado;
    }
    
    /**
     * Obtiene el número de vértices en el grafo.
     * @return El número de vértices
     */
    public int numeroVertices() {
        return adyacencias.size();
    }
    
    /**
     * Obtiene el número de aristas en el grafo.
     * @return El número de aristas
     */
    public int numeroAristas() {
        return numeroAristas;
    }
    
    /**
     * Obtiene todos los vértices del grafo.
     * @return Conjunto de vértices
     */
    public Set<V> obtenerVertices() {
        return new HashSet<>(adyacencias.keySet());
    }
    
    /**
     * Verifica si el grafo está vacío.
     * @return true si no tiene vértices, false en caso contrario
     */
    public boolean estaVacio() {
        return adyacencias.isEmpty();
    }
    
    /**
     * Verifica si un vértice existe en el grafo.
     * @param vertice El vértice a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean contieneVertice(V vertice) {
        return vertice != null && adyacencias.containsKey(vertice);
    }
    
    /**
     * Realiza un recorrido en profundidad (DFS) desde un vértice inicial.
     * @param inicio El vértice inicial
     * @return Lista de vértices visitados en orden DFS
     */
    public List<V> recorridoProfundidad(V inicio) {
        if (!contieneVertice(inicio)) {
            return new ArrayList<>();
        }
        
        List<V> resultado = new ArrayList<>();
        Set<V> visitados = new HashSet<>();
        dfsRecursivo(inicio, visitados, resultado);
        return resultado;
    }
    
    /**
     * Método auxiliar recursivo para DFS.
     */
    private void dfsRecursivo(V vertice, Set<V> visitados, List<V> resultado) {
        visitados.add(vertice);
        resultado.add(vertice);
        
        List<Arista<V>> aristas = adyacencias.get(vertice);
        if (aristas != null) {
            for (Arista<V> arista : aristas) {
                V adyacente = arista.getDestino();
                if (!visitados.contains(adyacente)) {
                    dfsRecursivo(adyacente, visitados, resultado);
                }
            }
        }
    }
    
    /**
     * Realiza un recorrido en anchura (BFS) desde un vértice inicial.
     * @param inicio El vértice inicial
     * @return Lista de vértices visitados en orden BFS
     */
    public List<V> recorridoAnchura(V inicio) {
        if (!contieneVertice(inicio)) {
            return new ArrayList<>();
        }
        
        List<V> resultado = new ArrayList<>();
        Set<V> visitados = new HashSet<>();
        Queue<V> cola = new LinkedList<>();
        
        cola.offer(inicio);
        visitados.add(inicio);
        
        while (!cola.isEmpty()) {
            V vertice = cola.poll();
            resultado.add(vertice);
            
            List<Arista<V>> aristas = adyacencias.get(vertice);
            if (aristas != null) {
                for (Arista<V> arista : aristas) {
                    V adyacente = arista.getDestino();
                    if (!visitados.contains(adyacente)) {
                        visitados.add(adyacente);
                        cola.offer(adyacente);
                    }
                }
            }
        }
        
        return resultado;
    }
    
    /**
     * Limpia el grafo, eliminando todos los vértices y aristas.
     */
    public void limpiar() {
        adyacencias.clear();
        numeroAristas = 0;
    }
    
    @Override
    public String toString() {
        if (estaVacio()) {
            return "Grafo vacío";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Grafo Dirigido (").append(numeroVertices())
          .append(" vértices, ").append(numeroAristas()).append(" aristas)\n");
        
        for (Map.Entry<V, List<Arista<V>>> entrada : adyacencias.entrySet()) {
            sb.append(entrada.getKey()).append(" -> ");
            List<Arista<V>> aristas = entrada.getValue();
            if (aristas.isEmpty()) {
                sb.append("[]");
            } else {
                sb.append(aristas);
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
}

