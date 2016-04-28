package sudoku;
import java.lang.*;

public class Square {
	
	private int value;
	Boolean isFinal;
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Boolean getIsFinal() {
		return isFinal;
	}
	public void setIsFinal(Boolean isFinal) {
		this.isFinal = isFinal;
	}
	
}