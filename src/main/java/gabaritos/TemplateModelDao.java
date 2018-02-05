package gabaritos;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterOutputStream;
import org.paim.commons.BinaryImage;
import org.paim.commons.ImageFactory;

/**
 * Dao layer for the template model
 */
public class TemplateModelDao {

    /**
     * Loads the template model
     *
     * @return Template model
     */
    public static TemplateModel load() {
        Reader reader = new InputStreamReader(TemplateModelDao.class.getResourceAsStream("/gabarito/templates.json"));
        return gson().fromJson(reader, TemplateModel.class);
    }

    /**
     * Persists the specified model
     *
     * @param model
     */
    public static void persist(TemplateModel model) {
        String path = TemplateModelDao.class.getResource("/gabarito/templates.json").getFile();
        path = path.replace("target/classes", "src/main/resources");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path)))) {
            writer.write(gson().toJson(model));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crates the Gson instance to be used
     *
     * @return Gson
     */
    private static Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(BinaryImage.class, new BinaryImageAdapter())
                .create();
    }

    private static class BinaryImageAdapter extends TypeAdapter<BinaryImage> {

        @Override
        public BinaryImage read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            try {
                String buffer = decompress(reader.nextString());
                String[] parts = buffer.split(",");
                int width = Integer.parseInt(parts[0]);
                int height = Integer.parseInt(parts[1]);
                String bits = parts[2];
                BinaryImage value = ImageFactory.buildBinaryImage(width, height);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        int index = y + x * height;
                        value.set(x, y, bits.charAt(index) == '1');
                    }
                }
                return value;
            } catch (Base64DecodingException | NumberFormatException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void write(JsonWriter writer, BinaryImage value) throws IOException {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getWidth()).append(",").append(value.getHeight()).append(",");
            for (int x = 0; x < value.getWidth(); x++) {
                for (int y = 0; y < value.getHeight(); y++) {
                    sb.append(value.get(x, y) ? "1" : "0");
                }
            }
            writer.value(compress(sb.toString()));
        }

        /**
         * Compress some text
         * 
         * @param string
         * @return String
         * @throws IOException 
         */
        private String compress(String string) throws IOException {
            byte[] source = string.getBytes();
            ByteArrayInputStream sourceStream = new ByteArrayInputStream(source);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(source.length / 2);
            try (OutputStream compressor = new DeflaterOutputStream(outputStream, new Deflater())) {
                ByteStreams.copy(sourceStream, compressor);
            }
            return Base64.encode(outputStream.toByteArray());
        }

        /**
         * Decompress some text
         * 
         * @param string
         * @return String
         * @throws IOException
         * @throws Base64DecodingException 
         */
        private String decompress(String string) throws IOException, Base64DecodingException {
            byte[] source = Base64.decode(string);
            ByteArrayInputStream sourceStream = new ByteArrayInputStream(source);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(source.length * 2);
            try (OutputStream compressor = new InflaterOutputStream(outputStream, new Inflater())) {
                ByteStreams.copy(sourceStream, compressor);
            }
            return new String(outputStream.toByteArray());
        }

    }
}
