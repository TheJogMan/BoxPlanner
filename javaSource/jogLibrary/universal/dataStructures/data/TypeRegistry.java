package jogLibrary.universal.dataStructures.data;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jogLibrary.universal.Result;
import jogLibrary.universal.ReturnResult;
import jogLibrary.universal.dataStructures.data.TypeRegistry.RegisteredType.ValidationState;
import jogLibrary.universal.dataStructures.data.values.BooleanValue;
import jogLibrary.universal.dataStructures.data.values.ByteValue;
import jogLibrary.universal.dataStructures.data.values.CharacterValue;
import jogLibrary.universal.dataStructures.data.values.DoubleValue;
import jogLibrary.universal.dataStructures.data.values.FloatValue;
import jogLibrary.universal.dataStructures.data.values.IntegerValue;
import jogLibrary.universal.dataStructures.data.values.LongValue;
import jogLibrary.universal.dataStructures.data.values.ShortValue;
import jogLibrary.universal.dataStructures.data.values.StringValue;
import jogLibrary.universal.dataStructures.data.values.UUIDValue;
import jogLibrary.universal.dataStructures.data.values.Vector3DoubleValue;
import jogLibrary.universal.indexable.Consumer;

public class TypeRegistry
{
	private HashMap<Class<?>, RegisteredType> classMap = new HashMap<>();
	private HashMap<String, RegisteredType> nameMap = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	boolean init(boolean log)
	{
		ArrayList<Result> results = new ArrayList<>();
		
		results.add(register("Byte", ByteValue.class, log));
		results.add(register("Boolean", BooleanValue.class, log));
		results.add(register("Short", ShortValue.class, log));
		results.add(register("Integer", IntegerValue.class, log));
		results.add(register("Long", LongValue.class, log));
		results.add(register("Float", FloatValue.class, log));
		results.add(register("Double", DoubleValue.class, log));
		results.add(register("UUID", UUIDValue.class, log));
		results.add(register("Character", CharacterValue.class, log));
		results.add(register("String", StringValue.class, log));
		results.add(register("Vector3Double", Vector3DoubleValue.class, log));
		
		results.add(register("ListValue", (Class<? extends Value<?, ?>>)ListValue.class, log));
		results.add(register("InvalidValue", InvalidValue.class, log));
		results.add(register("Data", DataValue.class, log));
		
		boolean allValidated = true;
		for (Iterator<Result> iterator = results.iterator(); iterator.hasNext();)
		{
			Result result = iterator.next();
			if (!result.success())
			{
				allValidated = false;
				break;
			}
		}
		return allValidated;
	}
	
	public Result register(String name, Class<? extends Value<?, ?>> valueType)
	{
		if (classMap.containsKey(valueType))
			return new Result("Could not register type " + valueType.getName() + " as '" + name + "': Already registered under a different name.");
		else if (nameMap.containsKey(name))
			return new Result("Could not register type " + valueType.getName() + " as '" + name + "': Another type is already registered under that name.");
		
		ReturnResult<RegisteredType> creationResult = RegisteredType.create(name, valueType);
		if (!creationResult.success())
			return new Result("Could not register type " + valueType.getName() + " as '" + name + "': Not a valid value type implementation: " + creationResult.description());
		creationResult.value().validationState = ValidationState.PROCESSING;
		classMap.put(valueType, creationResult.value());
		nameMap.put(name, creationResult.value());
		
		Result validationResult = validate(creationResult.value());
		if (!validationResult.success())
		{
			creationResult.value().validationState = ValidationState.FAILED;
			nameMap.remove(name);
			classMap.remove(valueType);
			return new Result("Could not register type " + valueType.getName() + " as '" + name + "': Validation Failure: " + validationResult.description());
		}
		else
		{
			creationResult.value().validationState = ValidationState.PASSED;
			return new Result(true);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Result validate(RegisteredType type)
	{
		List<Value<?, ?>> values;
		try
		{
			values = (List<Value<?, ?>>)type.validationValueObjects.invoke(null);
		}
		catch (Exception e)
		{
			return new Result("Exception occured getting validation value objects, " + Result.describeExceptionFull(e));
		}
		if (values == null)
			return new Result("Validation value objects can not be null.");
		if (values.size() < 1)
			return new Result("There must be at least 1 validation value object.");
		
		List<Value<?, ?>> validationValues;
		try
		{
			validationValues = (List<Value<?, ?>>)type.validationValues.invoke(null);
		}
		catch (Exception e)
		{
			return new Result("Exception occurred getting validation values, " + Result.describeExceptionFull(e));
		}
		if (validationValues == null)
			return new Result("Validation values can not be null.");
		if (validationValues.size() < 1)
			return new Result("There must be at least 1 validation value.");
		
		
		Result result = values.get(0).validate();
		if (!result.success())
			return result;
		
		for (int validationValue = 0; validationValue < values.size(); validationValue++)
		{
			Object value = values.get(validationValue);
			if (value == null)
				return new Result("Validation value can not be null, validation value #" + validationValue);
			
			result = values.get(validationValue).validateValidationValue(validationValue);
			if (!result.success())
				return result;
		}
		
		return new Result();
	}
	
	public List<RegisteredType> registeredTypes()
	{
		return new ArrayList<RegisteredType>(nameMap.values());
	}
	
	public Result register(String name, Class<? extends Value<?, ?>> valueType, boolean logIssues)
	{
		Result result = register(name, valueType);
		if (logIssues && !result.success())
			System.err.println(result.description());
		else if (logIssues)
			System.out.println("Registered type " + valueType + " as '" + name + "'");
		return result;
	}
	
	/**
	 * Get the registration of a Value Type
	 * @param type The Value Type you want the registration of
	 * @return The registration of the Value Type, or null if the given class was not a registered Value Type
	 * @since 1.0
	 * @author TheJogMan
	 */
	public RegisteredType get(Class<?> type)
	{
		return classMap.get(type);
	}
	
	/**
	 * Get the registration of a Value Type
	 * @param name The name of the Value Type you want the registration of
	 * @return The registration of the Value Type, or null if no Value Type was registered with the given name
	 * @since 1.0
	 * @author TheJogMan
	 */
	public RegisteredType get(String name)
	{
		return nameMap.get(name);
	}
	
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface EmptyValue
	{
		
	}
	
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface ByteConsumer
	{
		
	}
	
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface CharacterConsumer
	{
		
	}
	
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface ValidationValues
	{
		
	}
	
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface ValidationValueObjects
	{
		
	}
	
	public static final class RegisteredType
	{
		Method emptyValue;
		Method byteConsumer;
		Method characterConsumer;
		Method validationValues;
		Method validationValueObjects;
		
		//we organize the required methods into a table instead of just hard-coding their unique checks, so that new methods can be added more easily
		//I wish that it were possible to define the annotation, field, and RequiredMethod object all in one spot to improve organization, but alas Java hates fun
		private static final RequiredMethod[] requiredMethods =
		{
			new RequiredMethod(EmptyValue.class, "emptyValue", false, (returnType, valueType, consumptionResultType) ->
			{
				if (returnType.equals(consumptionResultType))
					return new Result();
				else
					return new Result("Must return " + consumptionResultType.getTypeName());
			}),
			
			new RequiredMethod(ByteConsumer.class, "byteConsumer", true, (returnType, valueType, consumptionResultType) ->
			{
				//make sure the return type is a parameterized type
				if (!(returnType instanceof ParameterizedType))
					return new Result("Must return " + Consumer.class.getName() + "<" + Value.class.getName() + "<?, " + consumptionResultType + ">, " + Byte.class.getName() + ">");
				ParameterizedType rootType = (ParameterizedType)returnType;
				
				//make sure the return type is a Consumer with 2 set parameters
				if (!(rootType.getRawType().equals(Consumer.class) && rootType.getActualTypeArguments().length == 2))
					return new Result("Must return " + Consumer.class.getName() + "<" + Value.class.getName() + "<?, " + consumptionResultType + ">, " + Byte.class.getName() + ">");
				
				//make sure that the first parameter is a parameterized type, and that the second is a Byte
				if (!(rootType.getActualTypeArguments()[0] instanceof ParameterizedType && rootType.getActualTypeArguments()[1].equals(Byte.class)))
					return new Result("Must return " + Consumer.class.getName() + "<" + Value.class.getName() + "<?, " + consumptionResultType + ">, " + Byte.class.getName() + ">");
				
				//make sure that the first parameter is a Value with two set parameters
				ParameterizedType consumptionResultRoot = (ParameterizedType)rootType.getActualTypeArguments()[0];
				if (!(consumptionResultRoot.getRawType().equals(Value.class) && consumptionResultRoot.getActualTypeArguments().length == 2))
					return new Result("Must return " + Consumer.class.getName() + "<" + Value.class.getName() + "<?, " + consumptionResultType + ">, " + Byte.class.getName() + ">");
				
				//make sure that the first value parameter is a wild card and that the second value parameter is the consumption result type
				if (!(consumptionResultRoot.getActualTypeArguments()[0] instanceof WildcardType && consumptionResultRoot.getActualTypeArguments()[1].equals(consumptionResultType)))
					return new Result("Must return " + Consumer.class.getName() + "<" + Value.class.getName() + "<?, " + consumptionResultType + ">, " + Byte.class.getName() + ">");
				
				//if we have reached this point, then all is good
				return new Result();
			}),
			
			new RequiredMethod(CharacterConsumer.class, "characterConsumer", true, (returnType, valueType, consumptionResultType) ->
			{
				//make sure the return type is a parameterized type
				if (!(returnType instanceof ParameterizedType))
					return new Result("Must return " + Consumer.class.getName() + "<" + Value.class.getName() + "<?, " + consumptionResultType + ">, " + Character.class.getName() + ">");
				ParameterizedType rootType = (ParameterizedType)returnType;
				
				//make sure the return type is a Consumer with 2 set parameters
				if (!(rootType.getRawType().equals(Consumer.class) && rootType.getActualTypeArguments().length == 2))
					return new Result("Must return " + Consumer.class.getName() + "<" + Value.class.getName() + "<?, " + consumptionResultType + ">, " + Character.class.getName() + ">");
				
				//make sure that the first parameter is a parameterized type, and that the second is a Character
				if (!(rootType.getActualTypeArguments()[0] instanceof ParameterizedType && rootType.getActualTypeArguments()[1].equals(Character.class)))
					return new Result("Must return " + Consumer.class.getName() + "<" + Value.class.getName() + "<?, " + consumptionResultType + ">, " + Character.class.getName() + ">");
				
				//make sure that the first parameter is a Value with two set parameters
				ParameterizedType consumptionResultRoot = (ParameterizedType)rootType.getActualTypeArguments()[0];
				if (!(consumptionResultRoot.getRawType().equals(Value.class) && consumptionResultRoot.getActualTypeArguments().length == 2))
					return new Result("Must return " + Consumer.class.getName() + "<" + Value.class.getName() + "<?, " + consumptionResultType + ">, " + Character.class.getName() + ">");
				
				//make sure that the first value parameter is a wild card and that the second value parameter is the consumption result type
				if (!(consumptionResultRoot.getActualTypeArguments()[0] instanceof WildcardType && consumptionResultRoot.getActualTypeArguments()[1].equals(consumptionResultType)))
					return new Result("Must return " + Consumer.class.getName() + "<" + Value.class.getName() + "<?, " + consumptionResultType + ">, " + Character.class.getName() + ">");
				
				//if we have reached this point, then all is good
				return new Result();
			}),
			
			new RequiredMethod(ValidationValues.class, "validationValues", true, (returnType, valueType, consumptionResultType) ->
			{
				if (!(returnType instanceof ParameterizedType))
					return new Result("Must return " + List.class.getName() + "<" + consumptionResultType.getTypeName() + ">");
				ParameterizedType type = (ParameterizedType)returnType;
				if (!(type.getRawType().equals(List.class) && type.getActualTypeArguments().length == 1 && type.getActualTypeArguments()[0].equals(consumptionResultType)))
					return new Result("Must return " + List.class.getName() + "<" + consumptionResultType.getTypeName() + ">");
				return new Result();
			}),
			
			new RequiredMethod(ValidationValueObjects.class, "validationValueObjects", true, (returnType, valueType, consumptionResultType) ->
			{
				//make sure the type is parameterized
				if (!(returnType instanceof ParameterizedType))
					return new Result("Must return " + List.class.getName() + "<" + Value.class.getName() + "<?, " + consumptionResultType.getTypeName() + ">>");
				
				//make sure its a List object that has a set parameter, and is not a raw List type
				ParameterizedType type = (ParameterizedType)returnType;
				if (!type.getRawType().equals(List.class) && type.getActualTypeArguments().length == 1)
					return new Result("Must return " + List.class.getName() + "<" + Value.class.getName() + "<?, " + consumptionResultType.getTypeName() + ">>");
				
				//now we must determine if the parameter is a Value object, and get its value type, which needs to be done differently depending on if its a generic Value object or a subclass
				Type parameterValue1;
				Type parameterValue2;
				Type parameter = type.getActualTypeArguments()[0];
				//first we check if its a generic Value object
				if (parameter instanceof ParameterizedType && ((ParameterizedType)parameter).getRawType().equals(Value.class))
				{
					ParameterizedType parameterizedParameter = (ParameterizedType)parameter;
					//if we have a generic value object, we must make sure it has a set parameter and isn't a raw type, if so then that parameter will be its value type
					if (parameterizedParameter.getActualTypeArguments().length == 2)
					{
						parameterValue1 = parameterizedParameter.getActualTypeArguments()[0];
						parameterValue2 = parameterizedParameter.getActualTypeArguments()[1];
					}
					else
						return new Result("Must return " + List.class.getName() + "<" + Value.class.getName() + "<?, " + consumptionResultType.getTypeName() + ">>");
				}
				else
				{
					//screw it, not gonna try and figure out how to check for subclasses right now
					return new Result("Must return " + List.class.getName() + "<" + Value.class.getName() + "<?, " + consumptionResultType.getTypeName() + ">>");
				}
				
				//finally we must make sure that the parameter value is the right value type
				if (!(parameterValue1 instanceof WildcardType && parameterValue2.equals(consumptionResultType)))
					return new Result("Must return " + List.class.getName() + "<" + Value.class.getName() + "<?, " + consumptionResultType.getTypeName() + ">>");
				
				//if we reached this point then all checks out
				return new Result();
			})
		};
		
		@SuppressWarnings("unchecked")
		public Consumer<Value<?, ?>, Byte> byteConsumer()
		{
			try
			{
				return (Consumer<Value<?, ?>, Byte>)byteConsumer.invoke(null);
			}
			catch (Exception e)
			{
				return null;
			}
		}
		
		@SuppressWarnings("unchecked")
		public Consumer<Value<?, ?>, Character> characterConsumer()
		{
			try
			{
				return (Consumer<Value<?, ?>, Character>)characterConsumer.invoke(null);
			}
			catch (Exception e)
			{
				return null;
			}
		}
		
		public List<?> validationValues()
		{
			try
			{
				return (List<?>)validationValues.invoke(null);
			}
			catch (Exception e)
			{
				return null;
			}
		}
		
		public Value<?, ?> newInstance()
		{
			try
			{
				return (Value<?, ?>)typeClass.newInstance();
			}
			catch (Exception e)
			{
				return null;
			}
		}
		
		@SuppressWarnings("unchecked")
		public List<Value<?, ?>> validationValueObjects()
		{
			try
			{
				return (List<Value<?, ?>>)validationValueObjects.invoke(null);
			}
			catch (Exception e)
			{
				return null;
			}
		}
		
		private static interface ReturnValidator
		{
			Result validate(Type returnType, Type valueType, Type consumptionResultType);
		}
		
		private static class RequiredMethod
		{
			private RequiredMethod(Class<? extends Annotation> annotation, String fieldName, boolean isStatic, ReturnValidator returnValidator)
			{
				this.annotation = annotation;
				this.returnValidator = returnValidator;
				this.isStatic = isStatic;
				
				try
				{
					this.field = RegisteredType.class.getDeclaredField(fieldName);
				}
				catch (NoSuchFieldException | SecurityException e)
				{//these exceptions should in theory never occur, but the IDE complains if we don't handle them
					System.err.println("Data TypeRegistry Could not initialize required method " + annotation.getSimpleName() + ": Could not find " + fieldName + " field.");
				}
			}
			
			ReturnValidator returnValidator;
			Class<? extends Annotation> annotation;
			Field field;
			boolean isStatic;
		}
		
		enum ValidationState
		{
			PENDING(false), PROCESSING(true), PASSED(true), FAILED(false);
			
			boolean canCreateInstance;
			
			ValidationState(boolean canCreateInstance)
			{
				this.canCreateInstance = canCreateInstance;
			}
		}
		
		Class<? extends Value<?, ?>> typeClass;
		String name;
		Type valueType;
		Type consumptionResultType;
		ValidationState validationState = ValidationState.PENDING;
		
		public Class<? extends Value<?, ?>> typeClass()
		{
			return typeClass;
		}
		
		public Type valueType()
		{
			return valueType;
		}
		
		public String name()
		{
			return name;
		}
		
		static ReturnResult<RegisteredType> create(String name, Class<? extends Value<?, ?>> typeClass)
		{
			Type valueType;
			Type consumptionResultType;
			//we begin by checking to make sure that this class extends the Value class
			Type superClass = typeClass.getGenericSuperclass();
			if (superClass instanceof ParameterizedType)
			{
				ParameterizedType parameterizedSuper = (ParameterizedType)superClass;
				if (parameterizedSuper.getRawType().equals(Value.class))
				{
					//if it does, then we can now extract the actual type of this value
					valueType = parameterizedSuper.getActualTypeArguments()[0];
					consumptionResultType = parameterizedSuper.getActualTypeArguments()[1];
				}
				else
					return new ReturnResult<>("Does not extend " + Value.class.getName());
			}
			else
				return new ReturnResult<>("Does not extend " + Value.class.getName());
			
			
			RegisteredType type = new RegisteredType();
			type.name = name;
			type.typeClass = typeClass;
			type.valueType = valueType;
			type.consumptionResultType = consumptionResultType;
			
			//once we know that this is indeed a Value class, and have the value type, we now need to check for all of our expected methods
			//and ensure that they are all defined properly
			try
			{
				Method[] methods = typeClass.getMethods();
				for (int methodIndex = 0; methodIndex < methods.length; methodIndex++)
				{
					for (int fieldIndex = 0; fieldIndex < requiredMethods.length; fieldIndex++)
					{
						//iterating through all the methods in the class, as well as all the annotations we are looking for
						//we single out the methods that have the proper annotations
						RequiredMethod method = requiredMethods[fieldIndex];
						if (methods[methodIndex].isAnnotationPresent(method.annotation))
						{
							//make sure the method has the correct modifiers and no arguments
							if (methods[methodIndex].getParameterCount() != 0)
								return new ReturnResult<>(method.annotation.getSimpleName() + " method must not require arguments.");
							int modifiers = methods[methodIndex].getModifiers();
							if (method.isStatic && !Modifier.isStatic(modifiers))
								return new ReturnResult<>(method.annotation.getSimpleName() + " method must be static.");
							else if (!method.isStatic && Modifier.isStatic(modifiers))
								return new ReturnResult<>(method.annotation.getSimpleName() + " method must not be static.");
							if (!Modifier.isPublic(modifiers))
								return new ReturnResult<>(method.annotation.getSimpleName() + " method must be public.");
							if (Modifier.isAbstract(modifiers))
								return new ReturnResult<>(method.annotation.getSimpleName() + " method must not be abstract.");
							
							//now we check to make sure there is only one method for each annotation
							if (method.field.get(type) != null)
								return new ReturnResult<>("Only one " + method.annotation.getSimpleName() + " method can be provided.");
							
							//validate that this method has the correct return type, given the annotation and value type
							Result result = method.returnValidator.validate(methods[methodIndex].getGenericReturnType(), type.valueType, type.consumptionResultType);
							if (!result.success())
								return new ReturnResult<>(method.annotation.getSimpleName() + " method does not have the correct return type: " + result.description());
							//if the method is properly defined, then we can set the field
							method.field.set(type, methods[methodIndex]);
						}
					}
				}
				
				//lastly, we check to make sure we found a method for each annotation, and if everything is in order then we can return a valid result
				for (int fieldIndex = 0; fieldIndex < requiredMethods.length; fieldIndex++)
				{
					if (requiredMethods[fieldIndex].field.get(type) == null)
						return new ReturnResult<>(requiredMethods[fieldIndex].annotation.getSimpleName() + " method was not provided.");
				}
				return new ReturnResult<>(type);
			}
			catch (IllegalAccessException e)
			{
				return new ReturnResult<>("Fields could not be accessed.");
			}//these exceptions should in theory never occur, but the IDE complains if we don't handle them, and I don't mind being thorough
			catch (IllegalArgumentException e)
			{
				return new ReturnResult<>("Fields could not be assigned.");
			}
		}
	}
}