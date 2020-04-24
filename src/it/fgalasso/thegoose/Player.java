package it.fgalasso.thegoose;

public class Player {
	
	private int position;
	private String name;
	private int prevPosition;
	
	public Player(String name) {
		super();
		this.name = name;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		this.prevPosition = this.position;
		this.position = this.prevPosition+position;
	}
	
	public void setPositionLight(int position) {
		this.position = position;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPrevPosition() {
		return prevPosition;
	}
	
	public void setPrevPosition(int prevPosition) {
		this.prevPosition = prevPosition;
	}

	@Override
	public String toString() {
		return "Player [position=" + position + ", name=" + name + ", prevPosition=" + prevPosition + "]";
	}
	
}
