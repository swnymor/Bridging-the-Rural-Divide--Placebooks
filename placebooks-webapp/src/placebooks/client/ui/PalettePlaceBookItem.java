package placebooks.client.ui;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.json.client.JSONObject;

import placebooks.client.model.PlaceBookItem;

public class PalettePlaceBookItem extends PaletteItem
{
	private final PlaceBookItem item;
	
	public PalettePlaceBookItem(PlaceBookItem placeBookItem, final PlaceBookEditor editor)
	{
		super();
		
		this.item = placeBookItem;
	
		text.setText(placeBookItem.getMetadata("title", "Unnamed"));

		image.setResource(item.getIcon());		
		panel.addMouseDownHandler(new MouseDownHandler()
		{
			@Override
			public void onMouseDown(final MouseDownEvent event)
			{			
				PlaceBookItemWidget itemWidget = editor.getCanvas().add(item);
				editor.handleDragStart((PlaceBookItemWidgetFrame) itemWidget, event);
			}
		});		
	}
	
	PlaceBookItem createItem()
	{
		PlaceBookItem newItem = PlaceBookItem.parse(new JSONObject(item).toString());
		if(newItem.getKey() != null)
		{
			newItem.setMetadata("originalItemID", newItem.getKey());
			newItem.setKey(null);			
		}
		return newItem;
	}	
}