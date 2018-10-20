/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import morse.translator.MorseTranslator;
import morse.translator.TranslateEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Guillaume Laroyenne
 */
public class Demo extends javax.swing.JPanel {

    private javax.swing.JScrollPane inputScrollPane;
    private javax.swing.JTextArea inputText;
    private MorseTranslator morseTranslator;
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

        autoLockElements();
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
            JFrame frame = new JFrame("Morse Translator");
            frame.add(new Demo());
            frame.setSize(300, 600);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }


    private void initComponents() {

        morseTranslator = new MorseTranslator();
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

        morseTranslator.setName("morse");
        morseTranslator.addTranslateListener(this::morseTranslatorTranslate);

        translateButton.setText("Translate");
        translateButton.addActionListener(this::translateButtonActionPerformed);

        playButton.setText("Play");
        playButton.addActionListener(this::playButtonActionPerformed);

        speedSlider.setMaximum(100);
        speedSlider.setMinimum(10);
        speedSlider.setValue(20);
        speedSlider.addChangeListener(this::speedStateChanged);

        ampSlider.setMaximum(100);
        ampSlider.setMinimum(0);
        ampSlider.setValue(50);
        ampSlider.addChangeListener(this::ampStateChanged);

        inputText.setColumns(20);
        inputText.setRows(5);
        inputScrollPane.setViewportView(inputText);
        inputText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
              inputTextKeyPressed(e);
            }
        });


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
                                        .addComponent(morseTranslator, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(inputScrollPane)
                                        .addComponent(translateScrollPane))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(morseTranslator, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void inputTextKeyPressed(java.awt.event.KeyEvent evt) {

        if(!morseTranslator.getTranslateText().equals(inputText.getText())) {
            morseTranslator.setText(inputText.getText());
            autoLockElements();
        }
    }

    private void translateButtonActionPerformed(java.awt.event.ActionEvent evt) {

        morseTranslator.convert();
        autoLockElements();
    }

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {

        playerThread.playSignal();
        autoLockElements();
    }

    public void speedStateChanged(ChangeEvent ce) {

        morseTranslator.setSpeed((double) speedSlider.getMaximum() / speedSlider.getValue());
        autoLockElements();
    }

    public void ampStateChanged(ChangeEvent ce) {

        morseTranslator.setAmplitude((double) ampSlider.getValue() / ampSlider.getMaximum());
        autoLockElements();
    }

    /* Fonction appelée par l’événement de traduction */
    private void morseTranslatorTranslate(TranslateEvent evt) {

        MorseTranslator morseTranslator = (MorseTranslator) evt.getSource();

        translateText.setText(morseTranslator.getTranslateText()); // On affiche le texte traduit
        autoLockElements();
    }

    public MorseTranslator getMorseTranslator() {
        return morseTranslator;
    }


    /* Rends les composants de la fenêtre accessible à l'utilisateur en fonction du status du MorseTranslator */
    public void autoLockElements() {


        switch (morseTranslator.getState()) {

            case Waiting:
                speedSlider.setEnabled(false);
                ampSlider.setEnabled(false);
                translateText.setEnabled(false);
                inputText.setEnabled(true);
                playButton.setEnabled(false);
                translateButton.setEnabled(false);
                break;

            case Reading:
                speedSlider.setEnabled(false);
                ampSlider.setEnabled(false);
                translateText.setEnabled(false);
                inputText.setEnabled(false);
                playButton.setEnabled(false);
                translateButton.setEnabled(false);
                break;

            case ReadyToTranslate:
                speedSlider.setEnabled(true);
                ampSlider.setEnabled(true);
                translateText.setEnabled(false);
                inputText.setEnabled(true);
                playButton.setEnabled(false);
                translateButton.setEnabled(true);
                break;

            case Translated:
                speedSlider.setEnabled(true);
                ampSlider.setEnabled(true);
                translateText.setEnabled(true);
                inputText.setEnabled(true);
                playButton.setEnabled(true);
                translateButton.setEnabled(false);
                break;

            case Translate:
                speedSlider.setEnabled(false);
                ampSlider.setEnabled(false);
                translateText.setEnabled(false);
                inputText.setEnabled(false);
                playButton.setEnabled(false);
                translateButton.setEnabled(false);
                break;
        }
    }
}
