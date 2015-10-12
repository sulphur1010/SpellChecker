package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

public class SpellCorrector{

    JLabel jLabel;

    HashSet<String> hashSet, hashSetWrong, hashSetCorrected;
    ArrayList<String> arrayList;
    Scanner scanner;

    FileWriter fileWriterCorrected, fileWriterWrong;

    int startIndex = 0, letters = 0;

    String correctText = "";

    public Main() {
        initializer();

        JFrame jFrame = new JFrame("Spell Checker");
        jFrame.setSize(500, 400);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLayout(new BorderLayout());

        TextField textField = new TextField();
        Font font = new Font("SansSerif", Font.BOLD, 20);
        textField.setFont(font);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                StringTokenizer tokenizer = new StringTokenizer(textField.getText(), " ");
                String input = "";
                while (tokenizer.hasMoreTokens()) {
                    input = tokenizer.nextElement().toString();
                }
                searchDictionary(input);

                if (arrayList.size() > 0) {
                    jLabel.setText(input + " => " + arrayList.get(0));

                    hashSetWrong.add(input);
                    hashSetCorrected.add(arrayList.get(0));

                    jLabel.setText(correctText);

                    System.out.println(arrayList);
                    if (!correctText.contains(arrayList.get(0))) {
                        correctText += arrayList.get(0) + " ";
                    }
                }

                if (textField.getText().length() == 0) {
                    arrayList.clear();
                    jLabel.setText("");
                    correctText = "";
                }
            }
        });
        jFrame.getContentPane().add(textField, BorderLayout.NORTH);

        jLabel = new JLabel();
        jLabel.setFont(font);
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jFrame.getContentPane().add(jLabel);

        jFrame.setLocationRelativeTo(null);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    for(String s: hashSetWrong){
                        fileWriterWrong.append(s + "\n");
                    }
                    for(String s: hashSetCorrected){
                        fileWriterCorrected.append(s + "\n");
                    }
                    fileWriterCorrected.flush();
                    fileWriterWrong.flush();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        jFrame.setVisible(true);
    }

    public void initializer() {
        hashSet = new HashSet<>();
        hashSetWrong = new HashSet<>();
        hashSetCorrected = new HashSet<>();
        try {
            scanner = new Scanner(new File("dictionary"));
            while (scanner.hasNext()) {
                hashSet.add(scanner.next().toLowerCase());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        arrayList = new ArrayList<>();

        try {
            File file = new File("document-corrected.txt");
            File fileWrong = new File("document.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            if(!fileWrong.exists()){
                fileWrong.createNewFile();
            }
            fileWriterCorrected = new FileWriter(file);
            fileWriterWrong = new FileWriter(fileWrong);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchDictionary(String input) {

        for (int i = 0; i < input.length(); i++) {
            String subString = input.substring(startIndex, i + 1);

            if (hashSet.contains(subString)) {
                arrayList.clear();
                arrayList.add(subString);

                letters = subString.length();

                if (!(i == input.length() - 1)) {
                    searchDictionary(input.substring(letters));
                }
            }
        }
    }

    public static void main(String[] args) {
        new Main();

    }
}