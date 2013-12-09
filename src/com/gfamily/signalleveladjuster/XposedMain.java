package com.gfamily.signalleveladjuster;

import java.util.Map;

import com.gfamily.common.logger.ILogger;
import com.gfamily.signalleveladjuster.Builders.MainObjectGraphBuilder;
import com.gfamily.signalleveladjuster.Business.ISignalLevelAdjuster;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class XposedMain implements IXposedHookLoadPackage
{
  private String _packageName;
  private ILogger _logger;
  private ISignalLevelAdjuster _signalLevelAdjuster;

  public XposedMain()
  {
    BuildObjects();

    try
    {
      InitializeFromSettings();
    }
    catch( Exception e )
    {
      WriteLog( e.getMessage() );
    }
  }

  private void BuildObjects()
  {
    _packageName = getClass().getPackage().getName();
    MainObjectGraphBuilder builder = new MainObjectGraphBuilder( _packageName );
    Map<String, Object> objects = builder.BuildObjects();

    _logger = (ILogger) objects.get( "Logger" );
    _signalLevelAdjuster = (ISignalLevelAdjuster) objects.get( "SignalLevelAdjuster" );
  }

  private void InitializeFromSettings() throws Exception
  {
    _signalLevelAdjuster.SetMaxLevel( 16 );
    _signalLevelAdjuster.SetNumberOfQuantization( 6 );
  }

  @Override
  public void handleLoadPackage( LoadPackageParam lpparam ) throws Throwable
  {
    HookSignalLevel( lpparam );
  }

  private void HookSignalLevel( LoadPackageParam lpparam )
  {
    if( !lpparam.packageName.equals( "com.android.systemui" ) ) return;

    WriteLog( "Loading SystemUI for signal adjustment hook" );

    _signalLevelAdjuster.AdjustSignal();
  }

  private void WriteLog( String message )
  {
    _logger.LogInfo( message );
  }
}
