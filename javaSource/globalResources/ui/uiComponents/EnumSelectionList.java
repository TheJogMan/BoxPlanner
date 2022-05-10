package globalResources.ui.uiComponents;

import java.util.HashMap;
import java.util.UUID;

import globalResources.ui.UIHolder;
import globalResources.utilities.VectorInt;

public class EnumSelectionList<E extends Enum<E>> extends SelectionList
{
	HashMap<UUID, Enum<E>> valueMap;
	HashMap<Enum<E>, UUID> keyMap;
	E defaultValue;
	
	public EnumSelectionList(UIHolder parent, VectorInt position, VectorInt dimensions, E defaultValue)
	{
		super(parent, position, dimensions);
		valueMap = new HashMap<UUID, Enum<E>>();
		keyMap = new HashMap<Enum<E>, UUID>();
	}
	
	public void setDefaultValue(E defaultValue)
	{
		if (defaultValue != null) this.defaultValue = defaultValue;
	}
	
	public E getDefaultValue()
	{
		return defaultValue;
	}
	
	public void setOptions(Enum<E>[] enms)
	{
		setOptions(enms, false);
	}
	
	public void setOptions(Enum<E>[] enms, boolean update)
	{
		HashMap<UUID, String> nameMap = new HashMap<UUID, String>();
		valueMap.clear();
		keyMap.clear();
		for (int index = 0; index < enms.length; index++)
		{
			UUID id = UUID.randomUUID();
			nameMap.put(id, enms[index].name());
			valueMap.put(id, enms[index]);
			keyMap.put(enms[index], id);
		}
		super.updateContents(nameMap, update);
	}
	
	@Override
	public void updateContents(HashMap<UUID, String> contents)
	{
		
	}
	
	@Override
	public void updateContents(HashMap<UUID, String> contents, boolean update)
	{
		
	}
	
	public void setSelectedEnum(Enum<E> enm)
	{
		setSelectedEnum(enm, false);
	}
	
	public void setSelectedEnum(Enum<E> enm, boolean trigger)
	{
		if (keyMap.containsKey(enm)) setSelection(keyMap.get(enm), trigger);
	}
	
	public UUID getEnumKey(Enum<E> enm)
	{
		if (keyMap.containsKey(enm)) return keyMap.get(enm);
		else return null;
	}
	
	public Enum<E> getEnum(UUID key)
	{
		if (key == null) return defaultValue;
		else if (valueMap.containsKey(key)) return valueMap.get(key);
		else return null;
	}
	
	@Override
	public String getSelectedItem()
	{
		return getSelectedEnum().name();
	}
	
	public boolean hasEnum(Enum<E> enm)
	{
		return keyMap.containsKey(enm);
	}
	
	@Override
	public UUID addItem(String item)
	{
		return null;
	}
	
	@Override
	public void setItem(UUID id, String item)
	{
		
	}
	
	@Override
	public void removeItem(UUID id)
	{
		Enum<E> enm = getEnum(id);
		valueMap.remove(id);
		keyMap.remove(enm);
		super.removeItem(id);
	}
	
	@Override
	public void clear()
	{
		valueMap.clear();
		keyMap.clear();
		super.clear();
	}
	
	public Enum<E> getSelectedEnum()
	{
		return getEnum(getSelectedID());
	}
}