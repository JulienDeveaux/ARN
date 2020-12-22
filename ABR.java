import java.util.*;

/**
 * <p>
 * Implantation de l'interface Collection basée sur les arbres binaires de
 * recherche. Les éléments sont ordonnés soit en utilisant l'ordre naturel (cf
 * Comparable) soit avec un Comparator fourni à la création.
 * </p>
 *
 * <p>
 * Certaines méthodes de AbstractCollection doivent être surchargées pour plus
 * d'efficacité.
 * </p>
 *
 * @param <E>
 *            le type des clés stockées dans l'arbre
 */
public class ABR<E> extends AbstractCollection<E> {
	private Noeud racine;
	private Noeud sentinelle;
	private int taille;
	private Comparator<? super E> cmp;
	public Noeud noeudNul = new Noeud(null);

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
		 *         dans ce noeud
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
		 *         l'ordre des clés, null si c'es le noeud contenant la plus
		 *         grande clé
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
	 * Crée un arbre vide. Les éléments sont ordonnés selon l'ordre naturel
	 */
	public ABR() {
		taille = 0;
		cmp = (e1, e2) -> ((Comparable<E>)e1).compareTo(e2);
		sentinelle =  sentinelle();
		racine = sentinelle;
	}

	/**
	 * Crée un arbre vide. Les éléments sont comparés selon l'ordre imposé par
	 * le comparateur
	 *
	 * @param cmp
	 *            le comparateur utilisé pour définir l'ordre des éléments
	 */
	public ABR(Comparator<? super E> cmp) {
		taille = 0;
		racine = sentinelle;
		this.cmp = cmp;
	}

	/**
	 * Constructeur par recopie. Crée un arbre qui contient les mêmes éléments
	 * que c. L'ordre des éléments est l'ordre naturel.
	 *
	 * @param c
	 *            la collection à copier
	 */
	public ABR(Collection<? extends E> c) {
		if(c.isEmpty()) {
			taille = 0;
			racine = sentinelle;
			cmp = (e1, e2) -> ((Comparable<E>)e1).compareTo(e2);
		} else {
			for(E e : c) {
				taille ++;
			}
			E t = c.iterator().next();
			Noeud n = new Noeud(t);
			racine = n;
			cmp = (e1, e2) -> ((Comparable<E>)e1).compareTo(e2);
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new ABRIterator();
	}

	@Override
	public int size() {
		return taille;
	}

	@Override
	public boolean remove(Object o)
	{
		return this.supprimer( this.rechercher((E)o)) != null;
	}


	// Quelques méthodes utiles

	/**
	 * Recherche une clé. Cette méthode peut être utilisée par
	 * {@link #contains(Object)} et {@link #remove(Object)}
	 *
	 * @param o
	 *            la clé à chercher
	 * @return le noeud qui contient la clé ou null si la clé n'est pas trouvée.
	 */
	public Noeud rechercher(Object o) {
		Noeud x = racine;

		while (x != null && x.cle != (E)o) {
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
	 *            le noeud à supprimer
	 * @return le noeud contenant la clé qui suit celle de z dans l'ordre des
	 *         clés. Cette valeur de retour peut être utile dans
	 *         {@link Iterator#remove()}
	 */
	private Noeud supprimer(Noeud z) {
		Noeud y;
		Noeud x;
		if (z.gauche == null || z.droit == null){
			y = z;
		}
		else{
			y = z.suivant();
		}
		// y est le nœud à détacher

		if(y == null){
			return null;
		}

		if (y.gauche != null) {
			x = y.gauche;
		}
		else {
			x = y.droit;
		}
		// x est le fils unique de y ou null si y n'a pas de fils

		if (x != null) {
			x.pere = y.pere;
		}

		if (y.pere == null) { // suppression de la racine
			this.racine = x;
		} else {
			if (y.equals(y.pere.gauche)) {
				y.pere.gauche = x;
			}
			else {
				y.pere.droit = x;
			}
		}

		if (!y.equals(z)) {
			z.cle = y.cle;
		}
		this.taille--;
		return z.suivant();
	}

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
			else                                    y.droit  = z;
		}

		z.gauche = z.droit = sentinelle;
		z.couleur = 'R';
		System.out.println("-------------------------------------");
		System.out.println("Ajout de la clé : " + e);
		System.out.println(this);
		ajouterCorrection(z);
		taille++;
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