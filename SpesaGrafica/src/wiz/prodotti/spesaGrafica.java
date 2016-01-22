package wiz.prodotti;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


import org.eclipse.swt.widgets.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class spesaGrafica {
	protected String [] carrello;
	protected Shell shell;
	protected String [] prodotti = {"patata", "cocciolata", "carota"};
	private String [] temp;

	
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
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		
		List list = new List(shell, SWT.BORDER);
		list.setItems(prodotti);
		list.setBounds(10, 32, 71, 219);
		
		List list_1 = new List(shell, SWT.BORDER);
		list_1.setBounds(353, 32, 71, 219);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				temp = new String[carrello.length+1];
				temp = carrello.clone();
				temp[temp.length-1] = list.getItems()[list.getSelectionIndex()];
				carrello = new String[temp.length];
				carrello = temp.clone();
				System.out.print(carrello);
				list_1.setItems(carrello);
				list_1.update();
			}
		});
		btnNewButton.setBounds(174, 50, 75, 25);
		btnNewButton.setText("Prendi");
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.setBounds(174, 127, 75, 25);
		btnNewButton_1.setText("Riponi");
		
		Label lblScaffali = new Label(shell, SWT.NONE);
		lblScaffali.setBounds(10, 10, 71, 15);
		lblScaffali.setText("Scaffali");
		
		Label lblCarrello = new Label(shell, SWT.NONE);
		lblCarrello.setBounds(353, 10, 55, 15);
		lblCarrello.setText("Carrello");

	}
}
