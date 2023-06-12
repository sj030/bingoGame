package javaFinalWork2;

public class Word {
	String eng;
	String kor;
	
	public Word(String eng, String kor) {
		super();
		this.eng = eng;
		this.kor = kor;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub	
		return "영단어: " + eng + "\t뜻: " + kor;
	}
}

