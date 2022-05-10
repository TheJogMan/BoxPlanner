package boxPlanner;

import java.io.File;
import java.io.InputStreamReader;

import globalResources.utilities.FIO;
import jogLibrary.universal.dataStructures.data.Data;
import jogLibrary.universal.dataStructures.data.DataValue;
import jogLibrary.universal.dataStructures.data.ListValue;
import jogLibrary.universal.dataStructures.data.Value;
import jogLibrary.universal.dataStructures.data.values.StringValue;
import jogLibrary.universal.indexable.CharacterInputStreamEater;
import jogLibrary.universal.indexable.Consumer.ConsumerResult;

public class Settings
{
	static final File file = new File("Settings.txt");
	static DataValue settingsData = new DataValue();
	
	@SuppressWarnings("unchecked")
	static ListValue<DataValue> boxList = (ListValue<DataValue>)settingsData.get().put("BoxList", new ListValue<DataValue>(Data.typeRegistry.get(DataValue.class)));
	@SuppressWarnings("unchecked")
	static ListValue<DataValue> rollList = (ListValue<DataValue>)settingsData.get().put("RollList", new ListValue<DataValue>(Data.typeRegistry.get(DataValue.class)));
	@SuppressWarnings("unchecked")
	static ListValue<DataValue> caseList = (ListValue<DataValue>)settingsData.get().put("CaseList", new ListValue<DataValue>(Data.typeRegistry.get(DataValue.class)));
	
	public static void load()
	{
		settingsData.setFromCharacters(StringValue.indexer(FIO.readString(file)));
	}
	
	public static void defaults()
	{
		CharacterInputStreamEater eater = new CharacterInputStreamEater(new InputStreamReader(Settings.class.getResourceAsStream("/defaultSettings.txt")));
		ConsumerResult<Value<Data, Data>, Character> result = settingsData.setFromCharacters(eater.iterator());
		if (!result.success())
			System.out.println("Could not parse default settings: " + result.description().toString());
	}
	
	public static void save()
	{
		FIO.writeString(file, settingsData.toString());
	}
}