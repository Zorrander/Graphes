package core;

import java.util.* ;

public class Sommet {

	int num_Sommet ;  //La numero de sommet serait en fait l'indice du tableau de sommets Sommets[] présent dans Graphe
	private float longitude ;
	private float lattitude ;
	public ArrayList<Arc> ListeDArcs ; // +++ placé en public pour le moment, peut être à modifier


	public Sommet (  float longi, float lat ) {
		//this.num_Sommet = numero ; 
		this.longitude = longi ;
		this.lattitude = lat ;
		this.ListeDArcs= new ArrayList<Arc>() ;
	}
	
	public Sommet(){
		
	}
	
	public void setLongitude( float new_long ) {
		this.longitude = new_long ;
	}

	public void setLattitude( float new_lat ) {
		this.lattitude = new_lat ;
	}
	
	public float getLongitude() {
		return this.longitude ;
	}

	public float getLattitude() {
		return this.lattitude ;
	}
	
	
	public void setList (int destination, int descripteur, int longueur){
		this.ListeDArcs.add(new Arc(destination,descripteur,longueur));
	}
	
	
}