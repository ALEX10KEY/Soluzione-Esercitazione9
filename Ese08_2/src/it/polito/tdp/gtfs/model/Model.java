package it.polito.tdp.gtfs.model;


import it.polito.tdp.gtfs.db.GtfsDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.onebusaway.gtfs.model.Agency;
import org.onebusaway.gtfs.model.Stop;

public class Model {

	private Collection<Agency> agencies ;
	private Collection<Stop> stops ;
	private GtfsDao dao;
	private FloydWarshallShortestPaths<Stop, DefaultEdge> floydPaths;


	
	private DirectedGraph<Stop, DefaultEdge> graph ;
	
	public Model() {
		
	}
	
	public Collection<Stop> getAllStops() {
		if( this.stops != null ) 
			return this.stops ;
		
		
		agencies = dao.getAllAgencies() ;
		
		stops = new ArrayList<Stop>() ;
		
		for( Agency ag : agencies ) {
			stops.addAll(dao.getAllStops(ag)) ;
		}
		
		return stops ;
	}
	
	public void buildGraph() {
		if(graph!=null)
			return;
		
		
		graph = new SimpleDirectedGraph<Stop, DefaultEdge>(DefaultEdge.class) ;
		
		List<Stop> listaFerroviaria = new LinkedList<Stop>();
		
		for(Stop s: this.getAllStops()){
			if(!s.getId().toString().endsWith("_B")){
				listaFerroviaria.add(s);
			}
		}
		
		Graphs.addAllVertices(graph, listaFerroviaria) ;
		
		
		long time0 = System.nanoTime() ;
		for( Stop s1 : graph.vertexSet() ) {
			Collection<Stop> connesse = dao.getNextStops(s1);
				for( Stop s2 : connesse ) {
						graph.addEdge(s1, s2) ;
						System.out.format("Edge: %s - %s\n", s1, s2) ;
					
				}
		}
		
		long time1 = System.nanoTime() ;
		
		System.out.println(graph) ;
		
		System.out.format("Graph built: %d vertices, %d edges (%d ms)", 
				graph.vertexSet().size(), graph.edgeSet().size(),
				(time1-time0)/1000000) ;
	}

	public void setDao(GtfsDao dao) {
		// TODO Auto-generated method stub
		this.dao = dao;
	}

	public List<Stop> cercaPercorso(Stop partenza, Stop arrivo) {
		// TODO Auto-generated method stub
		
		if(floydPaths==null)
		floydPaths = new FloydWarshallShortestPaths<Stop, DefaultEdge>(graph);
		
		GraphPath<Stop, DefaultEdge> path =floydPaths.getShortestPath(partenza, arrivo);
		List<Stop> percorso = null;
		if(path!=null){
		percorso = Graphs.getPathVertexList(path);
		}
		return percorso;
	}

	public String stampaSequenza(List<Stop> sequenza) {
		// TODO Auto-generated method stub
		String ris="";
		for(Stop s : sequenza){
			ris+=s.getName() + "\n";
		}
		return ris;
	}
	
	
	
	
	
}
