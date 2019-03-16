package com.systec.hello;

import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

public class Exec {
    private static final String TAG = "Exec";

    private static int i = 0;

    private static final String key = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEpAIBAAKCAQEAtGzm3soVKxhQ166qaCK67BFYAWFNfC6kZOb8ZN4GEpyQJ8EH\n" +
            "KXjyu/zdh8XwIKKu/l9xE62m2QHPGk6Jp0iCdEaOBw9LON5iH5R29HTHSaliQ2tU\n" +
            "/wKvV7Cgqa6oElZx7xR8nkfHRUPZH90HsH2xPYp1PrxXyfa+qRQPwwFr5useZwhF\n" +
            "/fr2aUlyWBJwuO+Vvx7IHTrIpBQKYPIz4DQ/X3pqsUwqrpVOOKRngYD3ps/lh+gH\n" +
            "fmk13agQmYJVPlHhTqF6iv+PPZ9Os0gJDQhoYMzbnKCNpOo0LkGy5sVwVMVjeuY/\n" +
            "5qne525F5qRZmSAd/ev2gBNHPo7LOPoEUn9vhQIDAQABAoIBAClR+3mwbDKnafRL\n" +
            "QymAMrDXtAD06Mr0359gKxNqqZdflcAU++/qjCSzX4S1ty2yNEN7Vik8jsaKHIUY\n" +
            "0XunJoE8m1BQl1iZzX0QAkIv8GgVS7THAvs4ATIi+FiD8GfqtkMbvQJ+y8s4I6Sh\n" +
            "eR5ZWtsON5W7ipttT/Lm7kkprS1/7sBvTKrrror+J/I45TEuZuUlcc3jrx7OWS+D\n" +
            "Jws+HZbFpvZyFhj78r3sjRfNM+bU5ngHWvNHcshnyy9WnX+ll5W3Y5fvdT84jB3O\n" +
            "GXhR0enUWSe7UyWssB0uILt3u20NNWUWprfCbhl7nus5CCEiXCqC3Yh02x2/JZeS\n" +
            "IpMwSMECgYEA3taPzBcQYXEX7t8tUo2ViGe/okHgWSe8fwnHQG9+1x0XC+9WOtFi\n" +
            "pPCLAUxfu3Zl3LVKbLkYr1BVb1fnxIa6OWRkd4X7ycapV3OaHza/aXIDfve5SlcK\n" +
            "5JoRNf52UZrF3MlkC5M7UO55+MPxtHg7wb+QMKB0RI24Vd6Mc5xuG3ECgYEAz0aM\n" +
            "EoCOAj0SikV0m0bnouH7xydh7FA53cJybMpAjne4MkF5R3+xmZMo2iJUHph6l1SW\n" +
            "snOKCfCzZeXK2tcGnwGbKHPy6p5769laqAJWQwLObYuz7NP9ZuoQfdXc9o/11Joj\n" +
            "+I2b297ll4EWxv/uP3i5nd7l6+BEZf19/PZ+A1UCgYB4tqajGoNnJcqfPgrhQ42T\n" +
            "nRA0p5cO9PWpo/RqNXvyr/GCJ58AsdjMHPpQM71QYe0ASL0OdJ6oqc3+SRJmZc2P\n" +
            "tZCvJselIJcvppIBArliN78pEq0vVkOyXrugEj6qKjuxRO+LTbC+QNGWLx9Kci98\n" +
            "33cOwHz7fgIzkiFnp4AGIQKBgQCfVcDZfD8uE1qMNkYkE0Z/HK7acjjBWw6QnSgu\n" +
            "3u7vVbKf11rujoes4cYWoSr+9gHPEPRjK5Qifgi6PQJKZd8uHiLy3ucOWlQygjxB\n" +
            "SMKc8qxS44ClfnSeRzH0OZGJbDLygqdCK0FzrI3bgB1NTnIMErbFWBJw105dBJdz\n" +
            "/NvznQKBgQC0BXn/2mV3ykVFlH6UQaMab2HbG39vj/jyaXYDRVqHFia1a4clXz9i\n" +
            "xqVsiRh75nWbz0NNl70F9M6pXTbo57ZQBJnvSS0qQ7kpNzlzHEQ2MCtUxV5F2kN2\n" +
            "Np/CjMr4Ax5nBjXUS2QBXq24IiDeDZFZhE2e3CxfUMDkCbJr6wo9uw==\n" +
            "-----END RSA PRIVATE KEY-----";

    public static void Hello() {
        JSch jsch = new JSch();
        com.jcraft.jsch.Session session = null;
        String result = "";

        try {
            session = jsch.getSession("root", "tj.systec-pbx.net", 22);
//            session.setPassword("Systec-23708235");

            jsch.addIdentity("systec", key.getBytes(), null, null);

            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();

            // SSH Channel
            ChannelExec channel = (ChannelExec)session.openChannel("exec");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            channel.setOutputStream(stream);

            // Execute command
            channel.setCommand(String.format("echo 1234%d > /root/hello.txt", ++i));
            channel.connect(1000);
            java.lang.Thread.sleep(500);   // this kludge seemed to be required.
            channel.disconnect();

            result = stream.toString();
            Log.i(TAG, "Hello: "+result);
        }
        catch (JSchException ex) {
            String s = ex.toString();
            System.out.println(s);
        }
        catch (InterruptedException ex) {
            String s = ex.toString();
            System.out.println(s);
        }
        finally {
            if (session != null)
                session.disconnect();
        }
    }
}
