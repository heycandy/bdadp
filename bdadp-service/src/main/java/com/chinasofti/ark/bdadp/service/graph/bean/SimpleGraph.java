package com.chinasofti.ark.bdadp.service.graph.bean;

import java8.util.stream.StreamSupport;

import java.util.*;

/**
 * Created by White on 2016/09/16.
 */
public class SimpleGraph implements Graph {

    private final String id;

    private final Map<String, Vertex> vertexMap;

    private final Map<String, Edge> edgeMap;

    private final Map<String, Set<Edge>> inEdgeMap;
    private final Map<String, Set<Edge>> outEdgeMap;

    public SimpleGraph(String id) {
        this.id = id;

        vertexMap = new HashMap<>();

        edgeMap = new HashMap<>();

        inEdgeMap = new HashMap<>();
        outEdgeMap = new HashMap<>();

    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void addVertex(Vertex v) {
        this.vertexMap.put(v.getId(), v);
    }

    @Override
    public void addVertex(Collection<Vertex> collection) {
        StreamSupport.stream(collection).forEach(this::addVertex);
    }

    @Override
    public Vertex getVertex(String vertexId) {
        return this.vertexMap.get(vertexId);
    }

    @Override
    public Collection<Vertex> getAllVertex() {
        return this.vertexMap.values();
    }

    @Override
    public Collection<Vertex> getStartVertexes() {
        Set<Vertex> vertexSet = new HashSet<>();

        StreamSupport.stream(this.getAllVertex()).forEach(v -> {
            if (!this.inEdgeMap.containsKey(v.getId())) {
                vertexSet.add(v);
            }
        });

        return vertexSet;
    }

    @Override
    public Collection<Vertex> getEndVertexes() {
        Set<Vertex> vertexSet = new HashSet<>();

        StreamSupport.stream(this.getAllVertex()).forEach(v -> {
            if (!this.outEdgeMap.containsKey(v.getId())) {
                vertexSet.add(v);
            }
        });

        return vertexSet;
    }

    @Override
    public Set<Edge> getVertexInEdges(String vertexId) {
        return this.inEdgeMap.get(vertexId);
    }

    @Override
    public Set<Edge> getVertexOutEdges(String vertexId) {
        return this.outEdgeMap.get(vertexId);
    }

    @Override
    public void addEdge(Edge e) {
        Vertex fromVertex = e.getFromVertex();
        Vertex toVertex = e.getToVertex();

        Set<Edge> outEdge = getVertexOutEdges(fromVertex.getId());
        if (outEdge == null) {
            outEdge = new HashSet<>();
            this.outEdgeMap.put(fromVertex.getId(), outEdge);
        }

        outEdge.add(e);

        Set<Edge> inEdge = getVertexInEdges(toVertex.getId());
        if (inEdge == null) {
            inEdge = new HashSet<>();
            this.inEdgeMap.put(toVertex.getId(), inEdge);
        }

        inEdge.add(e);

        this.edgeMap.put(e.getId(), e);
    }

    @Override
    public void addEdge(Collection<Edge> collection) {
        StreamSupport.stream(collection).forEach(this::addEdge);
    }

    @Override
    public Edge getEdge(String edgeId) {
        return this.edgeMap.get(edgeId);
    }

    @Override
    public Collection<Edge> getAllEdge() {
        return this.edgeMap.values();
    }

    @Override
    public boolean isTerminalVertex(String vertexId) {
        Collection<Edge> edges = getVertexOutEdges(vertexId);

        return edges != null && edges.size() > 0;
    }

    @Override
    public boolean isJoinVertex(String vertexId) {
        Collection<Edge> edges = getVertexInEdges(vertexId);

        return edges != null && edges.size() > 1;
    }

    @Override
    public boolean isJoinReady(String vertexId) {
        Collection<Edge> edges = getVertexInEdges(vertexId);

        return StreamSupport.stream(edges)
                .map(Edge::getFromVertex)
                .allMatch(v -> v.getState() == VertexState.SUCCESS.ordinal());
    }

    @Override
    public String toString() {
        return "SimpleGraph{" +
                "id='" + id + '\'' +
                ", \nvertexMap=" + vertexMap +
                ", \nedgeMap=" + edgeMap +
                ", \ninEdgeMap=" + inEdgeMap +
                ", \noutEdgeMap=" + outEdgeMap +
                ", \nstartVertexes=" + getStartVertexes() +
                ", \nendVertexes=" + getEndVertexes() +
                '}';
    }
}
