package minesweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Minesweeper extends JPanel{
	public static Minesweeper panel;
	public static int deadX, deadY;
	public static JFrame frame;
	public static Box[][] board;
	public static boolean initial, game, win;
	public static void main(String[] args) {
		panel = new Minesweeper();
		frame = new JFrame("Minesweeper");
		frame.setVisible(true);
		frame.setBackground(Color.BLACK);
		frame.setDefaultCloseOperation(3);
		frame.setBounds(600,100,818,847);
		frame.add(panel);
		newGame();
		panel.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				int x = e.getY()/40;
				int y = e.getX()/40;
				if(game){
					if(initial){
						initial = false;
						board[x][y].setClicked(true);
						List<Integer> used = new ArrayList<Integer>();
						used.add((x*20)+y);
						Random rand = new Random();
						for(int i = 0; i < 70; i++){
							int num = rand.nextInt(400);
							while(used.contains(num)){
								num = rand.nextInt(400);
							}
							board[num/20][num%20].setBomb(true);
							used.add(num);
						}
						evaluate();
						click(x,y);
						panel.repaint();
						checkWin();
					}
					else{
						if(e.getButton() == 1 && !board[x][y].isFlagged()){
							if(!board[x][y].isClicked()){
								board[x][y].setClicked(true);
								click(x,y);
								if(board[x][y].getValue() == 9){
									deadX = x;
									deadY = y;
									gameOver();
								}
							}
							else{
								System.out.println(surroundingFlags(x,y));
								if(board[x][y].getValue() == surroundingFlags(x,y)){
									for(int i = y-1; i < y+2; i++){
										if(isInBounds(x-1,i) && !board[x-1][i].isFlagged()){
											board[x-1][i].setClicked(true);
											click(x-1,i);
											if(board[x-1][i].getValue() == 9){
												deadX = x-1;
												deadY = i;
												gameOver();
											}
										}
										if(isInBounds(x+1,i) && !board[x+1][i].isFlagged()){
											board[x+1][i].setClicked(true);
											click(x+1,i);
											if(board[x+1][i].getValue() == 9){
												deadX = x+1;
												deadY = i;
												gameOver();
											}
										}
									}
									if(isInBounds(x,y-1) && !board[x][y-1].isFlagged()){
										board[x][y-1].setClicked(true);
										click(x,y-1);
										if(board[x][y-1].getValue() == 9){
											deadX = x;
											deadY = y-1;
											gameOver();
										}
									}
									if(isInBounds(x,y+1) && !board[x][y+1].isFlagged()){
										board[x][y+1].setClicked(true);
										click(x,y+1);
										if(board[x][y+1].getValue() == 9){
											deadX = x;
											deadY = y+1;
											gameOver();
										}
									}
								}
							}
						}
						else if(e.getButton() == 3){
							if(!board[x][y].isFlagged() && !board[x][y].isClicked()){
								board[x][y].setFlagged(true);
							}
							else{
								board[x][y].setFlagged(false);
							}
						}
						panel.repaint();
						checkWin();
					}
					panel.repaint();
				}
				else{
					if(x == deadX && y == deadY){
						newGame();
						panel.repaint();
					}
				}
			}
		});
	}
	public static int surroundingFlags(int x, int y){
		int n = 0;
		for(int i = y-1; i < y+2; i++){
			if(isInBounds(x-1,i) && board[x-1][i].isFlagged()){
				n++;
			}
			if(isInBounds(x+1,i) && board[x+1][i].isFlagged()){
				n++;
			}
		}
		if(isInBounds(x,y-1) && board[x][y-1].isFlagged()){
			n++;
		}
		if(isInBounds(x,y+1) && board[x][y+1].isFlagged()){
			n++;
		}
		return n;
	}
	public static boolean isInBounds(int x, int y){
		return (x > -1) && (x < 20) && (y > -1) && (y < 20);
	}
	public static void evaluate(){
		for(int x = 1; x < 19; x++){
			for(int y = 1; y < 19; y++){
				int i = 0;
				if(board[x-1][y-1].hasBomb()){
					i++;
				}
				if(board[x-1][y].hasBomb()){
					i++;
				}
				if(board[x-1][y+1].hasBomb()){
					i++;
				}
				if(board[x][y-1].hasBomb()){
					i++;
				}
				if(board[x][y+1].hasBomb()){
					i++;
				}
				if(board[x+1][y-1].hasBomb()){
					i++;
				}
				if(board[x+1][y].hasBomb()){
					i++;
				}
				if(board[x+1][y+1].hasBomb()){
					i++;
				}
				board[x][y].setValue(i);
			}
		}
		for(int x = 1; x < 19; x++){
			int i = 0;
			if(board[1][x-1].hasBomb()){
				i++;
			}
			if(board[1][x].hasBomb()){
				i++;
			}
			if(board[1][x+1].hasBomb()){
				i++;
			}
			if(board[0][x-1].hasBomb()){
				i++;
			}
			if(board[0][x+1].hasBomb()){
				i++;
			}
			board[0][x].setValue(i);
		}
		for(int x = 1; x < 19; x++){
			int i = 0;
			if(board[18][x-1].hasBomb()){
				i++;
			}
			if(board[18][x].hasBomb()){
				i++;
			}
			if(board[18][x+1].hasBomb()){
				i++;
			}
			if(board[19][x-1].hasBomb()){
				i++;
			}
			if(board[19][x+1].hasBomb()){
				i++;
			}
			board[19][x].setValue(i);
		}
		for(int x = 1; x < 19; x++){
			int i = 0;
			if(board[x-1][1].hasBomb()){
				i++;
			}
			if(board[x][1].hasBomb()){
				i++;
			}
			if(board[x+1][1].hasBomb()){
				i++;
			}
			if(board[x-1][0].hasBomb()){
				i++;
			}
			if(board[x+1][0].hasBomb()){
				i++;
			}
			board[x][0].setValue(i);
		}
		for(int x = 1; x < 19; x++){
			int i = 0;
			if(board[x-1][18].hasBomb()){
				i++;
			}
			if(board[x][18].hasBomb()){
				i++;
			}
			if(board[x+1][18].hasBomb()){
				i++;
			}
			if(board[x-1][19].hasBomb()){
				i++;
			}
			if(board[x+1][19].hasBomb()){
				i++;
			}
			board[x][19].setValue(i);
		}
		int i = 0;
		if(board[0][1].hasBomb()){
			i++;
		}
		if(board[1][1].hasBomb()){
			i++;
		}
		if(board[1][0].hasBomb()){
			i++;
		}
		board[0][0].setValue(i);
		i = 0;
		if(board[0][18].hasBomb()){
			i++;
		}
		if(board[1][18].hasBomb()){
			i++;
		}
		if(board[1][19].hasBomb()){
			i++;
		}
		board[0][19].setValue(i);
		i = 0;
		if(board[19][18].hasBomb()){
			i++;
		}
		if(board[18][18].hasBomb()){
			i++;
		}
		if(board[18][19].hasBomb()){
			i++;
		}
		board[19][19].setValue(i);
		i = 0;
		if(board[19][1].hasBomb()){
			i++;
		}
		if(board[18][1].hasBomb()){
			i++;
		}
		if(board[18][0].hasBomb()){
			i++;
		}
		board[19][0].setValue(i);
	}
	public static void click(int x, int y){
		if(board[x][y].getValue() != 0){
			return;
		}
		else{
			board[x][y].setChecked(true);
			if(x == 0 && y == 0){
				if(!board[0][1].isFlagged())
					board[0][1].setClicked(true);
				if(board[0][1].getValue() == 0 && !board[0][1].isChecked() && !board[0][1].isFlagged())
					click(0,1);
				if(!board[1][1].isFlagged())
					board[1][1].setClicked(true);
				if(board[1][1].getValue() == 0 && !board[1][1].isChecked() && !board[1][1].isFlagged())
					click(1,1);
				if(!board[1][0].isFlagged())
					board[1][0].setClicked(true);
				if(board[1][0].getValue() == 0 && !board[1][0].isChecked() && !board[1][0].isFlagged())
					click(1,0);
			}
			else if(x == 0 && y == 19){
				if(!board[0][18].isFlagged())
					board[0][18].setClicked(true);
				if(board[0][18].getValue() == 0 && !board[0][18].isChecked() && !board[0][18].isFlagged())
					click(0,18);
				if(!board[1][18].isFlagged())
					board[1][18].setClicked(true);
				if(board[1][18].getValue() == 0 && !board[1][18].isChecked() && !board[1][18].isFlagged())
					click(1,18);
				if(!board[1][19].isFlagged())
					board[1][19].setClicked(true);
				if(board[1][19].getValue() == 0 && !board[1][19].isChecked() && !board[1][19].isFlagged())
					click(1,19);
			}
			else if(x == 0){
				if(!board[0][y-1].isFlagged())
					board[0][y-1].setClicked(true);
				if(board[0][y-1].getValue() == 0 && !board[0][y-1].isChecked() && !board[0][y-1].isFlagged())
					click(0,y-1);
				if(!board[0][y+1].isFlagged())
					board[0][y+1].setClicked(true);
				if(board[0][y+1].getValue() == 0 && !board[0][y+1].isChecked() && !board[0][y+1].isFlagged())
					click(0,y+1);
				if(!board[1][y-1].isFlagged())
					board[1][y-1].setClicked(true);
				if(board[1][y-1].getValue() == 0 && !board[1][y-1].isChecked() && !board[1][y-1].isFlagged())
					click(1,y-1);
				if(!board[1][y].isFlagged())
					board[1][y].setClicked(true);
				if(board[1][y].getValue() == 0 && !board[1][y].isChecked() && !board[1][y].isFlagged())
					click(1,y);
				if(!board[1][y+1].isFlagged())
					board[1][y+1].setClicked(true);
				if(board[1][y+1].getValue() == 0 && !board[1][y+1].isChecked() && !board[1][y+1].isFlagged())
					click(1,y+1);
			}
			else if(x == 19 && y == 0){
				if(!board[18][0].isFlagged())
					board[18][0].setClicked(true);
				if(board[18][0].getValue() == 0 && !board[18][0].isChecked() && !board[18][0].isFlagged())
					click(18,0);
				if(!board[18][1].isFlagged())
					board[18][1].setClicked(true);
				if(board[18][1].getValue() == 0 && !board[18][1].isChecked() && !board[18][1].isFlagged())
					click(18,1);
				if(!board[19][1].isFlagged())
					board[19][1].setClicked(true);
				if(board[19][1].getValue() == 0 && !board[19][1].isChecked() && !board[19][1].isFlagged())
					click(19,1);
			}
			else if(x == 19 && y == 19){
				if(!board[18][19].isFlagged())
					board[18][19].setClicked(true);
				if(board[18][19].getValue() == 0 && !board[18][19].isChecked() && !board[18][19].isFlagged())
					click(18,19);
				if(!board[18][18].isFlagged())
					board[18][18].setClicked(true);
				if(board[18][18].getValue() == 0 && !board[18][18].isChecked() && !board[18][18].isFlagged())
					click(18,18);
				if(!board[19][18].isFlagged())
					board[19][18].setClicked(true);
				if(board[19][18].getValue() == 0 && !board[19][18].isChecked() && !board[19][18].isFlagged())
					click(19,18);
			}
			else if(x == 19){
				if(!board[19][y-1].isFlagged())
					board[19][y-1].setClicked(true);
				if(board[19][y-1].getValue() == 0 && !board[19][y-1].isChecked() && !board[19][y-1].isFlagged())
					click(19,y-1);
				if(!board[19][y+1].isFlagged())
					board[19][y+1].setClicked(true);
				if(board[19][y+1].getValue() == 0 && !board[19][y+1].isChecked() && !board[19][y+1].isFlagged())
					click(19,y+1);
				if(!board[18][y-1].isFlagged())
					board[18][y-1].setClicked(true);
				if(board[18][y-1].getValue() == 0 && !board[18][y-1].isChecked() && !board[18][y-1].isFlagged())
					click(18,y-1);
				if(!board[18][y].isFlagged())
					board[18][y].setClicked(true);
				if(board[18][y].getValue() == 0 && !board[18][y].isChecked() && !board[18][y].isFlagged())
					click(18,y);
				if(!board[18][y+1].isFlagged())
					board[18][y+1].setClicked(true);
				if(board[18][y+1].getValue() == 0 && !board[18][y+1].isChecked() && !board[18][y+1].isFlagged())
					click(18,y+1);
			}
			else if(y == 0){
				if(!board[x-1][0].isFlagged())
					board[x-1][0].setClicked(true);
				if(board[x-1][0].getValue() == 0 && !board[x-1][0].isChecked() && !board[x-1][0].isFlagged())
					click(x-1,0);
				if(!board[x+1][0].isFlagged())
					board[x+1][0].setClicked(true);
				if(board[x+1][0].getValue() == 0 && !board[x+1][0].isChecked() && !board[x+1][0].isFlagged())
					click(x+1,0);
				if(!board[x-1][1].isFlagged())
					board[x-1][1].setClicked(true);
				if(board[x-1][1].getValue() == 0 && !board[x-1][1].isChecked() && !board[x-1][1].isFlagged())
					click(x-1,1);
				if(!board[x][1].isFlagged())
					board[x][1].setClicked(true);
				if(board[x][1].getValue() == 0 && !board[x][1].isChecked() && !board[x][1].isFlagged())
					click(x,1);
				if(!board[x+1][1].isFlagged())
					board[x+1][1].setClicked(true);
				if(board[x+1][1].getValue() == 0 && !board[x+1][1].isChecked() && !board[x+1][1].isFlagged())
					click(x+1,1);
			}
			else if(y == 19){
				if(!board[x-1][19].isFlagged())
					board[x-1][19].setClicked(true);
				if(board[x-1][19].getValue() == 0 && !board[x-1][19].isChecked() && !board[x-1][19].isFlagged())
					click(x-1,19);
				if(!board[x+1][19].isFlagged())
					board[x+1][19].setClicked(true);
				if(board[x+1][19].getValue() == 0 && !board[x+1][19].isChecked() && !board[x+1][19].isFlagged())
					click(x+1,19);
				if(!board[x-1][18].isFlagged())
					board[x-1][18].setClicked(true);
				if(board[x-1][18].getValue() == 0 && !board[x-1][18].isChecked() && !board[x-1][18].isFlagged())
					click(x-1,18);
				if(!board[x][18].isFlagged())
					board[x][18].setClicked(true);
				if(board[x][18].getValue() == 0 && !board[x][18].isChecked() && !board[x][18].isFlagged())
					click(x,18);
				if(!board[x+1][18].isFlagged())
					board[x+1][18].setClicked(true);
				if(board[x+1][18].getValue() == 0 && !board[x+1][18].isChecked() && !board[x+1][18].isFlagged())
					click(x+1,18);
			}
			else{
				if(!board[x-1][y-1].isFlagged())
					board[x-1][y-1].setClicked(true);
				if(board[x-1][y-1].getValue() == 0 && !board[x-1][y-1].isChecked() && !board[x-1][y-1].isFlagged())
					click(x-1,y-1);
				if(!board[x-1][y].isFlagged())
					board[x-1][y].setClicked(true);
				if(board[x-1][y].getValue() == 0 && !board[x-1][y].isChecked() && !board[x-1][y].isFlagged())
					click(x-1,y);
				if(!board[x-1][y+1].isFlagged())
					board[x-1][y+1].setClicked(true);
				if(board[x-1][y+1].getValue() == 0 && !board[x-1][y+1].isChecked() && !board[x-1][y+1].isFlagged())
					click(x-1,y+1);
				if(!board[x][y-1].isFlagged())
					board[x][y-1].setClicked(true);
				if(board[x][y-1].getValue() == 0 && !board[x][y-1].isChecked() && !board[x][y-1].isFlagged())
					click(x,y-1);
				if(!board[x][y+1].isFlagged())
					board[x][y+1].setClicked(true);
				if(board[x][y+1].getValue() == 0 && !board[x][y+1].isChecked() && !board[x][y+1].isFlagged())
					click(x,y+1);
				if(!board[x+1][y-1].isFlagged())
					board[x+1][y-1].setClicked(true);
				if(board[x+1][y-1].getValue() == 0 && !board[x+1][y-1].isChecked() && !board[x+1][y-1].isFlagged())
					click(x+1,y-1);
				if(!board[x+1][y].isFlagged())
					board[x+1][y].setClicked(true);
				if(board[x+1][y].getValue() == 0 && !board[x+1][y].isChecked() && !board[x+1][y].isFlagged())
					click(x+1,y);
				if(!board[x+1][y+1].isFlagged())
					board[x+1][y+1].setClicked(true);
				if(board[x+1][y+1].getValue() == 0 && !board[x+1][y+1].isChecked() && !board[x+1][y+1].isFlagged())
					click(x+1,y+1);
			}
		}
	}
	public static boolean checkWin(){
		for(int x = 0; x < 20; x++){
			for(int y = 0; y < 20; y++){
				if(board[x][y].getValue() != 9 && !board[x][y].isClicked()){
					return false;
				}
			}
		}
		for(int x = 0; x < 20; x++){
			for(int y = 0; y < 20; y++){
				if(board[x][y].getValue() == 9 && !board[x][y].isClicked()){
					deadX = x;
					deadY = y;
				}
			}
		}
		game = false;
		win = true;
		frame.setTitle("Minesweeper - CLICK GOLDEN SQUARE TO PLAY AGAIN");
		JOptionPane.showMessageDialog(null, "YOU WIN!");
		panel.repaint();
		return true;
	}
	public static void newGame(){
		frame.setTitle("Minesweeper");
		deadX = -1;
		deadY = -1;
		board = new Box[20][20];
		initial = true;
		for(int x = 0; x < 20; x++){
			for(int y = 0; y < 20; y++){
				board[x][y] = new Box(0);
			}
		}
		game = true;
		win = false;
	}
	public static void gameOver(){
		game = false;
		win = false;
		frame.setTitle("Minesweeper - CLICK RED MINE TO PLAY AGAIN");
		for(int x = 0; x < 20; x++){
			for(int y = 0; y < 20; y++){
				if(board[x][y].getValue() == 9 && !board[x][y].isFlagged()){
					board[x][y].setClicked(true);
				}
			}
		}
	}
	public void paint(Graphics g){
		super.paintComponents(g);
		for(int x = 0; x < 20; x++){
			for(int y = 0; y < 20; y++){ 
				if(!board[x][y].isClicked()){
					g.setColor(Color.DARK_GRAY);
					g.fillRect(y*40, x*40, 40, 40);
					g.setColor(Color.GRAY);
					g.fillRect(y*40+2, x*40+2, 36, 36);
					if(board[x][y].isFlagged()){
						g.setColor(Color.RED);
						g.fillPolygon(new int[]{y*40+20,y*40+6,y*40+20}, new int[]{x*40+6,x*40+11,x*40+16}, 3);
						g.setColor(Color.WHITE);
						g.fillRect(y*40+10, x*40+30, 22, 2);
						g.fillRect(y*40+20, x*40+6, 2, 24);
					}
				}
				else{
					g.setColor(Color.DARK_GRAY);
					g.fillRect(y*40, x*40, 40, 40);
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(y*40+2, x*40+2, 36, 36);
					String v = Integer.toString(board[x][y].getValue());
					switch(v){
					case "0":
						v = "";
						break;
					case "1":
						g.setColor(Color.BLUE);
						break;
					case "2":
						g.setColor(new Color(0,100,0));
						break;
					case "3":
						g.setColor(Color.RED);
						break;
					case "4":
						g.setColor(new Color(0,0,100));
						break;
					case "5":
						g.setColor(new Color(100,0,0));
						break;
					case "6":
						g.setColor(new Color(0,100,100));
						break;
					case "7":
						g.setColor(Color.MAGENTA);
						break;
					case "8":
						g.setColor(Color.DARK_GRAY);
						break;
					case "9":
						v = "";
						int x1 = y*40;
						int y1 = x*40;
						g.setColor(Color.DARK_GRAY);
						g.fillOval(x1+10, y1+10, 20, 20);
						g.fillPolygon(new int[]{x1+20,x1+23,x1+18}, new int[]{y1+5,y1+12,y1+12}, 3);
						g.fillPolygon(new int[]{x1+20,x1+23,x1+18}, new int[]{y1+35,y1+28,y1+28}, 3);
						g.fillPolygon(new int[]{x1+5,x1+12,x1+12}, new int[]{y1+20,y1+18,y1+23}, 3);
						g.fillPolygon(new int[]{x1+35,x1+28,x1+28}, new int[]{y1+20,y1+18,y1+23}, 3);
						g.fillPolygon(new int[]{x1+9,x1+15,x1+13}, new int[]{y1+9,y1+12,y1+15}, 3);
						g.fillPolygon(new int[]{x1+32,x1+25,x1+27}, new int[]{y1+8,y1+12,y1+15}, 3);
						g.fillPolygon(new int[]{x1+8,x1+16,x1+13}, new int[]{y1+32,y1+28,y1+25}, 3);
						g.fillPolygon(new int[]{x1+32,x1+25,x1+27}, new int[]{y1+32,y1+28,y1+24}, 3);
						break;
					}
					g.setFont(new Font("ARIAL",Font.BOLD,20));
					g.drawString(v, y*40+14, x*40+26);
				}
				if(!game){
					int x1 = y*40;
					int y1 = x*40;
					if(board[x][y].isFlagged() && board[x][y].getValue() != 9){
						g.setColor(Color.RED);
						g.fillPolygon(new int[]{x1+2,x1+4,x1+38,x1+36}, new int[]{y1+4,y1+1,y1+36,y1+38}, 4);
						g.fillPolygon(new int[]{x1+36,x1+38,x1+4,x1+2}, new int[]{y1+1,y1+4,y1+38,y1+36}, 4);
					}
				}
			}
		}
		if(!win){
			g.setColor(new Color(255,0,0, 50));
		}
		else{
			g.setColor(new Color(255,255,0,64));
		}
		g.fillRect(deadY*40, deadX*40, 40, 40);
	}
}
