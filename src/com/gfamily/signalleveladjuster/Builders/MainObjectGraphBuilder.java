package com.gfamily.signalleveladjuster.Builders;

import java.util.*;

import com.gfamily.common.logger.XposedBridgeLogger;
import com.gfamily.signalleveladjuster.Business.SignalLevelAdjuster;

public class MainObjectGraphBuilder
{
  private String _className;

  public MainObjectGraphBuilder( String mainClassName )
  {
    _className = mainClassName;
  }

  public Map<String, Object> BuildObjects()
  {
    HashMap<String, Object> objects = new HashMap<String, Object>();

    XposedBridgeLogger logger = new XposedBridgeLogger( _className + ": " );
    SignalLevelAdjuster signalLevelAdjuster = new SignalLevelAdjuster( logger );

    objects.put( "Logger", logger );
    objects.put( "SignalLevelAdjuster", signalLevelAdjuster );

    return objects;
  }
}
