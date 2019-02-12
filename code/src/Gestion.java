import visidia.simulation.SimulationConstants.PropertyStatus;
import visidia.simulation.process.algorithm.LC0_Algorithm;
import visidia.simulation.process.algorithm.LC1_Algorithm;
import visidia.simulation.process.edgestate.MarkedState;
import java.util.ArrayList;

public class Gestion extends LC1_Algorithm{
	// Description de la classe s'affichant sous ViSiDiA
	@Override
	public String getDescription(){
		return "Election dans un arbre";
	}

	// Fonction s'exécutant avant le commencement de l'algorithme sous ViSiDiA
	@Override
	protected void beforeStart(){
        // Propriété premettant l'affichage d'un état (N, F ou E) sous un sommet (vertex)
		setLocalProperty("label", vertex.getLabel());
        // Propriété premettant l'affichage de l'état (stable ou non) du point
        setLocalProperty("estStable", false);
        // Propriété premettant l'affichage de la distance à laquelle se situe le point stable
        setLocalProperty("distancePointStable", -1);
        // Propriété stockant l'ordre du graphe
        setLocalProperty("ordre", 1);
        
        // Affichage de l'état "estStable"
        putProperty("estStable", getLocalProperty("estStable"), PropertyStatus.DISPLAYED);
        // Affichage de la "distancePointStable"
        putProperty("distancePointStable", getLocalProperty("distancePointStable"), PropertyStatus.DISPLAYED);
        // Affichage de l' "ordre"
        putProperty("ordre", getLocalProperty("ordre"), PropertyStatus.DISPLAYED);
	}

	// TO DO
	@Override
	protected void onStarCenter(){
        // Détermination du diamètre du graphe
        if((Boolean)getLocalProperty("estStable") == true){
        	// Récupère le chemin du port 0
        	int min_a = (Integer)getNeighborProperty(0, "distancePointStable");
        	// Parcours des chemins jusqu'alors générés par les voisins, afin d'en sélectionner le plus court
        	for(int i=1; i < getArity(); i++){
        		// Si une distance inférieure à la distance courante est trouvée, l'ancienne distance est remplacée par la courante
        		if((Integer)getNeighborProperty(i, "distancePointStable") < min_a){
        			// Mise à jour de la distance
        			min_a = (Integer)getNeighborProperty(i, "distancePointStable");
                }
            }
        	
        	int a = min_a + 1;
        	
        	// Empêche l'incrémentation de la valeur du chemin lorsque l'ordre est atteint (au niveau de l'élu)
        	if (getLocalProperty("label").equals("E")){
        		if ( a > (Integer)getLocalProperty("ordre")){
        			a = (Integer)getLocalProperty("ordre");
        		}
        	}
        			
            setLocalProperty("distancePointStable", a);
            // Affichage de la "distancePointStable"
            putProperty("distancePointStable", getLocalProperty("distancePointStable"), PropertyStatus.DISPLAYED);
        }
        
        
		// N---N -> F---N (si le centre a un seul voisin à "N")
		if (getLocalProperty("label").equals("N") && hasOnlyOneNeighborToN()){
			// Changement du label
			setLocalProperty("label", "F");
            // Mise à l'état "stable" du point
            setLocalProperty("estStable", true);
            // Initialisation de la distance
            setLocalProperty("distancePointStable", 0);
            // Incrémentation de l'ordre au niveau du point
            incrementOrder();
            
            // Affichage de l'état "estStable"
            putProperty("estStable", getLocalProperty("estStable"), PropertyStatus.DISPLAYED);
            // Affichage de la "distancePointStable"
            putProperty("distancePointStable", getLocalProperty("distancePointStable"), PropertyStatus.DISPLAYED);
            // Affichage de l' "ordre"
            putProperty("ordre", getLocalProperty("ordre"), PropertyStatus.DISPLAYED);
		}
		
		// F---N---F -> F---E---F ("N" élu en "E" lorsque tous les autres points du graphe sont à l'état "F")
		else if (getLocalProperty("label").equals("N") && hasAllNeighborToF()){		
			// Changement du label
			setLocalProperty("label", "E");
            // Mise à l'état "stable" du point
            setLocalProperty("estStable", true);
            // Initialisation de la distance
            setLocalProperty("distancePointStable", 0);
            // Incrémentation de l'ordre au niveau du point
            incrementOrder();
            
            // Affichage de l'état "estStable"
            putProperty("estStable", getLocalProperty("estStable"), PropertyStatus.DISPLAYED);
            // Affichage de la "distancePointStable"
            putProperty("distancePointStable", getLocalProperty("distancePointStable"), PropertyStatus.DISPLAYED);
            // Affichage de l' "ordre"
            putProperty("ordre", getLocalProperty("ordre"), PropertyStatus.DISPLAYED);
		}
	}

	// Clonage de la classe Election au niveau de chaque noeud
	@Override
	public Object clone(){
		return new Gestion();
	}

	// Test indiquant si un seul voisin est à N
	public boolean hasOnlyOneNeighborToN(){
		// Compteur de voisins "N"
		int compteurDeN = 0;
		// Parcours des voisins du point central
		for(int i = 0; i < getArity(); i++){
			// Incrémentation du compteur si un voisin a l'état "N" (en récupérant l'état via "label")
			if( getNeighborProperty(i, "label").equals("N")){
				compteurDeN++;
			}
		}
		// Un seul voisin est à l'état "N"
		if(compteurDeN == 1){
			return true;
		}
		// Aucun ou plus d'un voisin est à l'état "N"
		else{
			return false;
		}
	}

	// Test indiquant si tous les voisins sont à F
	public boolean hasAllNeighborToF() {
		// Compteur de voisins "F"
		int compteurDeF = 0;
		// Parcours des voisins du point central
		for(int i = 0; i <  getArity(); i++){
			// Incrémentation du compteur si un voisin a l'état "F" (en récupérant l'état via "label")
			if( getNeighborProperty(i, "label").equals("F")){
				compteurDeF++;
			}
		}
		// Tous les voisins sont à l'état "F"
		if(compteurDeF == getArity()){
			return true;
		}
		// Tous les voisins ne sont pas à "F"
		else{
			return false;
		}
	}

	public void incrementOrder(){
		for(int i=0; i < getArity(); i++){
        	if(getNeighborProperty(i,"label") == "F"){
        		setLocalProperty("ordre", (Integer)getLocalProperty("ordre") + (Integer)getNeighborProperty(i,"ordre"));
        	}	
        }
	}
}