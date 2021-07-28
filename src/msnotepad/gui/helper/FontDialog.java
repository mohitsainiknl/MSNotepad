/*
 * Copyright (c) 2021 Mohit Saini, Under MIT License. Use is subject to license terms.
 * 
 */

package msnotepad.gui.helper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import msnotepad.init.InitialValues;

import javax.swing.GroupLayout;

/**
 * FontDialog class handle the font choosing function of the MSNotepad,
 * this Dialog uses the GroupLayout as the LayoutManager.
 */
public class FontDialog extends JDialog implements ListSelectionListener {
    private static final int foregroundTextSize = 13;
    private String fontNames[], fontStyleNames[], fontSizeNames[];
    private Parameter fontName, fontStyle, fontSize;

    private JLabel sampleLabel;
    private JPanel samplePanel;
    private JViewport sampleViewport;

    private JButton okButton, cancelButton;

    /**
     * FontDialog Constructor get parent container and some information
     * to initialize the dialog.
     */
    public FontDialog(JFrame frame, String title, boolean mobality) {
        super(frame, title , mobality);

        initializeDialog();
        prepareLayout();

        setSize(440, 445);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * initalizeFindDialog method is help to do the initial work and setup
     * the Dialog and their components.
     */
    private void initializeDialog() {
        fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontStyleNames = new String[] {"Roman", "Bold", "Italic", "Bold Italic"};
        fontSizeNames = new String[] {"8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72"};

        fontName = new Parameter("Font", fontNames);
        fontStyle = new Parameter("Style", fontStyleNames);
        fontSize = new Parameter("Size", fontSizeNames);

        fontName.setScrollPaneSize(220, 120);
        fontStyle.setScrollPaneSize(140, 120);
        fontSize.setScrollPaneSize(30, 120);

        {
            Font font = InitialValues.getEditorFont();
            fontName.setSelectedValue(font.getFamily(), true);
            fontStyle.setSelectedValue(getStyleName(font.getStyle()), true);
            {
                fontSize.list.setSelectedValue(font.getSize() + "", true);
                fontSize.field.setText(font.getSize() + "");
                fontSize.field.selectAll();
            }
        }

        sampleLabel = new JLabel("AaBbYyZz") {
            @Override
            public void setFont(Font font) {
                String family = font.getFamily();
                int style = font.getStyle();
                int size = font.getSize();
                super.setFont(new Font(family, style, size + 5));
            }
        };
        sampleLabel.setHorizontalAlignment(JLabel.CENTER);
        sampleLabel.setPreferredSize(new Dimension(1000, 70));
        sampleViewport = new JViewport();
        sampleViewport.setView(sampleLabel);
        samplePanel = new JPanel(new BorderLayout());
        samplePanel.setBorder(new TitledBorder(new LineBorder(new Color(220, 220, 220), 1), "Sample"));
        samplePanel.setPreferredSize(new Dimension((int)samplePanel.getSize().getWidth(), 70));
        {
            JPanel panel = new JPanel();
            panel.add(sampleViewport);
            samplePanel.add(panel, BorderLayout.CENTER);
        }
        updateSample();

        fontName.field.addFocusListener(fontName);
        fontStyle.field.addFocusListener(fontStyle);

        fontName.field.addCaretListener(fontName);
        fontStyle.field.addCaretListener(fontStyle);
        fontSize.field.addCaretListener(fontSize);
        
        fontName.list.addListSelectionListener(this);
        fontStyle.list.addListSelectionListener(this);
        fontSize.list.addListSelectionListener(this);

        fontName.list.setCellRenderer(new FontCellRenderer(true));
        fontStyle.list.setCellRenderer(new FontCellRenderer(false));

        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Font font = sampleLabel.getFont();
                InitialValues.setEditorFont(new Font(font.getFamily(), font.getStyle(), font.getSize() - 5));
                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }


    /**
     * prepareLayout method is help to set the components with
     * the help of the layout, here we have used the
     * GroupLayout as the LayoutManager.
     */
    private void prepareLayout() {
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(false);
        layout.setAutoCreateContainerGaps(false);

        layout.setHorizontalGroup(layout.createSequentialGroup().addGap(13)
            .addGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(fontName.label)
                .addComponent(fontName.field)
                .addComponent(fontName.scrollPane)).addGap(15)
            .addGroup(layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addGap(2)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(fontStyle.label)
                        .addComponent(fontStyle.field)
                        .addComponent(fontStyle.scrollPane)).addGap(17)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(fontSize.label)
                        .addComponent(fontSize.field)
                        .addComponent(fontSize.scrollPane)).addGap(3))
                .addComponent(samplePanel)
                .addGroup(layout.createSequentialGroup().addGap(70)
                    .addComponent(okButton).addGap(6)
                    .addComponent(cancelButton)
                )
            ).addGap(13)
        );
        layout.linkSize(okButton, cancelButton);

        layout.setVerticalGroup(layout.createSequentialGroup().addGap(13)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(fontName.label)
                    .addComponent(fontStyle.label)
                    .addComponent(fontSize.label)).addGap(3)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE).addGap(0)
                    .addComponent(fontName.field)
                    .addComponent(fontStyle.field)
                    .addComponent(fontSize.field))
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(fontName.scrollPane)
                    .addComponent(fontStyle.scrollPane)
                    .addComponent(fontSize.scrollPane)).addGap(13)
            .addComponent(samplePanel).addGap(33)
            .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(okButton)
                .addComponent(cancelButton))
            ).addGap(13)
        );
    }


    /**
     * updateSample method update the Sample Label in the
     * FontDialog.
     */
    private void updateSample() {
        if(fontName.list.getSelectedValue() != null && fontStyle.list.getSelectedValue() != null ) {
            String name = fontName.list.getSelectedValue().toString();
            int size = 0;
            if(fontSize.list.getSelectedValue() != null) {
                size = Integer.parseInt(fontSize.list.getSelectedValue().toString());
            }
            else {
                size = Integer.parseInt(fontSize.field.getText());
            }
            String styleName = fontStyle.list.getSelectedValue().toString();
            int style = getStyleNum(styleName);

            sampleLabel.setFont(new Font(name, style, size));
            sampleLabel.repaint();
        }
    }

    /**
     * getStyleNum method convert the Style name into it's int value.
     * @param styleName the name of the style.
     * @return the int value of the Style.
     */
    private int getStyleNum(String styleName) {
        if(styleName == "Roman")
            return Font.PLAIN;
        else if(styleName == "Bold")
            return Font.BOLD;
        else if(styleName == "Italic")
            return Font.ITALIC;
        else if(styleName == "Bold Italic")
            return Font.BOLD + Font.ITALIC;
        else
            return Font.PLAIN;
    }

    /**
     * getStyleNum method help to convert the style int value
     * into the equivalent name of the Style.
     * @param style int value of the style.
     * @return the Name of the Style.
     */
    private String getStyleName(int style) {
        if(style == Font.PLAIN)
            return "Roman";
        else if(style == Font.BOLD)
            return "Bold";
        else if(style == Font.ITALIC)
            return "Italic";
        else if(style == Font.BOLD + Font.ITALIC)
            return "Bold Italic";
        else
            return "Roman";
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getSource() instanceof JList<?>) {
            JList<?> list = (JList<?>) e.getSource();
            String value = list.getSelectedValue().toString();

            if(e.getSource() == fontName.list) {
                if(sampleLabel.getFont().getFamily() != value.toString()) {
                    if(fontName.isCaretUpdated == false)
                        fontName.field.setText(value.toString());
                    updateSample();
                }
                fontName.field.requestFocus();
                fontName.field.selectAll();
            }
            else if(e.getSource() == fontStyle.list) {
                if(sampleLabel.getFont().getStyle() != getStyleNum(value.toString())) {
                    if(fontName.isCaretUpdated == false)
                        fontStyle.field.setText(value.toString());
                    updateSample();
                }
                fontStyle.field.requestFocus();
                fontStyle.field.selectAll();
            }
            else if(e.getSource() == fontSize.list) {
                if((sampleLabel.getFont().getSize() - 5)!= Integer.parseInt(value.toString())) {
                    if(fontName.isCaretUpdated == false)
                        fontSize.field.setText(value.toString());
                    updateSample();
                }
                fontSize.field.requestFocus();
                fontSize.field.selectAll();
            }
        }
    }

    //========================== Inner Class Parameter =============================

    private class Parameter implements FocusListener, CaretListener {
        JLabel label;
        JTextField field;
        JList<String> list;
        JScrollPane scrollPane;
        String name;
        String[] listArray;

        private Parameter(String name, String[] listArray) {
            this.name = name;
            this.listArray = listArray;

            //------- Initialization --------------

            label = new JLabel(name + " :");
            field = new JTextField(10);
            list = new JList<String>(listArray);
            scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            //------- Setting Values ---------------

            label.setLabelFor(field);
            scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(18, getHeight()));
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setFont(new Font("", Font.PLAIN, foregroundTextSize));
        }

        public void setSelectedValue(String selectionName, boolean scroll) {
            list.setSelectedValue(selectionName, scroll);
            field.setText(list.getSelectedValue().toString());
            field.selectAll();
        }

        public void setScrollPaneSize(int width, int height) {
            Dimension size = new Dimension(width, height);
            scrollPane.setPreferredSize(size);
        }

        @Override
        public void focusGained(FocusEvent e) {
            field.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            String text = list.getSelectedValue();
            field.setText(text);
        }

        private String tempText = "";
        private boolean isCaretUpdated = false;
        @Override
        public void caretUpdate(CaretEvent e) {
            String text = field.getText().toLowerCase();
            if(tempText != text) {
                boolean isInList = false;
                for (String string : listArray) {
                    string = string.toLowerCase();
                    if(string.equals(text)) {
                        isInList = true;
                    }
                }
                if(isInList == true) {
                    isCaretUpdated = true;
                    list.setSelectedValue(field.getText(), true);
                }
                else if(name.equals("Size")) {
                    try {
                        String name = fontName.list.getSelectedValue().toString();
                        String styleName = fontStyle.list.getSelectedValue().toString();
                        int style = getStyleNum(styleName);
                        int size = 0;
                        size = Integer.parseInt(text);
                        sampleLabel.setFont(new Font(name, style, size));
                        sampleLabel.repaint();
                    } catch (NumberFormatException ee) {
                        field.setText("");
                        System.out.println("NumberFormatException!");
                    }
                }
                tempText = text;
            }
            isCaretUpdated = false;
        }
    }

    //========================== Inner Class FontCellRenderer =============================

    private class FontCellRenderer<E> implements ListCellRenderer<E> {
        private boolean isFontName = true;

        private FontCellRenderer(boolean isFontName) {
            this.isFontName = isFontName;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            return new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    String text = (String) value;
                    Font font;
                    if(isFontName == true) {
                        font = new Font(text, Font.PLAIN, foregroundTextSize + 2);
                    }
                    else {
                        font = new Font("", getStyleNum(text), foregroundTextSize + 2);
                    }
                    FontMetrics fm = g.getFontMetrics(font);
                    g.setColor(isSelected ? list.getSelectionBackground() : list.getBackground());
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(isSelected ? list.getSelectionForeground() : list.getForeground());
                    g.setFont(font);
                    g.drawString(text, 0, fm.getAscent());
                }

                @Override
                public Dimension getPreferredSize() {
                    String fontName = (String) value;
                    Font font = new Font(fontName, Font.PLAIN, foregroundTextSize + 2);
                    Graphics g = getGraphics();
                    FontMetrics fm = g.getFontMetrics(font);
                    return new Dimension(fm.stringWidth(fontName), fm.getHeight());
                }
            };
        }
    }
}
