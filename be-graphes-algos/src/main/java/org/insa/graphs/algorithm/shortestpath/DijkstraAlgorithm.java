package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.shortestpath.Label;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Path;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.model.Graph;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    //Initialisattion du label
    
		protected Label[] initLabels() {
			
	        final ShortestPathData data = getInputData();
			
	        Graph graph = data.getGraph();
			final int nbNodes = graph.size();
			Label[] labels = new Label[nbNodes];
			Node nodeorigin =data.getOrigin();
			
			for (int i = 0; i < labels.length; i++) {
				Node nodes = graph.get(i);
				labels[i] = new Label(false, nodes, Double.POSITIVE_INFINITY, null);
			}

			labels[nodeorigin.getId()].setCost(0);

			return labels;
		}

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        // TODO:
      // Initialisation
        
        Node nodedest = data.getDestination();
        Graph graph = data.getGraph();             
        ArrayList<Node> nodesFinal = new ArrayList<Node>(); // Création de la liste qui contiendra les nodes du chemin final trouvé
        Node nodeorigin =data.getOrigin();
        
        
        // Initialisation de la liste de label
        
        Label[] labelList=initLabels();
        // Notify observers about the first event (origin processed).
     	notifyOriginProcessed(data.getOrigin());
       
     	// Initialisation du tas
     	
     	BinaryHeap<Label> tas = new BinaryHeap<Label>();
        tas.insert(labelList[data.getOrigin().getId()]);
        
        // Vérification que le départ ne soit pas au même niveau que l'arrivée
        
        if (data.getOrigin().getId()==nodedest.getId())
        {
        	
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE );
        	
        	return solution;
        }
        
        //La condition du cours est un peu mal formulée. Il y a deux conditions de sortie de la boucle:
        // 1) Soit j'ai trouvé la destination (je l'ai sorti du tas et marqué), 2) Soit mon tas est vide (je ne peux pas atteindre la solution).
        
        // Programme principal
        
        while(tas.isEmpty() == false) { //Copie du code dans le cours
        	
        	double costxmin = 0;
        	Label xmin = tas.deleteMin();
        	
        	notifyNodeMarked(xmin.getNode());
        	
        	xmin.setMarked();
        	costxmin = xmin.getCost();
        	
        	for(Arc y : xmin.getNode().getSuccessors()) {
        		
        		if(data.isAllowed(y)) {
        			
        			int xsuiv = y.getDestination().getId();
        			
        			double costy = labelList[xsuiv].getCost();
        			
        			if(labelList[xsuiv].getMarked() == false) {
        				
        				if(costy> costxmin + data.getCost(y)) {
        					
        					labelList[xsuiv].setCost(costxmin + data.getCost(y));
        					
        					if(labelList[xsuiv].getMarked() == true){
        						
        	                       tas.remove(labelList[xsuiv]);
        	                    } 
        					
        					else {
        							tas.insert(labelList[xsuiv]);
        	                    	
        	                    }
        					
        					notifyNodeReached(y.getDestination());
        					
        					
        					
        					
        					//labelList[xsuiv].setFather(xmin.getNode());	
        					
        						
        					
        					
        					
        					
        				}
        				
        				
        			}
        			
    	
        		}
        		
        		
        	}
        	
        	// Condition d'arrêt de l'algorithme
        	
        	if (xmin.getNode().compareTo(nodedest)==0) //L'algorithme est stoppé si le dernier node visité est égale à la destination.
        	{
        		
        		break;
        	}
        	
        	
        	
        }
        
        // Représentation du chemin
        
        int currentNode =nodedest.getId();
        
        while(labelList[currentNode].getFather()!=null){
 
        			nodesFinal.add(labelList[currentNode].getNode());
        			currentNode=labelList[currentNode].getFather().getId();
        	
        }
        
        if (labelList[currentNode].getNode().equals(data.getOrigin()) == false){
        	
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        	
        	return solution;
        }
        
        notifyDestinationReached(data.getDestination());
        
        
        nodesFinal.add(nodeorigin); // A
        
        Collections.reverse(nodesFinal); // Liste inversée
        
        
        
        
        if (data.getMode().name()=="TIME") { //Choix du mode
        	
        	
        	
        	solution = new ShortestPathSolution(data, Status.OPTIMAL, Path.createFastestPathFromNodes(graph,nodesFinal));
        }
        
        else {
        	
        	solution = new ShortestPathSolution(data, Status.OPTIMAL, Path.createShortestPathFromNodes(graph,nodesFinal));
        	
        }	
        
        return solution;
        
    }



}
