package org.openbaton.sdk.api.rest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openbaton.catalogue.nfvo.NFVImage;
import org.openbaton.catalogue.nfvo.VNFPackage;
import org.openbaton.sdk.api.util.AbstractRestAgent;


import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ogo on 03.03.16.
 */
public class VNFPackageAgent extends AbstractRestAgent<VNFPackage> {

    public VNFPackageAgent(String username, String password, String nfvoIp, String nfvoPort, String path, String version) {
        super(username, password, nfvoIp, nfvoPort, path, version, VNFPackage.class);
    }
    private static String scripttoString(JSONArray payload) {
        String script = null;
        ArrayList<String> list = new ArrayList<String>();
        if (payload != null) {
            int len = payload.length();
            for (int i=0;i<len;i++){
                list.add(payload.get(i).toString());
            }
        }
        String[] arrayS = new String[list.size()];
        arrayS = list.toArray(arrayS);
        byte[] b = new byte[arrayS.length];
        //System.out.println(arrayS[1]);
        for (int k = 0; k < arrayS.length - 1; k++) {
            int l = Integer.parseInt(arrayS[k]);

            b[k] = (byte)l;

        }

        try {
            script = new String(b, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        return script;
    }


    public List<VNFPackage> findAll() {
        System.out.println("Accessing Packages");
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://localhost:8080/api/v1/vnf-packages");
        CloseableHttpResponse response = null;
        try {
            response = client.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String respS = null;
        try {
            respS = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Fetching scripts");
        //System.out.println(respS);
        JSONArray array = new JSONArray(respS);
        //System.out.println(listScripts(respS));
        List<VNFPackage> packages = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            VNFPackage newPackage = new VNFPackage();
            JSONObject elem = array.getJSONObject(i);
            newPackage.setId(elem.getString("id"));
            newPackage.setImageLink(null);
            newPackage.setName(elem.getString("name"));
            newPackage.setVersion(elem.getInt("version"));
            NFVImage newImage = new NFVImage();
            newImage.setId(elem.getJSONObject("image").getString("id"));
            newImage.setVersion(elem.getJSONObject("image").getInt("version"));
            newImage.setMinRam(elem.getJSONObject("image").getInt("minRam"));
            newImage.setMinDiskSpace(elem.getJSONObject("image").getInt("minDiskSpace"));
            newImage.setIsPublic(elem.getJSONObject("image").getBoolean("isPublic"));
            newPackage.setImage(newImage);
            try {
                newPackage.setScriptsLink(elem.getString("scriptsLink"));
            }
            catch (Exception e) {
                newPackage.setImageLink(null);
            }
            packages.add(newPackage);

        }
        System.out.println(packages.toString());
        return packages;


    }


    private static String listScripts(String respS) throws Exception {

        String scriptsString = "";
        JSONArray array = new JSONArray(respS);

        System.out.println("Starting conversion");
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            JSONArray scripts = obj.getJSONArray("scripts");
            for (int j = 0; j < scripts.length(); j++) {
                JSONArray payload = scripts.getJSONObject(j).getJSONArray("payload");
                String final1 = scripttoString(payload);
                scriptsString = scriptsString.concat(final1);
                scriptsString = scriptsString.concat("\n\n\n\n");
            }


        }
        System.out.println("Done");
        return scriptsString;
    }
    public static void create(String file) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost:8080/api/v1/vnf-packages");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody
                ("file", new File(file), ContentType.APPLICATION_OCTET_STREAM, "file.ext");
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        try {
            HttpResponse response = client.execute(post);
            System.out.println(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] arg) throws IOException {
        try {
            //findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //postPackage("/home/packages/scscf.tar");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    }




