package com.chinasofti.ark.bdadp.service.graph.bean;

import java.util.Collection;

/**
 * Created by White on 2016/09/16.
 */
public interface Graph {

    String getId();

    void addVertex(Vertex v);

    void addVertex(Collection<Vertex> iterable);

    Vertex getVertex(String vertexId);

    Collection<Vertex> getAllVertex();

    Collection<Vertex> getStartVertexes();

    Collection<Vertex> getEndVertexes();

    Collection<Edge> getVertexInEdges(String vertexId);

    Collection<Edge> getVertexOutEdges(String vertexId);

    void addEdge(Edge e);

    void addEdge(Collection<Edge> iterable);

    Edge getEdge(String edgeId);

    Collection<Edge> getAllEdge();

    boolean isTerminalVertex(String vertexId);

    boolean isJoinVertex(String vertexId);

    boolean isJoinReady(String vertexId);

}
