package model;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class PalletAdapter {

	/** Map of pallet elements. */
	private Map palletMap;
	
	Map<String, Object> keys;

	public PalletAdapter(Map palletMap) {
		this.palletMap = palletMap;
		keys = new HashMap<String, Object>();
		for (Object key : palletMap.keySet()) {
			keys.put(key.toString().substring(1), key);
		}
	}
	
	public Integer getLength() {
		return ((Long) palletMap.get(keys.get("length"))).intValue();
	}
	
	public Integer getWidth() {
		return ((Long) palletMap.get(keys.get("width"))).intValue();
	}
}
