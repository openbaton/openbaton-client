package org.openbaton.sdk.api.rest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
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
import org.openbaton.catalogue.nfvo.Script;
import org.openbaton.catalogue.nfvo.VNFPackage;
import org.openbaton.sdk.api.util.AbstractRestAgent;


import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by ogo on 03.03.16.
 */
public class VNFPackageAgent extends AbstractRestAgent<VNFPackage> {

    public VNFPackageAgent(String username, String password, String nfvoIp, String nfvoPort, String path, String version) {
        super(username, password, nfvoIp, nfvoPort, path, version, VNFPackage.class);
    }
    private static byte[]  scripttoString(JSONArray payload) {
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
        for (int k = 0; k < arrayS.length - 1; k++) {
            int l = Integer.parseInt(arrayS[k]);

            b[k] = (byte)l;

        }
        //System.out.println(arrayS[1]);
        //System.out.println(b[0]);
        return b;
    }


    public List<VNFPackage> findAll() {
        //System.out.println("Accessing Packages");
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
        try {
            listScripts(respS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(listScripts(respS));
        List<VNFPackage> packages = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            VNFPackage newPackage = new VNFPackage();
            JSONObject elem = array.getJSONObject(i);
            newPackage.setId(elem.getString("id"));
            try {
                newPackage.setImageLink(elem.getString("imageLink"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
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
                newPackage.setScripts(listScripts(respS));
            } catch (Exception e) {
                e.printStackTrace();
            }
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


    private static Set<Script> listScripts(String respS) throws Exception {
        Set<Script> scriptsList = new HashSet<>();
        JSONArray array = new JSONArray(respS);

        //System.out.println("Starting conversion");
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            JSONArray scripts = obj.getJSONArray("scripts");
            for (int j = 0; j < scripts.length(); j++) {
                Script elem = new Script();
                elem.setId(scripts.getJSONObject(j).getString("id"));
                elem.setVersion(scripts.getJSONObject(j).getInt("version"));
                elem.setName(scripts.getJSONObject(j).getString("name"));
                JSONArray payload = scripts.getJSONObject(j).getJSONArray("payload");
                elem.setPayload(scripttoString(payload));
                scriptsList.add(elem);
            }
        }
        //System.out.println(scriptsString);
        //System.out.println("Done");
        return scriptsList;
    }
    public void create(String file) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(this.baseUrl);
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
    public void delete(String id) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpDelete delete = new HttpDelete(baseUrl + "/" + id);
        try {
            client.execute(delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] arg) throws IOException {

        VNFPackageAgent agent = new VNFPackageAgent(null, null, "localhost", "8080", "/vnf-packages", "1");
        try {
            //agent.create("/home/packages/scscf.tar");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            agent.findAll();
            //agent.delete("1e0b17c9-7ad2-4641-99ae-be17aa2f3e54");

        } catch (Exception e) {
            e.printStackTrace();
        }



    }



    }




