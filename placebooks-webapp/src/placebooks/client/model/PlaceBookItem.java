package placebooks.client.model;

import placebooks.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.resources.client.ImageResource;

public class PlaceBookItem extends JavaScriptObject
{
	public enum ItemType
	{
		AUDIO("placebooks.model.AudioItem"), GPS("placebooks.model.GPSTraceItem"), IMAGE("placebooks.model.ImageItem"), TEXT(
				"placebooks.model.TextItem"), VIDEO("placebooks.model.VideoItem"), WEB("placebooks.model.WebBundleItem");

		private final String typeName;

		private ItemType(final String typeName)
		{
			this.typeName = typeName;
		}

		private String getTypeName()
		{
			return typeName;
		}
	}

	public static final native PlaceBookItem parse(final String json) /*-{
		return eval('(' + json + ')');
	}-*/;

	public static final native JsArray<PlaceBookItem> parseArray(final String json) /*-{
		return eval('(' + json + ')');
	}-*/;

	protected PlaceBookItem()
	{
	}

	public final native String getClassName() /*-{
		return this["@class"];
	}-*/;

	public final native String getGeometry() /*-{
		return this.geom;
	}-*/;

	public final native String getHash() /*-{
		return this.hash;
	}-*/;

	public final ImageResource getIcon()
	{
		if (is(ItemType.TEXT))
		{
			return Resources.INSTANCE.text();
		}
		else if (is(ItemType.IMAGE))
		{
			return Resources.INSTANCE.picture();
		}
		else if (is(ItemType.VIDEO))
		{
			return Resources.INSTANCE.movies();
		}
		else if (is(ItemType.AUDIO))
		{
			return Resources.INSTANCE.music();
		}
		else if (is(ItemType.GPS))
		{
			return Resources.INSTANCE.map();
		}
		else if (is(ItemType.WEB)) { return Resources.INSTANCE.web_page(); }
		return null;
	}

	public final native String getKey() /*-{
		return this.id;
	}-*/;

	public final native String getMetadata(String name) /*-{
		return this.metadata[name];
	}-*/;

	public final native String getMetadata(String name, final String defaultValue)
	/*-{
		if ('metadata' in this && name in this.metadata) {
			return this.metadata[name];
		}
		return defaultValue;
	}-*/;

	public final native int getParameter(String name) /*-{
		return this.parameters[name];
	}-*/;

	public final native int getParameter(String name, final int defaultValue)
	/*-{
		if ('parameters' in this && name in this.parameters) {
			return this.parameters[name];
		}
		return defaultValue;
	}-*/;

	public final String getShortClassName()
	{
		final String name = getClassName().toLowerCase();
		return name.substring(name.lastIndexOf(".") + 1);
	}

	public final native String getSourceURL() /*-{
		return this.sourceURL;
	}-*/;

	public final native String getText() /*-{
		return this.text;
	}-*/;

	private boolean isMedia(String shortClass)
	{
		return shortClass.equals("imageitem") || shortClass.equals("gpstraceitem") || shortClass.equals("audioitem")
				|| shortClass.equals("videoitem");
	}

	public final String getURL()
	{
		final String shortClass = getShortClassName();
		String key = getKey();
		if (key == null)
		{
			key = getMetadata("originalItemID", null);
		}
		if (key != null && isMedia(shortClass))
		{
			return GWT.getHostPageBaseURL() + "placebooks/a/admin/serve/"
				+ getShortClassName() + "/" + key + "?" + getHash();
		}

		return getSourceURL();
	}

	public final native boolean hasMetadata(String name) /*-{
		return 'metadata' in this && name in this.metadata;
	}-*/;

	public final native boolean hasParameter(String name) /*-{
		return 'parameters' in this && name in this.parameters;
	}-*/;

	public final boolean is(final ItemType type)
	{
		return getClassName().equals(type.getTypeName());
	}

	public final native void removeMetadata(String name)
	/*-{
		if (('metadata' in this)) {
			delete this.metadata[name];
		}
	}-*/;

	public final native void removeParameter(String name)
	/*-{
		if (('parameters' in this)) {
			delete this.parameters[name];
		}
	}-*/;

	public final native void setGeometry(String string)
	/*-{
		this.geom = string;
	}-*/;

	public final native void setKey(String key) /*-{
		this.id = key;
	}-*/;

	public final native void setMetadata(String name, String value)
	/*-{
		if (!('metadata' in this)) {
			this.metadata = new Object();
		}
		this.metadata[name] = value;
	}-*/;

	public final native void setParameter(String name, int value)
	/*-{
		if (!('parameters' in this)) {
			this.parameters = new Object();
		}
		this.parameters[name] = value;
	}-*/;

	public final native void setSourceURL(String value) /*-{
		this.sourceURL = value;
	}-*/;

	public final native void setText(String newText) /*-{
		this.text = newText;
	}-*/;

	public final native void setHash(String hash)
	/*-{
		this.hash = hash;
	}-*/;
}
