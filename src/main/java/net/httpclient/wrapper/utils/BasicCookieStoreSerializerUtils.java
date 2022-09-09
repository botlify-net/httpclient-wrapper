package net.httpclient.wrapper.utils;

import org.apache.http.impl.client.BasicCookieStore;

import java.io.*;
import java.util.Base64;

public class BasicCookieStoreSerializerUtils {

    public static String serializableToBase64(BasicCookieStore serializable) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(serializable);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static BasicCookieStore base64ToSerializable(String base64) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(base64);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        BasicCookieStore object = (BasicCookieStore)ois.readObject();
        ois.close();
        return (object);
    }

}
