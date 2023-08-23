import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    int b_height=500;

    int b_width=500;

    int max_Dots=2500;  //max dots 500*500

    int dot_Size=10;

    int dots;

    int[] x=new int[max_Dots];  //x axis array

    int[] y=new int[max_Dots];  //y axis array


    //coordinates of apple

    int apple_x;

    int apple_y;

    Image apple,body,head;

    //initialize timer

    Timer timer;

    int delay=170;

    boolean leftDirection=true;

    boolean rightDirection=false;

    boolean upDirection=false;

    boolean downDirection=false;

    boolean inGame=true;
    Board(){

        TAdapter tAdapter=new TAdapter();

        addKeyListener(tAdapter);

        setFocusable(true);

        setPreferredSize(new Dimension(b_width,b_height));

        setBackground(Color.BLACK);

        game();
        loadImage();

    }

    public void game(){
        dots=3;   //initial length of the snake

        //initialize the game

        x[0]=250;

        y[0]=250;

        for(int i=1;i<dots;i++){

            x[i]=x[0]+dot_Size*i;

            y[i]=y[0];
        }

        //Giving coordinates for the apple

        locApple();

        timer=new Timer(delay,this);

        timer.start();
    }

    //load the images from resources folder to image object

    public void loadImage(){
        ImageIcon bodyIcon=new ImageIcon("src/resources/dot.png");

        body=bodyIcon.getImage();

        ImageIcon headIcon=new ImageIcon("src/resources/head.png");

        head=headIcon.getImage();

        ImageIcon appleIcon=new ImageIcon("src/resources/apple.png");

        apple=appleIcon.getImage();

    }


    //get snake and apple images at particular place

    @Override

    public  void paintComponent(Graphics g){

        super.paintComponent(g);

        doDraw(g);
    }

    //draw image

    public void doDraw(Graphics g){
if(inGame) {
    g.drawImage(apple, apple_x, apple_y, this);

    for (int i = 0; i < dots; i++) {

        if (i == 0) {
            g.drawImage(head, x[0], y[0], this);
        } else {
            g.drawImage(body, x[i], y[i], this);
        }
    }
}
else {
    gameOver(g);
    timer.stop();
}
    }

    //randomise the apple

    public  void locApple(){
        apple_x=((int)(Math.random()*49))*dot_Size;

        apple_y=((int)(Math.random()*49))*dot_Size;
    }

    @Override

    public  void actionPerformed(ActionEvent actionEvent){

        if(inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    //make snake move

    public void move(){
        for(int i=dots-1;i>0;i--){

            x[i]=x[i-1];

            y[i]=y[i-1];
        }

        if(leftDirection){
            x[0]-=dot_Size;
        }
        if(rightDirection){
            x[0]+=dot_Size;
        }
        if(upDirection){
            y[0]-=dot_Size;
        }
        if(downDirection){
            y[0]+=dot_Size;
        }
    }

    //make snake eat food

    public  void checkApple(){
        if(apple_x==x[0] && apple_y==y[0]){
            dots++;
            locApple();
        }
    }

    //implement class

    private class TAdapter extends KeyAdapter{
        @Override
        public  void  keyPressed(KeyEvent keyEvent){
            int key=keyEvent.getKeyCode();
            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }

    //check collisions with border and body

    public void checkCollision(){

        //collision with body
        for(int i=0;i<dots;i++){

            if(i>4 && x[0]==x[i] && y[0]==y[i]){
                inGame=false;
            }
        }

        if(x[0]<0 || x[0]>=b_width || y[0]<0 || y[0]>=b_height){
            inGame=false;
        }

    }

    //gameOver

    public  void gameOver(Graphics g) {

        String msg = "Game Over";
        int score = (dots-3)*100;
        String scoremsg = "\nScore: "+ Integer.toString(score);
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (b_width - metr.stringWidth(msg)) / 2, (b_height / 2)-10);
        g.drawString(scoremsg, (b_width - metr.stringWidth(scoremsg)) / 2,(b_height / 2)+10 );
    }

}
