package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.shortestpath.Label;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.*;

import java.util.ArrayList;
//import java.util.Arrays;
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
        final int nbNodes = graph.size();
        Arc[] nodesFinal = new Arc[nbNodes]; // Création de la liste qui contiendra les nodes du chemin final trouvé Arc[] predecessorArcs = new Arc[nbNodes];
        //Node nodeorigin =data.getOrigin();
        
        
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
        															//La bonne méthode c'est de repartir du label de ton noeud de destination, qui stocke l'arc 
        														    //qui le relie vers son père (d'ailleur, petite modification à faire également ici, pour 
        															//stocker le père d'un noeud, on stocke plutot l'arc, ça facilite la vie justement 
        															//à cette étape pour recréer le chemin)
        		if(data.isAllowed(y)) {
        			
        			int xsuiv = y.getDestination().getId();
        			
        			double costy = labelList[xsuiv].getCost();
        			
        			if(labelList[xsuiv].getMarked() == false) {
        				
        				if(costy> costxmin + data.getCost(y)) {
        					
        					
        					
        					if(labelList[xsuiv].getFather() != null){
        						
        	                       tas.remove(labelList[xsuiv]);
        	                       labelList[xsuiv].setCost(costxmin + data.getCost(y));
               					   labelList[xsuiv].setFather(y);
        	                       tas.insert(labelList[xsuiv]);
        	                    } 
        					
        					else {
        							notifyNodeReached(y.getDestination());
        							labelList[xsuiv].setCost(costxmin + data.getCost(y));
                  					labelList[xsuiv].setFather(y);
                  					tas.insert(labelList[xsuiv]);
        							
        	                    }	
        					
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
        
        if(labelList[nodedest.getId()].getFather() != null) {		//Si mon label qui représente mon noeud de destination à un père, alors j'ai trouvé une solution, sinon c'est infeasable"
			notifyDestinationReached(data.getDestination());
			
	        // Représentation du chemin
	        
			ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = labelList[nodedest.getId()].getFather();
            //label[destinations...].getfather
            while (arc != null) {
                arcs.add(arc);
                arc = labelList[arc.getOrigin().getId()].getFather();
            }

            // Reverse the path...
            Collections.reverse(arcs);
			

			// Create the final solution.
			solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        	
        }
        
        else {

        	solution = new ShortestPathSolution(data, Status.INFEASIBLE);
		}
        

		return solution;
        
    }



}
