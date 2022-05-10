package mechanic.entities;

import globalResources.graphics.Canvas;

/**
 * Abstract AI module
 */
public abstract class AI
{
	public final Entity entity;
	
	/**
	 * Creates a new instance of this AI module, and adds it to the given entity
	 * @param entity the entity to receive this AI module
	 */
	public AI(Entity entity)
	{
		this.entity = entity;
		entity.aiModules.add(this);
	}
	
	/**
	 * Removes this AI module from its entity
	 */
	public void remove()
	{
		if (entity.useAIQueue)
		{
			entity.new AIRemove(this);
		}
		else
		{
			removed();
			entity.aiModules.remove(this);
		}
	}
	
	/**
	 * Called during this AI modules update cycle
	 */
	public abstract void update();
	/**
	 * Called when this AI module is removed from its entity
	 */
	public abstract void removed();
	/**
	 * Called during this AI modules draw cycle
	 * @param canvas the canvas the entity was drawn to
	 * @param x the x coordinate on the canvas where the entity was drawn
	 * @param y the y coordinate on the canvas where the entity was drawn
	 */
	public abstract void draw(Canvas canvas, int x, int y);
}