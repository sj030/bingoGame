package javaFinalWork2;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener {
	Container frame = this.getContentPane();
	JPanel startPanel;
	JLabel titleL, userNameL, rateL, mapSizeL, welcomeL;
	JButton startButton;
	JComboBox<Integer> mapSizeC;
	DefaultComboBoxModel<Integer> mapSizeModel;

	List<Word> voc = new ArrayList<>();
	String userName;
	int win, lose, draw;
	double rate;
	int mapSize = 3;
	Integer[] mapSizeRange = { 3, 4, 5, 6, 7, 8, 9, 10 };

	public MainFrame(String uName, String ratefile, String vocfile) {
		userName = uName;
		int isOpenedR = 0, isOpenedV = 0;

		// [생성자에서 승률 파일 읽기]
		try {
			Scanner scan = new Scanner(new File(ratefile));
			while (scan.hasNextLine()) {
				String str = scan.nextLine();
				String[] temp = str.split("\t");
				if (temp[1].compareTo("numberOfWin") == 0) {
					win = (int) Double.parseDouble(temp[0]);
				} else if (temp[1].compareTo("numberOfLose") == 0) {
					lose = (int) Double.parseDouble(temp[0]);
				} else if (temp[1].compareTo("numberOfDraw") == 0) {
					draw = (int) Double.parseDouble(temp[0]);
				}
			}

			if ((win == 0) && (lose == 0) && (draw == 0)) {
				rate = 0;
			} else {
				rate = (((double) win) / (double) (win + lose + draw)) * 100;
			}

			System.out.println(userName + "의 승률이 업로드 되었습니다.");
			isOpenedR = 1;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, userName + "의 승률이 업로드 되지 않았습니다. \n파일명을 확인하고 다시 시작하세요", "Message",
					JOptionPane.ERROR_MESSAGE);

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
					"파일 내부 상태를 확인해주세요.\n" + "형식은 (숫자)\tnumberOfWin\n(숫자)\t	numberOfLose\n(숫자)\tnumberOfDraw입니다.",
					"Message", JOptionPane.ERROR_MESSAGE);
		}

		// 생성자에서 단어 파일 읽기
		try (Scanner scan = new Scanner(new File(vocfile))) {
			while (scan.hasNextLine()) {
				String str = scan.nextLine();
				String[] temp = str.split("\t");
				this.addWord(new Word(temp[0].trim(), temp[1].trim()));
			}
			isOpenedV = 1;
			System.out.println(userName + "의 단어장이 생성되었습니다.");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, userName + "의 단어장이 생성되지 않았습니다. \n 파일명을 확인하세요.", "Message",
					JOptionPane.ERROR_MESSAGE);
		}

		// 두 파일이 모두 실행되었을 때만 실행
		if ((isOpenedR == 1) && (isOpenedV == 1)) {
			this.setTitle("202211404 황서진 빙고게임");
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.setSize(1000, 500);
			initComponent();
			this.setVisible(true);
		}
	}

	private void addWord(Word word) {
		// TODO Auto-generated method stub
		voc.add(word);
	}

	private void initComponent() {
		// TODO Auto-generated method stub
		startPanel = new JPanel(new BorderLayout());
		JPanel north = new JPanel(new FlowLayout());
		JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));

		// **NORTH부분, titleL 담음
		titleL = new JLabel("BINGO - GAME");
		titleL.setFont(new Font("Serif", Font.BOLD, 100));
		north.add(titleL); // 가장위에 title 부착

		// **CENTER부분 중, inform 담는 패널 제작
		JPanel inform = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
		userNameL = new JLabel("USERNAME: " + userName + "님");
		userNameL.setFont(new Font("고딕체", Font.ITALIC, 25));

		rateL = new JLabel("승률: " + String.format("%.0f", rate) + "%");
		rateL.setFont(new Font("고딕체", Font.PLAIN, 25));

		mapSizeL = new JLabel("맵 크기: ");
		mapSizeL.setFont(new Font("고딕체", Font.PLAIN, 25));

		mapSizeModel = new DefaultComboBoxModel<>(mapSizeRange);
		mapSizeC = new JComboBox<Integer>(mapSizeModel);

		inform.add(userNameL);
		inform.add(rateL);
		inform.add(mapSizeL);
		inform.add(mapSizeC);

		// **CENTER부분 중, 환영, 시작버튼 담는 패널 제작
		JPanel start = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
		welcomeL = new JLabel(
				userName + "님, " + Integer.toString(mapSize) + " * " + Integer.toString(mapSize) + "맵의 빙고게임을 시작해보세요!");
		welcomeL.setFont(new Font("고딕체", Font.PLAIN, 20));
		startButton = new JButton("->  START  <-");
		startButton.setFont(new Font("Serif", Font.BOLD, 40));

		start.add(welcomeL);
		start.add(startButton);

		// initListener
		startButton.addActionListener(this);
		mapSizeC.addActionListener(this);

		center.add(inform);
		center.add(start);

		// 최종 패널 부착
		startPanel.add(north, BorderLayout.NORTH);
		startPanel.add(center, BorderLayout.CENTER);
		frame.add(startPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == startButton) {
			dispose();
			new GamePage(this, voc);

		} else if (e.getSource() == mapSizeC) {
			mapSize = (Integer) mapSizeC.getSelectedItem();
			welcomeL.setText(userName + "님, " + Double.toString(mapSize) + " * " + Double.toString(mapSize)
					+ "맵의 빙고게임을 시작해보세요!");

		}
	}

	public static void main(String[] args) {
		new MainFrame("황서진", "HSJrate.txt", "words1.txt");
	}
}
