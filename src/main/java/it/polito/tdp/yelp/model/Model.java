package it.polito.tdp.yelp.model;

import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	YelpDao dao;
	Graph<Business, DefaultWeightedEdge> grafo;

	public Model() {
		this.dao= new YelpDao();
	}
	public List<String> getAllCity(){
		return this.dao.getAllCity();
	}
	public void buildGraph(String city) {
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(city));
		for(Business b1: this.grafo.vertexSet()) {
			for(Business b2: this.grafo.vertexSet()) {
				if(!b1.equals(b2)) {
					double distance = LatLngTool.distance(b1.getCoordinate(), b2.getCoordinate(), LengthUnit.KILOMETER);
					Graphs.addEdge(this.grafo, b1, b2, distance);
				}
		}
		}
	}
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	public Set<Business> getVertici(){
		return this.grafo.vertexSet();
	}
	public Business getLocalePiuDistante(Business b) {	
			double distanzaMax=0;
			for(DefaultWeightedEdge e: grafo.edgesOf(b)) {
				if(grafo.getEdgeWeight(e)>distanzaMax) {
					distanzaMax=grafo.getEdgeWeight(e);
				}
			}
			Business result=null;
			for(DefaultWeightedEdge e: grafo.edgesOf(b)) {
				if(grafo.getEdgeWeight(e)==distanzaMax) {
					result=Graphs.getOppositeVertex(grafo, e, b);
				}
				}
			return result;
		}
	public double getDistanzaLocaleDistante(Business b) {
		double distanzaMax=0;
		for(DefaultWeightedEdge e: grafo.edgesOf(b)) {
			if(grafo.getEdgeWeight(e)>distanzaMax) {
				distanzaMax=grafo.getEdgeWeight(e);
			}
		}
		return distanzaMax;
	}
	}

