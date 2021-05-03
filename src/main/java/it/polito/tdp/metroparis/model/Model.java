package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
				//arco non pesato
	Graph<Fermata,DefaultEdge> grafo;
	
	public void creaGrafo() {
		this.grafo=new SimpleGraph<>(DefaultEdge.class);
		
		MetroDAO dao=new MetroDAO();
		List<Fermata> fermate=dao.getAllFermate();
		
	//	for(Fermata f:fermate) {
		//	this.grafo.addVertex(f);
	//	}
		
		//possiamo semplicemente scrivere:
		Graphs.addAllVertices(this.grafo, fermate);
		
		//aggiungiamo gli archi
	/*	for(Fermata f1:this.grafo.vertexSet()) {
			for(Fermata f2:this.grafo.vertexSet()) {
				if(!f1.equals(f2) && dao.fermateCollegate(f1,f2)) {
					this.grafo.addEdge(f1, f2);
				}
			}
		}*/
		
		List<Connessione> connessioni=dao.getAllConnessioni(fermate);
		for(Connessione c:connessioni)
			this.grafo.addEdge(c.getStazP(), c.getStazA());
		
		System.out.println(this.grafo);
		
		//ES per prendere la fermata collegata a un altra da un arco
		//== dato un vertice dimmi quali sono quelli collegati nei vertici non orientati
		/*Fermata f;
		Set<DefaultEdge> archi = this.grafo.edgesOf(f) ;
		
		for(DefaultEdge e:archi) {
			Fermata f1=this.grafo.getEdgeSource(e);  //punto di partenza arco 
			Fermata f2=this.grafo.getEdgeTarget(e);  //punto di arrivo arco
				//uno dei due sarà la mia fermata stessa, l'altro sarà ciò che mi interessa
			if(f1.equals(f)) {
				//f2 è quello che mi serve
			}
			else {
				//f1 è quello che mi serve
			}
			
			//più comodo usare
			f1=Graphs.getOppositeVertex(this.grafo, e, f);  //grafo,arco associato al vertice,vertice di partenza
		}
		
		//metodo più usato:
		List<Fermate> fermateAdiacenti=Graphs.successorListOf(this.grafo, f);  //dato un vertice mi dà i vertici adiacenti, senza pensare agli archi
		
		Graphs.predecessorListOf(null, null);   //mi dà i vertici precedenti
		//nel caso di grafo non orientato mi danno gli stessi vertici, i due metodi sono intercambiabili  
		*/
	}
	
	public List<Fermata> fermateRaggiungibili(Fermata partenza) {  //restituisce una lista di fermate raggiungibili partendo dalla fermata di partenza
		//iteratore con algoritmo di visita:
		BreadthFirstIterator<Fermata,DefaultEdge> bfv=new BreadthFirstIterator<>(this.grafo,partenza);  //algoritmo di visita in ampiezza
			//grafo e vertice di partenza, navighiamo il grafo da quello, se non lo forniamo parte da dove vuole
		
		List<Fermata> result=new ArrayList<>();
		
		while(bfv.hasNext()) {
			Fermata f=bfv.next();
			result.add(f);
		}
		
		return result;
	}
	
	public Fermata trovaFermata(String nome) {
		for(Fermata f:this.grafo.vertexSet())
			if(f.getNome().equals(nome))
				return f;
		return null;
	}
	
}
