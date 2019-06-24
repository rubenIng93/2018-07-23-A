package it.polito.tdp.newufosightings.model;

import java.time.Duration;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;
import it.polito.tdp.newufosightings.model.Evento.TipoEvento;

public class Simulatore {
	
	private PriorityQueue<Evento> queue;
	
	//MODELLO DEL MONDO
	private Graph<State, DefaultWeightedEdge> grafo;
	private Random rand;
	private Model model;
	private List<Sighting> avvistamenti;
	private Map<String, State> idMap;
	private NewUfoSightingsDAO dao;
	
	//PARAMETRI
	private Double probabilita;
	private Duration T1;
	
	
	public void init(Year anno, String shape, Integer prob, Duration T1) {
		
		this.model = new Model();
		model.creaGrafo(anno, shape);
		this.grafo = model.getGrafo();
		this.queue = new PriorityQueue<>();
		this.rand = new Random();
		this.dao = new NewUfoSightingsDAO();
		this.avvistamenti = dao.getAvvistamenti(anno, shape);
		this.idMap = new HashMap<>();
		
		List<State> stati = dao.loadAllStates();
		for(State s : stati) {
			idMap.put(s.getId().toLowerCase(), s);
		}
		
		this.probabilita = prob * 0.01;
		this.T1 = T1;
		
		for(Sighting s : avvistamenti) {
			Evento e = new Evento(TipoEvento.AVVISTAMENTO, s.getDatetime(), idMap.get(s.getState()));
			this.queue.add(e);
			//System.out.println(e);
		}
		
	}
	
	public void run() {
		
		while(!queue.isEmpty()) {
			
			Evento e = queue.poll();
			State coinvolto = e.getStato();
			double prob = rand.nextDouble();
			
			switch (e.getTipo()) {
			
			case AVVISTAMENTO:
				/*
				 * se ho un avvistamento decremento il defcon e controllo i vicini 
				 * per decrementare il defcon in base ad alfa; dopodichè schedulo un nuovo evento 
				 * CESSATA_ALLERTA dopo il tempo T1
				 */
				if(coinvolto.getDefcon() > 1)
					coinvolto.setDefcon(coinvolto.getDefcon() - 1.0);
				
				this.queue.add(new Evento(TipoEvento.CESSATA_ALLERTA, e.getData().plus(T1), coinvolto));
					
				for(State s : Graphs.successorListOf(grafo, coinvolto)) {
					
					if(this.probabilita > prob) {
						s.setDefcon(s.getDefcon() - 0.5);
						this.queue.add(new Evento(TipoEvento.CESSATA_ALLERTA, e.getData().plus(T1), s));
					}
				}
				
				
				break;
				
			case CESSATA_ALLERTA:
				/*
				 * Aumento di nuovo il defcon (mai > 5) di 1 o di 0.5 nei confinanti
				 */
					
				if(coinvolto.getDefcon() < 5) {
					coinvolto.setDefcon(coinvolto.getDefcon() + 1.0);
					for(State s : Graphs.successorListOf(grafo, coinvolto)) {
						if(!s.equals(coinvolto) && s.getDefcon() < 4.5 && prob < this.probabilita)
							s.setDefcon(s.getDefcon() + 0.5);
					}
				}
				
			
			}
			
			System.out.println(e);
			
		}
		
		
		
	}

	public Map<String, State> getIdMap() {
		return idMap;
	}

	
	
	

}
