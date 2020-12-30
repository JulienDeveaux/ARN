import java.util.*;
import java.math.*;

/**
 * <p>
 * Implantation de l'interface Collection basée sur les arbres binaires de
 * recherche. Les éléments sont ordoDés soit en utilisant l'ordre naturel (cf
 * Comparable) soit avec un Comparator fourni à la création.
 * </p>
 *
 * <p>
 * Certaines méthodes de AbstractCollection doivent être surchargées pour plus
 * d'efficacité.
 * </p>
 *
 * @param <E>
 *			le type des clés stockées dans l'arbre
 */
public class ABR<E> extends AbstractCollection<E> {
	private Noeud racine;
	private Noeud sentinelle;
	private Comparator<? super E> cmp;

	private class Noeud {
		E cle;
		Noeud gauche;
		Noeud droit;
		Noeud pere;
		char couleur;

		Noeud(E cle) {
			this.cle = cle;
			this.droit = null;
			this.gauche = null;
			this.pere = null;
			couleur = 'R';
		}

		/**
		 * Renvoie le noeud contenant la clé minimale du sous-arbre enraciné
		 * dans ce noeud
		 *
		 * @return le noeud contenant la clé minimale du sous-arbre enraciné
		 *		 dans ce noeud
		 */
		Noeud minimum() {
			Noeud x = this;
			while (x.gauche != sentinelle) {
				x = x.gauche;
			}
			return x;
		}

		/**
		 * Renvoie le successeur de ce noeud
		 *
		 * @return le noeud contenant la clé qui suit la clé de ce noeud dans
		 *		 l'ordre des clés, null si c'es le noeud contenant la plus
		 *		 grande clé
		 */
		Noeud suivant() {
			Noeud x = this;
			if(x.droit != null) {
				return x.droit.minimum();
			}
			Noeud y = x.pere;
			while(y != null && x == y.droit) {
				x = y;
				y = y.pere;
			}
			return y;
		}



		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Noeud noeud = (Noeud) o;
			return cle.equals(noeud.cle);
		}

		@Override
		public int hashCode() {
			return Objects.hash(cle);
		}
	}

	// Consructeurs

	/**
	 * Crée un arbre vide. Les éléments sont ordoDés selon l'ordre naturel
	 */
	public ABR() {
		cmp = (e1, e2) -> ((Comparable<E>)e1).compareTo(e2);
		sentinelle =  sentinelle();
		racine = sentinelle;
	}

	/**
	 * Crée un arbre vide. Les éléments sont comparés selon l'ordre imposé par
	 * le comparateur
	 *
	 * @param cmp
	 *			le comparateur utilisé pour définir l'ordre des éléments
	 */
	public ABR(Comparator<? super E> cmp) {
		sentinelle =  sentinelle();
		racine = null;
		this.cmp = cmp;
	}

	/**
	 * Constructeur par recopie. Crée un arbre qui contient les mêmes éléments
	 * que c. L'ordre des éléments est l'ordre naturel.
	 *
	 * @param c
	 *			la collection à copier
	 */
	public ABR(Collection<? extends E> c) {
		this();
		Iterator it = c.iterator();
		while(it.hasNext()) {
			E t = (E)it.next();
			this.add(t);
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new ABRIterator();
	}

	@Override
	public int size() {return size(racine);}

	private int size(Noeud r) {
		if(r == sentinelle) {
			return 0;
		}
		return size(r.gauche) + 1 + size(r.droit);
	}

	public int diametre() {
		return diametre(racine); 
	}

	private int diametre(Noeud r) {
		if(r == sentinelle || r == null) {
			return 0;
		}
		int hauteurgauche = hauteur(r.gauche);
		int hauteurdroite = hauteur(r.droit);

		int diametregauche = diametre(r.gauche);
		int diametredroit = diametre(r.droit);
		return Math.max(hauteurgauche + hauteurdroite + 1, Math.max(diametregauche, diametredroit));
	}

	public int hauteur() {
		return hauteur(racine);
	}

	private int hauteur(Noeud r) {
		if(r == sentinelle || r == null) {
			return 0;
		}
		return (1 + Math.max(hauteur(r.gauche), hauteur(r.droit)));
	}

	public int profondeur() {
		return profondeur(racine); 
	}

	private int profondeur(Noeud r) {
		if(r == sentinelle || r == null) {
			return 0;
		}
		int profondeurgauche = profondeur(r.gauche);
		int profondeurdroit = profondeur(r.droit);

		if(profondeurgauche > profondeurdroit) {
			return (profondeurgauche + 1);
		} else {
			return (profondeurdroit + 1);
		}
	}

	@Override
	public boolean remove(Object o)
	{
		Noeud t = this.rechercher((E)o);
		System.out.println("Supprimer : "+(E)o);
		if(t != sentinelle) {
			return this.supprimer( this.rechercher((E)o)) != sentinelle;
		} else {
			return false;
		}
		
	}


	// Quelques méthodes utiles

	/**
	 * Recherche une clé. Cette méthode peut être utilisée par
	 * {@link #contains(Object)} et {@link #remove(Object)}
	 *
	 * @param o
	 *			la clé à chercher
	 * @return le noeud qui contient la clé ou null si la clé n'est pas trouvée.
	 */
	private Noeud rechercher(Object o) {
		Noeud x = racine;

		while (x != sentinelle && cmp.compare(x.cle, (E)o) != 0 ) {
			if (cmp.compare(x.cle, (E)o) > 0) {
				x = x.gauche;
			} else {
				x = x.droit;
			}
		}
		return x;
	}

	/**
	 * Supprime le noeud z. Cette méthode peut être utilisée dans
	 * {@link #remove(Object)} et {@link Iterator#remove()}
	 *
	 * @param z
	 *			le noeud à supprimer
	 * @return le noeud contenant la clé qui suit celle de z dans l'ordre des
	 *		 clés. Cette valeur de retour peut être utile dans
	 *		 {@link Iterator#remove()}
	 */
	private Noeud supprimer(Noeud z) {
		Noeud x, y = z;
		char yOriginal = y.couleur;

		if(z.gauche == sentinelle) {
			x = z.droit;
			echange(z, z.gauche);
		} else if(z.droit == sentinelle) {
			x = z.gauche;
			echange(z, z.gauche);
		} else {
			y = z.suivant();
			yOriginal = y.couleur;
			x = y.droit;

			if(y.pere == z) {
				x.pere = y;
			} else {
				echange(y, y.droit);
				y.droit = z.droit;
				y.droit.pere = y;
			}
			echange(z, y);
			y.gauche = z.gauche;
			y.gauche.pere = y;
			y.couleur = z.couleur;
		}
		if(yOriginal != 'R') {
			supprimerfix(x);
		}
		return z.suivant();
	}

	private void echange(Noeud x, Noeud y) {
		if(x.pere == sentinelle) {
			racine = y;
		} else if (x == x.pere.gauche) {
			x.pere.gauche = y;
		} else {
			x.pere.droit = y;
		}
		y.pere = x.pere;
	}

	private void supprimerfix(Noeud x) {
		Noeud w;

		while(x != racine && x.couleur != 'R') {
			if(x == x.pere.gauche) {
				w = x.pere.droit;
				if(w.couleur == 'R') {
					w.couleur = 'N';
					x.pere.couleur = 'R';
					rotationDroite(x.pere);
					w = x.pere.droit;
				}
				if(w.gauche.couleur == 'N' && w.droit.couleur =='N') {
					w.couleur = 'R';
					x = x.pere;
				} else {
					if(w.droit.couleur != 'R') {
						w.gauche.couleur = 'N';
						w.couleur = 'R';
						rotationDroite(w);
						w = x.pere.droit;
					}
					w.couleur = x.pere.couleur;
					x.pere.couleur = 'N';
					w.droit.couleur = 'N';
					rotationGauche(x.pere);
					x = racine;
				}
			} else {
				w = x.pere.gauche;
				if(w.couleur == 'R') {
					w.couleur = 'N';
					x.pere.couleur = 'R';
					rotationDroite(x.pere);
					w = x.pere.gauche;
				}
				if(w.gauche.couleur != 'R' && w.droit.couleur != 'R') {
					w.couleur = 'R';
					x = x.pere;
				} else {
					if(w.gauche.couleur != 'R') {
						w.droit.couleur = 'N';
						w.couleur = 'R';
						rotationGauche(w);
						w = x.pere.gauche;
					}
					w.couleur = x.pere.couleur;
					x.pere.couleur = 'N';
					w.gauche.couleur = 'N';
					rotationDroite(x.pere);
					x = racine;
				}
			}
		}
		x.couleur = 'N';
	}
	/*private Noeud supprimer(Noeud z) {
		if(z == sentinelle) {
			return sentinelle;
		}
		if(z == racine) {
			racine = sentinelle;
			return sentinelle;
		}
		if(z.couleur == 'R' || z.gauche.couleur == 'R' || z.droit.couleur == 'R') {
			Noeud e = z.gauche != sentinelle ? z.gauche : z.droit;

			if(z == z.pere.gauche) {
				z.pere.gauche = e;
				if(e != sentinelle) {
					e.pere = z.pere;
				}
				e.couleur = 'N';
				//delete(z);
			} else {
				z.pere.droit = e;
				if(e != sentinelle) {
					e.pere = z.pere;
				}
				e.couleur = 'N';
				//delete(z);
			}
		} else {
			Noeud s = sentinelle;
			Noeud pere = sentinelle;
			Noeud ptr = z;
			ptr.couleur = 'D';		// D pour Double Noir
			while(ptr != racine && ptr.couleur == 'D') {
				pere = pere.gauche;
				if(ptr == pere.gauche) {
					s = pere.droit;
					if(s.couleur == 'R') {
						s.couleur = 'N';
						pere.couleur = 'R';
						rotationGauche(pere);
					} else {
						if(s.gauche.couleur == 'N' && s.droit.couleur == 'N') {
							s.couleur = 'R';
							if(pere.couleur == 'R') {
								pere.couleur = 'N';
							} else {
								pere.couleur = 'D';
							}
							ptr = pere;
						} else {
							if(s.droit.couleur == 'N') {
								s.gauche.couleur = 'N';
								s.couleur = 'R';
								rotationDroite(s);
								s = pere.droit;
							}
							s.couleur = pere.couleur;
							pere.couleur = 'N';
							s.droit.couleur = 'N';
							rotationGauche(pere);
							break;
						}
					}
				} else {
					s = pere.gauche;
					if(s.couleur == 'R') {
						s.couleur = 'N';
						pere.couleur = 'R';
						rotationDroite(pere);
					} else {
						if(s.gauche.couleur == 'N' && s.droit.couleur == 'N') {
							s.couleur = 'R';
							if(pere.couleur == 'R') {
								pere.couleur = 'N';
							} else {
								pere.couleur = 'D';
							}
							ptr = pere;
						} else {
							if(s.gauche.couleur == 'N') {
								s.droit.couleur = 'N';
								s.couleur = 'R';
								s = pere.gauche;
							}
							s.couleur = pere.couleur;
							pere.couleur = 'N';
							s.gauche.couleur = 'N';
							rotationDroite(pere);
							break;
						}
					}
				}
			}
			if (z == z.pere.gauche) {
				z.pere.gauche = sentinelle;
			} else {
				z.pere.droit = sentinelle;
			}
			//delete(z);
			racine.couleur = 'N';
		}
		return z.suivant();
	}*/

	public Noeud sentinelle(){
		Noeud s = new Noeud(null);
		s.pere = s.gauche = s.droit = s;
		s.couleur = 'N';
		return s;
	}

	@Override
	public boolean add(E e)
	{
		if (e == null) return false;
		Noeud t = this.rechercher(e);
		if(t.cle == e) {
			System.out.println("Noeud a ajouter déja dans l'arbre, annulation de l'ajout");
			return false;
		}
		Noeud z = new Noeud(e);
		Noeud y = sentinelle;
		Noeud x = racine;


		while (x != sentinelle)
		{
			y = x;
			x = this.cmp.compare(z.cle, x.cle) < 0 ? x.gauche : x.droit;
		}

		z.pere = y;

		if( y == sentinelle )
		{
			z.couleur = 'N';
			racine = z;
		}
		else
		{
			if( this.cmp.compare(z.cle, y.cle) < 0) y.gauche = z;
			else									y.droit  = z;
		}

		z.gauche = z.droit = sentinelle;
		z.couleur = 'R';
		ajouterCorrection(z);
		return true;
	}


	private void ajouterCorrection(Noeud z) {
		Noeud y;
		while (z.pere.couleur == 'R') {
			// (*) La seule propriété RN violée est (4) : z et z.pere sont rouges
			if (z.pere == z.pere.pere.gauche) {
				y = z.pere.pere.droit; // l'oncle de z
				if (y.couleur == 'R') {
					// cas 1
					z.pere.couleur = 'N';
					y.couleur = 'N';
					z.pere.pere.couleur = 'R';
					z = z.pere.pere;
				} else {
					if (z == z.pere.droit) {
						// cas 2
						z = z.pere;
						rotationGauche(z);
					}
					// cas 3
					z.pere.couleur = 'N';
					z.pere.pere.couleur = 'R';
					rotationDroite(z.pere.pere);
				}
			} else {
				y = z.pere.pere.gauche; // l'oncle de z
				if (y.couleur == 'R') {
					// cas 1
					z.pere.couleur = 'N';
					y.couleur = 'N';
					z.pere.pere.couleur = 'R';
					z = z.pere.pere;
				} else {
					if (z == z.pere.gauche) {
						// cas 2
						z = z.pere;
						rotationDroite(z);
					}
					// cas 3
					z.pere.couleur = 'N';
					z.pere.pere.couleur = 'R';
					rotationGauche(z.pere.pere);
				}

				// idem en miroir, gauche <-> droite
				// cas 1', 2', 3'
			}
		}
		// (**) La seule propriété (potentiellement) violée est (2)
		racine.couleur = 'N';
	}

	private void rotationGauche(Noeud x) {
		Noeud y = x.droit;
		x.droit = y.gauche;
		if(y.gauche != sentinelle){
			y.gauche.pere = x;
		}
		y.pere = x.pere;

		if(x.pere == sentinelle){
			this.racine = y;
		}
		else if(x == x.pere.gauche){
			x.pere.gauche = y;
		}
		else {
			x.pere.droit = y;
		}
		y.gauche = x;
		x.pere = y;
	}

	private  void rotationDroite(Noeud x){
		Noeud y = x.gauche;
		x.gauche = y.droit;
		if(y.gauche != sentinelle){
			y.gauche.pere = x;
		}
		y.pere = x.pere;
		if (x.pere == sentinelle){
			this.racine = y;
		}
		else if (x == x.pere.droit){
			x.pere.droit = y;
		}
		else {
			// avant : else x.pere.droit = y;
			x.pere.gauche = y;
		}
		y.droit = x;
		x.pere = y;
	}

	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		for( E e : c )
			if( !this.add(e) )
				return false;

		return true;
	}

	/**
	 * Les itérateurs doivent parcourir les éléments dans l'ordre ! Ceci peut se
	 * faire facilement en utilisant {@link Noeud#minimum()} et
	 * {@link Noeud#suivant()}
	 */
	private class ABRIterator implements Iterator<E> {
		Noeud courant;

		ABRIterator(){
			Noeud courant = racine;
		}

		public boolean hasNext() {
			return courant != null;
		}

		public E next() {
			E tmp;
			if (courant == null){
				return null;
			}
			tmp = courant.cle;
			this.courant.suivant();

			return tmp;
		}

		public void remove() {
			this.courant = ABR.this.supprimer(courant);
		}
	}

	// Pour un "joli" affichage

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		toString(racine, buf, "", maxStrLen(racine));
		return buf.toString();
	}

	private void toString(Noeud x, StringBuffer buf, String path, int len) {
		if (x == sentinelle)
			return;
		toString(x.droit, buf, path + "D", len);
		for (int i = 0; i < path.length(); i++) {
			for (int j = 0; j < len + 6; j++)
				buf.append(' ');
			char c = ' ';
			if (i == path.length() - 1)
				c = '+';
			else if (path.charAt(i) != path.charAt(i + 1))
				c = '|';
			buf.append(c);
		}
		if (x.couleur == 'R') {
			buf.append("-- " + "\u001B[31m" + x.cle.toString() + "\u001B[0m");
		} else {
			buf.append("-- " + x.cle.toString());
		}
		if (x.gauche != sentinelle || x.droit != sentinelle) {
			buf.append(" --");
			for (int j = x.cle.toString().length(); j < len; j++)
				buf.append('-');
			buf.append('|');
		}
		buf.append("\n");
		toString(x.gauche, buf, path + "G", len);
	}

	private int maxStrLen(Noeud x) {
		return x == sentinelle ? 0 : Math.max(x.cle.toString().length(),
				Math.max(maxStrLen(x.gauche), maxStrLen(x.droit)));
	}

	// TODO : voir quelles autres méthodes il faut surcharger
}