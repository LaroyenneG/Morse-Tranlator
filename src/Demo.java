/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import morse.Morse;

import javax.swing.*;

/**
 * @author guillaume laroyenne
 */
public class Demo extends javax.swing.JPanel {

    private javax.swing.JScrollPane inputScrollPane;
    private javax.swing.JTextArea inputText;
    private morse.Morse morse;
    private javax.swing.JButton playButton;
    private javax.swing.JSlider speedSlider;
    private javax.swing.JSlider ampSlider;
    private javax.swing.JButton translateButton;
    private javax.swing.JScrollPane translateScrollPane;
    private javax.swing.JTextArea translateText;
    private AudioThread playerThread;


    public Demo() {
        initComponents();
        playerThread = new AudioThread(this);
        playerThread.start();
    }


    public static void main(String[] args) {

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


        java.awt.EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Morse");
            frame.add(new Demo());
            frame.setSize(300, 550);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }


    private void initComponents() {

        morse = new morse.Morse();
        translateButton = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        speedSlider = new javax.swing.JSlider();
        ampSlider = new javax.swing.JSlider();
        inputScrollPane = new javax.swing.JScrollPane();
        inputText = new javax.swing.JTextArea();
        translateScrollPane = new javax.swing.JScrollPane();
        translateText = new javax.swing.JTextArea();

        morse.setName("morse");
        morse.addTranslateListener(this::morseTranslate);

        translateButton.setText("Translate");
        translateButton.addActionListener(this::translateButtonActionPerformed);

        playButton.setText("Play");
        playButton.addActionListener(this::playButtonActionPerformed);
        playButton.setEnabled(false);

        speedSlider.setMaximum(100);
        speedSlider.setMinimum(10);
        speedSlider.setValue(20);

        ampSlider.setMaximum(100);
        ampSlider.setMinimum(0);
        ampSlider.setValue(50);

        inputText.setColumns(20);
        inputText.setRows(5);
        inputScrollPane.setViewportView(inputText);

        translateText.setColumns(20);
        translateText.setRows(5);
        translateText.setEditable(false);
        translateScrollPane.setViewportView(translateText);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(speedSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ampSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(translateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(morse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(inputScrollPane)
                                        .addComponent(translateScrollPane))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(morse, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                                .addComponent(translateScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(speedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ampSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(translateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
    }

    private void translateButtonActionPerformed(java.awt.event.ActionEvent evt) {

        Thread thread = new Thread(() -> {
            lockElements();
            morse.setText(inputText.getText());
            morse.setSpeed(speedSlider.getValue() / 10.0);
            morse.setAmplitude(ampSlider.getValue() / 100.0);
            morse.convert();
        });

        thread.start();
    }

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {
        playerThread.play();
    }

    private void morseTranslate(morse.TranslateEvent evt) {

        Morse morse = (Morse) evt.getSource();
        translateText.setText(morse.getTranslateText());

        unlockElements();
    }

    public Morse getMorse() {
        return morse;
    }

    public void lockElements() {
        playButton.setEnabled(false);
        translateButton.setEnabled(false);
        translateText.setEnabled(false);
        inputText.setEnabled(false);
        speedSlider.setEnabled(false);
        ampSlider.setEnabled(false);
    }

    public void unlockElements() {
        playButton.setEnabled(true);
        translateButton.setEnabled(true);
        translateText.setEnabled(true);
        inputText.setEnabled(true);
        speedSlider.setEnabled(true);
        ampSlider.setEnabled(true);
    }
}
