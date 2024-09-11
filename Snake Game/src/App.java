import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth=1024;
        int boardHeight=720;
        JFrame frame=new JFrame("Snake Game");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SnakeGame sg= new SnakeGame(boardWidth, boardHeight);
        frame.add(sg);
        frame.pack();
        sg.requestFocus();
    }
}
