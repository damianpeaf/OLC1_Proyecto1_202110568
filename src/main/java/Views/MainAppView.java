
package Views;

import OLCCompiler.OLCCompiler;
import OLCCompiler.Exception.EvaluationException;
import OLCCompiler.Exception.ParseException;
import OLCCompiler.Utils.ReporthPaths;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class MainAppView extends javax.swing.JFrame {

    private String currentFilePath = null;
    private boolean isSaved = true;
    private OLCCompiler compiler = new OLCCompiler();
    private String selectedRegex = null;


    public MainAppView() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        openFileChooser = new javax.swing.JFileChooser();
        saveFileChooser = new javax.swing.JFileChooser();
        codeScrollPane = new javax.swing.JScrollPane();
        codeEditor = new javax.swing.JTextArea();
        reportsTabbedPane = new javax.swing.JTabbedPane();
        consoleScrollPane = new javax.swing.JScrollPane();
        consoleArea = new javax.swing.JTextArea();
        editorLabel = new javax.swing.JLabel();
        consoleLabel = new javax.swing.JLabel();
        evaluateStringButton = new javax.swing.JToggleButton();
        generateAutomataButton = new javax.swing.JToggleButton();
        regexComboBox = new javax.swing.JComboBox<>();
        fileMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        fileMenuOpenItem = new javax.swing.JMenuItem();
        fileMenuNewAsItem = new javax.swing.JMenuItem();
        fileMenuSaveItem = new javax.swing.JMenuItem();
        fileMenuSaveAsItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ExRegan USAC");
        setBackground(new java.awt.Color(204, 204, 255));

        regexComboBox.setEnabled(false);

        openFileChooser.setApproveButtonText("Abrir");
        openFileChooser.setDialogTitle("Seleccionar archivo");
        openFileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
        FileFilter filter = new FileFilter() {
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".olc") || file.isDirectory();
            }

            public String getDescription() {
                return "Archivos OLC (*.olc)";
            }
        };

        // Aplicar el filtro de archivos al JFileChooser
        openFileChooser.setFileFilter(filter);

        saveFileChooser.setApproveButtonText("Guardar");
        saveFileChooser.setDialogTitle("Guardar archivo");
        saveFileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
        saveFileChooser.setFileFilter(filter);

        codeEditor.setColumns(25);
        codeEditor.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        codeEditor.setRows(5);
        codeEditor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codeEditorKeyPressed(evt);
            }
        });

        UndoManager undoManager = new UndoManager();
        codeEditor.getDocument().addUndoableEditListener(undoManager);
        KeyStroke undoKeyStroke = KeyStroke.getKeyStroke("control Z");

        Action undoAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }
        };

        codeEditor.getInputMap().put(undoKeyStroke, "undo");
        codeEditor.getActionMap().put("undo", undoAction);

        codeScrollPane.setViewportView(codeEditor);

        consoleArea.setEditable(false);
        consoleArea.setColumns(20);
        consoleArea.setRows(5);
        consoleScrollPane.setViewportView(consoleArea);

        editorLabel.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        editorLabel.setText("Editor");

        consoleLabel.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        consoleLabel.setText("Consola");

        evaluateStringButton.setBackground(new java.awt.Color(101, 143, 189));
        evaluateStringButton.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        evaluateStringButton.setText("Analizar entrada");
        evaluateStringButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateAutomataBtnActionPerformed(evt);
            }
        });

        generateAutomataButton.setBackground(new java.awt.Color(102, 189, 120));
        generateAutomataButton.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        generateAutomataButton.setText("Generar Automata");
        generateAutomataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateAutomataBtn1ActionPerformed(evt);
            }
        });

        regexComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regexComboBoxActionPerformed(evt);
            }
        });

        fileMenu.setText("Archivo");

        // Nuevo archivo
        fileMenuNewAsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        fileMenuNewAsItem.setText("Nuevo");
        fileMenuNewAsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuNewItemActionPerformed(evt);
            }
        });
        fileMenu.add(fileMenuNewAsItem);

        // Abrir archivo
        fileMenuOpenItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        fileMenuOpenItem.setText("Abrir");
        fileMenuOpenItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuOpenItemActionPerformed(evt);
            }
        });
        fileMenu.add(fileMenuOpenItem);

        fileMenuSaveItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        fileMenuSaveItem.setText("Guardar");
        fileMenuSaveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuSaveItemActionPerformed(evt);
            }
        });
        fileMenu.add(fileMenuSaveItem);

        fileMenuSaveAsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        fileMenuSaveAsItem.setText("Guardar Como");
        fileMenuSaveAsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuSaveAsItemActionPerformed(evt);
            }
        });
        fileMenu.add(fileMenuSaveAsItem);

        fileMenuBar.add(fileMenu);

        setJMenuBar(fileMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(consoleLabel)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(editorLabel)
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(consoleScrollPane)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(codeScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
                                                                .addComponent(reportsTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(26, 26, 26))))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addComponent(generateAutomataButton, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(107, 107, 107)
                                .addComponent(evaluateStringButton, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(regexComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(167, 167, 167))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(editorLabel)
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(reportsTabbedPane)
                                        .addComponent(codeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE))
                                .addGap(42, 42, 42)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(evaluateStringButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(generateAutomataButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(regexComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                                .addComponent(consoleLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(consoleScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38))
        );

        pack();
    }// </editor-fold>



    private void fileMenuOpenItemActionPerformed(java.awt.event.ActionEvent evt) {
        if(!this.isSaved) {
            int option = JOptionPane.showConfirmDialog(this, "Tienes cambios sin guardar, ¿Deseas guardar los cambios?", "Guardar", JOptionPane.YES_NO_CANCEL_OPTION);
            if(option == JOptionPane.YES_OPTION) {
                this.fileMenuSaveItemActionPerformed(evt);
            }else if(option == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }

        int result = openFileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = openFileChooser.getSelectedFile();
            try {
                this.currentFilePath = selectedFile.getAbsolutePath();
                String fileContent = new String(Files.readAllBytes(selectedFile.toPath()));
                codeEditor.setText(fileContent);
                this.isSaved = true;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al abrir el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void fileMenuNewItemActionPerformed(java.awt.event.ActionEvent evt){
        // New file
        if(!this.isSaved) {
            int option = JOptionPane.showConfirmDialog(this, "Tienes cambios sin guardar, ¿Deseas guardar los cambios?", "Guardar", JOptionPane.YES_NO_CANCEL_OPTION);
            if(option == JOptionPane.YES_OPTION) {
                this.fileMenuSaveItemActionPerformed(evt);
            }else if(option == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        this.currentFilePath = null;
        this.codeEditor.setText("");
    }

    private void fileMenuSaveItemActionPerformed(java.awt.event.ActionEvent evt) {

        if(this.currentFilePath == null) {
            this.fileMenuSaveAsItemActionPerformed(evt);
            return;
        }

        File file = new File(this.currentFilePath);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(codeEditor.getText());
            fileWriter.close();
            this.isSaved = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fileMenuSaveAsItemActionPerformed(java.awt.event.ActionEvent evt) {
        int result = saveFileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = saveFileChooser.getSelectedFile();
            try {
                this.currentFilePath = selectedFile.getAbsolutePath();
                FileWriter fileWriter = new FileWriter(selectedFile);
                fileWriter.write(codeEditor.getText());
                fileWriter.close();
                this.isSaved = true;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void generateAutomataBtn1ActionPerformed(java.awt.event.ActionEvent evt) {

        try {
            // Get file name
            String filename = null;

            if(this.currentFilePath != null) {
                filename = this.currentFilePath.substring(this.currentFilePath.lastIndexOf("\\") + 1);
            }

            String message = compiler.generateAutomatas(codeEditor.getText(), filename);
            this.consoleArea.setText(message);

            //Items for regex combo box
            String[] regex = compiler.getRegexNames();
            regexComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(regex));
            regexComboBox.setEnabled(true);

            // Panes for tabbed pane
            selectedRegex = regex[0];
            addReportTabs(selectedRegex);

        } catch (ParseException e) {
            this.consoleArea.setText(e.getMessage());
            resetReportComponents();
        }
    }

    // evaluate string button
    private void generateAutomataBtnActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String filename = null;

            if(this.currentFilePath != null) {
                filename = this.currentFilePath.substring(this.currentFilePath.lastIndexOf("\\") + 1);
            }

            String message = compiler.evalEntry(codeEditor.getText(), filename);
            this.consoleArea.setText(message);
        } catch (EvaluationException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            this.consoleArea.setText(e.getMessage());
            resetReportComponents();
        }
    }

    private void codeEditorKeyPressed(java.awt.event.KeyEvent evt) {
        this.isSaved = false;
    }

    private void regexComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        selectedRegex = (String) regexComboBox.getSelectedItem();
        addReportTabs(selectedRegex);
    }


    private void addReportTabs(String regexName) {
        reportsTabbedPane.removeAll();

        ReporthPaths report = compiler.getReportPaths(regexName);
        JScrollPane dfaPanel = new ReportPane(report.dfa);
        JScrollPane dfnaPanel = new ReportPane(report.ndfa);
        JScrollPane treePanel = new ReportPane(report.tree);
        JScrollPane nextTablePanel = new ReportPane(report.nextTable);
        JScrollPane transitionTablePanel = new ReportPane(report.transitionsTable);

        reportsTabbedPane.addTab("Árbol", treePanel);
        reportsTabbedPane.addTab("Tabla de siguientes", nextTablePanel);
        reportsTabbedPane.addTab("Tabla de transiciones", transitionTablePanel);
        reportsTabbedPane.addTab("AFD", dfaPanel);
        reportsTabbedPane.addTab("AFND", dfnaPanel);

        reportsTabbedPane.setSelectedComponent(treePanel);
    }

    private void resetReportComponents(){
        reportsTabbedPane.removeAll();
        regexComboBox.setEnabled(false);
        regexComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No hay reportes" }));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainAppView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainAppView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainAppView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainAppView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainAppView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JTextArea codeEditor;
    private javax.swing.JScrollPane codeScrollPane;
    private javax.swing.JTextArea consoleArea;
    private javax.swing.JLabel consoleLabel;
    private javax.swing.JScrollPane consoleScrollPane;
    private javax.swing.JLabel editorLabel;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuBar fileMenuBar;
    private javax.swing.JMenuItem fileMenuOpenItem;
    private javax.swing.JMenuItem fileMenuSaveAsItem;
    private javax.swing.JMenuItem fileMenuNewAsItem;
    private javax.swing.JMenuItem fileMenuSaveItem;
    private javax.swing.JToggleButton evaluateStringButton;
    private javax.swing.JToggleButton generateAutomataButton;
    private javax.swing.JTabbedPane reportsTabbedPane;
    private javax.swing.JFileChooser openFileChooser;
    private javax.swing.JFileChooser saveFileChooser;
    private javax.swing.JComboBox<String> regexComboBox;

    // End of variables declaration
}
