package wiz.prodotti;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Spinner;

public class spesaGrafica {
	protected ListaSpesa carrello = new ListaSpesa();
	protected Shell shell;
	protected String [] prodotti = {"patata", "cocciolata", "carota"};
	private String [] temp;
	private Text text;
	private Text text_1;
	private Text text_2;

	
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
	
	public static void clone(String [] s1, String [] s2){
		for(int i=0; i<s2.length; i++){
			s1[i] = s2[i];
		}
	}

	
	protected void createContents() {
		shell = new Shell();
		shell.setSize(573, 296);
		shell.setText("SWT Application");
		
		List list_1 = new List(shell, SWT.BORDER);
		list_1.setBounds(455, 31, 102, 227);
		
		List list = new List(shell, SWT.BORDER);
		list.setItems(prodotti);
		list.setBounds(0, 32, 102, 226);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				temp = new String[carrello.length+1];
				spesaGrafica.clone(temp, carrello);
				temp[temp.length-1] = list.getItems()[list.getSelectionIndex()];
				carrello = new String[temp.length];
				spesaGrafica.clone(carrello, temp);
				System.out.print(carrello);
				list_1.setItems(carrello);
				list_1.update();
			}
		});
		btnNewButton.setBounds(274, 50, 75, 25);
		btnNewButton.setText("Prendi");
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.setBounds(172, 50, 75, 25);
		btnNewButton_1.setText("Riponi");
		
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
		btnCaricaScontrino.setBounds(132, 96, 115, 25);
		btnCaricaScontrino.setText("Carica Scontrino");
		
		Button btnSalvaScontrino = new Button(shell, SWT.NONE);
		btnSalvaScontrino.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
				    File file = new File("scontrino.txt");
				    if(file.createNewFile()){
				    	System.out.println("File creato");
				    	FileWriter fw = new FileWriter(file);
				    	for(int i=0; i<carrello.length; i++){
				    		fw.write(i + ":" + carrello[i]);
				    	}
					    fw.flush();
					    fw.close();
				    }else{
				    	System.out.println("Non è stato possibile creare il file");
				    }
				  }
				  catch (IOException e1) {
				    e1.printStackTrace();
				  }
			}
		});
		btnSalvaScontrino.setText("Salva Scontrino");
		btnSalvaScontrino.setBounds(274, 96, 115, 25);

	}
}
