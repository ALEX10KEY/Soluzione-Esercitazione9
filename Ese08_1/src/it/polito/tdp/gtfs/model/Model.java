package it.polito.tdp.gtfs.model;

import it.polito.tdp.gtfs.db.GtfsDao;

import java.util.ArrayList;
import java.util.Collection;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.onebusaway.gtfs.model.Agency;
import org.onebusaway.gtfs.model.Stop;

public class Model {

	private Collection<Agency> agencies ;
	private Collection<Stop> stops ;
	
	private DirectedGraph<Stop, DefaultEdge> graph ;
	
	public Model() {
		
	}
	
	public Collection<Stop> getAllStops() {
		if( this.stops != null ) 
			return this.stops ;
		
		GtfsDao dao = new GtfsDao() ;
		
		agencies = dao.getAllAgencies() ;
		
		stops = new ArrayList<Stop>() ;
		
		for( Agency ag : agencies ) {
			stops.addAll(dao.getAllStops(ag)) ;
		}
		
		return stops ;
	}
	
	public void buildGraph() {
		graph = new SimpleDirectedGraph<Stop, DefaultEdge>(DefaultEdge.class) ;
		
		Graphs.addAllVertices(graph, getAllStops()) ;
		
		GtfsDao dao = new GtfsDao() ;
		
		long time0 = System.nanoTime() ;
		for( Stop s1 : graph.vertexSet() ) {
			Collection<Stop> vicini = dao.getNextStops(s1);
				for(Stop tmp : vicini) {
					graph.addEdge(s1, tmp) ;
					System.out.format("Edge: %s - %s\n", s1, tmp) ;
			
			}
		}
		
		long time1 = System.nanoTime() ;
		
		System.out.println(graph) ;
		
		System.out.format("Graph built: %d vertices, %d edges (%d ms)", 
				graph.vertexSet().size(), graph.edgeSet().size(),
				(time1-time0)/1000000) ;
	}
	
	public static void main(String[] args) {
		Model m = new Model() ;
		
		m.buildGraph();
	}
	
	
}
