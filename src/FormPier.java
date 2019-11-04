import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import java.awt.Font;

public class FormPier {
	
	private final int panelPierWidth = 870;
	private final int panelPierHeight = 560;
	private final int countLevels = 5;
	private MultiLevelPier pier;
	private Hashtable<Integer, ITransport> storageShip;
	private Hashtable<Integer, IDecks> storageDecks;
	private int storageIndex = 0;
	private ITransport ship;
	private IDecks deck;
	private JFrame frame;
	private JTextField textFieldIndex;
	private PierPanel panelPier;
	private JButton btnParkShip;
	private JButton btnParkDieselShip;
	private JButton btnTake;
	private TakePanel panelTake;
	private JList<String> list;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormPier window = new FormPier();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FormPier() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1130, 620);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		pier = new MultiLevelPier(countLevels, panelPierWidth, panelPierHeight);
		storageShip = new Hashtable<>();
		storageDecks = new Hashtable<>();
		
		panelPier = new PierPanel(pier.getPier(0));
		panelPier.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelPier.setBounds(10, 11, panelPierWidth, panelPierHeight);
		frame.getContentPane().add(panelPier);
		
		
		String[] levels = new String[countLevels];
		for(int i = 0; i < countLevels; i++) {
			levels[i] = "�������" + (i + 1);
		}
		list = new JList(levels);
		list.setSelectedIndex(0);
		list.setBounds(890, 11, 214, 186);
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				int index = list.getSelectedIndex();
				panelPier.setPier(pier.getPier(index));
				panelPier.repaint();
			}
		});
		frame.getContentPane().add(list);
		
		btnParkShip = new JButton("\u041F\u043E\u0441\u0442\u0430\u0432\u0438\u0442\u044C \u043A\u043E\u0440\u0430\u0431\u043B\u044C");
		btnParkShip.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		btnParkShip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(frame, "���� �������", Color.blue);
				if (newColor != null) {
					ship = new Ship(100, 1000, newColor, Color.blue);
					int place = pier.getPier(list.getSelectedIndex()).plus(ship);
					panelPier.repaint();
				}
			}
		});
		btnParkShip.setBounds(912, 205, 154, 36);
		frame.getContentPane().add(btnParkShip);
		
		btnParkDieselShip = new JButton("\u041F\u043E\u0441\u0442\u0430\u0432\u0438\u0442\u044C \u0442\u0435\u043F\u043B\u043E\u0445\u043E\u0434");
		btnParkDieselShip.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		btnParkDieselShip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color mainColor = JColorChooser.showDialog(frame, "�������� ���� ���������", Color.blue);
				if (mainColor != null) {
					Color dopColor = JColorChooser.showDialog(frame, "�������������� ���� ���������", Color.blue);
					if (dopColor != null) {
						ship = new DieselShip(100, 1000, Decks.One, mainColor, dopColor, Color.yellow, true, true);
						Random rnd = new Random();
						switch (rnd.nextInt(3)) {
							case 0:
								deck = new StandardDecks();
								break;
							case 1:
								deck = new RoundedDecks();
								break;
							case 2:
								deck = new TrapezeDecks();
								break;
						}
						int place = pier.getPier(list.getSelectedIndex()).plus(ship, deck);
						panelPier.repaint();
					}					
				}
			}
		});
		btnParkDieselShip.setBounds(912, 252, 154, 36);
		frame.getContentPane().add(btnParkDieselShip);
		
		JLabel label = new JLabel("\u0417\u0430\u0431\u0440\u0430\u0442\u044C \u043A\u043E\u0440\u0430\u0431\u043B\u044C:");
		label.setBounds(926, 301, 122, 14);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("\u041C\u0435\u0441\u0442\u043E:");
		label_1.setBounds(923, 326, 48, 14);
		frame.getContentPane().add(label_1);
		
		textFieldIndex = new JTextField();
		textFieldIndex.setBounds(981, 323, 58, 20);
		frame.getContentPane().add(textFieldIndex);
		textFieldIndex.setColumns(10);
		
		btnTake = new JButton("\u0417\u0430\u0431\u0440\u0430\u0442\u044C");
		btnTake.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textFieldIndex.getText() != "") {
					ship = pier.getShip(list.getSelectedIndex(),Integer.parseInt(textFieldIndex.getText()));
					if (ship != null) {
						panelTake.clear();
						storageShip.put(storageIndex, ship);
						deck = pier.getDecks(list.getSelectedIndex(), Integer.parseInt(textFieldIndex.getText()));
						if (deck != null) {
							panelTake.drawShip(ship, deck);
							storageDecks.put(storageIndex, deck);
						} else {
							panelTake.drawShip(ship);
						}
						storageIndex++;
						panelTake.ship.setPosition(100, 50, panelPierWidth, panelPierHeight);
						panelPier.repaint();
						panelTake.repaint();
					}
				}
			}
		});
		btnTake.setBounds(920, 351, 119, 23);
		frame.getContentPane().add(btnTake);
		
		panelTake = new TakePanel();
		panelTake.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelTake.setBounds(889, 453, 215, 118);
		frame.getContentPane().add(panelTake);
		
		JButton btnShowCollection = new JButton("\u041A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u044F");
		btnShowCollection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CollectionInfo info = new CollectionInfo();
				info.showCollection(storageShip, storageDecks);
				info.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				info.setVisible(true);
			}
		});
		btnShowCollection.setBounds(929, 406, 119, 23);
		frame.getContentPane().add(btnShowCollection);
	}
}
