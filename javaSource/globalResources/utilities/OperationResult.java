package globalResources.utilities;

/**
 * Represents the result of an operation
 */
public final class OperationResult
{
	private static boolean defaultSuccess					= true;
	public static String defaultShortReason					= "No reason given.";
	public static String defaultLongReason					= "No additional reasoning given.";
	private static Object[] defaultAdditionalInformation	= new Object[0];
	private static long defaultResultID						= 0;
	
	private boolean success;
	private String shortReason;
	private String longReason;
	private Object[] additionalInformation;
	private long resultID;
	private int reClaimDepth;
	
	/**
	 * 
	 * @param success
	 * @param shortReason
	 * @param additionalInformation
	 * @param longReason
	 * @param resultID
	 */
	public OperationResult(boolean success, String shortReason, Object[] additionalInformation, String longReason, long resultID)
	{
		this.success = success;
		this.shortReason = shortReason;
		this.additionalInformation = additionalInformation;
		this.longReason = longReason;
		this.resultID = resultID;
		reClaimDepth = 0;
	}
	
	/**
	 * <p>
	 * success = true
	 * </p>
	 * <p>
	 * shortReason = No reason given.
	 * </p>
	 * <p>
	 * longReason = No additional reasoning given.
	 * </p>
	 * <p>
	 * additionalInformation = empty array with length of 0
	 * </p>
	 * <p>
	 * resultID = 0
	 * </p>
	 */
	public OperationResult()
	{
		this(defaultSuccess, defaultShortReason, defaultAdditionalInformation, defaultLongReason, defaultResultID);
	}
	
	/**
	 * <p>
	 * success = false
	 * </p>
	 * <p>
	 * shortReason = No reason given.
	 * </p>
	 * <p>
	 * longReason = No additional reasoning given.
	 * </p>
	 * <p>
	 * additionalInformation = empty array with length of 0
	 * </p>
	 * @param resultID
	 */
	public OperationResult(long resultID)
	{
		this(false, defaultShortReason, defaultAdditionalInformation, defaultLongReason, resultID);
	}
	
	public OperationResult(long resultID, boolean success, String shortReason, String longReason, Object... additionalInformation)
	{
		this(success, shortReason, additionalInformation, longReason, resultID);
	}
	
	/**
	 * <p>
	 * shortReason = No reason given.
	 * </p>
	 * <p>
	 * longReason = No additional reasoning given.
	 * </p>
	 * <p>
	 * additionalInformation = empty array with length of 0
	 * </p>
	 * @param resultID
	 * @param success
	 */
	public OperationResult(long resultID, boolean success)
	{
		this(success, defaultShortReason, defaultAdditionalInformation, defaultLongReason, resultID);
	}
	
	/**
	 * <p>
	 * success = true
	 * </p>
	 * <p>
	 * longReason = No additional reasoning given.
	 * </p>
	 * <p>
	 * additionalInformation = empty array with length of 0
	 * </p>
	 * @param resultID
	 * @param shortReason
	 */
	public OperationResult(long resultID, String shortReason)
	{
		this(false, shortReason, defaultAdditionalInformation, defaultLongReason, resultID);
	}
	
	/**
	 * Transfers components from nested result to this new one
	 * <p>
	 * success = true
	 * </p>
	 * <p>
	 * longReason = No additional reasoning given.
	 * </p>
	 * <p>
	 * additionalInformation = additionalInformation from nested result followed by this resultID and shortReason
	 * </p>
	 * @param result
	 * @param newShortReason
	 * @param resultID
	 */
	public OperationResult(OperationResult result, String newShortReason, long resultID)
	{
		this(result.success, newShortReason, result.additionalInformation, result.longReason, resultID);
		
		Object[] newInfo = new Object[additionalInformation.length + 2];
		newInfo[newInfo.length - 2] = result.resultID;
		newInfo[newInfo.length - 1] = result.shortReason;
		for (int index = 0; index < additionalInformation.length; index++) newInfo[index] = additionalInformation[index];
		
		additionalInformation = newInfo;
		reClaimDepth = result.reClaimDepth + 1;
	}
	
	/**
	 * Transfers components from nested result to this new one
	 * <p>
	 * success = true
	 * </p>
	 * <p>
	 * longReason = No additional reasoning given.
	 * </p>
	 * <p>
	 * additionalInformation = additionalInformation from nested result followed by this resultID and the nested results shortReason
	 * </p>
	 * @param result
	 * @param resultID
	 */
	public OperationResult(OperationResult result, long resultID)
	{
		this(result, result.shortReason, resultID);
	}
	
	/**
	 * Whether the operation was successful
	 * @return success
	 */
	public boolean successful()
	{
		return success;
	}
	
	/**
	 * Short reason given for the outcome of the operation
	 * @return shortReason
	 */
	public String reason()
	{
		return shortReason;
	}
	
	/**
	 * Long reason given for the outcome of the operation
	 * @return longReason
	 */
	public String longReason()
	{
		return longReason;
	}
	
	/**
	 * Additional information provided by the operation
	 * <p>
	 * Typically this can be used to return the output of the operation
	 * </p>
	 * @return additionalInformation
	 */
	public Object[] additionalInformation()
	{
		return additionalInformation;
	}
	
	/**
	 * An ID code for this particular outcome of the operation
	 * @return resultID
	 */
	public long resultID()
	{
		return resultID;
	}
	
	/**
	 * How many times the original OperationResult got nested
	 * @return depth
	 */
	public int reClaimDepth()
	{
		return reClaimDepth;
	}
}