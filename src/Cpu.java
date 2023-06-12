package javaFinalWork2;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Cpu extends JFrame {
	Container frame = this.getContentPane();
	Set<Word> cpuVoc = new HashSet<>();
	JPanel Center;
	JButton[][] btnCpu;
	int[][] w;
	Word[][] cpuBoard;
	int size;

	public Cpu(int S, List<Word> allV) {
		size = S;
		Random random = new Random();
		while (cpuVoc.size() <= (size * size)) {
			int idx = random.nextInt(allV.size());
			cpuVoc.add(allV.get(idx));
		}
		this.setTitle("컴퓨터 게임 진행 화면입니다.");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(1000, 500);
		w = initweight();
		init();
	}

	private int[][] initweight() {
		// TODO Auto-generated method stub
		int[][] weight = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++)
				weight[i][j]++;
		}
		int j = 0;
		for (int i = 0; i < size; i++) {
			weight[i][j]++;
			j++;
		}
		j = size - 1;
		for (int i = 0; i < size; i++) {
			weight[i][j]++;
			j--;
		}

		return weight;
	}

	private void init() {
		// TODO Auto-generated method stub
		Center = new JPanel();
		Center.setLayout(new GridLayout(size, size));
		btnCpu = new JButton[size][size];
		cpuBoard = new Word[size][size];
		Iterator<Word> citer = cpuVoc.iterator();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				// user 저장
				Word temp2 = citer.next();
				cpuBoard[i][j] = temp2;
				JButton tempB2 = new JButton(temp2.eng);
				btnCpu[i][j] = tempB2;
				Center.add(tempB2);
			}
		}
		frame.add(Center, BorderLayout.CENTER);
	}

	public Word cpuWorking(String inputU) {
		this.boardChange(inputU);
		Word cpuAnswer = this.chooseAnswer();
		boardChange(cpuAnswer.eng);
		return cpuAnswer;
	}

	private Word chooseAnswer() {
		// TODO Auto-generated method stub
		int max = 0;
		int maxI = 0, maxJ = 0;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (max < w[i][j]) {
					max = w[i][j];
					maxI = i;
					maxJ = j;
				}
			}
		}
		return cpuBoard[maxI][maxJ];
	}

	private void boardChange(String eng) {
		// TODO Auto-generated method stub
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (cpuBoard[i][j].eng.equals(eng)) { // 찾았으면
					btnCpu[i][j].setText("O");
					btnCpu[i][j].setFont(new Font("고딕체", Font.BOLD, 40));
					updateWeight(i, j);
				}
			}
		}
	}

	public void updateWeight(int row, int col) {
		System.out.println();
		System.out.println();
		w[row][col] = 0;
		if ((row + 1 != size) && (w[row + 1][col] != 0))
			w[row + 1][col]++;
		if ((row != 0) && (w[row - 1][col] != 0))
			w[row - 1][col]++;
		if ((col + 1 != size) && (w[row][col + 1] != 0))
			w[row][col + 1]++;
		if ((col != 0) && (w[row][col - 1] != 0))
			w[row][col - 1]++;

		if ((col == row)) {
			for (int i = row + 1; i < size; i++) {
				if (w[i][i] != 0)
					w[i][i]++;
			}
			for (int i = row - 1; i >= 0; i--) {
				if (w[i][i] != 0)
					w[i][i]++;
			}
		}
		if ((col + row) == (size - 1)) { // 반대 대각선
			for (int i = row + 1; i < size; i++) {
				if (w[i][size - 1 - i] != 0)
					w[i][size - 1 - i]++;
			}
			for (int i = row - 1; i >= 0; i--) {
				if (w[i][size - 1 - i] != 0)
					w[i][size - 1 - i]++;
			}
		}
	}

	public int getBingoCount() {
		int count = 0;
		// 가로 검사
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (w[i][j] != 0)
					break;
				if (j == (size - 1)) {
					count++;
				}
			}
		}
		// 세로 검사
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (w[j][i] != 0)
					break;
				if (j == (size - 1)) {
					count++;
				}
			}
		}
		int j = 0, i = 0;
		while (true) {

			if (w[i][j] != 0)
				break;
			if (j == (size - 1)) {
				count++;
				break;
			}
			j++;
			i++;

		}
		i = 0;
		j = size - 1;
		while (true) {
			if (w[i][j] != 0)
				break;
			if (j == 0) {
				count++;
				break;
			}
			j--;
			i++;
		}
		if (count == (2 * size + 2))
			return -1;
		return count;

	}

}
