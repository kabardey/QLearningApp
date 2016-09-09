package main;

public class stateModel {
	private int xCoor;
	private int yCoor;
	private int action;
	public double p;
	
	public stateModel(int xCoor,int yCoor,int action,double p){
		this.xCoor = xCoor;
		this.yCoor = yCoor;
		this.action = action;
		this.p = p;
	}
	
	public int getxCoor(){
		return xCoor;
	}
	
	public void setxCoor(int xCoor){
		this.xCoor = xCoor;
	}
	
	public int getyCoor(){
		return yCoor;
	}
	
	public void setyCoor(int yCoor){
		this.yCoor = yCoor;
	}
	
	public int getAction(){
		return action;
	}
	
	public void setAction(int action){
		this.action = action;
	}
	
	public double getp(){
		return p;
	}
	
	public void setp(double p){
		this.p = p;
	}
	
	
}
