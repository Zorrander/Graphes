package core;

import java.util.Vector;

public class Label {
	//quadruplet marquage, cout, pere, sommet courant
	
	private boolean marquage ; //booléen valant vrai si le sommet est définitivement fixé par l'algorithme
	private float cout ; //valeur courante du plus court chemin depuis l'origine vers le sommet
	private int pere ; //correspond au sommet précédent sur le chemin correspondant au plus court chemin courant
	private int sommet_courant ; //Le sommet courant est le sommet associé à ce label (sommet ou numéro de sommet)
	
	public static Vector<Label> tabLabels ;
	
	public Label ( boolean marquage, float cout, int pere, int sommet_courant ){
		this.marquage = marquage ;
		this.cout = cout ;
		this.pere = pere ;
		this.sommet_courant = sommet_courant ;
	}

	public static void setTabLabels ( int nb_noeuds ) {
		
		int index ;
		for (index=0 ; index < nb_noeuds ; index++) {
			tabLabels.add(new Label(false, -1, -1, index));
		}
	}
	
	public void updateLabel ( boolean marquage, float cout, int pere, int sommet_courant) {
		Label new_lab ;
		if ( ( tabLabels.get(sommet_courant).getCout() == -1 ) || (cout < tabLabels.get(sommet_courant).getCout() ) ) {
			new_lab = new Label ( marquage, cout, pere, sommet_courant ) ;
		 	tabLabels.setElementAt( new_lab, sommet_courant ) ;
		}		
	}
	
	public boolean isMarque () {
		return this.marquage ; 
	}
	
	public float getCout() {
		return this.cout ;
	}
	
}
