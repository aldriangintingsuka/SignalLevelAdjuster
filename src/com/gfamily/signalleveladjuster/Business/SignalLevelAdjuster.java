package com.gfamily.signalleveladjuster.Business;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import com.gfamily.common.logger.ILogger;

import android.telephony.SignalStrength;
import de.robv.android.xposed.XC_MethodHook;

public class SignalLevelAdjuster implements ISignalLevelAdjuster
{
  private ILogger _logger;
  private int _maxLevel;
  private int _numberOfQuantization;

  public SignalLevelAdjuster( ILogger logger )
  {
    _logger = logger;
  }

  public void SetMaxLevel( int maxLevel )
  {
    _maxLevel = maxLevel;
  }

  public void SetNumberOfQuantization( int numberOfQuantization )
  {
    _numberOfQuantization = numberOfQuantization;
  }

  @Override
  public void AdjustSignal()
  {
    // correction for signal strength level
    XC_MethodHook getLevelHook = new XC_MethodHook()
      {
        @Override
        protected void afterHookedMethod( MethodHookParam param ) throws Throwable
        {
          //WriteLog( "Old level = " + (Integer) param.getResult() );
          int asu = ( (SignalStrength) param.thisObject ).getGsmSignalStrength();
          int correctedLevel = GetCorrectedLevel( asu );
          //WriteLog( "New level = " + correctedLevel );
          param.setResult( correctedLevel );
        }

        private int GetCorrectedLevel( int asu )
        {
          int correctedLevel = GetSignalLevel( asu );
          //WriteLog( "New GSM level = " + correctedLevel );

          correctedLevel = correctedLevel - 10000;

          return correctedLevel;
        }

        private int GetSignalLevel( int asu )
        {
          //WriteLog( "Old GSM level = " + asu );

          if( asu == 99 ) return 10000;

          asu = asu >= _maxLevel ? _maxLevel : asu;
          int newGsmLevel = 10000 + asu * _numberOfQuantization / _maxLevel;

          return newGsmLevel;
        };

      };

    findAndHookMethod( SignalStrength.class, "getLevel", getLevelHook );
  }

  private void WriteLog( String message )
  {
    _logger.LogInfo( message );
  }

}
