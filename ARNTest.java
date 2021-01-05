import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;



public class ARNTest {

	public static void main(String[] args) {
		System.out.println("TEST RANDOM");testRandom();
		System.out.println("TEST 1 : Itérateur");test1();
		System.out.println("TEST 2 : Valeur positif en double");test2();
		System.out.println("TEST 3 : Valeur positif");test3();
		System.out.println("TEST 4 : Positif et négatif");test4();
		System.out.println("TEST 5 : Arbre équilibré");test5();
	}

	static void test1() {

		ArrayList<Integer> l = new ArrayList<>();
		l.add(5);
		l.add(8);
		l.add(2);
		l.add(9);
		l.add(-2);
		l.add(-3);
		l.add(0);
		l.add(1);
		l.add(2000);
		l.add(-2000);
		l.add(500);
		l.add(501);
		ARN<Integer> arn = new ARN<>(l);
		System.out.println("taille : " + arn.size());
		System.out.println("hauteur : " + arn.hauteur());
		System.out.println(arn);

		Iterator<Integer> it = arn.iterator();

		while (it.hasNext()) {
			int cle = it.next();
			if (cle % 2 == 0) {
				it.remove();
				System.out.println("taille : " + arn.size());
				System.out.println(arn);
			}
		}
		System.out.println("taille (nb de noeuds) : " + arn.size());
		System.out.println("hauteur max de l'arbre : " + arn.hauteur());
}

	static void test2() {
		ARN<Integer> arn = new ARN<>();
		arn.add(13);
		arn.add(8);
		arn.add(17);
		arn.add(1);
		arn.add(11);
		arn.add(15);
		arn.add(25);
		arn.add(6);
		arn.add(22);
		arn.add(27);
		arn.add(27);
		arn.add(11);
		System.out.println(arn);
		arn.remove(17);
		System.out.println(arn);
		arn.remove(22);
		System.out.println(arn);
		arn.remove(6);
		System.out.println(arn);
		System.out.println("taille (nb de noeuds) : " + arn.size());
		System.out.println("hauteur max de l'arbre : " + arn.hauteur());
	}

	static void test3() {
		ARN<Integer> arn = new ARN<>();
		arn.add(13);
		arn.add(8);
		arn.add(17);
		arn.add(1);
		arn.add(11);
		arn.add(15);
		arn.add(25);
		arn.add(6);
		arn.add(22);
		arn.add(27);
		System.out.println(arn);
		System.out.println("taille (nb de noeuds) : " + arn.size());
		System.out.println("hauteur max de l'arbre : " + arn.hauteur());
		arn.remove(8);
		System.out.println(arn);
		System.out.println("taille (nb de noeuds) : " + arn.size());
		System.out.println("hauteur max de l'arbre : " + arn.hauteur());
	}

	static void test4() {
		ARN<Integer> arn = new ARN<>();
		arn.add(0);
		System.out.println(arn);
		arn.add(-1);
		System.out.println(arn);
		arn.add(-2);
		System.out.println(arn);
		arn.add(-3);
		System.out.println(arn);
		arn.add(1);
		System.out.println(arn);
		arn.add(2);
		System.out.println(arn);
		arn.add(3);
		System.out.println(arn);
		arn.remove(-1);       //enlève tout
		System.out.println(arn);
		System.out.println("taille (nb de noeuds) : " + arn.size());
		System.out.println("hauteur max de l'arbre : " + arn.hauteur());
	}

	static void testRandom() {
		ARN<Integer> arn = new ARN<>();
		Random r = new Random();
		for(int i = 0; i < 10; i++) {
			arn.add(r.nextInt(200));
		}
		System.out.println(arn);
		for(int i = 0; i < 10; i++) {
			arn.remove(r.nextInt(200));
		}
		System.out.println(arn);
		System.out.println("taille (nb de noeuds) : " + arn.size());
		System.out.println("hauteur max de l'arbre : " + arn.hauteur());
	}

	static void test5(){
		ARN<Integer> arn = new ARN<>();
		arn.add(15);
		System.out.println(arn);
		arn.add(25);
		System.out.println(arn);
		arn.add(5);
		System.out.println(arn);
		arn.add(10);
		System.out.println(arn);
		arn.add(20);
		System.out.println(arn);
		arn.add(2);
		System.out.println(arn);
		arn.add(27);
		System.out.println(arn);

	}
}
