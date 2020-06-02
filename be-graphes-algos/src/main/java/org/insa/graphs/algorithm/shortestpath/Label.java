package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;

public class Label implements Comparable<Label>{
	
	private boolean marked;
    private Node node;
    private double cost;
    private Arc father;
    

    public Label(boolean marked, Node node, double cost, Arc father) {
    	this.marked = marked;
    	this.node = node;
        this.cost = cost;
        this.father = father;
        
    }
    
    @Override
    public int compareTo(Label label) {
        return Double.compare(cost, label.cost);
    }

    public Node getNode() {
        return node;
    }

    public double getCost() {
        return cost;
    }
    
    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean getMarked() {
        return marked;
    }

    public void setMarked() {
       marked = true;
    }
    

    public Arc getFather() {
        return father;
    }
    
    public void setFather(Arc father) {
    	this.father = father ;
    }

}
