package com.gfamily.signalleveladjuster.Business;

public interface ISignalLevelAdjuster
{
  void AdjustSignal();
  void SetMaxLevel( int maxLevel );
  void SetNumberOfQuantization( int numberOfQuantization );
}
