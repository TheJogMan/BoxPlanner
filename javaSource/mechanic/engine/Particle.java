package mechanic.engine;

import java.util.ArrayList;
import java.util.Iterator;

import globalResources.utilities.Logger;
import globalResources.utilities.Logger.MessageType;
import globalResources.utilities.ShiftingLinkedList;
import globalResources.utilities.Vector;
import globalResources.utilities.VectorInt;

public abstract class Particle
{
	protected Vector position;
	private boolean keep;
	private int amount;
	private double size;
	private double speed;
	
	protected abstract void init(Object[] extraData);
	protected abstract void updateParticle();
	protected abstract void drawParticle(VectorInt drawPosition);
	
	public final int getAmount()
	{
		return amount;
	}
	
	public final double getSize()
	{
		return size;
	}
	
	public final double getSpeed()
	{
		return speed;
	}
	
	public final boolean removed()
	{
		return !keep;
	}
	
	public final void remove()
	{
		keep = false;
	}
	
	public final Vector getPosition()
	{
		return position.clone();
	}
	
	//-----------------------------------------------------------------------------------------static bits for particle management
	
	public static void createEffect(Class<? extends Particle> type, Vector position, int count, double size, double speed, Object... extraData)
	{
		for (int index = 0; index < count; index++)
		{
			try
			{
				Particle particle = type.newInstance();
				particle.keep = true;
				particle.position = position.clone();
				particle.amount = count;
				particle.speed = speed;
				particle.size = size;
				particle.init(extraData);
				particleAdditionQueue.add(particle);
			}
			catch (Exception e)
			{
				Logger.defaultLogger.log("Could not create particle " + type.getName(), e);
			}
		}
	}
	
	private static ArrayList<Particle> activeParticles;
	private static ArrayList<Particle> particleAdditionQueue;
	
	static void update()
	{
		int size = particleAdditionQueue.size();
		ShiftingLinkedList<Particle> queue = new ShiftingLinkedList<Particle>();
		for (int index = 0; index < size; index++)
		{
			Particle particle = particleAdditionQueue.get(index);
			activeParticles.add(particle);
			queue.add(particle);
		}
		for (Iterator<Particle> iterator = queue.iterator(); iterator.hasNext();) particleAdditionQueue.remove(iterator.next());
		queue.clear();
		for (Iterator<Particle> iterator = activeParticles.iterator(); iterator.hasNext();)
		{
			Particle particle = iterator.next();
			particle.updateParticle();
			if (!particle.keep) queue.add(particle);
		}
		for (Iterator<Particle> iterator = queue.iterator(); iterator.hasNext();) activeParticles.remove(iterator.next());
	}
	
	static void draw()
	{
		Vector offset = Engine.getCameraOffset();
		for (Iterator<Particle> iterator = activeParticles.iterator(); iterator.hasNext();)
		{
			Particle particle = iterator.next();
			Vector drawPosition = particle.position.clone();
			drawPosition.add(offset);
			particle.drawParticle(new VectorInt(drawPosition));
		}
	}
	
	static void init()
	{
		if (activeParticles == null)
		{
			Logger.defaultLogger.log("Initializing Particle system");
			activeParticles = new ArrayList<Particle>();
			particleAdditionQueue = new ArrayList<Particle>();
		}
		else Logger.defaultLogger.log("Could not initialize Particle system, already initialized.", MessageType.ERROR);
	}
}