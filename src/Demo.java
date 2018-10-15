/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import morse.Morse;

import javax.swing.*;
import java.awt.*;

/**
 * @author Guillaume Laroyenne
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
    private javax.swing.JLabel speedLabel;
    private javax.swing.JLabel ampLabel;

    private AudioThread playerThread;


    public Demo() {
        initComponents();
        playerThread = new AudioThread(this); // Création du thread de lecture audio
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
            frame.setSize(300, 600);
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
        ampLabel = new javax.swing.JLabel();
        speedLabel = new javax.swing.JLabel();

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
        translateText.setFont(new Font("SansSerif", Font.BOLD, 15));
        translateScrollPane.setViewportView(translateText);

        speedLabel.setText("Vitesse :");
        speedLabel.setFont(new Font("Arial", Font.ITALIC, 10));

        ampLabel.setText("Amplitude :");
        ampLabel.setFont(new Font("Arial", Font.ITALIC, 10));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(speedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(speedSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ampLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                                .addComponent(speedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(speedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ampLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            lockElements(); // Durant la phase de traduction on verrouille les entrées de la fenêtre.
            // Les composants seront automatiquement déverrouillés lorsque l'événement sera provoqué

            morse.setText(inputText.getText());
            morse.setSpeed((double) speedSlider.getMaximum() / speedSlider.getValue());
            morse.setAmplitude((double) ampSlider.getValue() / ampSlider.getMaximum());

            morse.convert();
        });

        thread.start();

        /* Ce thread n'est pas indispensable. Mais il permet de ne pas bloquer le bouton durant la phase de construction
         * du signal. Sans ce thread l'action de déverrouillage des composants provoqués par l'événement de traduction
         * ne pourrait pas être visible.
         */
    }

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {
        playerThread.playSignal();
    }


    /* Fonction appelée par l’événement de traduction */
    private void morseTranslate(morse.TranslateEvent evt) {

        Morse morse = (Morse) evt.getSource();

        translateText.setText(morse.getTranslateText()); // On affiche le texte traduit

        unlockElements(); // Le traitement est terminé, on rend la main à l'utilisateur
    }

    public Morse getMorse() {
        return morse;
    }

    /* Rends les composants de la fenêtre accessible à l'utilisateur */
    public void lockElements() {
        playButton.setEnabled(false);
        translateButton.setEnabled(false);
        translateText.setEnabled(false);
        inputText.setEnabled(false);
        speedSlider.setEnabled(false);
        ampSlider.setEnabled(false);
    }

    /* Verrouille les composants de la fenêtre à l'utilisateur */
    public void unlockElements() {
        playButton.setEnabled(true);
        translateButton.setEnabled(true);
        translateText.setEnabled(true);
        inputText.setEnabled(true);
        speedSlider.setEnabled(true);
        ampSlider.setEnabled(true);
    }
}
