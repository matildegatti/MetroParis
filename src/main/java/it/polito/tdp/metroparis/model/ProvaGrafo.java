package it.polito.tdp.metroparis.model;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
//import org.jgrapht.graph.SimpleGraph;

public class ProvaGrafo {

	public static void main(String[] args) {
		Graph<String,DefaultEdge> grafo=new SimpleDirectedGraph<>(DefaultEdge.class);  //creo il grafo vuoto
		
		grafo.addVertex("UNO");
		grafo.addVertex("DUE");
		grafo.addVertex("TRE");
		
		grafo.addEdge("UNO", "TRE");
		grafo.addEdge("TRE", "DUE");
		//grafo.addEdge("UNO", "SETTE"); mi darebbe un eccezione: non esiste il vertice sette
		//grafo.addEdge("UNO", "UNO"); eccezione: loop non consentiti nel simplegraph
		
		System.out.println(grafo);
	}

}
