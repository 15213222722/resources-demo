package com.resourses.tool.SerializeUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {
	/**
	 * 对象序列化
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {
	        byte[] bytes = null;
	       try {
	    	   ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
	    	   ObjectOutputStream objectOS = new ObjectOutputStream(byteOS);
		    	objectOS.writeObject(object);
		    	bytes=byteOS.toByteArray();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         return bytes;
  }



	/**
	 * 对象反序列化
	 * @param bytes
	 * @return
	 */
	public static Object deserialization( byte[] bytes) {
        	 Object object = null;
			try {
				 ObjectInputStream objectIS = new ObjectInputStream(new ByteArrayInputStream(bytes));
				 object = objectIS.readObject();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         return object;

  }
}
