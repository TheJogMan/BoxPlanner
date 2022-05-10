package jogLibrary.universal.dataStructures.data;

public abstract class EnumValueUtil
{
	/*
	public EnumValueUtil()
	{
		super();
	}
	
	public EnumValueUtil(EnumType value)
	{
		super(value);
	}
	
	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] toByteData()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<Enum<?>, EnumType> makeDuplicate()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Value<?, ?> value)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String toString()
	{
		return convertOut(get().toString());
	}
	
	@Override
	public byte[] toByteData()
	{
		return (new StringValue(get().toString())).toByteData();
	}
	
	@Override
	public Value<SelectedRegionBehavior, SelectedRegionBehavior> makeDuplicate()
	{
		return new SelectedRegionBehaviorValue(get());
	}
	
	@Override
	public boolean equals(Value<?, ?> value)
	{
		if (value instanceof SelectedRegionBehaviorValue && ((SelectedRegionBehaviorValue)value).get().equals(get()))
			return true;
		else
			return false;
	}
	
	@EmptyValue
	public SelectedRegionBehavior emptyValue()
	{
		return SelectedRegionBehavior.IGNORE;
	}
	
	@CharacterConsumer
	public static Consumer<Value<?, SelectedRegionBehavior>, Character> characterConsumer()
	{
		return ((source) ->
		{
			ConsumerResult<String, Character> result = StringValue.consumeString(source, ' ');
			if (!result.success())
				return new ConsumerResult<>(source, result.description());
			else
			{
				try
				{
					return new ConsumerResult<>(new SelectedRegionBehaviorValue(SelectedRegionBehavior.valueOf(convertIn(result.value()))), source);
				}
				catch (Exception e)
				{
					return new ConsumerResult<>(source, "Invalid Selected Region behavior.");
				}
			}
		});
	}
	
	@ByteConsumer
	public static Consumer<Value<?, SelectedRegionBehavior>, Byte> byteConsumer()
	{
		return ((source) ->
		{
			ConsumerResult<String, Byte> result = StringValue.simpleByteConsume(source);
			if (!result.success())
				return new ConsumerResult<>(source, result.description());
			else
			{
				try
				{
					return new ConsumerResult<>(new SelectedRegionBehaviorValue(SelectedRegionBehavior.valueOf(result.value())), source);
				}
				catch (Exception e)
				{
					return new ConsumerResult<>(source, "Invalid Selected Region behavior.");
				}
			}
		});
	}
	
	@ValidationValues
	public static List<SelectedRegionBehavior> validationValues()
	{
		ArrayList<SelectedRegionBehavior> list = new ArrayList<SelectedRegionBehavior>(2);
		list.add(SelectedRegionBehavior.IGNORE);
		list.add(SelectedRegionBehavior.CONSTRAIN);
		return list;
	}
	
	@ValidationValueObjects
	public static List<Value<?, SelectedRegionBehavior>> validationValueObjects()
	{
		List<SelectedRegionBehavior> values = validationValues();
		ArrayList<Value<?, SelectedRegionBehavior>> objects = new ArrayList<>(values.size());
		values.forEach(value -> objects.add(new SelectedRegionBehaviorValue(value)));
		return objects;
	}
	*/
	//public static <EnumType extends Enum<?>> Consumer<EnumType, Byte> byteConusmer(Class<EnumType> cls)
	//{
	//	
	//}
	
	public static String convertOut(String name)
	{
		String newName = "";
		boolean capitalize = false;
		for (int index = 0; index < name.length(); index++)
		{
			char ch = name.charAt(index);
			if (ch == '_') capitalize = true;
			else
			{
				if (!capitalize) ch = Character.toLowerCase(ch);
				newName += ch;
				capitalize = false;
			}
		}
		return newName;
	}
	
	public static String convertIn(String name)
	{
		String newName = "";
		for (int index = 0; index < name.length(); index++)
		{
			char ch = name.charAt(index);
			if (Character.isUpperCase(ch))
			{
				newName += "_" + ch;
			}
			else newName += Character.toUpperCase(ch);
		}
		return newName;
	}
}