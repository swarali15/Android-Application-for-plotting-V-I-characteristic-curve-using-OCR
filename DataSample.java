package com.example.graphi_v;
import com.jjoe64.graphview.series.DataPoint;

class DataSample
{
    public int  voltage;
    public int current;
    public DataPoint get()
    {
        DataPoint dataPoints = new DataPoint(voltage,current);
        return dataPoints;
    }
    public void set(int current,int voltage)
    {
        this.current=current;
        this.voltage = voltage;
    }
}
