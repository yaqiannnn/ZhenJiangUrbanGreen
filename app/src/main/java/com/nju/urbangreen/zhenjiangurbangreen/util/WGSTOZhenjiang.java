package com.nju.urbangreen.zhenjiangurbangreen.util;

import com.esri.core.geometry.Point;

/**
 * Created by HCQIN on 2016/9/28.
 */

public class WGSTOZhenjiang {

    public static Point WGS2ZJ(Point BL)
    {
        return WGS2ZJ(BL.getX(), BL.getY());
    }
    public static Point WGS2ZJ(double b,double l)
    {

        final double dx=-73.180552;
        final double dy=129.776953;
        final double dz=96.320427;
        final double wx=0.0000038563;
        final double wy=-0.0000027916;
        final double wz=0.0000106881;
        final double m=0.999989234988;

        double B,L,H;
        B=b*Math.PI/180;
        L=l*Math.PI/180;
        H=10;

        //计算WGS84空间直角坐标
        double a=6378137.00;
        double ee=0.0066943799013;
        double N=a/Math.sqrt(1-ee*Math.pow(Math.sin(B), 2));
        double X84=(N+H)*Math.cos(B)*Math.cos(L);
        double Y84=(N+H)*Math.cos(B)*Math.sin(L);
        double Z84=(N*(1-ee)+H)*Math.sin(B);

        //计算BJ54空间直角坐标
        double X54=dx+m*X84-wy*Z84+wz*Y84;
        double Y54=dy+m*Y84+wx*Z84-wz*X84;
        double Z54=dz+m*Z84-wx*Y84+wy*X84;
        //BJ54空间直角坐标转BJ54大地坐标
        double L54,B54,H54 = 10;
        double a54=6378245.0,b54=6356863.0188;
        double ee1=0.006693421622965949;
        double ee2=0.006738525415;
        double radius=180/Math.PI;
        double Lcentral_meridian=119.5;
        double yuzhi=0;
        L54=Math.atan(Y54/X54)+Math.PI;
        double B0=Math.atan(Z54*Math.sin(L54*radius)/(Y54*(1-ee1)));
        double N0=a54/Math.pow((1-ee1*Math.pow(Math.sin(B0), 2)), 0.5);
        double H0=Y54/(Math.cos(B0)*Math.sin(L54)-N0);
        double d0=H0*ee1/(N0+H0);
        double dd=-N0*d0*ee1*Math.cos(B0)*Math.sin(L54)/(Y54*(1-ee1))*(Math.pow(Math.sin(B0),2)-ee1*Math.pow(Math.sin(2*B0),2)/(4*(1-ee1*Math.pow(Math.sin(B0),2))));
        double temp=Z54*Math.sin(L54)/(Y54*(1-ee1+d0+dd));
        B54=Math.atan(temp);

        //高斯正算
        double N54=a54/Math.pow((1-ee1*Math.pow(Math.sin(B54), 2)),0.5);
        double t=Math.tan(B54);
        double n2=ee2*Math.pow(Math.cos(B54),2);
        double A0,A2,A4,A6,A8;
        A0=1+3.0/4*ee1+45.0/64*Math.pow(ee1, 2)+350.0/512*Math.pow(ee1, 3)+11025.0/16384*Math.pow(ee1, 4);
        A2=(3.0/4*ee1+60.0/64*Math.pow(ee1, 2)+525.0/512*Math.pow(ee1, 3)+17640.0/16384*Math.pow(ee1, 4))*0.5;
        A4=(15.0/64*Math.pow(ee1, 2)+210.0/512*Math.pow(ee1, 3)+8820.0/16384*Math.pow(ee1, 4))*1.0/4;
        A6=(35.0/512*Math.pow(ee1, 3)+2520.0/16384*Math.pow(ee1, 4))*1.0/6;
        A8=(315.0/16384*Math.pow(ee1, 4))*1.0/8;
        double pi=Math.PI/180;
        double Xzw=a54*(1-ee1)*(A0*(B54)+A2*Math.sin(-2*B54)+A4*Math.sin(4*B54)+A6*Math.sin(-6*B54)+A8*Math.sin(8*B54));
        double dL=L54-Lcentral_meridian*pi;

        //投影后坐标
        double zjx1=Xzw+N54/2*Math.sin(B54)*Math.cos(B54)*Math.pow(dL, 2)+N54/24*Math.sin(B54)*Math.pow(Math.cos(B54), 3)*(5-Math.pow(t, 2)+9*n2+4*Math.pow(n2, 2))*Math.pow(dL, 4);
        double zjx2=N54/720*Math.sin(B54)*Math.pow(Math.cos(B54), 5)*(61-58*Math.pow(t, 2)+Math.pow(t, 4))*Math.pow(dL, 6);
        double zjy1=N54*Math.cos(B54)*dL+N54/6.0*Math.pow(Math.cos(B54), 3)*(1-Math.pow(t, 2)+n2)*Math.pow(dL, 3);
        double zjy2=N54/120.0*Math.pow(Math.cos(B54), 5)*(5-18*Math.pow(t, 2)+Math.pow(t, 4)+14*n2-58*n2*Math.pow(t, 2))*Math.pow(dL, 5);
        double zjx=zjx1+zjx2;
        double zjy=zjy1+zjy2+500000;
        double zjh=H54;
        return new Point(zjy, zjx);
    }
}

