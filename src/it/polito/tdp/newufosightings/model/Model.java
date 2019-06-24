package it.polito.tdp.newufosightings.model;

import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {
	
	private NewUfoSightingsDAO dao;
	private List<String> shapes;
	private Graph<State, DefaultWeightedEdge> grafo;
	private List<Confini> confini;
	private Map<String, State> idMap;

	
	
	public Model() {
		dao = new NewUfoSightingsDAO();
		confini = dao.loadAllConfini();
		this.idMap = new HashMap<String, State>();
		
	}
	
	public List<String> getShapesByYear(Year anno) {
		shapes = dao.loadShapeByYear(anno);
		return shapes;
	}
	
	public void creaGrafo(Year anno, String shape) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, dao.loadAllStates());//aggiunta degli stati come vertici
		
		for(State s1 : grafo.vertexSet()) {
			
			idMap.put(s1.getId(), s1);
			
			for(State s2 : grafo.vertexSet()) {
				if(!s1.equals(s2) && !grafo.containsEdge(s1, s2)) {
					if(isConfinante(s1.getId(), s2.getId())) {
						Graphs.addEdgeWithVertices(grafo, s1, s2, dao.calcolaPeso(anno, s1.getId(), s2.getId(), shape));
					}
					
				}
			}
		}
		
		
		
	}
	
	public boolean isConfinante(String s1, String s2) {
		
		for(Confini c : confini) {
			if((c.getStato().equals(s1) && c.getStato2().equals(s2))/* || (c.getStato().equals(s2) && c.getStato2().equals(s1))*/) {
				return true;
			}
			
		}
		return false;
		
	}

	public Graph<State, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public int pesoArchiAdiacenti(State s) {
		
		int peso = 0;
		
		for(DefaultWeightedEdge arco : grafo.incomingEdgesOf(s)) {
			peso += grafo.getEdgeWeight(arco);
		}
		
		return peso;
		
	}
	
	public List<Sighting> avvistamentiByYearByShape(Year anno, String shape){
		List<Sighting> avvistamenti = dao.getAvvistamenti(anno, shape);
		return avvistamenti;
	}

	public Map<String, State> getIdMap() {
		return idMap;
	}
	
	
	
	

}
