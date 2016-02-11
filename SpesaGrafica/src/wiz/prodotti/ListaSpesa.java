package wiz.prodotti;

public class ListaSpesa {
	private Prodotto [] lista;
	private int numProdotti;
	private int max;
	private boolean tessera;
	
	public ListaSpesa(boolean tessera, int max){
		this.max = max;
		this.numProdotti = 0;
		lista = new Prodotto[max];
		this.tessera = tessera;
	}
	
	public ListaSpesa(boolean tessera, int max, Prodotto [] p){
		this.max = max;
		this.numProdotti = 0;
		lista = new Prodotto[max];
		this.tessera = tessera;
		for(int i=0; i<5; i++){
			try {
				this.aggiungiProdotto(p[i]);
			} catch (MyOwnException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
	}

	public Prodotto[] getLista() {
		return lista;
	}
	public void setLista(Prodotto[] lista) {
		this.lista = lista;
	}
	public int getNumProdotti() {
		return numProdotti;
	}
	public void setNumProdotti(int numProdotti) {
		this.numProdotti = numProdotti;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public boolean isTessera() {
		return tessera;
	}
	public void setTessera(boolean tessera) {
		this.tessera = tessera;
	}
	
	public void aggiungiProdotto(Prodotto p) throws MyOwnException{
		if(numProdotti<=max){
			if(tessera){
				p.applicaSconto();
				lista[numProdotti++] = p;
			}else{
				lista[numProdotti++] = p;
			}
		}
	}
	
	public double calcolaSpesa(){
		double tot = 0;
		for(int i=0; i<numProdotti; i++){
			tot += lista[i].getPrezzo();
		}
		return tot;
	}
	
	public void eliminaProdotto(int p){
		for(int i=p; i<numProdotti; i++){
			lista[i]=lista[i+1];
		}
		numProdotti--;
	}
}
