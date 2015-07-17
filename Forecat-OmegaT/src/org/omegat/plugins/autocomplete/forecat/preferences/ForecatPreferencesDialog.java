package org.omegat.plugins.autocomplete.forecat.preferences;

import java.awt.FlowLayout;
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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.omegat.core.Core;
import org.omegat.gui.exttrans.IMachineTranslation;
import org.omegat.gui.exttrans.MachineTranslateTextArea;
import org.omegat.util.Preferences;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ForecatPreferencesDialog extends JDialog {

	private static final long serialVersionUID = -3684055356687934832L;
	private final JPanel contentPanel = new JPanel();
	private JButton okButton;
	private JButton cancelButton;
	private JTabbedPane tabbedPane;
	private JPanel panel_internal;

	private DualList dualList;
	private JPanel panel_preferences;
	private JRadioButton rdbtn_local;
	private JRadioButton rdbtn_api;
	private JLabel lblForecatApiUrl;
	private JTextField textfield_api;

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
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 1.0, 1.0, 0.0,
				Double.MIN_VALUE };
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
		tabbedPane.addTab("New tab", null, panel_preferences, null);
		panel_preferences
				.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		rdbtn_local = new JRadioButton("Use local forecat with OmegaT MT");
		panel_preferences.add(rdbtn_local, "2, 2");

		rdbtn_api = new JRadioButton("Use forecat API interface");
		panel_preferences.add(rdbtn_api, "2, 4");

		lblForecatApiUrl = new JLabel("Forecat API URL");
		panel_preferences.add(lblForecatApiUrl, "2, 6, right, default");

		textfield_api = new JTextField();
		panel_preferences.add(textfield_api, "4, 6, fill, default");
		textfield_api.setColumns(10);

		panel_internal = new JPanel();
		tabbedPane.addTab("OmegaT MT", null, panel_internal, null);
		panel_internal
				.setLayout(new BoxLayout(panel_internal, BoxLayout.X_AXIS));

		dualList = new DualList();
		panel_internal.add(dualList);

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtn_api);
		group.add(rdbtn_local);

		ActionListener rbActionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				manageRdBtn();
			}
		};
		
		rdbtn_api.addActionListener(rbActionListener);
		rdbtn_local.addActionListener(rbActionListener);

		init();
	}

	private void init() {

		if (Preferences.getPreference(ForecatPreferences.FORECAT_USE_API)
				.equals("false")) {
			rdbtn_local.setSelected(true);
		} else {
			rdbtn_api.setSelected(true);
		}

		textfield_api.setText(Preferences
				.getPreference(ForecatPreferences.FORECAT_API_URL));

		getOmegaTMT();
		manageRdBtn();
	}

	private void manageRdBtn() {
		if (rdbtn_api.isSelected()) {
			textfield_api.setEnabled(true);
		} else if (rdbtn_local.isSelected()) {
			textfield_api.setEnabled(false);
		}
	}

	protected void getOmegaTMT() {
		IMachineTranslation mt[];
		MachineTranslateTextArea mtta = Core.getMachineTranslatePane();
		Field f;
		String enabled = Preferences
				.getPreference(ForecatPreferences.FORECAT_ENABLED_OMEGAT_ENGINES);
		String ignore = Preferences
				.getPreference(ForecatPreferences.FORECAT_IGNORE_OMEGAT_ENGINES);

		try {
			f = MachineTranslateTextArea.class.getDeclaredField("translators");
			f.setAccessible(true);
			mt = (IMachineTranslation[]) f.get(mtta);

			Method getNameMethod = IMachineTranslation.class
					.getDeclaredMethod("getName");
			getNameMethod.setAccessible(true);

			for (IMachineTranslation m : mt) {
				String nameMethod = getNameMethod.invoke(m).toString();
				if (!ignore.contains(":" + nameMethod.replace(":", ";") + ":")) {
					if (enabled.contains(":" + nameMethod.replace(":", ";")
							+ ":")) {
						dualList.addRightItem(nameMethod);
					} else {
						dualList.addLeftItem(nameMethod);
					}
				}
				// System.out.println(getNameMethod.invoke(m));
			}
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException
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

		for (String s : dualList.getRightItems()) {
			aux.append(s.replace(":", ";"));
			aux.append(":");
		}

		Preferences.setPreference(
				ForecatPreferences.FORECAT_ENABLED_OMEGAT_ENGINES,
				aux.toString());

		if (rdbtn_api.isSelected()) {
			Preferences.setPreference(ForecatPreferences.FORECAT_USE_API,
					"true");
		} else {
			Preferences.setPreference(ForecatPreferences.FORECAT_USE_API,
					"false");
		}

		Preferences.setPreference(ForecatPreferences.FORECAT_API_URL,
				textfield_api.getText());

		ForecatPreferences.init();
	}
}
