package core;


import java.util.ArrayList;
import java.util.Vector;


/** 
 [0-3]   = Magic number 0xdecafe (doit se trouver au début du fichier)
 [4-7]   = Version du format
 [8-11]  = Identifiant de carte
 [12-15] = Nombre de noeuds dans le chemin
 [16-19] = Identifiant du premier noeud (8 bits zone + 24 bits numéro noeud)
 [20-23] = Identifiant du dernier noeud (8 bits zone + 24 bits numéro noeud)

 [24-...] = Identifiant des noeuds du chemin
 */
public class Chemin {

	private int magic_number;
	private int version;
	private int identifiant;
	private int nbr_sommets;
	private int first_sommet;
	private int last_sommet;
	private ArrayList<Integer> sommetsChemin;
	
	public Chemin(int magic_number, int version, int identifiant, int nbr_sommets,
			int first_sommet, int last_sommet) {
		this.magic_number = magic_number;
		this.version = version;
		this.identifiant = identifiant;
		this.nbr_sommets = nbr_sommets;
		this.first_sommet = first_sommet;
		this.last_sommet = last_sommet;
		this.sommetsChemin = new ArrayList<Integer> ();
	
	}
	
	public void addSommet(int newSommet){
		this.sommetsChemin.add(newSommet);
	}

	
	public int getNoeud(int i){
		return this.sommetsChemin.get(i);
	}
		
	

   
}
