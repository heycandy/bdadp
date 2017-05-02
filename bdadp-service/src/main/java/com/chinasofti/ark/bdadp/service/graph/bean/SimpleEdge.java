package com.chinasofti.ark.bdadp.service.graph.bean;

/**
 * Created by White on 2016/09/16.
 */
public class SimpleEdge implements Edge {

    private final String id;
    private final Vertex fromVertex;
    private final Vertex toVertex;

    public SimpleEdge(Vertex fromVertex, Vertex toVertex) {
        this.id = String.format("%s->%s", fromVertex.getId(), toVertex.getId());
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
    }

    public SimpleEdge(String id, Vertex fromVertex, Vertex toVertex) {
        this.id = id;
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Vertex getFromVertex() {
        return this.fromVertex;
    }

    @Override
    public Vertex getToVertex() {
        return this.toVertex;
    }

    @Override
    public String toString() {
        return "SimpleEdge{" +
                "id='" + id + '\'' +
                ", fromVertex=" + fromVertex +
                ", toVertex=" + toVertex +
                '}';
    }
}
