package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
				//arco non pesato
	Graph<Fermata,DefaultEdge> grafo;
	
	Map<Fermata,Fermata> predecessore;
	
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
		
								//sto creando una nuova classe senza nome che implementa l'interfaccia
					//all'interno implementiamo i metodi richiesti (inline, all'interno di un'espressione)
				//posso crearlo in un oggetto separato
		
		this.predecessore=new HashMap<>();
		this.predecessore.put(partenza, null);
		bfv.addTraversalListener(new TraversalListener<Fermata, DefaultEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {  //chiamato tutte le volte che l'algoritmo attraversa un nuovo arco per scoprire un nuovo vertice
				DefaultEdge arco=e.getEdge();
				Fermata f1=grafo.getEdgeSource(arco);
				Fermata f2=grafo.getEdgeTarget(arco);
				//uno è il vertice nuovo appena trovato l'altro l conoscevo già
				//due casi: ho scoperto f2 arrivando da f1 (f1 lo conoscevo già)
				if(predecessore.containsKey(f2) && !predecessore.containsKey(f1)) {
					predecessore.put(f1, f2);
				}
				else if(predecessore.containsKey(f1) && !predecessore.containsKey(f2))
					predecessore.put(f2, f1);
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Fermata> e) { 
			//	Fermata nuova=e.getVertex();
			//	Fermata precedente= ;//vertice adiacente a nuova che sia già raggiunto
						//cioè è già presente nelle key della mappa
			//	predecessore.put(nuova,precedente);  //la nuova fermata è raggiungibile dalla precedente
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Fermata> e) {
			}
			
		}); //associo un gestore degli eventi all'iteratore
		
		//iteratore con algoritmo di visita in profondità
		DepthFirstIterator<Fermata,DefaultEdge> dfv=new DepthFirstIterator<>(this.grafo,partenza);
		
		//mi daranno gli stessi vertici con ordine diverso
		
		List<Fermata> result=new ArrayList<>();
		
		while(bfv.hasNext()) {
			Fermata f=bfv.next();
			result.add(0,f); //la aggiungo per prima, altrimenti li stampa al contrario
		}
		
		return result;
	}
	
	public Fermata trovaFermata(String nome) {
		for(Fermata f:this.grafo.vertexSet())
			if(f.getNome().equals(nome))
				return f;
		return null;
	}
	
	public List<Fermata> trovaCammino(Fermata partenza,Fermata arrivo){
		fermateRaggiungibili(partenza);
		List<Fermata> result=new ArrayList<>();
		result.add(arrivo);
		Fermata f=arrivo;
		while(predecessore.get(f)!=null) {
			f=predecessore.get(f);
			result.add(f);
		}
		
		return result;  //contiene tutte le fermate dall'arrivo alla partenza sul cammino minimo
	}
	
}
