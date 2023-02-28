import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PLAYER_WIDTH = 25;
    static final int PLAYER_HEIGHT = 100;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Player player1;
    Player player2;
    Ball ball;
    Score score;


    GamePanel(){
        newPlayers();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new ActionListener());
        this.setPreferredSize(SCREEN_SIZE);
        gameThread = new Thread(this);
        gameThread.start();
        

    }
    
    public void newBall() {
        random = new Random();
		ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),random.nextInt(GAME_HEIGHT-BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);
    }

    public void newPlayers(){
        player1 = new Player(0,(GAME_HEIGHT/2)-(PLAYER_HEIGHT/2), PLAYER_WIDTH, PLAYER_HEIGHT, 1);
		player2 = new Player(GAME_WIDTH-PLAYER_WIDTH,(GAME_HEIGHT/2)-(PLAYER_HEIGHT/2), PLAYER_WIDTH, PLAYER_HEIGHT,2);

    }

    public void paint(Graphics g){
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g){

        player1.draw(g);
        player2.draw(g);
        ball.draw(g);
        score.draw(g);

    }

    public void move() {
        player1.move();
        player2.move();
        ball.move();
    }


    public void checkCollision(){
    if(player1.y<=0)
        player1.y=0;
    if(player1.y >= (GAME_HEIGHT-PLAYER_HEIGHT))
        player1.y = GAME_HEIGHT-PLAYER_HEIGHT;
    if(player2.y<=0)
        player2.y=0;
    if(player2.y >= (GAME_HEIGHT-PLAYER_HEIGHT))
        player2.y = GAME_HEIGHT-PLAYER_HEIGHT;

	if(ball.y <=0) {
        ball.setYDirection(-ball.yVelocity);
		}
		if(ball.y >= GAME_HEIGHT-BALL_DIAMETER) {
			ball.setYDirection(-ball.yVelocity);
		}
		//scoring
		if(ball.intersects(player1)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; 
			if(ball.yVelocity>0)
				ball.yVelocity++;
			else
				ball.yVelocity--;
			ball.setXDirection(ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		if(ball.intersects(player2)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; 
			if(ball.yVelocity>0)
				ball.yVelocity++; 
			else
				ball.yVelocity--;
			ball.setXDirection(-ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}

        if(ball.x <=0) {
			score.player2++;
			newPlayers();
			newBall();
			System.out.println("Player 2: "+score.player2);
		}
		if(ball.x >= GAME_WIDTH-BALL_DIAMETER) {
			score.player1++;
			newPlayers();
			newBall();
			System.out.println("Player 1: "+score.player1);
		}

    }

    public void run(){
        System.out.println("oi");
        long lastTime = System.nanoTime();
		double amountOfTicks =60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while(true) {
			long now = System.nanoTime();
			delta += (now -lastTime)/ns;
			lastTime = now;
			if(delta >=1) {
				move();
				checkCollision();
				repaint();
				delta--;
                System.out.println("teste");
            }
        
        }

    }

    public class ActionListener extends KeyAdapter{

        public void keyPressed(KeyEvent e){
            player1.keyPressed(e);
            player2.keyPressed(e);
        }

        public void keyReleased(KeyEvent e){
            player1.keyReleased(e);
            player2.keyReleased(e);
            
        }
    }
}
