package org.omegat.plugins.autocomplete.forecat.preferences;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

public class DualList extends JPanel {

	private static final long serialVersionUID = 8088601856767192850L;
	DefaultListModel<String> leftModel;
	DefaultListModel<String> rightModel;
	
	public List<String> getLeftItems()
	{
		return Collections.list(leftModel.elements());
	}
	
	public List<String> getRightItems()
	{
		return Collections.list(rightModel.elements());
	}
	
	public void setLeftItems(List<String> items)
	{
		leftModel.ensureCapacity(items.size());
		for (String s : items)
		{
			leftModel.addElement(s);
		}
	}
	
	public void setRightItems(List<String> items)
	{
		rightModel.ensureCapacity(items.size());
		for (String s : items)
		{
			rightModel.addElement(s);
		}
	}
	
	public void addLeftItem(String item)
	{
		leftModel.addElement(item);
	}
	
	public void addRightItem(String item)
	{
		rightModel.addElement(item);
	}
	
	public DualList() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.bounded(Sizes.MINIMUM, Sizes.constant("0dlu", true), Sizes.constant("50dlu", true)), 0),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JScrollPane scrollPane_left = new JScrollPane();
		add(scrollPane_left, "2, 2, fill, fill");
		
		final JList<String> list_left = new JList<String>();
		scrollPane_left.setViewportView(list_left);
		leftModel = new DefaultListModel<String>();
		list_left.setModel(leftModel);
					
		JPanel panel = new JPanel();
		add(panel, "4, 2, fill, fill");
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panel_pad_1 = new JPanel();
		panel_pad_1.setBorder(null);
		panel.add(panel_pad_1);
		
		JButton button_move_to_right = new JButton(">");
		button_move_to_right.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(button_move_to_right);
		
		JPanel panel_pad_2 = new JPanel();
		panel_pad_2.setBorder(null);
		panel.add(panel_pad_2);
		
		JButton button_move_to_left = new JButton("<");
		panel.add(button_move_to_left);
		
		JPanel panel_pad_3 = new JPanel();
		panel_pad_3.setBorder(null);
		panel.add(panel_pad_3);
		
		JScrollPane scrollPane_right = new JScrollPane();
		add(scrollPane_right, "6, 2, fill, fill");
		
		final JList<String> list_right = new JList<String>();
		scrollPane_right.setViewportView(list_right);
		rightModel = new DefaultListModel<String>();
		list_right.setModel(rightModel);

		
		button_move_to_right.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for (int i : list_left.getSelectedIndices())
				{
					rightModel.addElement(leftModel.get(i));
					leftModel.remove(i);
				}
			}
		});
		button_move_to_left.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for (int i : list_right.getSelectedIndices())
				{
					leftModel.addElement(rightModel.get(i));
					rightModel.remove(i);
				}
			}
		});
	}
}
