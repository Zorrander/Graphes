package core;

// import base.Descripteur;

public class Arc {

	private int num_sommet_arrivee ;
	private int num_descripteur ;
	private float longueur ; // en mètres
	// Segment[] segments ; pas important, juste pour la représentation graphique
	
	public Arc ( int num_arrivee , int num_des, int longueur) {
		this.num_sommet_arrivee = num_arrivee ;
		this.num_descripteur = num_des ;
		this.longueur = longueur ;
	}
	
	public float getLongueur (){return this.longueur;} 
	public int getDescripteur (){return this.num_descripteur;} 
	public int getDest (){return this.num_sommet_arrivee;} 
}
