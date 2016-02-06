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

import javax.swing.JTable;

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
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class spesaGrafica {
	protected ListaSpesa carrello = new ListaSpesa(true, 40);
	protected Shell shell;
	private Prodotto [] p = new Prodotto[5];
	protected ListaSpesa prodotti = new ListaSpesa(true, 40);
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
	private Table table;
	private Table table_1;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table table_2;
	private Table table_3;
	
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
		shell.setBackgroundImage(SWTResourceManager.getImage("SPESA.jpg"));
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
		shell.setSize(717, 752);
		shell.setText("SWT Application");
		shell.setLayout(new FormLayout());
		List list_1 = new List(shell, SWT.BORDER);
		FormData fd_list_1 = new FormData();
		fd_list_1.bottom = new FormAttachment(0, 326);
		fd_list_1.right = new FormAttachment(0, 691);
		fd_list_1.top = new FormAttachment(0, 32);
		fd_list_1.left = new FormAttachment(0, 472);
		list_1.setLayoutData(fd_list_1);
		List list = new List(shell, SWT.BORDER);
		FormData fd_list = new FormData();
		fd_list.bottom = new FormAttachment(0, 326);
		fd_list.right = new FormAttachment(0, 196);
		fd_list.top = new FormAttachment(0, 32);
		fd_list.left = new FormAttachment(0, 10);
		list.setLayoutData(fd_list);
		for(int i=0; i<prodotti.getNumProdotti(); i++){
			list.add(prodotti.getLista()[i].getDescrizione());
		}
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.right = new FormAttachment(0, 277);
		fd_btnNewButton.top = new FormAttachment(0, 31);
		fd_btnNewButton.left = new FormAttachment(0, 202);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				temp = new ListaSpesa(true, carrello.getMax());
				temp.setNumProdotti(carrello.getNumProdotti()+1);
				for(int i=0; i<carrello.getNumProdotti(); i++){
					temp.getLista()[i] = carrello.getLista()[i];
				}
				temp.getLista()[temp.getNumProdotti()-1] = prodotti.getLista()[list.getSelectionIndex()];
				carrello = new ListaSpesa(true, temp.getMax());
				carrello.setNumProdotti(temp.getNumProdotti());
				for(int i=0; i<temp.getNumProdotti(); i++){
					carrello.getLista()[i] = temp.getLista()[i];

				}
				list_1.add(carrello.getLista()[carrello.getNumProdotti()-1].getDescrizione() +" - " + carrello.getLista()[carrello.getNumProdotti()-1].getCodice() + " - " + carrello.getLista()[carrello.getNumProdotti()-1].getPrezzo());
				list_1.update();
			}
		});
		btnNewButton.setText("Prendi");
		
		DateTime dateTime = new DateTime(shell, SWT.BORDER);
		FormData fd_dateTime = new FormData();
		fd_dateTime.right = new FormAttachment(0, 305);
		fd_dateTime.top = new FormAttachment(0, 187);
		fd_dateTime.left = new FormAttachment(0, 202);
		dateTime.setLayoutData(fd_dateTime);
		
		Button btnAlimentare = new Button(shell, SWT.RADIO);
		FormData fd_btnAlimentare = new FormData();
		fd_btnAlimentare.right = new FormAttachment(0, 305);
		fd_btnAlimentare.top = new FormAttachment(0, 62);
		fd_btnAlimentare.left = new FormAttachment(0, 202);
		btnAlimentare.setLayoutData(fd_btnAlimentare);
		btnAlimentare.setText("Alimentare");
		
		Button btnNonAlimentare = new Button(shell, SWT.RADIO);
		FormData fd_btnNonAlimentare = new FormData();
		fd_btnNonAlimentare.right = new FormAttachment(0, 420);
		fd_btnNonAlimentare.top = new FormAttachment(0, 62);
		fd_btnNonAlimentare.left = new FormAttachment(0, 311);
		btnNonAlimentare.setLayoutData(fd_btnNonAlimentare);
		btnNonAlimentare.setText("Non Alimentare");
		

		Label lblMateriale = new Label(shell, SWT.NONE);
		FormData fd_lblMateriale = new FormData();
		fd_lblMateriale.right = new FormAttachment(0, 365);
		fd_lblMateriale.top = new FormAttachment(0, 166);
		fd_lblMateriale.left = new FormAttachment(0, 310);
		lblMateriale.setLayoutData(fd_lblMateriale);
		lblMateriale.setText("Materiale");
		
		text_3 = new Text(shell, SWT.BORDER);
		FormData fd_text_3 = new FormData();
		fd_text_3.bottom = new FormAttachment(0, 211);
		fd_text_3.right = new FormAttachment(0, 447);
		fd_text_3.top = new FormAttachment(0, 187);
		fd_text_3.left = new FormAttachment(0, 311);
		text_3.setLayoutData(fd_text_3);
		
		Button btnCaricaProdotto = new Button(shell, SWT.NONE);
		FormData fd_btnCaricaProdotto = new FormData();
		fd_btnCaricaProdotto.right = new FormAttachment(0, 258);
		fd_btnCaricaProdotto.top = new FormAttachment(0, 242);
		fd_btnCaricaProdotto.left = new FormAttachment(0, 202);
		btnCaricaProdotto.setLayoutData(fd_btnCaricaProdotto);
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
		btnCaricaProdotto.setText("+");
		
		Button btnEliminaProdotto = new Button(shell, SWT.NONE);
		FormData fd_btnEliminaProdotto = new FormData();
		fd_btnEliminaProdotto.right = new FormAttachment(0, 466);
		fd_btnEliminaProdotto.top = new FormAttachment(0, 242);
		fd_btnEliminaProdotto.left = new FormAttachment(0, 411);
		btnEliminaProdotto.setLayoutData(fd_btnEliminaProdotto);
		btnEliminaProdotto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.print(list_1.getSelectionIndex() + "\n");
				carrello.eliminaProdotto(list_1.getSelectionIndex());
				list_1.remove(list_1.getSelectionIndex());
			}
		});
		btnEliminaProdotto.setText("-");
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		FormData fd_btnNewButton_1 = new FormData();
		fd_btnNewButton_1.right = new FormAttachment(0, 466);
		fd_btnNewButton_1.top = new FormAttachment(0, 31);
		fd_btnNewButton_1.left = new FormAttachment(0, 391);
		btnNewButton_1.setLayoutData(fd_btnNewButton_1);
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
		btnNewButton_1.setText("Riponi");
		
		Button btnCaio = new Button(shell, SWT.NONE);
		FormData fd_btnCaio = new FormData();
		fd_btnCaio.right = new FormAttachment(0, 371);
		fd_btnCaio.top = new FormAttachment(0, 31);
		fd_btnCaio.left = new FormAttachment(0, 296);
		btnCaio.setLayoutData(fd_btnCaio);
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
		btnCaio.setText("Browse File");
		
		Label lblScaffali = new Label(shell, SWT.NONE);
		FormData fd_lblScaffali = new FormData();
		fd_lblScaffali.right = new FormAttachment(0, 81);
		fd_lblScaffali.top = new FormAttachment(0, 10);
		fd_lblScaffali.left = new FormAttachment(0, 10);
		lblScaffali.setLayoutData(fd_lblScaffali);
		lblScaffali.setText("Scaffali");
		
		Label lblCarrello = new Label(shell, SWT.NONE);
		FormData fd_lblCarrello = new FormData();
		fd_lblCarrello.right = new FormAttachment(0, 608);
		fd_lblCarrello.top = new FormAttachment(0, 11);
		fd_lblCarrello.left = new FormAttachment(0, 553);
		lblCarrello.setLayoutData(fd_lblCarrello);
		lblCarrello.setText("Carrello");
		
		text = new Text(shell, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.right = new FormAttachment(0, 442);
		fd_text.top = new FormAttachment(0, 85);
		fd_text.left = new FormAttachment(0, 310);
		text.setLayoutData(fd_text);
		
		text_1 = new Text(shell, SWT.BORDER);
		FormData fd_text_1 = new FormData();
		fd_text_1.right = new FormAttachment(0, 443);
		fd_text_1.top = new FormAttachment(0, 112);
		fd_text_1.left = new FormAttachment(0, 311);
		text_1.setLayoutData(fd_text_1);
		
		Label lblCodiceProdotto = new Label(shell, SWT.NONE);
		FormData fd_lblCodiceProdotto = new FormData();
		fd_lblCodiceProdotto.right = new FormAttachment(0, 304);
		fd_lblCodiceProdotto.top = new FormAttachment(0, 88);
		fd_lblCodiceProdotto.left = new FormAttachment(0, 202);
		lblCodiceProdotto.setLayoutData(fd_lblCodiceProdotto);
		lblCodiceProdotto.setText("Codice Prodotto");
		
		Label lblPrezzo = new Label(shell, SWT.NONE);
		FormData fd_lblPrezzo = new FormData();
		fd_lblPrezzo.right = new FormAttachment(0, 304);
		fd_lblPrezzo.top = new FormAttachment(0, 115);
		fd_lblPrezzo.left = new FormAttachment(0, 202);
		lblPrezzo.setLayoutData(fd_lblPrezzo);
		lblPrezzo.setText("Prezzo");
		
		text_2 = new Text(shell, SWT.BORDER);
		FormData fd_text_2 = new FormData();
		fd_text_2.right = new FormAttachment(0, 443);
		fd_text_2.top = new FormAttachment(0, 139);
		fd_text_2.left = new FormAttachment(0, 311);
		text_2.setLayoutData(fd_text_2);
		
		Label lblDescrizione = new Label(shell, SWT.NONE);
		FormData fd_lblDescrizione = new FormData();
		fd_lblDescrizione.right = new FormAttachment(0, 304);
		fd_lblDescrizione.top = new FormAttachment(0, 142);
		fd_lblDescrizione.left = new FormAttachment(0, 202);
		lblDescrizione.setLayoutData(fd_lblDescrizione);
		lblDescrizione.setText("Descrizione");
		
		Spinner spinner = new Spinner(shell, SWT.BORDER);
		FormData fd_spinner = new FormData();
		fd_spinner.right = new FormAttachment(0, 365);
		fd_spinner.top = new FormAttachment(0, 244);
		fd_spinner.left = new FormAttachment(0, 311);
		spinner.setLayoutData(fd_spinner);
		
		Button btnCaricaScontrino = new Button(shell, SWT.NONE);
		FormData fd_btnCaricaScontrino = new FormData();
		fd_btnCaricaScontrino.right = new FormAttachment(0, 691);
		fd_btnCaricaScontrino.top = new FormAttachment(0, 332);
		fd_btnCaricaScontrino.left = new FormAttachment(0, 576);
		btnCaricaScontrino.setLayoutData(fd_btnCaricaScontrino);
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
		btnCaricaScontrino.setText("Carica Scontrino");
		
		Button btnSalvaScontrino = new Button(shell, SWT.NONE);
		FormData fd_btnSalvaScontrino = new FormData();
		fd_btnSalvaScontrino.right = new FormAttachment(0, 125);
		fd_btnSalvaScontrino.top = new FormAttachment(0, 332);
		fd_btnSalvaScontrino.left = new FormAttachment(0, 10);
		btnSalvaScontrino.setLayoutData(fd_btnSalvaScontrino);
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
		
		Label lblDataScadenxa = new Label(shell, SWT.NONE);
		FormData fd_lblDataScadenxa = new FormData();
		fd_lblDataScadenxa.right = new FormAttachment(0, 304);
		fd_lblDataScadenxa.top = new FormAttachment(0, 166);
		fd_lblDataScadenxa.left = new FormAttachment(0, 202);
		lblDataScadenxa.setLayoutData(fd_lblDataScadenxa);
		lblDataScadenxa.setText("Data Scadenza");
		
		table_3 = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table_3.setLinesVisible(true);
		String [] s = {"Prodotto", "Prezzo", "Codice", "Materiale", "Data scadenza"};
		for (int i = 0; i < 5; i++) {
			TableColumn column = new TableColumn(table_3, SWT.NONE);
			column.setWidth(100);
			column.setText(s[i]);
		}
		for (int i = 0; i < prodotti.getNumProdotti(); i++) {
		    TableItem item = new TableItem(table_3, SWT.NONE);
		    item.setText(new String[] { prodotti.getLista()[i].getDescrizione(),String.valueOf(prodotti.getLista()[i].getPrezzo()),prodotti.getLista()[i].getCodice()});
		}
		FormData fd_table_3 = new FormData();
		fd_table_3.right = new FormAttachment(btnNonAlimentare, 143, SWT.RIGHT);
		fd_table_3.bottom = new FormAttachment(btnSalvaScontrino, 237, SWT.BOTTOM);
		fd_table_3.top = new FormAttachment(btnSalvaScontrino, 6);
		fd_table_3.left = new FormAttachment(0, 109);
		table_3.setLayoutData(fd_table_3);
		formToolkit.adapt(table_3);
		formToolkit.paintBordersFor(table_3);
		table_3.setHeaderVisible(true);
		table_3.setLinesVisible(true);
		

	}
}
