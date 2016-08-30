package org.omegat.plugins.autocomplete.forecat.preferences;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.omegat.core.Core;
import org.omegat.gui.exttrans.IMachineTranslation;
import org.omegat.gui.exttrans.MachineTranslateTextArea;
import org.omegat.util.Preferences;

import com.jgoodies.forms.layout.FormLayout;
import com.googlecode.fannj.Fann;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.awt.FileDialog;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.border.TitledBorder;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ForecatPreferencesDialog extends JDialog {

	private static final long serialVersionUID = -3684055356687934832L;
	private final JPanel contentPanel = new JPanel();
	private JButton okButton;
	private JButton cancelButton;
	private JTabbedPane tabbedPane;
	private JPanel panel_internal;
	private JPanel panel_preferences;
	private JCheckBoxList list;
	private JLabel lblMinimumSubsegmentLength;
	private JSpinner minimumSpinner;
	private JLabel lblMaximumSubsegmentLength;
	private JSpinner maximumSpinner;
	private JButton button;
	private JTextField textField;
	private JSpinner maxSuggestionsSpinner;
	private JRadioButton rdbtnHeuristicRanker;
	private JRadioButton rdbtnNeuralNetworkRanker;
	private ButtonGroup radio;
	private JLabel lblNeuralNetworkFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ForecatPreferencesDialog dialog = new ForecatPreferencesDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ForecatPreferencesDialog() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Forecat preferences");
		setBounds(100, 100, 624, 429);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 1.0, 1.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridheight = 3;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		contentPanel.add(tabbedPane, gbc_tabbedPane);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						accept();
						ForecatPreferencesDialog.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ForecatPreferencesDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		panel_preferences = new JPanel();
		tabbedPane.addTab("General options", null, panel_preferences, null);
		panel_preferences.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(
				new TitledBorder(null, "Segmentation options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 12, 583, 76);
		panel_preferences.add(panel);
		panel.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow(9)"),
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, }));

		lblMinimumSubsegmentLength = new JLabel("Minimum subsegment length");
		panel.add(lblMinimumSubsegmentLength, "2, 2");

		minimumSpinner = new JSpinner();
		panel.add(minimumSpinner, "4, 2");
		minimumSpinner.setModel(new SpinnerNumberModel(new Integer(0), null, null, new Integer(1)));

		lblMaximumSubsegmentLength = new JLabel("Maximum subsegment length");
		panel.add(lblMaximumSubsegmentLength, "2, 4");

		maximumSpinner = new JSpinner();
		panel.add(maximumSpinner, "4, 4");

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Suggestion ranking options", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		panel_1.setBounds(12, 100, 583, 213);
		panel_preferences.add(panel_1);
		panel_1.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

		JLabel lblMaximumNumberOf = new JLabel("Maximum number of suggestions");
		panel_1.add(lblMaximumNumberOf, "2, 2");

		maxSuggestionsSpinner = new JSpinner();
		panel_1.add(maxSuggestionsSpinner, "4, 2, right, default");

		rdbtnHeuristicRanker = new JRadioButton("Heuristic ranker");
		panel_1.add(rdbtnHeuristicRanker, "2, 4");

		rdbtnNeuralNetworkRanker = new JRadioButton("Neural network ranker");
		panel_1.add(rdbtnNeuralNetworkRanker, "2, 6");

		lblNeuralNetworkFile = new JLabel("Neural network file");
		panel_1.add(lblNeuralNetworkFile, "2, 8");

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, "4, 8, fill, fill");
		panel_2.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

		textField = new JTextField();
		panel_2.add(textField, "2, 2, fill, default");
		textField.setColumns(10);

		button = new JButton("...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileDialog fd = new FileDialog(
						(Frame) SwingUtilities.getWindowAncestor(SwingUtilities.getWindowAncestor(contentPanel)));
				if (!"".equals(textField.getText())) {
					fd.setDirectory(textField.getText().replaceAll("/[^/]*$", ""));
					System.out.println("PATH " + textField.getText().replaceAll("/[^/]*$", ""));
				}
				fd.setVisible(true);
				if (fd.getFile() != null) {
					textField.setText(fd.getDirectory() + fd.getFile());
				}
			}
		});
		panel_2.add(button, "4, 2");

		panel_internal = new JPanel();
		tabbedPane.addTab("OmegaT MT", null, panel_internal, null);
		panel_internal.setLayout(new BorderLayout(0, 0));

		list = new JCheckBoxList();
		list.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_internal.add(list);

		radio = new ButtonGroup();
		radio.add(rdbtnNeuralNetworkRanker);
		radio.add(rdbtnHeuristicRanker);

		init();
	}

	private void init() {
		getOmegaTMT();

		minimumSpinner.setValue(
				Integer.parseInt(Preferences.getPreference(ForecatPreferences.FORECAT_MINIMUM_SUBSEGMENT_LENGTH)));
		maximumSpinner.setValue(
				Integer.parseInt(Preferences.getPreference(ForecatPreferences.FORECAT_MAXIMUM_SUBSEGMENT_LENGTH)));

		maxSuggestionsSpinner
				.setValue(Integer.parseInt(Preferences.getPreference(ForecatPreferences.FORECAT_MAXIMUM_SUGGESTIONS)));

		if ("neural".equals(Preferences.getPreference(ForecatPreferences.FORECAT_SUGGESTION_RANKER))) {
			rdbtnNeuralNetworkRanker.setSelected(true);
		} else {
			rdbtnHeuristicRanker.setSelected(true);
		}

		textField.setText(Preferences.getPreference(ForecatPreferences.FORECAT_ANN_FILE));

		if (!Fann.hasFann()) {
			rdbtnNeuralNetworkRanker.setEnabled(false);
			rdbtnHeuristicRanker.setSelected(true);
			textField.setEnabled(false);
			lblNeuralNetworkFile.setEnabled(false);
		}
	}

	protected void getOmegaTMT() {
		IMachineTranslation mt[];
		MachineTranslateTextArea mtta = Core.getMachineTranslatePane();
		Field f;
		String enabled = Preferences.getPreference(ForecatPreferences.FORECAT_ENABLED_OMEGAT_ENGINES);
		String ignore = Preferences.getPreference(ForecatPreferences.FORECAT_IGNORE_OMEGAT_ENGINES);

		try {
			f = MachineTranslateTextArea.class.getDeclaredField("translators");
			f.setAccessible(true);
			mt = (IMachineTranslation[]) f.get(mtta);

			Method getNameMethod = IMachineTranslation.class.getDeclaredMethod("getName");
			getNameMethod.setAccessible(true);

			DefaultListModel<JCheckBox> model = new DefaultListModel<JCheckBox>();
			for (IMachineTranslation m : mt) {
				String nameMethod = getNameMethod.invoke(m).toString();
				if (!ignore.contains(":" + nameMethod.replace(":", ";") + ":")) {
					JCheckBox jb = new JCheckBox(nameMethod);
					if (enabled.contains(":" + nameMethod.replace(":", ";") + ":")) {
						jb.setSelected(true);
					} else {
						jb.setSelected(false);
					}
					model.addElement(jb);
				}
			}
			list.setModel(model);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	protected String getOkButtonText() {
		return okButton.getText();
	}

	protected void setOkButtonText(String text) {
		okButton.setText(text);
	}

	protected String getCancelButtonText() {
		return cancelButton.getText();
	}

	protected void setCancelButtonText(String text_1) {
		cancelButton.setText(text_1);
	}

	protected String getThisTitle() {
		return getTitle();
	}

	protected void setThisTitle(String title) {
		setTitle(title);
	}

	protected void accept() {

		StringBuilder aux = new StringBuilder();
		aux.append(":");

		for (int i = 0; i < list.getModel().getSize(); i++) {
			JCheckBox o = list.getModel().getElementAt(i);
			if (o.isSelected()) {
				aux.append(o.getText().replace(":", ";"));
				aux.append(":");
			}
		}
		Preferences.setPreference(ForecatPreferences.FORECAT_ENABLED_OMEGAT_ENGINES, aux.toString());
		Preferences.setPreference(ForecatPreferences.FORECAT_MINIMUM_SUBSEGMENT_LENGTH, minimumSpinner.getValue());
		Preferences.setPreference(ForecatPreferences.FORECAT_MAXIMUM_SUBSEGMENT_LENGTH, maximumSpinner.getValue());
		Preferences.setPreference(ForecatPreferences.FORECAT_MAXIMUM_SUGGESTIONS, maxSuggestionsSpinner.getValue());

		if (rdbtnNeuralNetworkRanker.isSelected()) {
			Preferences.setPreference(ForecatPreferences.FORECAT_SUGGESTION_RANKER, "neural");
		} else if (rdbtnHeuristicRanker.isSelected()) {
			Preferences.setPreference(ForecatPreferences.FORECAT_SUGGESTION_RANKER, "heuristic");
		}
		Preferences.setPreference(ForecatPreferences.FORECAT_ANN_FILE, textField.getText());

		ForecatPreferences.init();
	}
}
