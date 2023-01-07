/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/WebService.java to edit this template
 */
package MapShowcase;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;


@WebService(serviceName = "MapShowcaseService", targetNamespace = "MapShowcase")
public class MapShowcaseService {
    

final Double minLat = 54.04270752753084;
final Double maxLat = 54.11773308594;
final Double minLong = 21.314587417775144;
final Double maxLong = 21.442646800979137;

    @WebMethod(operationName = "getEncodedMap")
    public String getEncodedMap(@WebParam(name = "lat0") double lat0, @WebParam(name = "long0") double long0, @WebParam(name = "lat1") double lat1, @WebParam(name = "long1") double long1) {        
        System.out.println("lat0 = " + lat0);
        System.out.println("lat1 = " + lat1);
        System.out.println("long0 = " + long0);
        System.out.println("long1 = " + long1);

        Double ImageSize = 750D;
        
        lat0 = GetLatitudeAnchor(lat0);
        lat1 = GetLatitudeAnchor(lat1);
        long0 = GetLongitudeAnchor(long0);
        long1 = GetLongitudeAnchor(long1);

        System.out.println("lat0 = " + lat0);
        System.out.println("lat1 = " + lat1);
        System.out.println("long0 = " + long0);
        System.out.println("long1 = " + long1);
        
        if(lat0 == lat1)
        {
            lat0 = 0.001;
            lat1 = 1.0;
        }
        if(long0 == long1)
        {
            long0 = 0.001;
            long1 = 1.0;
        }
        
        int x = (int)(long0 * ImageSize);
        int y = (int)(ImageSize - (lat1 * ImageSize));

        int x2 = (int)((long1 * ImageSize));
        int y2 = (int)(ImageSize - (lat0 * ImageSize));
        
        int width = x2 - x;
        int height = y2 - y;
        
        System.out.println("x = " + x);
        System.out.println("y = " + y);
        System.out.println("x2 = " + x2);
        System.out.println("y2 = " + y2);
        System.out.println("width = " + width);
        System.out.println("height = " + height);

        try{
            InputStream fullMapImageBase64 = getClass().getClassLoader().getResourceAsStream("mapa.png");
            BufferedImage img = ImageIO.read(fullMapImageBase64);
            BufferedImage croppedImage = cropImage(img, new Rectangle(x, y, width, height));

            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            ImageIO.write(croppedImage, "png", baos);
            String cropped = Base64.getEncoder().encodeToString(baos.toByteArray());
            return cropped;

        }
        catch(IOException e)
        {
            
        }

        //TODO write your implementation code here:
        String testString = "hello world";
        String encodedString = Base64.getEncoder().encodeToString(testString.getBytes());
        return encodedString;
    }
    
    private Double GetLatitudeAnchor(Double lat)
    {        
        lat = Math.min(lat, maxLat);
        lat = Math.max(lat, minLat);
        
        Double divisor = maxLat - minLat;
        Double result = (lat - minLat) / divisor;
        
        return result;
    }
    
    private Double GetLongitudeAnchor(Double lon) {
        lon = Math.min(lon, maxLong);
        lon = Math.max(lon, minLong);
        
        Double divisor = maxLong - minLong;
        Double result = (lon - minLong) / divisor;
        
        return result;
    }
    
    private BufferedImage cropImage(BufferedImage src, Rectangle rect) {
      BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
      return dest; 
   }
}
