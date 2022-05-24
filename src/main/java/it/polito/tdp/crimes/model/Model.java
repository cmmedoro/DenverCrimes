package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private Graph<String, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private List<String> best; //soluzione migliore dell'algoritmo ricorsivo
	
	public Model() {
		this.dao = new EventsDao();
		
	}
	
	public void creaGrafo(String categoria, int mese) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Aggiunta vertici ---> tipologia di reato entro una categoria ed entro un certo mese
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(categoria, mese));
		//Aggiunta archi
		for (Adiacenza a : this.dao.getArchi(categoria, mese)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getE1(), a.getE2(), a.getPeso());
			//Avendo messo > nella query avremo gli archi in una sola direzione
		}
		//System.out.println("Grafo creato!");
		//System.out.println("# VERTICI: "+this.grafo.vertexSet().size());
		//System.out.println("# ARCHI: "+this.grafo.edgeSet().size());
	}
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenza> getArchi(){
		List<Adiacenza> archi = new ArrayList<Adiacenza>();
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			archi.add(new Adiacenza(this.grafo.getEdgeSource(e),
						this.grafo.getEdgeTarget(e), 
						(int) this.grafo.getEdgeWeight(e)));
		}
		return archi;
	}
	
	public List<String> getCategorie(){
		return this.dao.getCategorie();
	}
	
	public List<Adiacenza> getArchiMaggioriPesoMedio(){
		//scorro gli archi del grafo e calcolo il peso medio
		double pesoTot = 0;
		double pesoMedio = 0;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			pesoTot += this.grafo.getEdgeWeight(e);
		}
		pesoMedio = pesoTot/this.grafo.edgeSet().size();
		System.out.println("PESO MEDIO: "+pesoMedio);
		//ri-scorro tutti gli archi prendendo quelli con peso maggiore di pesoMedio
		List<Adiacenza> result = new ArrayList<>();
		for(DefaultWeightedEdge e  : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e) > pesoMedio) {
				result.add(new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), (int)this.grafo.getEdgeWeight(e)));
			}
		}
		return result;
	}
	
	public List<String> calcolaPercorso(String sorgente, String destinazione){
		this.best = new LinkedList<>();
		List<String> parziale = new LinkedList<>();
		parziale.add(sorgente); //questo è sicuramente il primo passo
		cerca(parziale, destinazione);
		return this.best;
	}
	
	//cammino aciclico
	private void cerca(List<String> parziale, String destinazione) {
		//caso terminale: mi fermo quando arrivo a destinazione
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			//se l'ultimo elemento inserito è pari alla destinazione: controllo sia la soluzione migliore
			if(parziale.size() > this.best.size()) {
				this.best = new LinkedList<>(parziale); //sovrascrivo facendo una new
			}
			return; //esco dalla ricorsione: è inutile andare avanti
		}
		//caso normale
		//Esploro i percorsi che vanno verso gli adiacenti all'ultimo inserito
		for(String s : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(s)) { //così evito si creino cicli
				parziale.add(s);
				cerca(parziale, destinazione);
				parziale.remove(parziale.size()-1);
			}
		}
	}
	

}
