package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SaoLei implements ActionListener {
	
	JFrame window = new JFrame("Sao Lei"); // window
	JButton reset = new JButton("Reset"); // reset button
	Container container = new Container(); // game field
	final int rows = 20;
	final int cols = 20;
	final int bombs = 30; // number of bombs
	JButton[][] fields = new JButton[rows][cols];
	int[][] count = new int[rows][cols]; // bombs possible position
	final int bombCode = 10;
	
	// constructor:: 
	public SaoLei() {
		// Step 1: create  a game window
		window.setSize(1024, 768); // size
		window.setResizable(false); // not responsive
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close window
		//  Step 2: add reset button
		addResetButton(); 
		// Step 3: create game field
		createFields();
		// Step 4: add bombs
		addBombs();
		// Step 5: counts of bombs 
		calcNeighbour();
		
		window.setVisible(true); // display
	}
	// Step 2:
	void addResetButton() {
		reset.setBackground(Color.green);
		reset.setOpaque(true);
		reset.addActionListener(this);
		window.add(reset, BorderLayout.NORTH); // put button on top of window
	}
	// Step 3:
	// create a play game field
	void createFields() {
		window.add(container, BorderLayout.CENTER);
		container.setLayout(new GridLayout(rows, cols));
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				JButton cell = new JButton();
				cell.setBackground(Color.yellow);
				cell.setOpaque(false );
				cell.addActionListener(this); // add event listener to each cell.
				fields[i][j] = cell;
				container.add(cell);
			}
		}
	}
	// Step 4 : set bombs
	void addBombs() {
		// set bombs randomly
		Random random = new Random();
		int randomRow;
		int randomCol;
		for (int b = 0; b < bombs; b++) {
			randomRow = random.nextInt(rows);
			randomCol = random.nextInt(cols);
			// check if current cell visited
			if (count[randomRow][randomCol] == bombCode) {
				b--;
			} else {
				count[randomRow][randomCol] = bombCode;
//				fields[randomRow][randomCol].setText("X"); // mark the cell
			}
		}
	}
	// Step 5: add function to calculate number of bombs for neighbour cells
	void calcNeighbour() {
		int cur ;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				cur = 0;
				if (r > 0 && c > 0 && count[r - 1][c - 1] == bombCode) {
					cur++;
				}
				if (r > 0 && count[r - 1][c ] == bombCode) {
					cur++;
				}
				if (r > 0 && c < cols - 1 && count[r - 1][c + 1] == bombCode) {
					cur++;
				}
				if (c > 0 && count[r ][c - 1] == bombCode) {
					cur++;
				}
				// if current position has bomb, do nothing
				if (count[r][c] == bombCode) {
					continue; 
				}
				if (c < cols - 1 && count[r ][c + 1] == bombCode) {
					cur++;
				}
				if (r < rows - 1 && c > 0 && count[r + 1][c - 1] == bombCode) {
					cur++;
				}
				if (r < rows - 1 && count[r + 1][c] == bombCode) {
					cur++;
				}
				if (r < rows - 1 && c < cols - 1 && count[r + 1][c + 1] == bombCode) {
					cur++;
				}
				count[r][c] = cur;
//				fields[r][c].setText(count[r][c] + "");
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) { // every time cell is clicked, following method will be triggered
		// TODO Auto-generated method stub
		JButton button = (JButton) e.getSource();
		if (button.equals(reset)) { // if reset is clicked
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					fields[r][c].setText(" ");
					fields[r][c].setEnabled(true);
					fields[r][c].setBackground(Color.yellow);
					fields[r][c].setOpaque(false);
					count[r][c] = 0;
				}
			}
			addBombs();
			calcNeighbour();
		} else { // if field is clicked
			int cur = 0;
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					if (button.equals(fields[r][c])) {
						cur = count[r][c];
						if (cur == bombCode) { // if bomb is clicked
							gameOver();
						} else {
//							button.setOpaque(true);
//							button.setText(cur + " "); // set cell value to 0 if clicked
//							button.setEnabled(false); // cannot be clicked again if clicked already
							openCell(r, c);
							checkWin();
						}
						return;
					}
				}
			}
		}
	}
	void gameOver() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				int cur = count[r][c];
				if (cur == bombCode) {
					fields[r][c].setText("X");
					fields[r][c].setBackground(Color.red);
					fields[r][c].setOpaque(true);
					fields[r][c].setEnabled(false);
				} else {
					fields[r][c].setText(cur + " ");
					fields[r][c].setEnabled(false);
				}
			}
		}
	}
	void openCell(int r, int c) {
		// corner case: if current position is opened already
		if (fields[r][c].isEnabled() == false) {
			return;
		}
		fields[r][c].setOpaque(true);
		fields[r][c].setEnabled(false);
		if (count[r][c] == 0) {
			if (r > 0 && c > 0 && count[r - 1][c - 1] != bombCode) {
				openCell(r - 1, c - 1);
			}
			if (r > 0 && count[r - 1][c ] != bombCode) {
				openCell(r - 1, c);
			}
			if (r > 0 && c < cols - 1 && count[r - 1][c + 1] != bombCode) {
				openCell(r - 1, c + 1);
			}
			if (c > 0 && count[r ][c - 1] != bombCode) {
				openCell(r, c - 1);
			}
//			if (count[r][c] != bombCode) {
//				openCell(r, c); 
//			}
			if (c < cols - 1 && count[r ][c + 1] != bombCode) {
				openCell(r, c + 1);
			}
			if (r < rows - 1 && c > 0 && count[r + 1][c - 1] != bombCode) {
				openCell(r + 1, c - 1);
			}
			if (r < rows - 1 && count[r + 1][c] != bombCode) {
				openCell(r + 1, c);
			}
			if (r < rows - 1 && c < cols - 1 && count[r + 1][c + 1] != bombCode) {
				openCell(r + 1, c + 1);
			}
		} else {
			fields[r][c].setText(count[r][c] + " ");
		}
	}
	void checkWin() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (fields[r][c].isEnabled() == true && count[r][c] != bombCode) {
					return;
				}
			}
			JOptionPane.showMessageDialog(window, "You Win!");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SaoLei game = new SaoLei();
	}

	
	
	

}
