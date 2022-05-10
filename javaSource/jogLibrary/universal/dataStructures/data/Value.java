package jogLibrary.universal.dataStructures.data;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import jogLibrary.universal.ByteArrayBuilder;
import jogLibrary.universal.Result;
import jogLibrary.universal.dataStructures.data.TypeRegistry.RegisteredType;
import jogLibrary.universal.dataStructures.data.values.StringValue;
import jogLibrary.universal.indexable.Consumer;
import jogLibrary.universal.indexable.Consumer.ConsumerResult;
import jogLibrary.universal.indexable.HoardingIndexer;
import jogLibrary.universal.indexable.Indexer;

public abstract class Value<ValueType, ConsumptionResult>
{
	public abstract String toString();
	public abstract byte[] toByteData();
	public abstract Value<ValueType, ConsumptionResult> makeDuplicate();
	public abstract boolean equals(Value<?, ?> value);
	
	private ValueType value = getEmptyValue();
	
	String name = null;
	Data parent = null;
	boolean persistent = false;
	ArrayList<ValueChangeListener<ValueType>> changeListeners = new ArrayList<>();
	
	public Value()
	{
		
	}
	
	public Value(ValueType initialValue)
	{
		set(initialValue);
	}
	
	public ValueChangeListener<ValueType> addChangeListener(ValueChangeListener<ValueType> listener)
	{
		changeListeners.add(listener);
		return listener;
	}
	
	public void removeChangeListener(ValueChangeListener<ValueType> listener)
	{
		changeListeners.remove(listener);
	}
	
	public ValueType get()
	{
		return value;
	}
	
	public static interface ValueChangeListener<ValueType>
	{
		void change(ValueType oldValue, ValueType newValue);
	}
	
	public void set(ValueType value)
	{
		ValueType old = this.value;
		this.value = value;
		persistent = true;
		changeListeners.forEach(listener ->
		{
			try
			{
				listener.change(old, value);
			}
			catch (Exception e)
			{
				System.err.println("Exception occurred while running change listener for value.");
				e.printStackTrace();
			}
		});
	}
	
	public Value<ValueType, ConsumptionResult> duplicate()
	{
		Value<ValueType, ConsumptionResult> value = makeDuplicate();
		value.persistent = persistent;
		return value;
	}
	
	public ConsumerResult<Value<ValueType, ConsumptionResult>, Byte> setFromBytes(Indexer<Byte> source)
	{
		ConsumerResult<Value<ValueType, ConsumptionResult>, Byte> result = getByteConsumer().consume(source);
		if (result.success())
			set(result.value().get());
		return result;
	}
	
	public ConsumerResult<Value<ValueType, ConsumptionResult>, Character> setFromCharacters(Indexer<Character> source)
	{
		ConsumerResult<Value<ValueType, ConsumptionResult>, Character> result = getCharacterConsumer().consume(source);
		if (result.success())
			set(result.value().get());
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ValueType getEmptyValue()
	{
		try
		{
			return (ValueType)Data.typeRegistry.get(getClass()).emptyValue.invoke(this);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Consumer<Value<ValueType, ConsumptionResult>, Byte> getByteConsumer()
	{
		try
		{
			return (Consumer<Value<ValueType, ConsumptionResult>, Byte>)Data.typeRegistry.get(getClass()).byteConsumer.invoke(null);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Consumer<Value<ValueType, ConsumptionResult>, Character> getCharacterConsumer()
	{
		try
		{
			return (Consumer<Value<ValueType, ConsumptionResult>, Character>)Data.typeRegistry.get(getClass()).characterConsumer.invoke(null);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			return null;
		}
	}
	
	public RegisteredType getType()
	{
		return Data.typeRegistry.get(getClass());
	}
	
	@Override
	public boolean equals(Object object)
	{
		if (object instanceof Value<?, ?>)
			return this.equals((Value<?, ?>)object);
		else
			return false;
	}
	
	Result validateValidationValue(int index)
	{
		Value<ValueType, ConsumptionResult> second;
		try
		{
			second = duplicate();
		}
		catch (Exception e)
		{
			return new Result("Exception occurred duplicating value, validation value #" + index + ", " + Result.describeExceptionFull(e));
		}
		
		second.set(get());
		boolean equal;
		try
		{
			equal = second.equals(this);
		}
		catch (Exception e)
		{
			return new Result("Exception occurred checking equality, validation value #" + index + ", " + Result.describeExceptionFull(e));
		}
		if (!equal)
			return new Result("Two instances did not equal each other despite having the same value, validation value #" + index);
		second = duplicate();
		if (!second.equals(this))
			return new Result("Duplicated value did not equal original, validation value #" + index);
		
		String string;
		try
		{
			string = second.toString();
		}
		catch (Exception e)
		{
			return new Result("Exception occurred converting to String, validation value #" + index + ", " + Result.describeExceptionFull(e));
		}
		if (string == null)
			return new Result("toString can not return null.");
		string += " ";
		ConsumerResult<Value<ValueType, ConsumptionResult>, Character> characterResult;
		HoardingIndexer<Character> characterSource = StringValue.indexer(string);
		try
		{
			characterResult = this.getCharacterConsumer().consume(characterSource);
		}
		catch (Exception e)
		{
			return new Result("Exception occurred parsing String, validation value #" + index + ", " + Result.describeExceptionFull(e));
		}
		if (characterResult == null)
			return new Result("Character consumer can not return null.");
		if (!characterResult.success())
			return new Result("Consumption error parsing String, validation value #" + index + ", " + characterResult.description());
		second.set(characterResult.value().get());
		if (!second.equals(this))
			return new Result("Value parsed from String does not equal original, validation value #" + index);
		if (characterSource.atEnd())
			return new Result("Character consumer consumed too much data.");
		else if (characterSource.atEnd())
			return new Result("Character consumer didn't consume enough data.");
		
		byte[] byteData;
		try
		{
			byteData = second.toByteData();
		}
		catch (Exception e)
		{
			return new Result("Exception occurred converting to byte data, validation value #" + index + ", " + Result.describeExceptionFull(e));
		}
		if (byteData == null)
			return new Result("toByteData can not return null.");
		ByteArrayBuilder builder = new ByteArrayBuilder();
		builder.add(byteData);
		builder.add(new byte[] {0});
		byteData = builder.toPrimitiveArray();
		ConsumerResult<Value<ValueType, ConsumptionResult>, Byte> byteResult;
		Indexer<Byte> byteSource = ByteArrayBuilder.indexer(byteData);
		try
		{
			byteResult = this.getByteConsumer().consume(byteSource);
		}
		catch (Exception e)
		{
			return new Result("Exception occurred parsing byte data, validation value #" + index + ", " + Result.describeExceptionFull(e));
		}
		if (byteResult == null)
			return new Result("Byte consumer can not return null.");
		if (!byteResult.success())
			return new Result("Consumption error parsing byte data, validation value #" + index + ", " + byteResult.description());
		second.set(byteResult.value().get());
		if (!second.equals(this))
			return new Result("Value parsed from byte data does not equal original, validation value #" + index);
		if (byteSource.atEnd())
			return new Result("Byte consumer consumed too much data.");
		else if (byteSource.atEnd())
			return new Result("Byte consumer didn't consume enough data.");
		
		return new Result();
	}
	
	@SuppressWarnings("unchecked")
	Result validate()
	{
		RegisteredType type = Data.typeRegistry.get(getClass());
		
		ValueType empty;
		try
		{
			empty = (ValueType)type.emptyValue.invoke(this);
		}
		catch (Exception e)
		{
			return new Result("Exception occurred getting empty value, " + Result.describeExceptionFull(e));
		}
		if (empty == null)
			return new Result("Empty value can not be null.");
		
		Consumer<Value<ValueType, ConsumptionResult>, Character> characterConsumer;
		Consumer<Value<ValueType, ConsumptionResult>, Byte> byteConsumer;
		try
		{
			characterConsumer = (Consumer<Value<ValueType, ConsumptionResult>, Character>)type.characterConsumer.invoke(null);
			if (characterConsumer == null)
				return new Result("Character consumer can not be null.");
		}
		catch (Exception e)
		{
			return new Result("Exception occurred getting character consumer, " + Result.describeExceptionFull(e));
		}
		try
		{
			byteConsumer = (Consumer<Value<ValueType, ConsumptionResult>, Byte>)type.byteConsumer.invoke(null);
			if (byteConsumer == null)
				return new Result("Byte consumer can not be null.");
		}
		catch (Exception e)
		{
			return new Result("Exception occurred getting byte consumer, " + Result.describeExceptionFull(e));
		}
		
		ValueType getSetTest;
		try
		{
			getSetTest = get();
		}
		catch (Exception e)
		{
			return new Result("Exception occurred when getting value, " + Result.describeExceptionFull(e));
		}
		try
		{
			set(getSetTest);
		}
		catch (Exception e)
		{
			return new Result("Exception occurred when setting value, " + Result.describeExceptionFull(e));
		}
		
		return new Result();
	}
}