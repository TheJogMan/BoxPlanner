package globalResources.commander;

public class BlankValidator implements AbstractValidator
{
	@Override
	public final ValidationResult validate(AbstractExecutor executor)
	{
		return new ValidationResult(executor, true, "Valid");
	}
}