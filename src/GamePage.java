package javaFinalWork2;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GamePage extends JFrame implements ActionListener {
	Container frame = this.getContentPane();
	Cpu cpuC;
	Set<Word> userVoc = new HashSet<>();

	Word[][] userBoard;

	JButton[][] btnUser;

	JPanel north, center, south, cpu;
	JMenuBar mb;
	JMenu Help;
	JMenuItem show;
	JLabel showInput;
	JTextField tf = new JTextField(10);

	int size = 3;

	String usern;
	int win, lose, draw;
	double rate;

	public GamePage(MainFrame start, List<Word> allV) {
		size = start.mapSize;

		if (size * size > allV.size()) {
			JOptionPane.showMessageDialog(null, "맵 크기를 조절하거나 단어장에 단어를 추가하신 후, 게임을 다시 시작해주세요.", "Message",
					JOptionPane.ERROR_MESSAGE);
		} else {
			cpuC = new Cpu(size, allV);
			usern = start.userName;
			win = start.win;
			lose = start.lose;
			draw = start.draw;

			// 단어장 정의
			Random random = new Random();
			while (userVoc.size() <= (size * size)) {
				int idx = random.nextInt(allV.size());
				userVoc.add(allV.get(idx));
			}

			this.setTitle(start.userName + "의 게임 화면");
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.setSize(1000, 500);
			init();
			this.setVisible(true);
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		initMenu();
		initNorth();
		initCenter();
		initSouth();
		listenerAdd();
	}

	private void initSouth() {
		// TODO Auto-generated method stub
		south = new JPanel();
		south.add(new JLabel("입력: "));
		south.add(tf);
		frame.add(south, BorderLayout.SOUTH);
	}

	private void initCenter() {
		// TODO Auto-generated method stub
		center = new JPanel();
		center.setLayout(new GridLayout(size, size));

		btnUser = new JButton[size][size];
		userBoard = new Word[size][size];
		Iterator<Word> uiter = userVoc.iterator();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				// word 저장
				Word temp1 = uiter.next();
				userBoard[i][j] = temp1;
				// button 저장
				JButton tempB1 = new JButton(temp1.eng);
				btnUser[i][j] = tempB1;
				// 패널에 추가
				center.add(tempB1);
				tempB1.addActionListener(this);
			}
		}

		frame.add(center, BorderLayout.CENTER);
	}

	private void initNorth() {
		// TODO Auto-generated method stub
		north = new JPanel();
		north.setLayout(new FlowLayout());
		showInput = new JLabel("영단어를 입력하면 게임이 시작됩니다.");
		showInput.setFont(new Font("고딕체", Font.PLAIN, 15));
		north.add(showInput);
		frame.add(north, BorderLayout.NORTH);
	}

	private void initMenu() {
		// TODO Auto-generated method stub
		mb = new JMenuBar();
		Help = new JMenu("Help");
		show = new JMenuItem("show CPU's Board");
		Help.add(show);
		mb.add(Help);
		this.setJMenuBar(mb);
	}

	private void listenerAdd() {
		// TODO Auto-generated method stub
		show.addActionListener(this);
		tf.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == show) {
			// 메뉴선택
			// cpu 화면 보여주기
			cpuC.setVisible(true);
		} else if (e.getSource() == tf) {
			JTextField t = (JTextField) e.getSource();
			String inputU = t.getText();

			// 유저 보드에서 해당 영단어 찾기 및 체크
			Word inputUW = boardChange(inputU);
			Word inputCW = cpuC.cpuWorking(inputU);

			// cpu 선택값 체크
			boardChange(inputCW.eng);
			showInput.setText("user의 입력 : " + inputUW + "           cpu의 입력: " + inputCW);

			// 승리 패배 검사
			int cpuScore = cpuC.getBingoCount();
			int userScore = this.getBingoCount();
			if (userScore != cpuScore) { // 승리 또는 패배 없는경우 = 0
				if (userScore > cpuScore) { // 이겼으면
					JOptionPane.showMessageDialog(null, userScore + "빙고!!" + usern + "님이 승리했습니다!", "WIN",
							JOptionPane.PLAIN_MESSAGE);
					win++;
				} else if (userScore < cpuScore) {
					JOptionPane.showMessageDialog(null, "CPU가 " + cpuScore + "빙고를 달성했습니다." + usern + "님이 패배했습니다.",
							"LOSE", JOptionPane.PLAIN_MESSAGE);
					lose++;
				} else if ((userScore + cpuScore) == -2) { // 무승부, 모든 판이 오픈되면 -1 리턴
					JOptionPane.showMessageDialog(null, "무승부입니다", "draw", JOptionPane.PLAIN_MESSAGE);
					draw++;
				}
				rateUpdate();
				System.exit(0);
			}
		}
		tf.setText("");
	}

	private int getBingoCount() {
		// TODO Auto-generated method stub

		int userBingo = 0;
		// 가로 검사
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (btnUser[i][j].getText() != "O")
					break;
				if (j == (size - 1)) {
					userBingo++;
				}
			}
		}
		// 세로검사
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (btnUser[j][i].getText() != "O")
					break;
				if (j == (size - 1)) {
					userBingo++;
				}
			}
		}
		int j = 0, i = 0;
		while (true) {
			if (btnUser[i][j].getText() != "O")
				break;
			if (j == (size - 1)) {
				userBingo++;
				break;
			}
			j++;
			i++;
		}

		i = 0;
		j = size - 1;
		while (true) {
			if (btnUser[i][j].getText() != "O")
				break;
			if (j == 0) {
				userBingo++;
				break;
			}
			j--;
			i++;
		}

		if (userBingo == (2 * size + 2))
			return -1;
		return userBingo;
	}

	private void rateUpdate() {
		// TODO Auto-generated method stub
		// 승률 = win / (win + lose)
		FileWriter fw;
		try {
			File old_File = new File("HSJrate.txt");
			old_File.delete();
			File NEW_File = new File("HSJrate.txt");

			fw = new FileWriter(NEW_File, false);
			fw.write(win + "\tnumberOfWin\n" + lose + "\tnumberOfLose\n" + draw + "\tnumberOfDraw");
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private Word boardChange(String inputU) {
		Word inputUW = null;
		// TODO Auto-generated method stub
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (userBoard[i][j].eng.equals(inputU)) { // 찾았으면
					inputUW = userBoard[i][j]; // word저장
					btnUser[i][j].setText("O");
					btnUser[i][j].setFont(new Font("고딕체", Font.BOLD, 40));
				}
			}
		}
		return inputUW;
	}
}
