package model;

public interface Item {

	int getId();
	
	int getLength();
	
	int getWidth();
	
	@Override
	int hashCode();
	
	@Override
	boolean equals(Object obj);
	
	@Override
	String toString();
}
