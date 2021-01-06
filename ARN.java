import java.util.*;
//Code réalisé en binôme : Deveaux Julien et Lemesle Justine.
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
public class ARN<E> extends AbstractCollection<E> {
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


		Noeud minimum(Noeud x) {
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
	public ARN() {
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
	public ARN(Comparator<? super E> cmp) {
		sentinelle =  sentinelle();
		racine = sentinelle;
		this.cmp = cmp;
	}

	/**
	 * Constructeur par recopie. Crée un arbre qui contient les mêmes éléments
	 * que c. L'ordre des éléments est l'ordre naturel.
	 *
	 * @param c
	 *			la collection à copier
	 */
	public ARN(Collection<? extends E> c) {
		this();
		Iterator it = c.iterator();
		while(it.hasNext()) {
			E t = (E)it.next();
			this.add(t);
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new ARNIterator();
	}

	@Override
	public int size() {return size(racine);}

	private int size(Noeud r) {
		if(r == sentinelle) {
			return 0;
		}
		return size(r.gauche) + 1 + size(r.droit);
	}

	public int hauteur() {
		return hauteur(racine);
	}

	private int hauteur(Noeud r) {
		if(r == sentinelle || r == null) {
			return 0;
		}
		int hauteurgauche = hauteur(r.gauche);
		int hauteurdroit = hauteur(r.droit);

		if(hauteurgauche > hauteurdroit) {
			return (hauteurgauche + 1);
		} else {
			return (hauteurdroit + 1);
		}
	}

	@Override
	public boolean contains(Object o) {
		if(this.rechercher(o) == sentinelle) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean remove(Object o)
	{
		Noeud t = this.rechercher((E)o);
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
		System.out.println("Clé à supprimer : "+z.cle);
		Noeud y = z;
		Noeud x;
		char yOriginal = y.couleur;

		if(z.gauche == sentinelle) {
			x = z.droit;
			this.echange(z, z.droit);
		} else if(z.droit == sentinelle) {
			x = z.gauche;
			this.echange(z, z.gauche);
		} else {
			y = z.minimum(z.droit);
			yOriginal = y.couleur;
			x = y.droit;

			if(y.pere == z) {
				x.pere = y;
			} else {
				this.echange(y, y.droit);
				y.droit = z.droit;
				y.droit.pere = y;
			}
			this.echange(z, y);
			y.gauche = z.gauche;
			y.gauche.pere = y;
			y.couleur = z.couleur;
		}
		if(yOriginal == 'N') {
			this.supprimerfix(x);
		}

		return z.suivant();
	}

	private void echange(Noeud x, Noeud y) {
		if(x.pere == sentinelle) {
			this.racine = y;
		} else if (x == x.pere.gauche) {
			x.pere.gauche = y;
		} else {
			x.pere.droit = y;
		}
		y.pere = x.pere;
	}

	private void supprimerfix(Noeud x) {
		Noeud w;

		while(x != racine && x.couleur == 'N') {
			if(x == x.pere.gauche) {
				w = x.pere.droit;
				if(w.couleur == 'R') {
					w.couleur = 'N';
					x.pere.couleur = 'R';
					this.rotationGauche(x.pere);
					w = x.pere.droit;
				}
				if(w.gauche.couleur == 'N' && w.droit.couleur =='N') {
					w.couleur = 'R';
					x.pere.couleur = 'N';
					x = x.pere;
				} else {
					if(w.droit.couleur == 'N') {
						w.gauche.couleur = 'N';
						w.couleur = 'R';
						this.rotationDroite(w);
						w = x.pere.droit;
					}
					w.couleur = x.pere.couleur;
					x.pere.couleur = 'N';
					w.droit.couleur = 'N';
					this.rotationGauche(x.pere);
					x = racine;
				}
			} else {
				w = x.pere.gauche;
				if(w.couleur == 'R') {
					w.couleur = 'N';
					x.pere.couleur = 'N';
					this.rotationDroite(x.pere);
					w = x.pere.gauche;
				}
				if(w.gauche.couleur == 'N' && w.droit.couleur == 'N') {
					w.couleur = 'R';
					x.pere.couleur = 'N';
					x = x.pere;
				} else {
					if(w.gauche.couleur == 'N') {
						w.droit.couleur = 'N';
						w.couleur = 'R';
						this.rotationGauche(w);
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

	private Noeud sentinelle(){
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
			}
		}
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
		Iterator it = c.iterator();
		while(it.hasNext()) {
			E t = (E)it.next();
			this.add(t);
		}
		return true;
	}

	/**
	 * Les itérateurs doivent parcourir les éléments dans l'ordre ! Ceci peut se
	 * faire facilement en utilisant {@link Noeud#minimum()} et
	 * {@link Noeud#suivant()}
	 */
	private class ARNIterator implements Iterator<E> {
		Noeud courant;
		Noeud suivant;

		public ARNIterator(){
			super();
			this.courant = ARN.this.sentinelle;
			this.suivant = ARN.this.racine.minimum();
		}

		public boolean hasNext() {
			return suivant != ARN.this.sentinelle;
		}

		public E next() {
			this.courant = this.suivant;
			this.suivant = this.suivant.suivant();
			return this.courant.cle;
		}

		public void remove() {
			this.suivant = ARN.this.supprimer(this.courant);
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