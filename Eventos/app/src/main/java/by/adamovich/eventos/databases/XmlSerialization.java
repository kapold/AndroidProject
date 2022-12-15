package by.adamovich.eventos.databases;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import by.adamovich.eventos.models.User;

public class XmlSerialization implements Serializable {
    String filename;

    public XmlSerialization(String filename) {
        this.filename = filename;
    }

    public void exportXml(Context context, List<User> dataList) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            serializer.startTag(null, "userList");
            for (int j = 0; j < dataList.size(); j++) {
                serializer.startTag(null, "user");

                serializer.startTag(null, "nickname");
                serializer.text(dataList.get(j).getNickname());
                serializer.endTag(null, "nickname");

                serializer.startTag(null, "name");
                serializer.text(dataList.get(j).getName());
                serializer.endTag(null, "name");

                serializer.startTag(null, "surname");
                serializer.text(dataList.get(j).getSurname());
                serializer.endTag(null, "surname");

                serializer.startTag(null, "phoneNumber");
                serializer.text(dataList.get(j).getPhoneNumber());
                serializer.endTag(null, "phoneNumber");

                serializer.endTag(null, "user");
            }
            serializer.endTag(null, "userList");
            serializer.endDocument();
            serializer.flush();

            fos.close();
        }
        catch (Exception ex) {
            Toast.makeText(context, "Ошибка экспорта XML", Toast.LENGTH_SHORT).show();
            Log.d("exportXml(): ", ex.getMessage());
        }
    }

    public List<User> importXml(Context context) {
        List<User> arrayResult = new ArrayList<>();
        FileInputStream fis;
        InputStreamReader isr;
        try {
            fis = context.openFileInput(filename);
            isr = new InputStreamReader(fis);

            char[] inputBuffer = new char[fis.available()];
            isr.read(inputBuffer);

            String data;
            data = new String(inputBuffer);

            isr.close();
            fis.close();

            /*
             * Converting the String data to XML format so
             * that the DOM parser understands it as an XML input.
            */

            InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));

            DocumentBuilderFactory dbf;
            DocumentBuilder db;
            NodeList items;
            Document dom;

            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            dom = db.parse(is);

            // Normalize the document
            dom.getDocumentElement().normalize();

            items = dom.getElementsByTagName("user");
            for (int i = 0; i < items.getLength(); i++) {
                User user = new User();
                Node item = items.item(i);
                NodeList parametres = item.getChildNodes();
                for (int j = 0; j < parametres.getLength(); j++) {
                    Node parametr = parametres.item(j);
                    if (parametr.getNodeName().equals("nickname"))
                        user.setNickname(parametr.getFirstChild().getNodeValue());
                    if (parametr.getNodeName().equals("name"))
                        user.setName(parametr.getFirstChild().getNodeValue());
                    if (parametr.getNodeName().equals("surname"))
                        user.setSurname(parametr.getFirstChild().getNodeValue());
                    if (parametr.getNodeName().equals("phoneNumber"))
                        user.setPhoneNumber(parametr.getFirstChild().getNodeValue());
                }
                arrayResult.add(user);
            }
        }
        catch (Exception ex) {
            Toast.makeText(context, "Ошибка импорта XML", Toast.LENGTH_SHORT).show();
            Log.d("importXml(): ", ex.getMessage());
        }
        return arrayResult != null ? arrayResult : new ArrayList<>();
    }
}
