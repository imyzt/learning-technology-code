package top.imyzt.learning.bitmap;

import org.roaringbitmap.RoaringBitmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.sql.*;

public class BitmapToMySQL {
    public static void saveBitmap(long tagId, RoaringBitmap bitmap, int count, Connection conn) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.serialize(new DataOutputStream(bos));
        byte[] data = bos.toByteArray();

        String sql = "REPLACE INTO tag_bitmap (tag_id, bitmap_data, bitmap_count) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, tagId);
            ps.setBytes(2, data);
            ps.setInt(3, count);
            ps.executeUpdate();
        }
    }

    public static RoaringBitmap loadBitmap(long tagId, Connection conn) throws Exception {
        String sql = "SELECT bitmap_data FROM tag_bitmap WHERE tag_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, tagId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    byte[] data = rs.getBytes("bitmap_data");
                    RoaringBitmap bitmap = new RoaringBitmap();
                    bitmap.deserialize(new DataInputStream(new ByteArrayInputStream(data)));
                    return bitmap;
                }
            }
        }
        return null;
    }
}
