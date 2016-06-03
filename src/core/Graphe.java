package core ;

/**
 *   Classe representant un graphe.
 *   A vous de completer selon vos choix de conception.
 */

import java.io.* ;
import java.util.* ;
import base.* ;
import core.* ;


public class Graphe {

    // Nom de la carte utilisee pour construire ce graphe
    private final String nomCarte ;

    // Fenetre graphique
    private final Dessin dessin ;

    // Version du format MAP utilise'.
    private static final int version_map = 4 ;
    private static final int magic_number_map = 0xbacaff ;

    // Version du format PATH.
    private static final int version_path = 1 ;
    private static final int magic_number_path = 0xdecafe ;

    // Identifiant de la carte
    private int idcarte ;

    // Numero de zone de la carte
    private int numzone ;

    /*
     	Tableau de sommets, chaque indice du tableau correspondant au numéro de sommet! 
     */
    private Vector<Sommet> sommets ;
    private Vector <Descripteur> descripteurs ;
    
    
    // Deux magnifiques getters.
    public Dessin getDessin() { return dessin ; }
    public int getZone() { return numzone ; }

    // Le constructeur cree le graphe en lisant les donnees depuis le DataInputStream
    public Graphe (String nomCarte, DataInputStream dis, Dessin dessin) {

	this.nomCarte = nomCarte ;
	this.dessin = dessin ;
	Utils.calibrer(nomCarte, dessin) ;
	
	// Lecture du fichier MAP. 
	// Voir le fichier "FORMAT" pour le detail du format binaire.
	try {
		
	    // Nombre d'aretes
	    int nb_tot_aretes = 0 ;

	    // Verification du magic number et de la version du format du fichier .map
	    int magic = dis.readInt () ;
	    int version = dis.readInt () ;
	    Utils.checkVersion(magic, magic_number_map, version, version_map, nomCarte, ".map") ;

	    // Lecture de l'identifiant de carte et du numero de zone, 
	    this.idcarte = dis.readInt () ;
	    this.numzone = dis.readInt () ;

	    // Lecture du nombre de descripteurs, nombre de noeuds.
	    int nb_descripteurs = dis.readInt () ;
	    int nb_nodes = dis.readInt () ;
	    
	    
	  
	    // Nombre de successeurs enregistrÃ©s dans le fichier.
	    // int[] nsuccesseurs_a_lire = new int[nb_nodes] ;
	    this.sommets = new Vector<Sommet>() ; // Taille du tableau sommets égale au nombre de sommets
	    								      // Les sommets sont bien numérotés à partir de 0
	    this.descripteurs = new Vector<Descripteur>() ;
	    
	    int[] successeurs_a_lire = new int[nb_nodes] ; // +++ Sert à connaitre le nombre de routes sortantes à partir de chaque noeud
	    											   // Vu qu'on en aura jamais besoin en dehors une fois tout construit je pense qu'on peut ne pas le mettre comme attribut de Graphe 
	    											   // mais juste le dévlarer ici ?
	    								
	    
	    
	
	 

	   
	    
	    // +++ utilisée plus loin que prévu finalement : lorsque qu'on ignore la zone 
	    int var_qui_sert_a_rien ; // qui servira à mettre des octects qui ne nous servent pas
	    						  // juste pour décaler le dis!
	    float var_qui_sert_a_rien2 ; // la même en float
	    
	    System.out.println("Fichier lu : " + nb_nodes + " sommets, " + nb_tot_aretes + " aretes, " 
			       + nb_descripteurs + " descripteurs.") ;
	    
	    /////////////////////////////////////////////////////////
	    /////////////////LECTURE DES NOEUDS/////////////////////
	    ///////////////////////////////////////////////////////
	    
	    //initialisation des champs longitude et latitude
	    //dimensionement des listes d'arcs si besoin
	    
	    float longitude = 0 ;
	    float lattitude = 0 ; 
	   
	    for (int num_node = 0 ; num_node < nb_nodes ; num_node++) {
	    	 longitude = (float)dis.readInt () / 1E6f  ;
		     lattitude = (float)dis.readInt () / 1E6f ; 
		 
			// Lecture du noeud numero num_node
			this.sommets.addElement(new Sommet(longitude,lattitude));  
			//var_qui_sert_a_rien = dis.readUnsignedByte() ; // on se décale d'un octet
			successeurs_a_lire[num_node] = dis.readUnsignedByte() ; // nécessaire pour se balader dans le fichier après
	    }
	    

	 
	 
	    Utils.checkByte(255, dis) ;
	    
		/////////////////////////////////////////////////////////
		/////////////////LECTURE DES DESCRIPTEURS/////////////////////
		///////////////////////////////////////////////////////
	    
	    //pas grand chose � ajouter 
	    for (int num_descr = 0 ; num_descr < nb_descripteurs ; num_descr++) {
	    	// Lecture du descripteur numero num_descr
	    	this.descripteurs.addElement(new Descripteur(dis));
	    	

	    	// On affiche quelques descripteurs parmi tous.
			if (0 == num_descr % (1 + nb_descripteurs / 400))
				System.out.println("Descripteur " + num_descr + " = " + descripteurs.elementAt(num_descr)) ;
	    }
	   
	    Utils.checkByte(254, dis) ;

	    
		/////////////////////////////////////////////////////////
		/////////////////LECTURE DES ROUTES SORTANTES/////////////////////
		///////////////////////////////////////////////////////
	    

	    
	    // Lecture des successeurs
	    for (int num_node = 0 ; num_node < nb_nodes ; num_node++) {
		// Lecture de tous les successeurs du noeud num_node
		for (int num_succ = 0 ; num_succ < successeurs_a_lire[num_node] ; num_succ++) {
			
		    // zone du successeur
		    var_qui_sert_a_rien = dis.readUnsignedByte() ; //  ignoré pour le moment

		    // numero de noeud du successeur
		    int dest_node = Utils.read24bits(dis) ;  // +++ BIG ENDIAN?!

		    // descripteur de l'arete
		    int descr_num = Utils.read24bits(dis) ;

		    // longueur de l'arete en metres
		    int longueur  = dis.readUnsignedShort() ;

		    // Nombre de segments constituant l'arete
		    int nb_segments  = dis.readUnsignedShort() ; // on en a besoin pour savoir combien de fois il va falloir avancer dis entre chaque sommet pour passer tous les segments...

		    nb_tot_aretes++ ; // +++ on incrèmente le nombre d'arretes total
		    
		    Couleur.set(dessin, descripteurs.elementAt(descr_num).getType()) ; // +++ ça fait quoi ce truc ..??
		    														 // je le laisse dans le doute !

		    // On crée l'arc à l'aide du constructeur que l'on va mettre dans la liste d'arcs qui est avec le sommet
		    this.sommets.elementAt(num_node).ListeDArcs.add( new Arc( dest_node, descr_num, longueur )) ; // #plutotfierdecetteligne
		    
		    // +++ On a le numéro du descripteur => on veut savoir si c'est pas double sens pour savoir s'il faut dédoubler l'arc !!
		    if ( descripteurs.elementAt(descr_num).isSensUnique() ) {} // route à sens unique : on fait rien
		    else {  // route à double sens : on rajoute l'arc dans l'autre sens
		    	sommets.elementAt(dest_node).ListeDArcs.add( new Arc( num_node, descr_num, longueur )) ;
		    }

		    
		    // Chaque segment est dessine' // +++ SAUF qu'on s'en b***** des segments on a dit !!
		    							   // subtilité, faut quand même déplacer notre dis et donc avoir retenu le nombre de segments avant!
		    for (int i = 0 ; i < nb_segments ; i++) {
			var_qui_sert_a_rien2 = (dis.readShort()) / 2.0E5f ; // delta_lon
			var_qui_sert_a_rien2 = (dis.readShort()) / 2.0E5f ; // delta_lat
			// dessin.drawLine(current_long, current_lat, (current_long + delta_lon), (current_lat + delta_lat)) ;
			//current_long += delta_lon ;
			//current_lat  += delta_lat ;				  -----------	 ON DESSINE PAS LES SEGMENTS    -----------
		    }
		    
		    //			+++ #OSEF +++
		    
		    // Le dernier trait rejoint le sommet destination.
		    // On le dessine si le noeud destination est dans la zone du graphe courant.
		    //if (succ_zone == numzone) {
		    	dessin.drawLine(sommets.elementAt(num_node).getLongitude(), sommets.elementAt(num_node).getLattitude(),
		    					sommets.elementAt(dest_node).getLongitude(), sommets.elementAt(dest_node).getLattitude() ) ;
		     //}
		    
		    //			+++ #OSEF +++
		    
		   
		} // on passe au successeur suivant
	    } // on passe au node suivant
	    
	    Utils.checkByte(253, dis) ;

	    
	    /*  +++ POINT IMPORTANT : on s'est mis d'accord pour dédoubler tous les noeuds qui sont à double sens... c'est parti !
	     *  ---------------------------------------------------------------------------------------
	     *  Sera fait au dessus, lorsque l'on est avec chaque arc déja dans les mains.
	     *  Sinon faudrait reparcourir chaque arc de chaque noeud une nouvelle fois !!
	     *  ---------------------------------------------------------------------------------------	    
	     */
	    
	    
	    
	    System.out.println("Fichier lu : " + nb_nodes + " sommets, " + nb_tot_aretes + " aretes, " 
			       + nb_descripteurs + " descripteurs.") ;

	} catch (IOException e) {
	    e.printStackTrace() ;
	    System.exit(1) ;
	}

    } // fin du constructeur Graphe()
    
    public int getNbNoeuds() {return this.sommets.size() ; }
    

    
    
    // Rayon de la terre en metres
    private static final double rayon_terre = 6378137.0 ;


    /**
     *  Calcule de la distance orthodromique - plus court chemin entre deux points à la surface d'une sphère
     *  @param long1 longitude du premier point.
     *  @param lat1 latitude du premier point.
     *  @param long2 longitude du second point.
     *  @param lat2 latitude du second point.
     *  @return la distance entre les deux points en metres.
     *  Methode décrite par Thomas Thiebaud, mai 2013
     */
    public static double distance(double long1, double lat1, double long2, double lat2) {
        double sinLat = Math.sin(Math.toRadians(lat1))*Math.sin(Math.toRadians(lat2));
        double cosLat = Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2));
        double cosLong = Math.cos(Math.toRadians(long2-long1));
        return rayon_terre*Math.acos(sinLat+cosLat*cosLong);
    }

    /**
     *  Attend un clic sur la carte et affiche le numero de sommet le plus proche du clic.
     *  A n'utiliser que pour faire du debug ou des tests ponctuels.
     *  Ne pas utiliser automatiquement a chaque invocation des algorithmes.
     */
    
    
    /* --------------------
    	FONCTION MISE EN COM CAR SOURCE D'ERREUR POUR LE MOMENT
    	ON S'EN SERT PAS ENCORE
    
    public void situerClick() {

	System.out.println("Allez-y, cliquez donc.") ;
	
	if (dessin.waitClick()) {
	    float lon = dessin.getClickLon() ;
	    float lat = dessin.getClickLat() ;
	    
	    System.out.println("Clic aux coordonnees lon = " + lon + "  lat = " + lat) ;

	    // On cherche le noeud le plus proche. O(n)
	    float minDist = Float.MAX_VALUE ;
	    int   noeud   = 0 ;
	    
	    for (int num_node = 0 ; num_node < longitudes.length ; num_node++) {
		float londiff = (longitudes[num_node] - lon) ;
		float latdiff = (latitudes[num_node] - lat) ;
		float dist = londiff*londiff + latdiff*latdiff ;
		if (dist < minDist) {
		    noeud = num_node ;
		    minDist = dist ;
		}
	    }

	    System.out.println("Noeud le plus proche : " + noeud) ;
	    System.out.println() ;
	    dessin.setColor(java.awt.Color.red) ;
	    dessin.drawPoint(longitudes[noeud], latitudes[noeud], 5) ;
	}
    }

 ------------------------------- FIN DE situerClick() */

    /**
     *  Charge un chemin depuis un fichier .path (voir le fichier FORMAT_PATH qui decrit le format)
     *  Verifie que le chemin est empruntable et calcule le temps de trajet.
     */
    public void verifierChemin(DataInputStream dis, String nom_chemin, Graphe laCarte) {
	
	try {
	    
	    // Verification du magic number et de la version du format du fichier .path
	    int magic = dis.readInt () ;
	    int version = dis.readInt () ;
	    Utils.checkVersion(magic, magic_number_path, version, version_path, nom_chemin, ".path") ;

	    // Lecture de l'identifiant de carte
	    int path_carte = dis.readInt () ;

	    if (path_carte != this.idcarte) {
		System.out.println("Le chemin du fichier " + nom_chemin + " n'appartient pas a la carte actuellement chargee." ) ;
		System.exit(1) ;
	    }

	    int nb_noeuds = dis.readInt () ;

	    // Origine du chemin
	    int first_zone = dis.readUnsignedByte() ;
	    int first_node = Utils.read24bits(dis) ;

	    // Destination du chemin
	    int last_zone  = dis.readUnsignedByte() ;
	    int last_node = Utils.read24bits(dis) ;

	    System.out.println("Chemin de " + first_zone + ":" + first_node + " vers " + last_zone + ":" + last_node) ;

	    int current_zone = 0 ;
	    int current_node = 0 ;
	    

	    //CREATION DU CHEMIN
	    
		Chemin testChemin = new Chemin(magic,version,path_carte,nb_noeuds,first_node,last_node);
	
	    // Tous les noeuds du chemin
	    for (int i = 0 ; i < nb_noeuds ; i++) {
		current_zone = dis.readUnsignedByte() ;
		current_node = Utils.read24bits(dis) ;
		System.out.println(" --> " + current_zone + ":" + current_node) ;
		testChemin.addSommet(current_node);
		
	    }
	   
	    if ((current_zone != last_zone) || (current_node != last_node)) {
		    System.out.println("Le chemin " + nom_chemin + " ne termine pas sur le bon noeud.") ;
		    System.exit(1) ;
		}
	   
	    //CALCUL DU COUT DU CHEMIN EN TEMPS
		
	    float time = 0 ; //temps de parcours
	    float time_min ;
	    float dist = 0 ; //distance interm�diaire
	    float dist_totale = 0 ; //distance entre le sommet initial et le sommet final du chemin
	    
	    //On parcourt la liste des sommets du chemin
	   
	    
	    Sommet current_sommet ;
	    int arcTrouves = 0 ;
	    
	    
	    for (int i = 0; i < nb_noeuds-1 ; i++) {
	    //temps de trajet = distance entre les deux sommets / vitesse max sur la route
	    time_min = 9999999 ;
	    //variable avec laquelle on se d�place dans le chemin
	    current_sommet = laCarte.sommets.elementAt(testChemin.getNoeud(i)) ;
	   
	    // Pour chaque sommet on parcourt la liste des arcs qui partent de ce sommet 
		// Quand on a trouv� l'arc correspondant � la route entre ce sommet et le suivant dans la liste des
		// sommets du chemin on fait un getLongueur
	    System.out.println("("+ testChemin.getNoeud(i)+"->"+ testChemin.getNoeud(i+1) + "), Arcs suivants possibles : ");
	    for (Arc a:current_sommet.ListeDArcs){
	    	System.out.println(a.getDest());
	    		if (a.getDest()==testChemin.getNoeud(i+1) && (time_min > (a.getLongueur()/((1000/60)*(laCarte.descripteurs.elementAt(a.getDescripteur())).vitesseMax())))){
	    			
	    		arcTrouves++;
	    		 dist = a.getLongueur() ;
	    		 //on met � jour la distance et le temps de parcours
	    		 time_min = (dist/((1000/60)*(laCarte.descripteurs.elementAt(a.getDescripteur())).vitesseMax()));

	    		 System.out.println("Descripteur : " + a.getDest() + ", vitesse " + laCarte.descripteurs.elementAt(a.getDescripteur()).vitesseMax());
	    		 
	    		}	 
			}
		 time = time + time_min;
		 dist_totale = dist_totale + dist ;
	    }
	    System.out.println(nom_chemin + " : " + time + " minutes pour faire "+dist_totale+"m");
	    System.out.println("Arcs trouves : " + arcTrouves+"/"+(nb_noeuds-1));

	} catch (IOException e) {
	    e.printStackTrace() ;
	    System.exit(1) ;
	}

    }

}
