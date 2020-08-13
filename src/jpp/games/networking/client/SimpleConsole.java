package jpp.games.networking.client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimpleConsole {
    private JFrame frame;
    private BlockingQueue<String> inputQueue;

    private JTextArea output;
    private JTextArea input;
    private JScrollBar scrollBar;

    private Font font;

    private boolean boardAvailable;
    private JPanel boardView;
    private Image cross;
    private Image circle;
    private Image empty;
    private JLabel[][] board;

    public SimpleConsole() {
        JPanel inputPanel = initInput();
        JScrollPane outputScroll = initOutput();

        boardAvailable = initImgs();

        board = new JLabel[3][3];
        inputQueue = new LinkedBlockingQueue<>();

        if (boardAvailable) initBoard();
        initFrame();

        frame.add(inputPanel, BorderLayout.PAGE_END);
        frame.add(outputScroll, BorderLayout.CENTER);
        if (boardAvailable) frame.add(boardView, BorderLayout.LINE_END);

        MouseListener listener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                input.grabFocus();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
        frame.addMouseListener(listener);
        output.addMouseListener(listener);

        scrollBar.addAdjustmentListener(e -> scrollBar.setValue(scrollBar.getMaximum()));
    }

    private boolean initImgs() {
        try {
            cross = ImageIO.read(findFile(System.getProperty("user.dir"), "cross.png").orElseThrow(NullPointerException::new));
            cross = cross.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            circle = ImageIO.read(findFile(System.getProperty("user.dir"), "circle.png").orElseThrow(NullPointerException::new));
            circle = circle.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            empty = ImageIO.read(findFile(System.getProperty("user.dir"), "empty.png").orElseThrow(NullPointerException::new));
            empty = empty.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            System.err.println("Spielbrett nicht verfügbar!");
            show("Spielbrett nicht verfügbar!");
            return false;
        } catch (NullPointerException e) {
            System.err.println("Spielbrett nicht verfügbar!");
            show("Spielbrett nicht verfügbar!");
            return false;
        }
        return true;
    }

    private JPanel initInput() {
        input = new JTextArea("");
        Font font = input.getFont();
        this.font = new Font(font.getFontName(), font.getStyle(), font.getSize() + 5);
        input.setFont(this.font);
        input.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    inputQueue.add(input.getText());
                    showInput(input.getText());
                    input.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Eingabe: ");
        label.setFont(this.font);
        inputPanel.add(label);
        inputPanel.add(input);
        inputPanel.setBackground(Color.WHITE);
        return inputPanel;
    }

    private JScrollPane initOutput() {
        output = new JTextArea();
        output.setFont(this.font);
        output.setEditable(false);
        output.setBackground(Color.BLACK);
        output.setForeground(Color.WHITE);

        JScrollPane outputScroll = new JScrollPane(output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollBar = outputScroll.getVerticalScrollBar();
        return outputScroll;
    }

    private void initBoard() {
        boardView = new JPanel(new GridBagLayout());
        boardView.setPreferredSize(new Dimension(300, 100));
        boardView.setBackground(Color.GRAY);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        boardView.add(new JLabel("Spielbrett:"), constraints);
        boardView.setVisible(false);
    }

    private void initFrame() {
        frame = new JFrame("Simple Client Console");
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }

    public void showServerAnswer(String line) {
        if (line.startsWith("New BoardState:\n") && boardAvailable) {
            parseBoardString(line.substring(line.indexOf("\n") + 1));
            drawBoard();
            showBoard();
        } else output.append("\n > SERVER: " + line);
    }

    public void showInput(String line) {
        output.append("\n > YOU: " + line);
    }

    public void show(String line) {
        output.append("\n > " + line);
    }

    public BlockingQueue<String> getQueue() {
        return inputQueue;
    }

    private void parseBoardString(String s) {
        InputStream is = new ByteArrayInputStream(s.getBytes());
        char next;
        try {
            for (int y = 0; y < board.length; y++) {
                for (int x = 0; x < board.length; x++) {
                    if (board[x][y] != null) boardView.remove(board[x][y]);

                    next = (char) is.read();
                    JLabel tmp;
                    if (next == 'X') tmp = new JLabel(new ImageIcon(cross));
                    else if (next == 'O') tmp = new JLabel(new ImageIcon(circle));
                    else tmp = new JLabel(new ImageIcon(empty));
                    board[x][y] = tmp;
                    is.read();
                }
                if (y <= 1) while (is.read() != '\n') ;
            }
        } catch (IOException ioe) {
        }
    }

    private void drawBoard() {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board.length; y++) {
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = x;
                constraints.gridy = y + 1;
                boardView.add(board[x][y], constraints);
            }
        }
        boardView.revalidate();
        boardView.repaint();
    }

    public void hideBoard() {
        if (boardAvailable) boardView.setVisible(false);
    }

    public void showBoard() {
        if (boardAvailable) boardView.setVisible(true);
    }

    public void close() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    public static Optional<File> findFile(String rootPath, String fileName) {
        File root = new File(rootPath);
        try {
            File[] files = root.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.getName().equals(fileName))
                        return Optional.of(new File(file.getAbsolutePath()));
                }

                for (File file : files) {
                    if (file.isDirectory()) {
                        Optional<File> op = findFile(file.getAbsolutePath(), fileName);
                        if (op.isPresent()) return op;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}