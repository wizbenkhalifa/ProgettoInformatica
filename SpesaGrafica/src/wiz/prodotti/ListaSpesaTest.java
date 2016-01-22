package wiz.prodotti;

public class ListaSpesaTest {
	
	public static void stampa(ListaSpesa ls){
		for(int i=0; i<ls.getNumProdotti(); i++){
			System.out.println(ls.getLista()[i].getCodice() + " - " + ls.getLista()[i].getDescrizione() + " - " + ls.getLista()[i].getPrezzo());
		}
	}

	public static void main(String[] args) {
		ListaSpesa ls = new ListaSpesa(true,100);
		NonAlimentare p = new NonAlimentare("01000010", "prodotto", 10, "carta");
		NonAlimentare p1 = new NonAlimentare("01101010", "prodotto", 15, "vetro");
		NonAlimentare p2 = new NonAlimentare("01111010", "prodotto", 20, "plastica");
		NonAlimentare p3 = new NonAlimentare("010010110", "prodotto", 12, "vetro");
		NonAlimentare p4 = new NonAlimentare("01111111010", "prodotto", 13, "carta");
		try {
			ls.aggiungiProdotto(p);
			ls.aggiungiProdotto(p1);
			ls.aggiungiProdotto(p2);
			ls.aggiungiProdotto(p3);
			ls.aggiungiProdotto(p4);
			System.out.println("Totale" + ls.calcolaSpesa());
			ls.eliminaProdotto(2);
		} catch (MyOwnException e) {
			e.printStackTrace();
		}
		stampa(ls);
	}

}
