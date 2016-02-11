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
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table table_3;
	private Text text_4;
	private float tot;
	private boolean replace;

	public spesaGrafica() {
		on = false;
	}

	public void compilaTabella() {
		table_3.removeAll();
		for (int i = 0; i < carrello.getNumProdotti(); i++) {
			TableItem item = new TableItem(table_3, SWT.NONE);
			Alimentare a;
			NonAlimentare na;
			if (carrello.getLista()[i] instanceof Alimentare) {
				a = (Alimentare) carrello.getLista()[i];
				item.setText(new String[] { a.getDescrizione(), String.valueOf(a.getPrezzo()), a.getCodice(), "/",
						a.getScadenza().toString() });
			} else {
				na = (NonAlimentare) carrello.getLista()[i];
				item.setText(new String[] { na.getDescrizione(), String.valueOf(na.getPrezzo()), na.getCodice(),
						na.getMateriale(), "/" });
			}
		}
		text_4.setText(Float.toString(tot));
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
		Button btnTesseraFedelt = new Button(shell, SWT.CHECK);
		btnTesseraFedelt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				carrello.setTessera(btnTesseraFedelt.getSelection());
			}
		});
		shell.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent arg0) {
				if (!on) {
					on = true;
					if (MessageDialog.openQuestion(shell, "Tessera", "Possiedi la tessera fedeltà?")) {
						carrello.setTessera(true);
						btnTesseraFedelt.setSelection(carrello.isTessera());

					} else {
						carrello.setTessera(false);
						btnTesseraFedelt.setSelection(carrello.isTessera());
					}
				}
			}
		});
		shell.setSize(983, 440);
		shell.setText("Ali & WiZ's");
		shell.setLayout(new FormLayout());
		List list = new List(shell, SWT.BORDER);
		FormData fd_list = new FormData();
		fd_list.top = new FormAttachment(0, 297);
		fd_list.bottom = new FormAttachment(100, -10);
		fd_list.right = new FormAttachment(100, -1310);
		fd_list.left = new FormAttachment(0, 10);
		list.setLayoutData(fd_list);

		DateTime dateTime = new DateTime(shell, SWT.BORDER);
		FormData fd_dateTime = new FormData();
		dateTime.setLayoutData(fd_dateTime);

		Button btnAlimentare = new Button(shell, SWT.RADIO);
		FormData fd_btnAlimentare = new FormData();
		fd_btnAlimentare.left = new FormAttachment(list, 0, SWT.LEFT);
		fd_btnAlimentare.right = new FormAttachment(0, 113);
		btnAlimentare.setLayoutData(fd_btnAlimentare);
		btnAlimentare.setText("Alimentare");

		Button btnNonAlimentare = new Button(shell, SWT.RADIO);
		FormData fd_btnNonAlimentare = new FormData();
		fd_btnNonAlimentare.left = new FormAttachment(dateTime, 0, SWT.LEFT);
		fd_btnNonAlimentare.top = new FormAttachment(btnAlimentare, 0, SWT.TOP);
		btnNonAlimentare.setLayoutData(fd_btnNonAlimentare);
		btnNonAlimentare.setText("Non Alimentare");

		Label lblMateriale = new Label(shell, SWT.NONE);
		FormData fd_lblMateriale = new FormData();
		fd_lblMateriale.right = new FormAttachment(btnAlimentare, 0, SWT.RIGHT);
		fd_lblMateriale.left = new FormAttachment(0, 10);
		lblMateriale.setLayoutData(fd_lblMateriale);
		lblMateriale.setText("Materiale");

		text_3 = new Text(shell, SWT.BORDER);
		fd_dateTime.left = new FormAttachment(text_3, 0, SWT.LEFT);
		fd_dateTime.top = new FormAttachment(text_3, 6);
		FormData fd_text_3 = new FormData();
		fd_text_3.left = new FormAttachment(lblMateriale, 6);
		text_3.setLayoutData(fd_text_3);

		Spinner spinner = new Spinner(shell, SWT.BORDER);

		Button btnCaricaProdotto = new Button(shell, SWT.NONE);
		FormData fd_btnCaricaProdotto = new FormData();
		btnCaricaProdotto.setLayoutData(fd_btnCaricaProdotto);
		btnCaricaProdotto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnAlimentare.getSelection()) {
					if (spinner.getSelection() > 0) {
						for (int i = 0; i < spinner.getSelection(); i++) {
							try {
								Prodotto pa = new Alimentare(text.getText(), text_2.getText(),
										Float.parseFloat(text_1.getText()),
										Data = new Data(dateTime.getDay(), dateTime.getMonth(), dateTime.getYear()));
								carrello.aggiungiProdotto(pa);
								tot += pa.getPrezzo();
							} catch (Exception e1) {
								MessageDialog.openError(shell, "Errore", "Campi inseriti non validi!");
							}
						}
					} else {
						try {
							Prodotto pa = new Alimentare(text.getText(), text_2.getText(),
									Float.parseFloat(text_1.getText()),
									Data = new Data(dateTime.getDay(), dateTime.getMonth(), dateTime.getYear()));
							carrello.aggiungiProdotto(pa);
							tot += pa.getPrezzo();
						} catch (Exception e1) {
							MessageDialog.openError(shell, "Errore", "Campi inseriti non validi!");
						}
					}
					compilaTabella();
				} else if (btnNonAlimentare.getSelection()) {
					if (spinner.getSelection() > 0) {
						for (int i = 0; i < spinner.getSelection(); i++) {
							try {
								Prodotto pa = new NonAlimentare(text.getText(), text_2.getText(),
										Float.parseFloat(text_1.getText()), text_3.getText());
								carrello.aggiungiProdotto(pa);
								tot += pa.getPrezzo();
							} catch (Exception e1) {
								MessageDialog.openError(shell, "Errore", "Campi inseriti non validi!");
							}
						}
					} else {
						try {
							Prodotto pa = new NonAlimentare(text.getText(), text_2.getText(),
									Float.parseFloat(text_1.getText()), text_3.getText());
							carrello.aggiungiProdotto(pa);
							tot += pa.getPrezzo();
						} catch (Exception e1) {
							MessageDialog.openError(shell, "Errore", "Campi inseriti non validi!");
						}
					}
					compilaTabella();
				} else {
					MessageDialog.openWarning(shell, "Avviso", "Selezionare tipo di prodotto");
				}
			}
		});
		btnCaricaProdotto.setText("+");

		Button btnEliminaProdotto = new Button(shell, SWT.NONE);
		FormData fd_btnEliminaProdotto = new FormData();
		fd_btnEliminaProdotto.top = new FormAttachment(lblMateriale, -5, SWT.TOP);
		fd_btnEliminaProdotto.left = new FormAttachment(text_3, 6);
		btnEliminaProdotto.setLayoutData(fd_btnEliminaProdotto);
		btnEliminaProdotto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (table_3.getSelectionIndex() != -1) {
						carrello.eliminaProdotto(table_3.getSelectionIndex());
						tot -= carrello.getLista()[table_3.getSelectionIndex()].getPrezzo();
					} else {
						carrello.eliminaProdotto(carrello.getNumProdotti() - 1);
						tot -= carrello.getLista()[carrello.getNumProdotti() - 1].getPrezzo();
					}
				} catch (Exception e1) {
					MessageDialog.openError(shell, "Errore", "Elemento Selezionato non esistente");
				}
				compilaTabella();
			}
		});
		btnEliminaProdotto.setText("-");

		Button btnCaio = new Button(shell, SWT.NONE);
		fd_btnAlimentare.top = new FormAttachment(btnCaio, 6);
		FormData fd_btnCaio = new FormData();
		fd_btnCaio.left = new FormAttachment(0, 10);
		btnCaio.setLayoutData(fd_btnCaio);
		btnCaio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(shell);
				fd.setFilterExtensions(new String[] { "*.txt", "*.csv", "*.*" });
				fs = fd.open();
				if (fs != null) {
					file = new File(fs);
				}

			}
		});
		btnCaio.setText("Browse File");

		text = new Text(shell, SWT.BORDER);
		fd_btnCaricaProdotto.left = new FormAttachment(text, 6);
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(btnNonAlimentare, 6);
		fd_text.right = new FormAttachment(100, -716);
		text.setLayoutData(fd_text);

		text_1 = new Text(shell, SWT.BORDER);
		FormData fd_text_1 = new FormData();
		fd_text_1.top = new FormAttachment(text, 6);
		text_1.setLayoutData(fd_text_1);

		Label lblCodiceProdotto = new Label(shell, SWT.NONE);
		fd_text.left = new FormAttachment(lblCodiceProdotto, 7);
		FormData fd_lblCodiceProdotto = new FormData();
		fd_lblCodiceProdotto.top = new FormAttachment(btnAlimentare, 6);
		fd_lblCodiceProdotto.left = new FormAttachment(list, 0, SWT.LEFT);
		fd_lblCodiceProdotto.right = new FormAttachment(0, 112);
		lblCodiceProdotto.setLayoutData(fd_lblCodiceProdotto);
		lblCodiceProdotto.setText("Codice Prodotto");

		Label lblPrezzo = new Label(shell, SWT.NONE);
		fd_text_1.left = new FormAttachment(lblPrezzo, 7);
		FormData fd_lblPrezzo = new FormData();
		fd_lblPrezzo.top = new FormAttachment(text_1, 3, SWT.TOP);
		fd_lblPrezzo.left = new FormAttachment(list, 0, SWT.LEFT);
		fd_lblPrezzo.right = new FormAttachment(0, 112);
		lblPrezzo.setLayoutData(fd_lblPrezzo);
		lblPrezzo.setText("Prezzo");

		text_2 = new Text(shell, SWT.BORDER);
		fd_text_3.top = new FormAttachment(text_2, 6);
		FormData fd_text_2 = new FormData();
		fd_text_2.top = new FormAttachment(text_1, 6);
		text_2.setLayoutData(fd_text_2);

		Label lblDescrizione = new Label(shell, SWT.NONE);
		fd_text_2.left = new FormAttachment(lblDescrizione, 7);
		fd_lblMateriale.top = new FormAttachment(lblDescrizione, 12);
		FormData fd_lblDescrizione = new FormData();
		fd_lblDescrizione.top = new FormAttachment(text_2, 3, SWT.TOP);
		fd_lblDescrizione.right = new FormAttachment(btnAlimentare, -1, SWT.RIGHT);
		fd_lblDescrizione.left = new FormAttachment(list, 0, SWT.LEFT);
		lblDescrizione.setLayoutData(fd_lblDescrizione);
		lblDescrizione.setText("Descrizione");

		FormData fd_spinner = new FormData();
		fd_spinner.bottom = new FormAttachment(btnEliminaProdotto, -16);
		fd_spinner.left = new FormAttachment(btnCaricaProdotto, 0, SWT.LEFT);
		fd_spinner.right = new FormAttachment(btnCaricaProdotto, 0, SWT.RIGHT);
		spinner.setLayoutData(fd_spinner);

		Button btnCaricaScontrino = new Button(shell, SWT.NONE);
		FormData fd_btnCaricaScontrino = new FormData();
		fd_btnCaricaScontrino.top = new FormAttachment(dateTime, 6);
		fd_btnCaricaScontrino.left = new FormAttachment(0, 6);
		btnCaricaScontrino.setLayoutData(fd_btnCaricaScontrino);
		btnCaricaScontrino.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] s1 = new String[4];
				String s = new String();
				try {
					fr = new FileReader(file);
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				BufferedReader br = new BufferedReader(fr);
				StringBuffer stringBuffer = new StringBuffer();
				carrello = new ListaSpesa(true, carrello.getMax());
				try {
					while ((s = br.readLine()) != null) {
						stringBuffer.append(s);
						s1 = s.split(" ");
						Prodotto p1 = null;
						try {
							if (s1[4].equals("Carta") || s1[4].equals("Vetro") || s1[4].equals("Plastica")) {
								p1 = new NonAlimentare(s1[2], s1[1], Float.parseFloat(s1[3]), s1[4]);
							} else {
								System.out.println(Data);
								p1 = new Alimentare(s1[2], s1[1], Float.parseFloat(s1[3]),
										Data = new Data(Integer.parseInt((s1[4].split("/"))[0]),
												Integer.parseInt((s1[4].split("/"))[1]),
												Integer.parseInt((s1[4].split("/"))[2])));
							}
						} catch (Exception e2) {
							MessageDialog.openError(shell, "Errore", "Errore nella Lettura!");
							break;
						}
						try {
							carrello.aggiungiProdotto(p1);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							MessageDialog.openError(shell, "Errore", "Errore nella Lettura!");
							break;
						}
						tot += p1.getPrezzo();
						compilaTabella();
					}
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					MessageDialog.openError(shell, "Errore", "Errore nella Lettura!");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					MessageDialog.openError(shell, "Errore", "Errore nella Lettura!");
				}
			}
		});
		btnCaricaScontrino.setText("Carica Scontrino");

		Button btnSalvaScontrino = new Button(shell, SWT.NONE);
		fd_btnCaricaScontrino.right = new FormAttachment(btnSalvaScontrino, -6);
		FormData fd_btnSalvaScontrino = new FormData();
		fd_btnSalvaScontrino.top = new FormAttachment(btnCaricaScontrino, 0, SWT.TOP);
		fd_btnSalvaScontrino.left = new FormAttachment(btnCaricaProdotto, 0, SWT.LEFT);
		btnSalvaScontrino.setLayoutData(fd_btnSalvaScontrino);
		btnSalvaScontrino.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (file.createNewFile()) {
						FileWriter f = new FileWriter(file);
						BufferedWriter fw = new BufferedWriter(f);
						int i = 0;
						Alimentare a;
						NonAlimentare na;
						while (i < carrello.getNumProdotti()) {
							if (carrello.getLista()[i] instanceof Alimentare) {
								a = (Alimentare) carrello.getLista()[i];
								fw.write(i + 1 + " " + a.getDescrizione() + " " + a.getPrezzo() + " " + a.getCodice()
										+ " " + a.getScadenza() + "\r\n");
							} else {
								na = (NonAlimentare) carrello.getLista()[i];
								fw.write(i + 1 + " " + na.getDescrizione() + " " + na.getPrezzo() + " " + na.getCodice()
										+ " " + na.getMateriale() + "\r\n");
							}

							i++;
						}
						fw.close();
					} else {
						if (MessageDialog.openConfirm(shell, "Avviso", "File già esistente, sovvrascrivere?")) {
							File fold = new File(fs);
							fold.delete();
							FileWriter f = new FileWriter(file, false);
							BufferedWriter fw = new BufferedWriter(f);
							int i = 0;
							Alimentare a;
							NonAlimentare na;
							while (i < carrello.getNumProdotti()) {
								if (carrello.getLista()[i] instanceof Alimentare) {
									a = (Alimentare) carrello.getLista()[i];
									fw.write(i + 1 + "-" + a.getDescrizione() + "-" + a.getPrezzo() + "-"
											+ a.getCodice() + "-" + a.getScadenza() + "\r\n");
								} else {
									na = (NonAlimentare) carrello.getLista()[i];
									fw.write(i + 1 + "-" + na.getDescrizione() + "-" + na.getPrezzo() + "-"
											+ na.getCodice() + "-" + na.getMateriale() + "\r\n");
								}
								i++;
							}
							fw.write("TOTALE:" + Float.toString(tot));
							fw.close();
							MessageDialog.openInformation(shell, "Avviso", "Salvataggio riuscito");
						} else {
							File fold = new File(fs);
							fold.delete();
							FileWriter f = new FileWriter(file, true);
							BufferedWriter fw = new BufferedWriter(f);
							int i = 0;
							Alimentare a;
							NonAlimentare na;
							while (i < carrello.getNumProdotti()) {
								if (carrello.getLista()[i] instanceof Alimentare) {
									a = (Alimentare) carrello.getLista()[i];
									fw.write(i + 1 + "-" + a.getDescrizione() + "-" + a.getPrezzo() + "-"
											+ a.getCodice() + "-" + a.getScadenza() + "\r\n");
								} else {
									na = (NonAlimentare) carrello.getLista()[i];
									fw.write(i + 1 + "-" + na.getDescrizione() + "-" + na.getPrezzo() + "-"
											+ na.getCodice() + "-" + na.getMateriale() + "\r\n");
								}
								i++;
							}
							fw.write("TOTALE:" + Float.toString(tot));
							fw.close();
							MessageDialog.openInformation(shell, "Avviso", "Salvataggio riuscito");
						}
					}
				} catch (IOException e1) {
					MessageDialog.openError(shell, "Errore", "Errore nella scrittura!");
				}
			}
		});
		btnSalvaScontrino.setText("Salva Scontrino");

		Label lblDataScadenxa = new Label(shell, SWT.NONE);
		FormData fd_lblDataScadenxa = new FormData();
		fd_lblDataScadenxa.top = new FormAttachment(lblMateriale, 9);
		fd_lblDataScadenxa.right = new FormAttachment(btnAlimentare, 0, SWT.RIGHT);
		fd_lblDataScadenxa.left = new FormAttachment(list, 1, SWT.LEFT);
		lblDataScadenxa.setLayoutData(fd_lblDataScadenxa);
		lblDataScadenxa.setText("Data Scadenza");

		table_3 = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		fd_btnCaricaProdotto.right = new FormAttachment(table_3, -91);
		fd_btnNonAlimentare.right = new FormAttachment(table_3, -248);
		fd_dateTime.right = new FormAttachment(table_3, -248);
		fd_text_1.right = new FormAttachment(table_3, -248);
		fd_text_2.right = new FormAttachment(table_3, -248);
		fd_btnEliminaProdotto.right = new FormAttachment(table_3, -91);
		fd_text_3.right = new FormAttachment(table_3, -248);
		fd_btnCaio.top = new FormAttachment(table_3, 0, SWT.TOP);
		table_3.setLinesVisible(true);
		String[] s = { "Prodotto", "Prezzo", "Codice", "Materiale", "Data scadenza" };
		for (int i = 0; i < 5; i++) {
			TableColumn column = new TableColumn(table_3, SWT.NONE);
			if (i == 1) {
				column.setWidth(50);
			} else {
				column.setWidth(100);
			}
			column.setText(s[i]);
		}
		for (int i = 0; i < carrello.getNumProdotti(); i++) {
			TableItem item = new TableItem(table_3, SWT.NONE);
			Alimentare a;
			NonAlimentare na;
			if (carrello.getLista()[i] instanceof Alimentare) {
				a = (Alimentare) carrello.getLista()[i];
				item.setText(new String[] { a.getDescrizione(), String.valueOf(a.getPrezzo()), a.getCodice(), "/",
						a.getScadenza().toString() });
			} else {
				na = (NonAlimentare) carrello.getLista()[i];
				item.setText(new String[] { na.getDescrizione(), String.valueOf(na.getPrezzo()), na.getCodice(),
						na.getMateriale(), "/" });
			}
		}
		FormData fd_table_3 = new FormData();
		fd_table_3.bottom = new FormAttachment(100, -20);
		fd_table_3.top = new FormAttachment(0, 16);
		fd_table_3.left = new FormAttachment(0, 499);
		fd_table_3.right = new FormAttachment(100, -10);
		table_3.setLayoutData(fd_table_3);
		formToolkit.adapt(table_3);
		formToolkit.paintBordersFor(table_3);
		table_3.setHeaderVisible(true);
		table_3.setLinesVisible(true);

		Button btnSvuotaCarrello = new Button(shell, SWT.NONE);
		fd_btnCaio.right = new FormAttachment(btnSvuotaCarrello, -6);
		fd_btnCaricaProdotto.top = new FormAttachment(btnSvuotaCarrello, 26);
		fd_btnSalvaScontrino.right = new FormAttachment(btnSvuotaCarrello, 0, SWT.RIGHT);
		btnSvuotaCarrello.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (int i = 0; i < carrello.getNumProdotti(); i++)
					carrello.eliminaProdotto(i);

				compilaTabella();
			}
		});
		FormData fd_btnSvuotaCarrello = new FormData();
		fd_btnSvuotaCarrello.left = new FormAttachment(0, 257);
		fd_btnSvuotaCarrello.right = new FormAttachment(table_3, -1);
		fd_btnSvuotaCarrello.top = new FormAttachment(btnCaio, 0, SWT.TOP);
		btnSvuotaCarrello.setLayoutData(fd_btnSvuotaCarrello);
		formToolkit.adapt(btnSvuotaCarrello, true, true);
		btnSvuotaCarrello.setText("Svuota carrello");

		FormData fd_btnTesseraFedelt = new FormData();
		fd_btnTesseraFedelt.bottom = new FormAttachment(list, -43);
		fd_btnTesseraFedelt.top = new FormAttachment(btnCaricaScontrino, 6);
		fd_btnTesseraFedelt.left = new FormAttachment(list, 0, SWT.LEFT);
		fd_btnTesseraFedelt.right = new FormAttachment(0, 133);
		btnTesseraFedelt.setLayoutData(fd_btnTesseraFedelt);
		formToolkit.adapt(btnTesseraFedelt, true, true);
		btnTesseraFedelt.setText("Tessera Fedelt\u00E0");

		Label lblTotale = new Label(shell, SWT.NONE);
		FormData fd_lblTotale = new FormData();
		fd_lblTotale.left = new FormAttachment(btnTesseraFedelt, 0, SWT.LEFT);
		fd_lblTotale.right = new FormAttachment(0, 76);
		lblTotale.setLayoutData(fd_lblTotale);
		formToolkit.adapt(lblTotale, true, true);
		lblTotale.setText("TOTALE:");

		text_4 = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		fd_lblTotale.top = new FormAttachment(text_4, 3, SWT.TOP);
		FormData fd_text_4 = new FormData();
		fd_text_4.right = new FormAttachment(table_3, -294);
		fd_text_4.left = new FormAttachment(lblTotale, 6);
		fd_text_4.bottom = new FormAttachment(btnTesseraFedelt, 39, SWT.BOTTOM);
		fd_text_4.top = new FormAttachment(btnTesseraFedelt, 15);
		text_4.setLayoutData(fd_text_4);
		formToolkit.adapt(text_4, true, true);

	}
}
