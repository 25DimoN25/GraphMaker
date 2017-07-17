import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.colorchooser.DefaultColorSelectionModel;

import net.objecthunter.exp4j.ExpressionBuilder;


@SuppressWarnings("serial")
public class Func {
	static String temp;
	
	static JList<String> list; 
	static DefaultListModel<String> listModel;
	
	static DefaultColorSelectionModel colorModel = new DefaultColorSelectionModel();
//	static WebColorChooserField color1 = new WebColorChooserField(), color2 = new WebColorChooserField();//
	static JCheckBox verifyDelete;
	
	static JPanel createFuncMenu = new JPanel(){{
		add(new JColorChooser(colorModel).getChooserPanels()[2]);
//		add(color1);//
	}};
	static JPanel editFuncMenu = new JPanel(){{
		add(new JColorChooser(colorModel).getChooserPanels()[2]);
//		add(color2);//
		add(verifyDelete = new JCheckBox("Удалить функцию"));
	}};

	
	
	static JFrame frame = new  JFrame("Функции") {{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(Main.midX, Main.midY,300,300);
		setResizable(false);
		add(
			list = new JList<String>(
				listModel = new DefaultListModel<String>()
			)
		);
		
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if ((list.getSelectedIndex()) == listModel.getSize()-1){
					if ( (temp = (String) JOptionPane.showInputDialog(null, createFuncMenu, "Введите функцию f(x)=", JOptionPane.INFORMATION_MESSAGE)) != null && temp.length() != 0 ) {
						try {
							new ExpressionBuilder(temp).variables("x", "e", "pi").build().setVariable("x", 1).setVariable("e", 1).setVariable("pi", 1).evaluate();
							
							listModel.add(listModel.getSize()-1, temp);
							Main.functions.put(temp, colorModel.getSelectedColor());
							Main.mainPannel.repaint();
						}  								
						 catch (Exception e) {
							JOptionPane.showMessageDialog(null, e.getMessage(), "Error" ,JOptionPane.ERROR_MESSAGE);
						} finally {
							System.gc();
						}
					}
					list.clearSelection();
				}
				else if ((list.getSelectedIndex()) < listModel.getSize()-1){
					temp = (String) JOptionPane.showInputDialog(null, editFuncMenu, "Изменение функции", JOptionPane.INFORMATION_MESSAGE, null, null, list.getSelectedValue());
					if (verifyDelete.isSelected() && temp != null) {
						Main.functions.remove(list.getSelectedValue());
						listModel.remove(list.getSelectedIndex());
						Main.mainPannel.repaint();
					} else
					if (!verifyDelete.isSelected() && temp != null && temp.length() != 0) {
							Main.functions.remove(list.getSelectedValue());
							Main.functions.put(temp, colorModel.getSelectedColor());
							listModel.set(list.getSelectedIndex(), temp);
							Main.mainPannel.repaint();
					}
					list.clearSelection();
				}
			}
		});
		
		listModel.addElement("добавить функцию...");
		
	}};
	static void show() {		
		list.clearSelection();
		frame.setVisible(true);
	}
}
