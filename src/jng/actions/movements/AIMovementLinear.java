package jng.actions.movements;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.component.Component;
import jng.ai.AIBehaviour;

public class AIMovementLinear extends AIMovementAction{
	
	private int nextIndex;
	
	private int index;
	
	// 1 for moving from 0 to length-1 and 
	// -1 for moving from length-1 to 0
	private int movingToEndPosition;
	
	private Vector2f direction;
	
	private float distance;
	
	private AIBehaviour aiBehaviour;

	public AIMovementLinear(float speed, int index, int nextIndex, AIBehaviour aiBehaviour) {
		super(speed, aiBehaviour.getPath()[0]);
		this.index = index;
		this.nextIndex = nextIndex;
		this.aiBehaviour = aiBehaviour;
		movingToEndPosition = 1;
		direction = new Vector2f(aiBehaviour.getPath()[nextIndex]).sub(position);
		direction.normalise();
		distance = position.distance(aiBehaviour.getPath()[nextIndex]);
	}

	@Override
	public Vector2f getNextPosition(Vector2f position, float speed,
			float rotation, int delta) {
		Vector2f pos = new Vector2f(position);
		
		float hip = speed * delta;
	    
		// a standard explicit euler integration is used
		pos.x += direction.x * hip;
		pos.y += direction.y * hip;
		if (pos.distance(aiBehaviour.getPath()[index]) >= distance)
		{
			
			if (nextIndex == aiBehaviour.getPath().length-1 ||
					nextIndex == 0)
			{
				index += movingToEndPosition;
				movingToEndPosition = -movingToEndPosition;
			}
			else
				index += movingToEndPosition;
			nextIndex += movingToEndPosition;
			direction = new Vector2f(aiBehaviour.getPath()[nextIndex]).sub(pos);
			direction.normalise();
			distance = pos.distance(aiBehaviour.getPath()[nextIndex]);
		}
		
		return pos;
	}
	
	public Vector2f getSpeedInDirection()
	{
		return new Vector2f(speed*direction.x, speed*direction.y);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
		Vector2f nextPos = getNextPosition(event.getOwnerEntity().getPosition(),
				speed, event.getOwnerEntity().getRotation(), delta);
		event.getOwnerEntity().setPosition(nextPos);
	}

	@Override
	public float getNextRotation(float speed, float rotation, int delta) {
		// TODO Auto-generated method stub
		return 0;
	}


}
