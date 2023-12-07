import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Stack;

public class AINotepad extends JFrame {
        private JTextArea textArea;
        private Stack<String> undoStack;
        private Stack<String> redoStack;

        public AINotepad() {
            setTitle("Advanced Notepad");
            setSize(600, 400);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            JLabel jLabel1 = new JLabel();
            JPanel jPanel1=new JPanel();
            jPanel1 = new javax.swing.JPanel();
            jLabel1 = new javax.swing.JLabel();
            textArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(textArea);
            setTitle(" A I Notepad");
            setSize(600, 400);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            // Create a new JLabel with specified text and font
            jLabel1 = new JLabel("Capture Your Thoughts here");
            jLabel1.setFont(new Font("Times New Roman", Font.PLAIN, 16));


            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");
            JMenuItem newFileItem = new JMenuItem("New File");
            JMenuItem openFileItem = new JMenuItem("Open Existing File");
            JMenuItem saveItem = new JMenuItem("Save");

            jPanel1.add(jLabel1);
            newFileItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    newFile();
                }
            });

            openFileItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openFile();
                }
            });

            saveItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveFile();
                }
            });

            fileMenu.add(newFileItem);
            fileMenu.add(openFileItem);
            fileMenu.add(saveItem);

            JMenu editMenu = new JMenu("Edit");
            JMenuItem undoItem = new JMenuItem("Undo");
            JMenuItem redoItem = new JMenuItem("Redo");
            JMenuItem findItem = new JMenuItem("Find");
            JMenuItem replaceItem = new JMenuItem("Replace");

            undoItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    undo();
                }
            });

            redoItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    redo();
                }
            });

            findItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    find();
                }
            });

            replaceItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    replace();
                }
            });

            editMenu.add(undoItem);
            editMenu.add(redoItem);
            editMenu.add(findItem);
            editMenu.add(replaceItem);

            menuBar.add(fileMenu);
            menuBar.add(editMenu);

            undoStack = new Stack<>();
            redoStack = new Stack<>();

            setJMenuBar(menuBar);
            add(scrollPane, BorderLayout.CENTER);
        }

        private void newFile() {
            textArea.setText("");
            undoStack.clear();
            redoStack.clear();
        }

        private void openFile() {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    textArea.setText(content.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error opening file", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void saveFile() {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try (PrintWriter writer = new PrintWriter(new FileWriter(fileToSave))) {
                    writer.write(textArea.getText());
                    JOptionPane.showMessageDialog(this, "File saved successfully!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }


        private void undo() {
            String currentText = textArea.getText();
            int caretPosition = textArea.getCaretPosition();
            int lastSpaceIndex = currentText.lastIndexOf(' ', caretPosition - 1);

            if (lastSpaceIndex == -1) {
                // If no space is found, clear the text
                textArea.setText("");
            } else {
                textArea.setText(currentText.substring(0, lastSpaceIndex));
            }

            redoStack.push(new String(currentText));
        }

        private void redo() {
            if (!redoStack.isEmpty()) {
                textArea.setText(redoStack.pop());
            }
        }


        private void find() {
            String searchTerm = JOptionPane.showInputDialog(this, "Enter text to find:");
            if (searchTerm != null) {
                String content = textArea.getText();
                Highlighter highlighter = textArea.getHighlighter();
                highlighter.removeAllHighlights();

                int index = content.indexOf(searchTerm);
                while (index != -1) {
                    try {
                        highlighter.addHighlight(index, index + searchTerm.length(), new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
                        index = content.indexOf(searchTerm, index + 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                int count = content.split(searchTerm, -1).length - 1;
                JOptionPane.showMessageDialog(this, count + " occurrences found.");
            }
        }

        private void replace() {
            String wordToReplace = JOptionPane.showInputDialog(this, "Enter word to replace:");
            if (wordToReplace != null) {
                String replacement = JOptionPane.showInputDialog(this, "Enter replacement word:");
                if (replacement != null) {
                    textArea.setText(textArea.getText().replace(wordToReplace, replacement));
                }
            }
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                AINotepad notepad = new AINotepad();
                notepad.setVisible(true);
            });
        }
    }



