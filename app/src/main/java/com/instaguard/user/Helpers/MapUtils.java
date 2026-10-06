package com.instaguard.user.Helpers;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

public class MapUtils {

   public static Polyline showCurvedPolyline(
           GoogleMap googleMap,
           LatLng startPoint,
           LatLng endPoint,
           double k,
           int routeColor
   ) {
      // Calculate distance and heading between two points
      double d = SphericalUtil.computeDistanceBetween(startPoint, endPoint);
      double h = SphericalUtil.computeHeading(startPoint, endPoint);
      // Midpoint position
      LatLng p = SphericalUtil.computeOffset(startPoint, d * 0.5, h);
      // Apply some mathematics to calculate position of the circle center
      double x = (1 - k * k) * d * 0.5 / (2 * k);
      double r = (1 + k * k) * d * 0.5 / (2 * k);
      LatLng c = SphericalUtil.computeOffset(p, x, h + 90.0);
      // Polyline options
      PolylineOptions options = new PolylineOptions();
      // Calculate heading between circle center and two points
      double h1 = SphericalUtil.computeHeading(c, startPoint);
      double h2 = SphericalUtil.computeHeading(c, endPoint);
      // Calculate positions of points on circle border and add them to polyline options
      int numpoints = 100;
      double step = (h2 - h1) / numpoints;
      for (int i = 0; i < numpoints; i++) {
         LatLng pi = SphericalUtil.computeOffset(c, r, h1 + i * step);
         options.add(pi);
      }
      // Draw polyline
      return googleMap.addPolyline(options.width(10f).color(routeColor).geodesic(false));
   }
}

