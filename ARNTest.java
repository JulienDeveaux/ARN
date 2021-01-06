import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
//Code réalisé en binôme : Deveaux Julien et Lemesle Justine.


public class ARNTest {

	public static void main(String[] args) {
		System.out.println("Ajout et suppresion");AjoutSuppressionContains();
		System.out.println("-------------------------------------------------------");
		System.out.println("Création d'un arbre grâce à une liste");ArbreParListe();
		System.out.println("-------------------------------------------------------");
		System.out.println("Test de l'itérateur");IteratorTest();
		System.out.println("-------------------------------------------------------");
		System.out.println("Test avec des valeurs négatives");negatif();
		System.out.println("-------------------------------------------------------");
		System.out.println("test addAll");testAddAll();
		System.out.println("-------------------------------------------------------");
		System.out.println("TEST 5 : Arbre équilibré");testCR();
		System.out.println("-------------------------------------------------------");
		System.out.println("Génération et suppresion de nombres aléatoires");TestAlea();
		System.out.println("-------------------------------------------------------");

	}


	static void AjoutSuppressionContains(){
		ARN<Integer> arn = new ARN<>();

		arn.add(34);
		arn.add(56);
		arn.add(76);
		arn.add(22);
		arn.add(0);
		arn.add(80);
		arn.add(79);
		arn.add(135);
		// Ajout de deux fois la même clé
		arn.add(56);
		System.out.println(arn);
		System.out.println("Suppression du noeud 80 : ");
		arn.remove(80);
		System.out.println(arn);
		System.out.println("Suppression de la feuille 135 : ");
		arn.remove(135);
		System.out.println(arn);
		System.out.println("Suppression de la clé inexistante 3 : ");
		arn.remove(3);
		System.out.println(arn);
		if(arn.contains(79)) {
			System.out.println("Noeud 79 trouvé");
		} else {
			System.out.println("Noeud 79 non trouvé");
		}
		if(arn.contains(56)) {
			System.out.println("Racine 56 trouvé");
		} else {
			System.out.println("Racine 56 non trouvé");
		}
		if(arn.contains((3))) {
			System.out.println("Noeud 3 (non présent) trouvé");
		} else {
			System.out.println(("Noeud 3 (non présent) non trouvé"));
		}
	}
	static void ArbreParListe() {
		ArrayList<Integer> liste = new ArrayList<>();
		liste.add(5);
		liste.add(8);
		liste.add(2);
		liste.add(9);
		liste.add(-2);
		liste.add(-3);
		liste.add(0);
		liste.add(1);
		liste.add(2000);
		liste.add(-2000);
		liste.add(500);
		liste.add(501);
		ARN<Integer> arn = new ARN<>(liste);
		System.out.println("taille : " + arn.size());
		System.out.println("hauteur : " + arn.hauteur());
		System.out.println(arn);
	}

	static void IteratorTest() {
		ARN<Integer> arn = new ARN<>();
		arn.add(1);
		arn.add(2);
		arn.add(3);
		arn.add(4);
		arn.add(5);
		arn.add(6);
		arn.add(7);
		arn.add(8);
		arn.add(9);
		arn.add(10);
		System.out.println(arn);

		Iterator<Integer> it = arn.iterator();
		int cle;

		if(it.hasNext()) {
			cle = it.next();
			System.out.println("Suppression de la première valeur de l'itérateur : " + cle);
			it.remove();
		}
		while (it.hasNext()) {
			cle = it.next();
			System.out.println("Clé renvoyée par l'itérateur : " + cle);
		}
		System.out.println(arn);
		System.out.println("taille (nb de noeuds) : " + arn.size());
		System.out.println("hauteur max de l'arbre : " + arn.hauteur());
}

	static void negatif() {
		ARN<Integer> arn = new ARN<>();
		arn.add(0);
		arn.add(1);
		arn.add(-1);
		arn.add(2);
		arn.add(-2);
		arn.add(3);
		arn.add(-3);
		arn.add(4);
		arn.add(-4);
		System.out.println(arn);
		System.out.println("taille (nb de noeuds) : " + arn.size());
		System.out.println("hauteur max de l'arbre : " + arn.hauteur());
		System.out.println("Suppression de la clé -1");
		arn.remove(-1);
		System.out.println(arn);
		System.out.println("Suppression de la clé -4");
		arn.remove(-4);
		System.out.println(arn);
		System.out.println("taille (nb de noeuds) : " + arn.size());
		System.out.println("hauteur max de l'arbre : " + arn.hauteur());
	}

	static void testAddAll() {
		ARN<Integer> arn = new ARN<>();
		arn.add(1);
		arn.add(2);
		System.out.println("Arbre initial :");
		System.out.println(arn);
		System.out.println("Création d'une liste avec 3, 4, 5, 6, -1 à l'intérieur puis ajout de cette liste dans notre arbre :");
		ArrayList<Integer> liste = new ArrayList<>();
		liste.add(3);
		liste.add(4);
		liste.add(5);
		liste.add(6);
		liste.add(-1);
		arn.addAll(liste);
		System.out.println("Arbre résultant :");
		System.out.println(arn);
	}

	static void testCR(){
		ARN<Integer> arn = new ARN<>();
		arn.add(15);
		arn.add(25);
		arn.add(5);
		arn.add(10);
		arn.add(20);
		arn.add(2);
		arn.add(27);
		System.out.println(arn);
	}

	static void TestAlea() {
		ARN<Integer> arn = new ARN<>();
		Random r = new Random();
		for(int i = 0; i < 10; i++) {
			arn.add(r.nextInt(50));
		}
		System.out.println("Suppression");
		System.out.println(arn);
		for(int i = 0; i < 10; i++) {
			arn.remove(r.nextInt(50));
		}
		System.out.println(arn);
		System.out.println("taille (nb de noeuds) : " + arn.size());
		System.out.println("hauteur max de l'arbre : " + arn.hauteur());
	}
}
