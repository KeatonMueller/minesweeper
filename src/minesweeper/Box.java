package minesweeper;

public class Box {
	private int value;
	private boolean flagged, clicked, bomb, checked;
	public Box(int v){
		value = v;
		flagged = false;
		clicked = false;
		bomb = false;
		checked = false;
	}
	public void setValue(int v){
		value = v;
		if(hasBomb()){
			value = 9;
		}
	}
	public void setFlagged(boolean f){
		flagged = f;
	}
	public void setClicked(boolean c){
		if(c)
			if(!isFlagged())
				clicked = c;
	}
	public void setBomb(boolean b){
		bomb = b;
		value = 9;
	}
	public void setChecked(boolean c){
		if(c)
			if(!isFlagged())
				checked = c;
	}
	public int getValue(){
		return value;
	}
	public boolean isFlagged(){
		return flagged;
	}
	public boolean isClicked(){
		return clicked;
	}
	public boolean hasBomb(){
		return bomb;
	}
	public boolean isChecked(){
		return checked;
	}
}
