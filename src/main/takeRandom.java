package main;


public class takeRandom {
	private final int i;
    private final int j;
    private final int action;
    
    public takeRandom(int i,int j,int action){
    	this.i = i;
    	this.j = j;
    	this.action = action;
    }
    public int getI(){
    	return i;
    }
    
    public int getJ(){
    	return j;
    }
    
    public int getAction(){
    	return action;
    }
}
