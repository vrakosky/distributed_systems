import visidia.simulation.SimulationConstants;
import visidia.simulation.SimulationConstants.PropertyStatus;
import visidia.simulation.process.algorithm.LC2_Algorithm;

public class DetectionPanne extends LC2_Algorithm{
	
	int voisinsId[];
	String voisinsLabel[];
	int compteur_synch = 0;
    
	@Override
    protected void beforeStart() {
		voisinsId = new int[getArity()];
		voisinsLabel = new String[getArity()];
		setLocalProperty("id", vertex.getId());
        setLocalProperty("label", vertex.getLabel());     
    }
    
    @Override
    public String getDescription() {
        return "Error detection algorithm using LC2";
    }
    
    @Override
    protected void onStarCenter() {    	
    		compteur_synch++;    		
	    	
    		if(getActiveDoors().contains(0)){
	    		voisinsId[0] = (Integer)getNeighborProperty(0, "id");
			}
    		
    		for(int i = 1 ; i < getArity() ; i++){
    			if(getActiveDoors().contains(i)){
		    		voisinsId[i] = (Integer)getNeighborProperty(i, "id");
		    		voisinsLabel[i] = (String)getNeighborProperty(i, "label");
    			}
	    	}
	    	
	    	if(compteur_synch >= 3 && getLocalProperty("label").equals("W")){
	    		localTermination();
	    	}
	    	
	    	else if(getArity() != getActiveDoors().size()){
	    		int missingPort = -1;
	    		for(int i=0; i<getArity(); i++){
	    			if(!getActiveDoors().contains(i)){
	    				missingPort = i;
	    				break;
	    			}
	    		}
	    		voisinsLabel[missingPort] = "W";
	    		putProperty("noeud_voisin", voisinsLabel[missingPort], PropertyStatus.DISPLAYED);
	    	}
	    	for (int i = 0 ; i < getActiveDoors().size() ; i++){
	    		putProperty("voisins"+i, voisinsLabel[i], PropertyStatus.DISPLAYED);	    		
	    	}
    }
    
    @Override
    public Object clone() {
        return new DetectionPanne();
    }
}
