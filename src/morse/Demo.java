
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package morse;


import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Guillaume LAROYENNE
 */
public class Demo extends JFrame {

    private JButton translateButton;
    private JButton playButton;
    private JTextField inputText;
    private JTextField translateText;
    private Morse morse;

    /**
     * Creates new form Demo
     */
    public Demo() {
        initComponents();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Demo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }


        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Demo().setVisible(true);
            }
        });
    }


    private void initComponents() {

        morse = new Morse();
        translateButton = new JButton();
        playButton = new JButton();
        inputText = new JTextField();
        translateText = new JTextField();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        morse.addTranslateListener(this::morseTranslate);

        translateButton.setText("Translate");
        translateButton.addActionListener(this::translateButtonActionPerformed);

        playButton.setLabel("Play");
        playButton.addActionListener(this::playButtonActionPerformed);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(inputText)
                                        .addComponent(morse, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(translateButton, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(playButton, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(translateText)
                                        .addContainerGap()))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(morse, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 140, Short.MAX_VALUE)
                                .addComponent(inputText, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(translateButton, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(playButton, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addContainerGap(230, Short.MAX_VALUE)
                                        .addComponent(translateText, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                                        .addGap(205, 205, 205)))
        );

        pack();
    }

    private void morseTranslate(TranslateEvent evt) {

        Morse morse = (Morse) evt.getSource();
        translateText.setText(morse.getTranslateText());
    }

    private void translateButtonActionPerformed(ActionEvent evt) {

        morse.setText(inputText.getText());
        morse.convert();
    }

    private void playButtonActionPerformed(ActionEvent evt) {


        final Morse MORSE = morse;
        final JButton BUTTON = (JButton) evt.getSource();

        BUTTON.setEnabled(false);


        Thread thread = new Thread(() -> {
            MORSE.play();
            BUTTON.setEnabled(true);
        });
        thread.start();
    }
}
