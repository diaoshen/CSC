package edu.diao.design.observer;

public class Listener2 implements Listener {

	@Override
	public void update(int x, int y) {
		// TODO Auto-generated method stub
		System.out.println("I'm Listener 2  and There is update !");
		
		System.out.println("X = " + x + " Y = " + y);
	}

}
