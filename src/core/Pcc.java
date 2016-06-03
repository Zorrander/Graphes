package core ;

import java.io.* ;

import base.BinaryHeap;
import base.Readarg ;
import core.Label;

public class Pcc extends Algo {

    // Numero des sommets origine et destination
    protected int zoneOrigine ;
    protected int origine ;

    protected int zoneDestination ;
    protected int destination ;

    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
	super(gr, sortie, readarg) ;

	this.zoneOrigine = gr.getZone () ;
	this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;

	// Demander la zone et le sommet destination.
	this.zoneOrigine = gr.getZone () ;
	this.destination = readarg.lireInt ("Numero du sommet destination ? ");
    }

    public void run() {	
    System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;

    //varible pour mémoriser le min 
    int x_min ;
    
    //Création du tas 
    BinaryHeap unTas = new BinaryHeap() ; 	
    	
    //Création du tableau de labels || initialisation de l'algo
    Label.setTabLabels(this.graphe.getNbNoeuds())	;

    //Le sommet d'origine est mis dans le tas pour commencer 
    unTas.insert(origine);
    //DEBUT DE L'ALGO 
    //while le sommet de destination n'est pas marqué
	for (; Label.tabLabels.get(destination).isMarque() == false ;) {
		x_min = unTas.findMin() ;
		//on cherche le sommet qui donnera le coût de chemin le plus faible parmi tous les successeurs du min 

	}	
    }

}
