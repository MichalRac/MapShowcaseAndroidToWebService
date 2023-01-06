package com.example.mapshowcaseandroidtowebservice;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class TestRequest {
    private static final String NAMESPACE = "MapShowcase"; // com.service.ServiceImpl
    private static final String URL = "http://192.168.0.32:8080/MapShowcaseAndroidToWebService/MapShowcase?wsdl";
    private static final String METHOD_NAME = "getEncodedMap";
    private static final String SOAP_ACTION = "http://192.168.0.32:8080/MapShowcaseAndroidToWebService/MapShowcase/getEncodedMap";

    private String webResponse = "";
    private Thread thread;

    public void startWebAccess(IMapProviderListener listener, Double long0, Double lat0, Double long1, Double lat1 ) {
        thread = new Thread() {
            public void run() {
                try {
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    PropertyInfo propInfoArg0 = new PropertyInfo();
                    propInfoArg0.setName("lat0");
                    propInfoArg0.setType(Double.class);
                    propInfoArg0.setValue(lat0);
                    request.addProperty(propInfoArg0);

                    PropertyInfo propInfoArg1 = new PropertyInfo();
                    propInfoArg1.setName("long0");
                    propInfoArg1.setType(Double.class);
                    propInfoArg1.setValue(long0);
                    request.addProperty(propInfoArg1);

                    PropertyInfo propInfoArg2 = new PropertyInfo();
                    propInfoArg2.setName("lat1");
                    propInfoArg2.setType(Double.class);
                    propInfoArg2.setValue(lat1);
                    request.addProperty(propInfoArg2);

                    PropertyInfo propInfoArg3 = new PropertyInfo();
                    propInfoArg3.setName("long1");
                    propInfoArg3.setType(Double.class);
                    propInfoArg3.setValue(long1);
                    request.addProperty(propInfoArg3);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.setOutputSoapObject(request);

                    MarshalDouble md = new MarshalDouble();
                    md.register(envelope);

                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                    androidHttpTransport.call(SOAP_ACTION, envelope);

                    SoapObject objectResult = (SoapObject) envelope.bodyIn;
                    webResponse = objectResult.toString();
                    System.out.println("response: " + webResponse);

                    int startIndex = webResponse.indexOf("=") + 1;
                    int endIndex = webResponse.lastIndexOf("=") + 1;
                    String formattedResponse = webResponse.substring(startIndex, endIndex);

                    listener.OnMapProvided(formattedResponse);

                } catch (SoapFault sp) {
                    sp.getMessage();
                    System.out.println("error = " + sp.getMessage());

                } catch (Exception e) {
                    System.out.println("problem8");
                    e.printStackTrace();

                    webResponse = "Connection/Internet problem";
                }

            }
        };

        thread.start();
    }
}
