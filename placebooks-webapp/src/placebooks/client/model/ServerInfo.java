package placebooks.client.model;

import com.google.gwt.core.client.JavaScriptObject;

public class ServerInfo extends JavaScriptObject
{
	protected ServerInfo()
	{
	}

	public final native String getOpenSpaceBaseURL()
	/*-{
		return this.openSpaceBaseURL;
	}-*/;

	public final native String getOpenSpaceHost()
	/*-{
		return this.openSpaceHost;
	}-*/;

	public final native String getOpenSpaceKey()
	/*-{
		return this.openSpaceKey;
	}-*/;

	public final native String getServerName()
	/*-{
		return this.serverName;
	}-*/;
	
	public final native int getImageSize()
	/*-{
		return this.maxImageSize;
	}-*/;
	
	public final native int getVideoSize()
	/*-{
		return this.maxVideoSize;
	}-*/;
	
	public final native int getAudioSize()
	/*-{
		return this.maxAudioSize;
	}-*/;			
}