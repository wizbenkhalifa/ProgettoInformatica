package wiz.prodotti;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;


import org.eclipse.swt.widgets.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.widgets.DateTime;

public class spesaGrafica {
	protected ListaSpesa carrello = new ListaSpesa(true, 20);
	protected Shell shell;
	private Prodotto [] p = new Prodotto[5];
	protected ListaSpesa prodotti = new ListaSpesa(true, 20);
	private ListaSpesa temp;
	private Text text;
	private Text text_1;
	private Text text_2;
	private String fs = new String();
	private File file = new File("scontrino.txt");
	private FileReader fr;
	private Data Data;
	private Text text_3;
	private Boolean on;
	
	public spesaGrafica(){
		p[0]= new NonAlimentare("1111" , "Patata", 10, "Vetro");
		p[1]= new NonAlimentare("1111" , "Alice", 10, "Carta");
		p[2]= new Alimentare("1111" , "Pizza", 10, Data = new Data());
		p[3]= new Alimentare("1111" , "Ciocccolata", 10, Data = new Data());
		p[4]= new NonAlimentare("1111" , "Cavei", 10, "Plastica");
		prodotti = new ListaSpesa(false, 10, p);
		on = false;
	}

	
	public static void main(String[] args) {
		try {
			spesaGrafica window = new spesaGrafica();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	protected void createContents() {
		shell = new Shell();
		shell.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent arg0) {
				if(!on){
					on = true;
					if(MessageDialog.openQuestion(shell, "Tessera", "Possiedi la tessera fedeltà?")){
						carrello.setTessera(true);
					}else{
						carrello.setTessera(false);
					}
				}
			}
		});
		shell.setSize(573, 418);
		shell.setText("SWT Application");
		List list_1 = new List(shell, SWT.BORDER);
		list_1.setBounds(419, 31, 138, 227);
		List list = new List(shell, SWT.BORDER);
		for(int i=0; i<prodotti.getNumProdotti(); i++){
			list.add(prodotti.getLista()[i].getDescrizione());
		}
		list.setBounds(0, 32, 126, 226);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				temp = new ListaSpesa(true, carrello.getMax());
				temp.setNumProdotti(carrello.getNumProdotti()+1);
				for(int i=0; i<carrello.getNumProdotti(); i++){
					temp.getLista()[i] = carrello.getLista()[i];
				}
				System.out.println(temp.getMax() + " " + temp.getNumProdotti());
				temp.getLista()[temp.getNumProdotti()-1] = prodotti.getLista()[list.getSelectionIndex()];
				carrello = new ListaSpesa(true, temp.getMax());
				carrello.setNumProdotti(temp.getNumProdotti());
				for(int i=0; i<temp.getNumProdotti(); i++){
					carrello.getLista()[i] = temp.getLista()[i];

				}
				System.out.println(carrello.getMax() +" "+carrello.getNumProdotti());


				list_1.add(carrello.getLista()[carrello.getNumProdotti()-1].getDescrizione());
				list_1.update();
			}
		});
		btnNewButton.setBounds(274, 50, 75, 25);
		btnNewButton.setText("Prendi");
		
		DateTime dateTime = new DateTime(shell, SWT.BORDER);
		dateTime.setBounds(283, 255, 80, 24);
		
		Button btnAlimentare = new Button(shell, SWT.RADIO);
		btnAlimentare.setBounds(172, 310, 90, 16);
		btnAlimentare.setText("Alimentare");
		
		Button btnNonAlimentare = new Button(shell, SWT.RADIO);
		btnNonAlimentare.setBounds(292, 310, 109, 16);
		btnNonAlimentare.setText("Non Alimentare");
		

		Label lblMateriale = new Label(shell, SWT.NONE);
		lblMateriale.setBounds(182, 276, 55, 15);
		lblMateriale.setText("Materiale");
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(273, 283, 76, 21);
		
		Button btnCaricaProdotto = new Button(shell, SWT.NONE);
		btnCaricaProdotto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnAlimentare.getSelection()){
					Prodotto pa = new Alimentare(text.getText(), text_2.getText(), Float.parseFloat(text_1.getText()), Data = new Data(dateTime.getDay(), dateTime.getMonth(), dateTime.getYear()));
					try {
						prodotti.aggiungiProdotto(pa);
					} catch (MyOwnException e1) {
						e1.printStackTrace();
					}
					list.add(pa.descrizione);
				}else if(btnNonAlimentare.getSelection()){
					Prodotto pa = new NonAlimentare(text.getText(), text_2.getText(), Float.parseFloat(text_1.getText()), text_3.getText());
					try {
						prodotti.aggiungiProdotto(pa);
					} catch (MyOwnException e1) {
						e1.printStackTrace();
					}
					list.add(pa.descrizione);
				}
			}
		});
		btnCaricaProdotto.setBounds(145, 133, 102, 25);
		btnCaricaProdotto.setText("Carica Prodotto");
		
		Button btnEliminaProdotto = new Button(shell, SWT.NONE);
		btnEliminaProdotto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.print(list_1.getSelectionIndex() + "\n");
				carrello.eliminaProdotto(list_1.getSelectionIndex());
				list_1.remove(list_1.getSelectionIndex());
			}
		});
		btnEliminaProdotto.setBounds(274, 133, 108, 25);
		btnEliminaProdotto.setText("Elimina prodotto");
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				list.add(carrello.getLista()[list_1.getSelectionIndex()].getDescrizione());
				try {
					prodotti.aggiungiProdotto(carrello.getLista()[list_1.getSelectionIndex()]);
					carrello.eliminaProdotto(list_1.getSelectionIndex());
				} catch (MyOwnException e1) {
					e1.printStackTrace();
				}
				list_1.remove(list_1.getSelectionIndex());
				System.out.println(carrello.getNumProdotti());
				System.out.println(prodotti.getNumProdotti());
			}
		});
		btnNewButton_1.setBounds(172, 50, 75, 25);
		btnNewButton_1.setText("Riponi");
		
		Button btnCaio = new Button(shell, SWT.NONE);
		btnCaio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(shell);
				fd.setFilterExtensions(new String[]{"*.txt", "*.csv", "*.*"});
				fs = fd.open();
				
				if(fs != null) {
					file = new File(fs);
				}

			}
		});
		btnCaio.setBounds(0, 264, 75, 25);
		btnCaio.setText("Browse File");
		
		Label lblScaffali = new Label(shell, SWT.NONE);
		lblScaffali.setBounds(10, 10, 71, 15);
		lblScaffali.setText("Scaffali");
		
		Label lblCarrello = new Label(shell, SWT.NONE);
		lblCarrello.setBounds(479, 10, 55, 15);
		lblCarrello.setText("Carrello");
		
		Button btnNewButton_2 = new Button(shell, SWT.NONE);
		btnNewButton_2.setBounds(353, 176, 29, 25);
		btnNewButton_2.setText("+");
		
		Button btnNewButton_3 = new Button(shell, SWT.NONE);
		btnNewButton_3.setBounds(353, 224, 29, 25);
		btnNewButton_3.setText("-");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(274, 178, 76, 21);
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(274, 201, 76, 21);
		
		Label lblCodiceProdotto = new Label(shell, SWT.NONE);
		lblCodiceProdotto.setBounds(166, 181, 102, 15);
		lblCodiceProdotto.setText("Codice Prodotto");
		
		Label lblPrezzo = new Label(shell, SWT.NONE);
		lblPrezzo.setText("Prezzo");
		lblPrezzo.setBounds(166, 204, 81, 15);
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(273, 226, 76, 21);
		
		Label lblDescrizione = new Label(shell, SWT.NONE);
		lblDescrizione.setText("Descrizione");
		lblDescrizione.setBounds(166, 225, 102, 15);
		
		Spinner spinner = new Spinner(shell, SWT.BORDER);
		spinner.setBounds(354, 200, 47, 22);
		
		Button btnCaricaScontrino = new Button(shell, SWT.NONE);
		btnCaricaScontrino.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					String [] s1 = new String[4];
					String s = new String();
					fr = new FileReader(file);
					BufferedReader br = new BufferedReader(fr);
					StringBuffer stringBuffer = new StringBuffer();
					list_1.removeAll();
					carrello = new ListaSpesa(true, carrello.getMax());
					while ((s = br.readLine()) != null) {
						stringBuffer.append(s);
						s1 = s.split(" ");
						Prodotto p1;
						if(s1[4].equals("Carta") || s1[4].equals("Vetro") || s1[4].equals("Plastica")){
							p1 = new NonAlimentare(s1[2], s1[1], Float.parseFloat(s1[3]), s1[4]);
						}else{
							System.out.println(Data);
							p1 = new Alimentare(s1[2], s1[1], Float.parseFloat(s1[3]), Data = new Data(Integer.parseInt((s1[4].split("/"))[0]), Integer.parseInt((s1[4].split("/"))[1]), Integer.parseInt((s1[4].split("/"))[2])));
						}
						carrello.aggiungiProdotto(p1);	
						list_1.add(s1[1] + " " + s1[4]);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MyOwnException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnCaricaScontrino.setBounds(132, 96, 115, 25);
		btnCaricaScontrino.setText("Carica Scontrino");
		
		Button btnSalvaScontrino = new Button(shell, SWT.NONE);
		btnSalvaScontrino.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
				    if(file.createNewFile()){
				    	FileWriter f = new FileWriter(file, false);
				    	BufferedWriter fw = new BufferedWriter(f);
				    	int i = 0;
				    	Alimentare a;
				    	NonAlimentare na;
				    	while(i<carrello.getNumProdotti()){				    		
				    		if (carrello.getLista()[i] instanceof Alimentare) {
				    			a = (Alimentare)carrello.getLista()[i];
				    			fw.write(i+1 + " "+a.getDescrizione() + " " + a.getPrezzo() + " " + a.getCodice() + " " + a.getScadenza() + "\r\n");
				    		} else {
				    			na = (NonAlimentare)carrello.getLista()[i];
				    			fw.write(i+1 + " "+na.getDescrizione() + " " + na.getPrezzo() + " " + na.getCodice() + " "+ na.getMateriale() + "\r\n");
				    		}
					    	
					    	i++;
				    	}
				    	fw.close();
				    }else{
				    	 File fold=new File(fs);
				         fold.delete();
				         FileWriter f = new FileWriter(file, false);
					    	BufferedWriter fw = new BufferedWriter(f);
					    	int i = 0;
					    	Alimentare a;
					    	NonAlimentare na;
					    	while(i<carrello.getNumProdotti()){				    		
					    		if (carrello.getLista()[i] instanceof Alimentare) {
					    			a = (Alimentare)carrello.getLista()[i];
					    			fw.write(i+1 + " "+a.getDescrizione() + " " + a.getPrezzo() + " " + a.getCodice() + " " + a.getScadenza() + "\r\n");
					    		} else {
					    			na = (NonAlimentare)carrello.getLista()[i];
					    			fw.write(i+1 + " "+na.getDescrizione() + " " + na.getPrezzo() + " " + na.getCodice() + " "+ na.getMateriale() + "\r\n");
					    		}
						    	
						    	i++;
					    	}
					    	fw.close();
					    	MessageDialog.openInformation(shell, "Avviso", "Salvataggio riuscito");
				    }
				  }
				  catch (IOException e1) {
				    e1.printStackTrace();
				  }
			}
		});
		btnSalvaScontrino.setText("Salva Scontrino");
		btnSalvaScontrino.setBounds(274, 96, 115, 25);
		
		Label lblDataScadenxa = new Label(shell, SWT.NONE);
		lblDataScadenxa.setBounds(172, 255, 102, 15);
		lblDataScadenxa.setText("Data Scadenza");
		

	}
}
